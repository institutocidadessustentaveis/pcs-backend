package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.org.cidadessustentaveis.model.boaspraticas.SolucaoBoaPratica;

public interface SolucaoBoaPraticaRepository extends JpaRepository<SolucaoBoaPratica, Long>{

	
	@Modifying
	@Query("DELETE FROM SolucaoBoaPratica solucao WHERE solucao.boaPratica.id = :id")
	public void deleteByIdBoaPratica(Long id);
}
