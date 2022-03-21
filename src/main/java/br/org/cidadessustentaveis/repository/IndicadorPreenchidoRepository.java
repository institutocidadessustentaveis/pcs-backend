package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.AvaliacaoVariavelDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CidadeMapaDTO;
import br.org.cidadessustentaveis.dto.CidadeQtIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO;
import br.org.cidadessustentaveis.dto.VariacaoReferenciasDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;

@Repository
public interface IndicadorPreenchidoRepository extends JpaRepository<IndicadorPreenchido, Long>{

	List<IndicadorPreenchido> findByIndicadorAndAno(Indicador indicador, Short ano);
	
	Optional<IndicadorPreenchido> findByIndicadorAndAnoAndPrefeitura(Indicador indicador, Short ano, Prefeitura prefeitura);
	
	Optional<IndicadorPreenchido> findByPrefeituraAndIndicadorAndAno(Prefeitura prefeitura, Indicador indicador, Short ano);
	
	@Query("SELECT vr FROM IndicadorPreenchido ip, ValorReferencia vr " + 
			"WHERE ip = ?1 AND vr MEMBER OF ip.indicador.valoresReferencia AND ?2 BETWEEN vr.valorde AND vr.valorate")
	Optional<ValorReferencia> findValorReferenciaByIndicadorPreenchido(IndicadorPreenchido preenchido, Double valor);
	
	@Query("SELECT vr FROM IndicadorPreenchido ip, ValorReferencia vr " + 
			"WHERE ip.prefeitura = ?1 AND ip = ?2 AND vr MEMBER OF ip.indicador.valoresReferencia AND ?3 BETWEEN vr.valorde AND vr.valorate")
	Optional<ValorReferencia> findValorReferenciaByIndicadorPreenchido(Prefeitura prefeitura, IndicadorPreenchido preenchido, Double valor);

	List<IndicadorPreenchido> findByPrefeituraAndIndicador(Prefeitura prefeitura, Indicador indicador);

	List<IndicadorPreenchido> findByIndicadorId(Long id);

	List<IndicadorPreenchido> findByIndicadorIdAndPrefeituraCidadeIdIn(Long id, List<Long> cidades);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO(ip.id, i.id, i.nome, i.descricao, ip.ano, ip.dataPreenchimento, ip.resultado, c.id, c.nome)  "
			+ " FROM IndicadorPreenchido ip "
			+ " JOIN ip.indicador i "
			+ " JOIN ip.prefeitura p "
			+ " JOIN p.cidade c "
			+ " WHERE i.id = ?1 and c.id in ?2 and ip.resultado is not null")
	List<IndicadorPreenchidoSimplesDTO> findByIndicadorAndCidade(Long id, List<Long> cidades );
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO(ip.id, i.id, i.nome, i.descricao, ip.ano, ip.dataPreenchimento, ip.resultado, c.id, c.nome) "
			+ " FROM IndicadorPreenchido ip "
			+ " JOIN ip.indicador i "
			+ " JOIN ip.prefeitura p "
			+ " JOIN p.cidade c "
			+ " WHERE i.id = ?1  and ip.resultado is not null")
	List<IndicadorPreenchidoSimplesDTO> findByIndicadorIdSimples(Long id );

	List<IndicadorPreenchido>findByAno(Short ano);

	@Query("SELECT ip FROM IndicadorPreenchido ip WHERE ip.prefeitura.id = ?4 AND ip.indicador.id = ?1 AND ip.ano BETWEEN ?2 AND ?3")
	List<IndicadorPreenchido> findByIndicadorAnoPrefeitura(Long idIndicador, short anoInicio, short anoFim, Long idPrefeitura);

	List<IndicadorPreenchido>findByPrefeitura(Prefeitura prefeitura);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.AvaliacaoVariavelDTO( ip.prefeitura.id, ip.prefeitura.cidade.nome,case when varpre.status != 'Avaliado' then count(distinct varpre.id) else 0 end) "
			+ " FROM IndicadorPreenchido ip"
			+ " left join ip.variaveisPreenchidas varpre"
			+ " left join Variavel var on varpre.variavel.id = var.id"
			+ " WHERE var.tipo='Texto livre' or var.tipo is null"
			+ " GROUP BY ip.prefeitura.id, ip.prefeitura.cidade.nome, varpre.status"
			+ " ORDER BY ip.prefeitura.cidade.nome")
	List<AvaliacaoVariavelDTO> findByAvaliacaoVariavelPorCidade();
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.AvaliacaoVariavelDTO(ip.prefeitura.id, max(varpre.dataPreenchimento),max(varpre.dataAvaliacao))"
			+ " FROM IndicadorPreenchido ip"
			+ " left join ip.variaveisPreenchidas varpre"
			+ " left join Variavel var on varpre.variavel.id = var.id"
			+ " WHERE var.tipo='Texto livre' or var.tipo is null"		
			+ " GROUP BY ip.prefeitura.id")
	List<AvaliacaoVariavelDTO> findByAvaliacaoVariavelPorCidadeData();
	
	public List<IndicadorPreenchido> findByVariaveisPreenchidas(VariavelPreenchida variavelPreenchida);

	public List<IndicadorPreenchido> findByPrefeituraCidadeIdAndIndicadorIdOrderByAno(Long cidade, Long idIndicador);

	List<IndicadorPreenchido> findByPrefeituraCidade(Cidade cidade);
	
	@Query("SELECT ip.ano FROM IndicadorPreenchido ip WHERE ip.prefeitura.id = ?1 GROUP BY ip.ano")
	public List<Short> buscarAnosIndicadoresPorPrefeitura(Long idPrefeitura);

	@Query("SELECT DISTINCT ip.indicador FROM IndicadorPreenchido ip")
	List<Indicador> findIndicadorJaPreenchido();
	
	@Query("SELECT DISTINCT new br.org.cidadessustentaveis.dto.CidadeDTO(c.id, CONCAT(c.nome, ', ', c.provinciaEstado.nome)) FROM IndicadorPreenchido ip JOIN ip.prefeitura p JOIN p.cidade c WHERE ip.indicador.id = ?1")
	List<CidadeDTO> findCidadesQueJaPreencheram(Long idIndicador);
	
	List<IndicadorPreenchido> findByIndicadorIdAndPrefeituraCidadeId(Long id, Long idCidade);
	
	List<IndicadorPreenchido> findByIndicadorIdAndPrefeituraCidadePopulacaoLessThanEqual(Long id, Long populacao);
	
	List<IndicadorPreenchido> findByIndicadorIdAndPrefeituraCidadeIdAndPrefeituraCidadePopulacaoLessThanEqual(Long id, Long idCidade, Long populacao);

	@Query("select distinct new br.org.cidadessustentaveis.dto.CidadeQtIndicadorPreenchidoDTO(c.nome, e.nome,  count(i.id), c.latitude, c.longitude, c.id)"
			+ " from IndicadorPreenchido i" +
			" join i.prefeitura p " +
			" join p.cidade c " +
			" join c.provinciaEstado e " +
			" where resultado is not null and c.isSignataria is true and p.signataria is true " +
			" group by c.nome, e.nome, c.latitude, c.longitude, c.id")
	List<CidadeQtIndicadorPreenchidoDTO> contarQuantidadeIndicadoresPreenchidos();
	
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.CidadeMapaDTO(i.prefeitura.cidade.nome, i.prefeitura.cidade.id, i.prefeitura.cidade.latitude, i.prefeitura.cidade.longitude) "
			+ "FROM IndicadorPreenchido i "
			+ "WHERE i.indicador.id = ?1 and "
			+ "i.prefeitura.cidade.id in ?2 and "
			+ "i.ano in ?3 ")
	List<CidadeMapaDTO> buscarCidadesPorIndicadorMandato(Long indicador, List<Long> cidades,  List<Short> anosMandato);
	
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.CidadeMapaDTO(i.prefeitura.cidade.nome, i.prefeitura.cidade.id, i.prefeitura.cidade.latitude, i.prefeitura.cidade.longitude) "
			+ "FROM IndicadorPreenchido i "
			+ "WHERE i.indicador.id = ?1 and "
			+ "i.ano in ?2 ")
	List<CidadeMapaDTO> buscarCidadesPorIndicadorMandato(Long indicador,  List<Short> anosMandato);
	
	

	List<IndicadorPreenchido> findByIndicadorEixoIdAndPrefeituraCidadeIdAndAnoBetweenOrderByIndicadorIdAscAnoAsc( Long idEixo, Long idCidade,
			Short anoInicial, Short anoFinal);
	
//	List<IndicadorPreenchido> findByIndicadorIdAndPrefeituraCidadeNomeIn(Long id, List<Long> cidadeNome);
	

	List<IndicadorPreenchido> findByPrefeituraCidadeIdAndIndicadorIdAndAnoBetweenOrderByAno(Long cidade,
																							Long indicador,
																							Short anoInicial,
																							Short anoFinal);

//	@Query("select new br.org.cidadessustentaveis.dto.IndicadorDadosAbertosDTO(c.id, c.nome, estado.sigla, i.eixo.id, i.id, i.nome, i.formulaResultado, meta.descricao, ods.numero, i.descricao, ip.dataPreenchimento, ip.resultado, ip.justificativa, estado.nome)" +
//			" from IndicadorPreenchido ip " +
//			" join ip.prefeitura p " +
//			" join p.cidade c " +
//			" join c.provinciaEstado estado " +
//			" join ip.indicador i " +
//			" join i.metaODS meta " +
//			" join i.ods ods")
//	List<IndicadorDadosAbertosDTO> buscarIndicadoresDadosAbertos();
	
	List<IndicadorPreenchido> findByIndicadorPrefeituraCidadeIdAndAnoBetweenOrderByIndicadorIdAscAnoAsc(Long idCidade, Short anoInicial, Short anoFinal);

	List<IndicadorPreenchido> findByResultadoApresentacaoIsNull();
	
	@Query("SELECT distinct ip FROM IndicadorPreenchido ip JOIN ip.indicador i JOIN i.variaveis v JOIN ip.prefeitura p JOIN p.cidade c WHERE ip.ano = ?1 AND c.id = ?3 AND v.id = ?2 ")
	List<IndicadorPreenchido> buscarPorAnoVpVariavelIdCidadeId(Short ano, Long idVariavel, Long idCidade);
	
	@Query("select new br.org.cidadessustentaveis.dto.VariacaoReferenciasDTO(vr.valorde, vr.valorate, vr.Label) from Indicador i join i.valoresReferencia vr where i.id = ?1")
	List<VariacaoReferenciasDTO> buscarReferenciasComVariacao(Long idIndicador);

	List<IndicadorPreenchido> findByIndicadorTipoConteudo(String string);

	IndicadorPreenchido findByIndicadorIdAndPrefeituraCidadeIdAndAno(Long id, Long id2, Short ano);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO(ip.id, i.id, i.nome, i.descricao, ip.ano, ip.dataPreenchimento, ip.resultado, c.id, c.nome)  "
			+ " FROM IndicadorPreenchido ip "
			+ " JOIN ip.indicador i "
			+ " JOIN ip.prefeitura p "
			+ " JOIN p.cidade c "
			+ " WHERE p.id = :idPrefeitura and ip.resultado is not null")
	List<IndicadorPreenchidoSimplesDTO> findByPrefeituraSimples(Long idPrefeitura);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO(ip.id, i.id, i.nome, i.descricao, ip.ano, ip.dataPreenchimento, ip.resultado, c.id, c.nome, i.complementar)  "
			+ " FROM IndicadorPreenchido ip "
			+ " JOIN ip.indicador i "
			+ " JOIN ip.prefeitura p "
			+ " JOIN p.cidade c "
			+ " WHERE p.id = :idPrefeitura and ip.resultado is not null")
	List<IndicadorPreenchidoSimplesDTO> findByPrefeituraSimplesRelatorio(Long idPrefeitura);
}

