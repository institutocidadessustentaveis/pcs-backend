package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ComentarioDTO;
import br.org.cidadessustentaveis.model.administracao.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepositoryImplementation<Comentario, Long> {
	
	@Query("select new br.org.cidadessustentaveis.dto.ComentarioDTO(c.id, c.titulo, c.nomeUsuario, c.dataPublicacao, c.horarioPublicacao, cid, prefeitura) from Comentario c"
			+ " left join c.prefeitura prefeitura"
			+ " left join c.cidade cid"
			+ " where c.usuario.id = ?1")
	List<ComentarioDTO> buscarComentariosToList(Long idUsuario);
	
	@Query("select new br.org.cidadessustentaveis.dto.ComentarioDTO(c.id, c.titulo, c.nomeUsuario, c.dataPublicacao, c.horarioPublicacao, cid, prefeitura) from Comentario c"
			+ " left join c.prefeitura prefeitura"
			+ " left join c.cidade cid")
	List<ComentarioDTO> buscarComentariosToListAdmin();
	
	@Query("select new br.org.cidadessustentaveis.dto.ComentarioDTO(c.id, c.titulo, c.nomeUsuario, c.dataPublicacao, c.horarioPublicacao,c.comentario, c.email, c.telefone, cid, prefeitura) from Comentario c"
			+ " left join c.prefeitura prefeitura"
			+ " left join c.cidade cid "
			+ "order by c.id desc")
	List<ComentarioDTO> buscarComentariosToListPublica();
	
	
}


