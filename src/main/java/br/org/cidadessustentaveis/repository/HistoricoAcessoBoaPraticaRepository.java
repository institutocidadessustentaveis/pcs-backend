package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.sistema.HistoricoAcessoBoaPratica;

@Repository
public interface HistoricoAcessoBoaPraticaRepository extends JpaRepository<HistoricoAcessoBoaPratica, Long> {

	@Query("select historico.id from HistoricoAcessoBoaPratica historico where historico.boaPratica.id = :id")
	List<Long> findAllByIdBoaPratica(Long id);
}
