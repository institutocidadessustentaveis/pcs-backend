package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.AlertaVisualizado;
import br.org.cidadessustentaveis.model.administracao.Usuario;

@Repository
public interface AlertaVisualizadoRepository extends JpaRepository<AlertaVisualizado, Long>{

	List<AlertaVisualizado> findByUsuarioOrderByAlertaIdDesc(Usuario usuario);
}
