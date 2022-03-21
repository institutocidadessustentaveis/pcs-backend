package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.Usuario;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long>{
	@Query("SELECT a FROM Alerta a LEFT OUTER JOIN a.alertasVisualizados av on av.usuario = ?1 WHERE av.id is null order by a.id desc")
	List<Alerta> buscarAlertasNaoVisualizados(Usuario usuario);
	
	@Query("SELECT count(a) FROM Alerta a LEFT OUTER JOIN a.alertasVisualizados av on av.usuario = ?1 WHERE av.id is null")
	Long contarAlertasNaoVisualizados(Usuario usuario);
}
