package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.PerfilUsuarioForumDTO;
import br.org.cidadessustentaveis.model.administracao.PerfilUsuarioForum;

@Repository
public interface PerfilUsuarioForumRepository extends JpaRepository<PerfilUsuarioForum, Long>{
	
	@Query("select new br.org.cidadessustentaveis.dto.PerfilUsuarioForumDTO(p.id, p.usuario, cid, p.email, p.nome ) from PerfilUsuarioForum p"
			+ " left join p.cidade cid"
			+ " where p.usuario.id = ?1")
	PerfilUsuarioForumDTO buscarPerfilPorIdUsuario(Long id);
	
	
}
