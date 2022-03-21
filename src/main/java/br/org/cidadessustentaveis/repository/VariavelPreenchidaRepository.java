package br.org.cidadessustentaveis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.org.cidadessustentaveis.dto.ObservacaoVariavelDTO;
import br.org.cidadessustentaveis.dto.VariavelJaPreenchidaSimplesDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaDuplicadaDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;

@Repository
public interface VariavelPreenchidaRepository extends JpaRepository<VariavelPreenchida, Long> {
	
	@Query("SELECT vp FROM VariavelPreenchida vp WHERE vp.variavel = ?1 AND vp.ano = ?2")
	Optional<VariavelPreenchida> findByVariavelAndAno(Variavel encontrada, Short ano);

	Optional<VariavelPreenchida> findByVariavelAndAnoAndPrefeitura(Variavel variavel, Short ano, Prefeitura prefeitura);
	
	Optional<VariavelPreenchida> findByPrefeituraAndVariavelAndAno(Prefeitura prefeitura, Variavel variavel, Short ano);
	
	@Query("SELECT vp FROM Indicador i, VariavelPreenchida vp "
			+ "WHERE vp.prefeitura = ?1 AND i = ?2 and vp.variavel MEMBER OF i.variaveis")
	List<VariavelPreenchida> findByIndicador(Prefeitura prefeitura, Indicador indicador);
	
	@Query("SELECT vp FROM Indicador i, VariavelPreenchida vp "
			+ "WHERE vp.prefeitura.cidade.id = ?1 AND i.id = ?2 and vp.variavel MEMBER OF i.variaveis")
	List<VariavelPreenchida> findByIndicadorAndCidade(Long idCidade, Long idIndicador);
	
	@Query("SELECT vp FROM Indicador i, VariavelPreenchida vp "
			+ "WHERE i.id = ?1 AND  vp.prefeitura.cidade.id = ?2 AND vp.variavel MEMBER OF i.variaveis AND vp.ano BETWEEN ?3 AND ?4")
	List<VariavelPreenchida> findByIndicadorCidadeAno( Long indicador, Long idCidade, Short anoInicial, Short anoFinal);

	Optional<VariavelPreenchida> findByVariavelAndAnoAndPrefeituraCidade(Variavel variavel, Short ano, Cidade cidade);

	List<VariavelPreenchida> findByVariavelIdAndPrefeituraCidadeIdIn(Long idVariavel, List<Long> idCidades);

	List<VariavelPreenchida> findByVariavelId(Long idVariavel);
	
	long countByVariavelId(Long idVariavel);
	
	@Query("SELECT distinct vp FROM VariavelPreenchida vp inner join vp.prefeitura p inner join p.cidade c "
            + "WHERE c.id = ?2 AND vp.variavel.id in( select distinct v.id from Indicador i join i.variaveis v where i.id= ?1 ) AND vp.ano = ?3")
	List<VariavelPreenchida> findByIndicadorIdCidadeidAno( Long indicador, Long idCidade, Short ano);

	@Query("SELECT distinct vp.ano FROM VariavelPreenchida vp ORDER BY vp.ano DESC")
	List<Short> carregarComboAnosPreenchidos();
	
	@Query("SELECT distinct vp.ano FROM VariavelPreenchida vp JOIN vp.variavel v WHERE v.id = :idVariavel ORDER BY vp.ano DESC")
	List<Short> carregarComboAnosPreenchidosPorIdVariavel(Long idVariavel);

	@Query("SELECT distinct CASE WHEN vp.fonteMigracao IS NOT NULL THEN vp.fonteMigracao ELSE f.nome END FROM VariavelPreenchida vp inner join vp.prefeitura p inner join p.cidade c  left join vp.instituicaoFonte f "
            + "WHERE c.id = ?2 AND vp.variavel.id in( select distinct v.id from Indicador i join i.variaveis v where i.id= ?1 )")
	List<String> buscarFontesPreenchidas(Long idIndicador, Long idCidade);

	@Query("SELECT DISTINCT vp.ano FROM VariavelPreenchida vp JOIN vp.prefeitura p JOIN p.cidade c "
			+ "WHERE vp.variavel.id in (select v.id from Indicador i join i.variaveis v where i.id = ?1) and c.id = ?2 ")
	List<Short> findAnoQueCidadeJaPreencheuUmaDasVariaveisDoIndicador(Long indicador, Long idCidade);
	@Query("SELECT new br.org.cidadessustentaveis.dto.VariavelPreenchidaDuplicadaDTO(v.id,p.id,vp.ano, count(vp.id)) From VariavelPreenchida vp JOIN vp.prefeitura p JOIN vp.variavel v GROUP BY v.id,p.id,vp.ano HAVING count(vp.id) >1 ")
	List<VariavelPreenchidaDuplicadaDTO> buscarDuplicadas();

	@Query("SELECT distinct vp FROM VariavelPreenchida vp inner join vp.prefeitura p "
            + "WHERE p.id = ?2 AND vp.variavel.id = ?1  AND vp.ano = ?3 ORDER BY vp.dataPreenchimento")
	List<VariavelPreenchida> findByVariavelIdAndAnoAndPrefeituraid(Long variavel,Long prefeitura, Short ano );
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.ObservacaoVariavelDTO(v.nome, vp.ano, vp.observacao)"
			+ " FROM Indicador i, VariavelPreenchida vp"
			+ " inner join vp.variavel v"
			+ " WHERE i.id = ?1"
			+ " AND  vp.prefeitura.cidade.id = ?2"
			+ " AND vp.variavel MEMBER OF i.variaveis"
			+ " AND vp.observacao != null"
			+ " AND TRIM(vp.observacao) not like ''")
	List<ObservacaoVariavelDTO> buscarObservacaoVariavel(Long idIndicador,Long idCidade);
	
	@Transactional
	@Modifying
	@Query("update VariavelPreenchida vp set valor = ?2 WHERE vp.id = ?1")
	 void atualizaValorPorIdVariavel(Long idVariavel, Double valor);
	
	@Query("SELECT distinct v FROM VariavelPreenchida vp inner join vp.variavel v "
            + "WHERE v.prefeitura is null ORDER BY v.nome ASC")
	List<Variavel> buscarVariaveisParaCombo();

	@Query("SELECT new br.org.cidadessustentaveis.dto.VariavelJaPreenchidaSimplesDTO(v.id, v.nome, (SELECT CASE WHEN COUNT(vp.id) > 0 THEN TRUE ELSE FALSE END from VariavelPreenchida vp where vp.variavel.id = v.id and vp.ano = ?2 and vp.prefeitura.cidade.id = ?1) ) from Variavel v left join v.prefeitura WHERE (v.prefeitura IS NULL OR (v.prefeitura IS NOT NULL AND v.prefeitura.cidade.id = ?1 ) ) order by v.nome")
	List<VariavelJaPreenchidaSimplesDTO> getVariaveisParaPreencher(Long idPrefeitura, Short ano);
	
	@Query("SELECT new br.org.cidadessustentaveis.dto.VariavelJaPreenchidaSimplesDTO(v.id, v.nome, (SELECT CASE WHEN COUNT(vp.id) > 0 THEN TRUE ELSE FALSE END from SubdivisaoVariavelPreenchida vp where vp.variavel.id = v.id and vp.ano = ?2 and vp.prefeitura.cidade.id = ?1 and vp.subdivisao.id = ?3) ) from Variavel v left join v.prefeitura WHERE (v.prefeitura IS NULL OR (v.prefeitura IS NOT NULL AND v.prefeitura.cidade.id = ?1 ) ) order by v.nome")
	List<VariavelJaPreenchidaSimplesDTO> getVariaveisParaPreencher(Long idPrefeitura, Short ano,Long idSubdivisao);

	List<VariavelPreenchida> findByVariavelIdAndPrefeituraCidadeIdOrderByAnoDesc(Long idVariavel, Long idCidade);

	VariavelPreenchida findByVariavelIdAndPrefeituraCidadeIdAndAno(Long idVariavel, Long idCidade, Short ano);

	@Query("SELECT svp FROM Indicador i, SubdivisaoVariavelPreenchida svp "
			+ "WHERE svp.prefeitura.cidade.id = ?1 AND i.id = ?2 AND svp.subdivisao.id = ?3 and svp.variavel MEMBER OF i.variaveis")
	List<SubdivisaoVariavelPreenchida> findByCidadeIdIndicadorIdSubdivisaoId(Long idCidade, Long idIndicador, Long idSubdivisao);
	
	@Query("SELECT svp FROM Indicador i, SubdivisaoVariavelPreenchida svp "
			+ "WHERE i.id = ?1 AND svp.subdivisao.id = ?2 and svp.variavel MEMBER OF i.variaveis")
	List<SubdivisaoVariavelPreenchida> findByIndicadorIdSubdivisaoId(Long idIndicador, Long idSubdivisao);
	
}
