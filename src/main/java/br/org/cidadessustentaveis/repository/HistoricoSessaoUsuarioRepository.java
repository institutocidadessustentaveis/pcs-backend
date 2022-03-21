package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.sistema.HistoricoSessaoUsuario;

@Repository
public interface HistoricoSessaoUsuarioRepository extends JpaRepository<HistoricoSessaoUsuario, Long>{

	List<HistoricoSessaoUsuario> findByUsuarioAndFimSessaoIsNullOrderByIdDesc(Usuario usuario);

	List<HistoricoSessaoUsuario> findByUsuarioOrderByIdDesc(Usuario usuario);

}
