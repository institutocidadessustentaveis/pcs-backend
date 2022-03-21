package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetasDetalhado;

@Repository
public interface PlanoDeMetasDetalhadoRepository extends JpaRepository<PlanoDeMetasDetalhado, Long>{

	@Query("SELECT pmd FROM PlanoDeMetasDetalhado pmd WHERE pmd.planoDeMetas .id = ?1 AND pmd.indicador.id = ?2")
	List<PlanoDeMetasDetalhado> findByIndicadorAndPlanoDeMetas(Long idPlanoDeMetas, Long idIndicador);

	@Query("SELECT pmd.planoDeMetas.id FROM PlanoDeMetasDetalhado pmd")
	List<Long> buscarIdsPlanoDeMetasPreenchidos();

}
