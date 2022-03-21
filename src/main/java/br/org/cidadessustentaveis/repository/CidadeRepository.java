package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CidadeComboDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CidadeDetalheDTO;
import br.org.cidadessustentaveis.dto.CidadeQtIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.CidadesPorEstadoDTO;
import br.org.cidadessustentaveis.dto.ContagemCidadesCidadesParticipantesNoEstadoDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>{
	
	@Query(value="select c.id, c.nome, c.codigo_ibge, c.populacao, c.ano_populacao, c.endereco_prefeitura, c.signataria, c.provincia_estado from Cidade c", nativeQuery = true)
	List<Cidade> findAllByOrderByNomeAsc();

	List<Cidade> findByProvinciaEstadoIdOrderByNomeAsc(Long idProvinciaEstado);
	
	@Query("select c from Cidade c inner join c.provinciaEstado p on c.provinciaEstado = p.id where p.nome = :nomeProvinciaEstado order by p.nome")
	List<Cidade> findByProvinciaEstadoNomeOrderByNomeAsc(String nomeProvinciaEstado);

	@Query(value = "select c from Cidade c where lower(c.nome) like %:nome% order by c.nome")
	List<Cidade> findByNomeLike(String nome);
	
	@Query(value = "select new br.org.cidadessustentaveis.dto.CidadeDTO(c.codigoIbge, p.nome, p.sigla) from Cidade c join c.provinciaEstado p on c.provinciaEstado = p.id where lower(c.nome) like :nome")
	CidadeDTO buscarPorNomeIgual(String nome);
	
	@Query(value = "select new br.org.cidadessustentaveis.dto.CidadeDTO (c.id, c.nome, c.provinciaEstado) from Cidade c where c.isSignataria = true and lower(c.nome) like %:nome% order by c.nome")
	List<CidadeDTO> findByNomeLikeAndSignataria(String nome);

	@Query(value = "select c from Cidade c where lower(c.nome) like %:nome% order by c.nome")
	Page<Cidade> findByNomeLike(String nome, Pageable pageable);
	
	@Query("SELECT new ProvinciaEstado(pe.id, pe.nome, pe.sigla, pe.populacao) FROM Cidade c LEFT JOIN c.provinciaEstado pe"
			+ " WHERE c.isSignataria = true GROUP BY pe.id ORDER BY pe.nome ASC")
	List<ProvinciaEstado> findEstadosSignatarios();
	
	@Query("SELECT new Cidade(c.id, c.nome, c.codigoIbge, c.populacao, c.anoDaPopulacao, c.enderecoDaPrefeitura, c.isSignataria)"
			+ " FROM Cidade c WHERE c.isSignataria = true AND c.provinciaEstado = ?1 ORDER BY c.nome ASC")
	List<Cidade> findCidadesSignataria(ProvinciaEstado estado);

	@Query("SELECT distinct new Cidade(i.prefeitura.cidade.id, i.prefeitura.cidade.nome) FROM IndicadorPreenchido i "
			+ " WHERE i.indicador.id= ?1")
	List<CidadeDTO> findCidadesPorIndicador(Long idIndicador);

	@Query("SELECT i.prefeitura.cidade FROM IndicadorPreenchido i "
			+ " WHERE i.indicador.id= ?1")
	List<Cidade> findCidadesPorIndicadorSemDTO(Long idIndicador);

	@Query("SELECT i.prefeitura.cidade FROM IndicadorPreenchido i "
			+ " WHERE i.indicador.id= ?1 and "
			+ " lower(i.prefeitura.cidade.nome) LIKE %?2%")
	List<Cidade> findCidadesPorIndicadorEPorNome(Long idIndicador, String nome);

	@Query("select new br.org.cidadessustentaveis.dto.ContagemCidadesCidadesParticipantesNoEstadoDTO(p.id, p.nome, (select count(c.id) from Cidade c where c.isSignataria = true and c.provinciaEstado = p.id)) " +
			" from ProvinciaEstado p " +
			" where p.pais.id = (select pa.id from Pais pa where pa.nome = 'Brasil')")
	List<ContagemCidadesCidadesParticipantesNoEstadoDTO> countCidadeSignatariaPorEstado();

	@Query("select new br.org.cidadessustentaveis.dto.CidadesPorEstadoDTO(c.provinciaEstado.id, c.provinciaEstado.nome, count(c.id)) "
		+ "from Cidade c "
		+ "group by c.provinciaEstado, c.provinciaEstado.nome")
	List<CidadesPorEstadoDTO> countTotalCidadesPorEstado();
	
	@Query("select new br.org.cidadessustentaveis.dto.CidadesPorEstadoDTO(c.provinciaEstado.id, c.provinciaEstado.nome, count(c.id)) "
			+ " from Cidade c "
			+ " where c.isSignataria = true "
			+ " group by c.provinciaEstado, c.provinciaEstado.nome")
	List<CidadesPorEstadoDTO> countTotalCidadesSignatariasPorEstado();

	List<Cidade> findByIsSignataria(boolean b);

	@Query("select new br.org.cidadessustentaveis.model.administracao.Cidade(c.id, c.nome, c.provinciaEstado.id, c.provinciaEstado.nome, c.provinciaEstado.sigla, c.latitude, c.longitude)" +
			" from Cidade c " +
			" where c.isSignataria = :b " +
			" order by c.nome")
	List<Cidade> findByIsSignatariaOrderByNome(boolean b);

	@Query("select new br.org.cidadessustentaveis.model.administracao.Cidade(c.id, c.nome)" +
			" from Cidade c" +
			" where replace(replace(lower(unaccent(c.nome)), '-', ''), ' ', '') like %?1% and" +
			" lower(c.provinciaEstado.sigla) like %?2%"
			)
	Cidade findByNomeAndProvinciaEstadoSiglaUrl(String nome, String sigla);
	
	@Query("select c from Cidade c "
			+ "inner join c.provinciaEstado pe "
			+ "where lower(slugify(unaccent(c.nome))) like lower(slugify(unaccent(:nome))) "
			+ "and lower(pe.sigla) like lower(:sigla)")
	Cidade findByNomeAndProvinciaEstadoSigla(String nome, String sigla);

	@Query("select new br.org.cidadessustentaveis.dto.CidadeQtIndicadorPreenchidoDTO(c.nome, c.provinciaEstado.nome, c.latitude, c.longitude, c.id)" +
			" from Cidade c " +
			" where " +
			" c.isSignataria = true " +
			" order by c.nome")
	List<CidadeQtIndicadorPreenchidoDTO> buscarQuantidadeDeIndicadoresPreenchidosCidadesSignatarias();

	@Query("select new br.org.cidadessustentaveis.model.administracao.Cidade(c.id, c.nome) " +
			" from Cidade c " +
			" inner join c.provinciaEstado pe " +
			" where c.isSignataria = true and pe.id = :idEstado")
	List<Cidade> findSignatariaPorEstado(Long idEstado);
	

	@Query("select new br.org.cidadessustentaveis.model.administracao.Cidade(c.id, c.nome)"
			+ " from Cidade c"
			+ " inner join c.provinciaEstado pe"
			+ " where pe.id = :idProvinciaEstado"
			+ " order by c.nome")
	List<Cidade> findCidadeByIdProvinciaEstadoOrderByNomeAsc(Long idProvinciaEstado);

	@Query("select new br.org.cidadessustentaveis.model.administracao.Cidade(c.id, c.nome)"
			+ " from Cidade c"
			+ " inner join c.provinciaEstado pe"
			+ " where pe.id = :idProvinciaEstado and c.isSignataria = true"
			+ " order by c.nome")
	List<Cidade> findCidadeByIdProvinciaEstadoPCSOrderByNomeAsc(Long idProvinciaEstado);
	
	@Query(value="SELECT c from Cidade c where c.codigoIbge != null")
	List<Cidade> findAllCidadesIbge();
	
	@Query("SELECT distinct new br.org.cidadessustentaveis.dto.CidadeDTO(i.prefeitura.cidade.id, i.prefeitura.cidade.nome, i.prefeitura.id)"
			+ " FROM IndicadorPreenchido i WHERE lower(i.prefeitura.cidade.nome) like %:nome% ORDER BY i.prefeitura.cidade.nome ASC")
	List<CidadeDTO> findCidadesPorNomeComIndicadoresPreenchidos(String nome);
	

	@Query("select c.id || ';' || c.nome || '-' || pe.sigla from Cidade c inner join c.provinciaEstado pe where c.isSignataria = true order by c.nome")
	List<String> buscaNomeCidadeSignataria();

	@Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(cidade.id, cidade.nome, cidade.provinciaEstado.sigla)"
			+ " FROM Cidade cidade WHERE cidade.isSignataria = true ORDER BY cidade.nome ASC")
	List<ItemComboDTO> buscarTodasCidadesSignatariasParaCombo();
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.CidadeComboDTO(cidade.id, cidade.nome, cidade.provinciaEstado.sigla)"
			+ " FROM Cidade cidade WHERE cidade.id = ?1 and cidade.provinciaEstado.id = ?2")
	List<CidadeComboDTO> buscarPorCidadeProvinciaEstado(Long idCidade, Long idProvinciaEstado);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.ItemComboDTO(cidade.id, cidade.nome)"
			+ " FROM Cidade cidade WHERE cidade.provinciaEstado.id = ?1 ORDER BY cidade.nome ASC")
	List<ItemComboDTO> listarComboPorEstado(Long idProvinciaEstado);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.CidadeComboDTO(cidade.id, cidade.nome, cidade.provinciaEstado.sigla)"
			+ " FROM Cidade cidade WHERE cidade.id = ?1")
	CidadeComboDTO buscarCidadeComboPorId(Long idCidade);
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(c.id, c.nome, pe.nome, p.nome) from Cidade c"
			+ " inner join c.provinciaEstado pe inner join pe.pais p order by p.nome, c.nome")
	List<ItemComboDTO> buscarCidadeComboBox();
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(c.id, c.nome, pe.nome) from Cidade c"
			+ " inner join c.provinciaEstado pe order by c.nome, pe.nome")
	List<ItemComboDTO> buscarCidadeEstadoComboBox();
	
	@Query("select new br.org.cidadessustentaveis.dto.CidadeDetalheDTO(c.id, c.nome,c.populacao, c.anoDaPopulacao, "
			+ "c.provinciaEstado.id, c.provinciaEstado.nome, c.provinciaEstado.sigla, c.provinciaEstado.pais.nome, c.provinciaEstado.pais.continente, c.latitude, c.longitude)" +
			" from Cidade c " +
			" where c.id = :idCidade ")
	CidadeDetalheDTO findByIdCidade(Long idCidade);
	
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO(b.municipio.id, b.municipio.nome, b.municipio.latitude, b.municipio.longitude, COUNT(b))"
			+ " FROM BoaPratica b WHERE b.municipio.id in ?1 and b.prefeitura = null"
			+ " GROUP BY b.municipio.id,  b.municipio.nome, b.municipio.latitude, b.municipio.longitude")
	List<CidadeComBoasPraticasDTO> findCidadesPorIds(List<Long> idsCidade);

	@Query("SELECT DISTINCT c.id FROM VariavelPreenchida vp JOIN vp.prefeitura p JOIN p.cidade c "
			+ "WHERE vp.variavel.id in (select v.id from Indicador i join i.variaveis v where i.id = ?1) ")
	List<Long> findCidadesQueJaPreencheramUmaDasVariaveisDoIndicador(Long indicador);

	@Query("SELECT DISTINCT vp.ano FROM VariavelPreenchida vp JOIN vp.prefeitura p JOIN p.cidade c "
			+ "WHERE vp.variavel.id in (select v.id from Indicador i join i.variaveis v where i.id = ?1) and c.id = ?2 ")
	List<Short> findAnoQueCidadeJaPreencheuUmaDasVariaveisDoIndicador(Long indicador, Long idCidade);
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(c.id, c.nome, pe.sigla) from Cidade c inner join c.provinciaEstado pe where c.provinciaEstado.id in (:idsEstados) order by c.nome")
	List<ItemComboDTO> buscarCidadeParaComboPorListaIdsEstados(List<Long> idsEstados);
	
	@Query("select new br.org.cidadessustentaveis.dto.ItemComboDTO(c.id, c.nome)"
			+ " from Cidade c inner join c.provinciaEstado pe"
			+ " where c.isSignataria = true and pe.id = :idEstado ORDER BY c.nome ASC")
	List<ItemComboDTO> buscarSignatariasComboPorIdEstado(Long idEstado);

	Cidade findByCodigoIbge(Long codigoLong);
	
	Cidade findByNome(String nome);
	
	@Query("select new br.org.cidadessustentaveis.dto.CidadeDTO(c.id, c.nome, c.codigoIbge, c.provinciaEstado, c.populacao)" +
			" from Cidade c " +
			" where c.isSignataria = :b " +
			" order by c.nome")
	List<CidadeDTO> findByIsSignatariaOrderByNomeCompleto(boolean b);
}
