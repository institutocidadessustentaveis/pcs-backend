package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;

@Repository
public interface SubdivisaoVariavelPreenchidaRepository extends JpaRepository<SubdivisaoVariavelPreenchida, Long> {

	SubdivisaoVariavelPreenchida findByVariavelIdAndPrefeituraCidadeIdAndAnoAndSubdivisaoId(Long idVariavel,
			Long idCidade, short parseShort, Long idSubdivisao);

	List<SubdivisaoVariavelPreenchida> findByVariavelIdAndPrefeituraCidadeIdAndSubdivisaoIdOrderByAnoDesc(Long id,
			Long idCidade, Long idSubdivisao);

	@Query("SELECT vp FROM Indicador i, SubdivisaoVariavelPreenchida vp "
			+ "WHERE i.id = ?1 AND  vp.prefeitura.cidade.id = ?2 AND vp.variavel MEMBER OF i.variaveis AND vp.ano BETWEEN ?3 AND ?4 AND vp.subdivisao.id = ?5")
	List<SubdivisaoVariavelPreenchida> findByIndicadorCidadeAnoSubdivisao( Long indicador, Long idCidade, Short anoInicial, Short anoFinal, Long idSubdivisao);
	
	
	@Query("SELECT vp FROM Indicador i, SubdivisaoVariavelPreenchida vp join vp.subdivisao s  "
			+ "WHERE i.id = ?1 AND  vp.prefeitura.cidade.id = ?2 AND vp.variavel MEMBER OF i.variaveis AND  s.id = ?3")
	List<SubdivisaoVariavelPreenchida> findByIndicadorCidadeSubdivisao(Long idIndicador, Long idCidade, Long idSubdivisao);

	
}
