package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;

@Repository
public interface SubdivisaoIndicadorPreenchidoRepository extends JpaRepository<SubdivisaoIndicadorPreenchido, Long> {

	SubdivisaoIndicadorPreenchido findByIndicadorIdAndPrefeituraCidadeIdAndAnoAndSubdivisaoId(Long id, Long idCidade,
			Short ano, Long idSubdivisao);
			
	List<SubdivisaoIndicadorPreenchido> findByIndicadorIdAndPrefeituraCidadeIdAndSubdivisaoId(Long id, Long idCidade,
	 Long idSubdivisao);
	 		
	List<SubdivisaoIndicadorPreenchido> findByIndicadorIdAndSubdivisaoId(Long id,
		Long idSubdivisao);

	List<SubdivisaoIndicadorPreenchido> findByIndicadorIdAndPrefeituraCidadeIdAndSubdivisaoTipoSubdivisaoNivel(Long idIndicador,
			Long idCidade, Long nivel);

	@Query("SELECT sip FROM SubdivisaoIndicadorPreenchido sip WHERE sip.indicador.id =?1 AND sip.prefeitura.cidade.id = ?2 AND sip.subdivisao.tipoSubdivisao.nivel = ?3 and sip.ano = ?4 order by sip.resultado ,sip.subdivisao.nome")
	List<SubdivisaoIndicadorPreenchido> findByIndicadorCidadeNivelAno(Long idIndicador, Long idCidade, Long nivel, short ano);

}
