package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ComentarioDiscussaoFilhoDTO;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussaoFilho;

@Repository
public interface ComentarioDiscussaoFilhoRepository extends JpaRepositoryImplementation<ComentarioDiscussaoFilho, Long>{

	@Query("select new br.org.cidadessustentaveis.dto.ComentarioDiscussaoFilhoDTO(cd.id, cd.forumDiscussao, cd.usuario, cd.comentario, cd.horarioPublicacao, cd.dataPublicacao, cd.editado, cd.horarioEdicao, cd.dataEdicao, cd.comentarioPai.id) "
			+ "from ComentarioDiscussaoFilho cd "
			+ "where cd.comentarioPai.id = :idComentarioPai order by cd.id asc")
	List<ComentarioDiscussaoFilhoDTO> buscarComentariosFilhosPorIdComentarioPai(Long idComentarioPai);
}
