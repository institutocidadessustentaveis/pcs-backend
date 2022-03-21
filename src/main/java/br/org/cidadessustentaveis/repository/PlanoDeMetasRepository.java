package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetas;

@Repository
public interface PlanoDeMetasRepository extends JpaRepository<PlanoDeMetas, Long>{

	List<PlanoDeMetas> findByPrefeitura(Prefeitura prefeitura);

	@Query("SELECT pm FROM PlanoDeMetas pm WHERE pm.prefeitura.id = ?1")
	PlanoDeMetas buscarPorIdPrefeitura(Long idPrefeitura);

}
