package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.sistema.AtividadeGestorMunicipal;

@Repository
public interface AtividadeGestorMunicipalRepository extends JpaRepository<AtividadeGestorMunicipal, Long> {

	@Query("select distinct new br.org.cidadessustentaveis.model.sistema.AtividadeGestorMunicipal(a.id, a.acao) from AtividadeGestorMunicipal a order by a.acao")
	List<AtividadeGestorMunicipal> findComboBoxAcao();
}
