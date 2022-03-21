package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ComentarioDiscussaoDTO;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;

@Repository
public interface ComentarioDiscussaoRepository extends JpaRepositoryImplementation<ComentarioDiscussao, Long>{

	@Query("select new br.org.cidadessustentaveis.dto.ComentarioDiscussaoDTO(cd.id, discussao, usuario, cd.comentario, cd.horarioPublicacao, cd.dataPublicacao, cd.editado, cd.horarioEdicao, cd.dataEdicao) from ComentarioDiscussao cd"
			+ " left join cd.forumDiscussao discussao"
			+ " left join cd.usuario usuario"
			+ " where cd.forumDiscussao.id = ?1")
	List<ComentarioDiscussaoDTO> buscarComentariosDiscussaoByIdDiscussao(Long idDiscussao);
}
