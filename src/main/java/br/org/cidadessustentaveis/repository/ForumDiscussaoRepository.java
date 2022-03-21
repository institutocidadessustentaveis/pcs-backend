package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.ForumDiscussaoListDTO;
import br.org.cidadessustentaveis.dto.ForumDiscussaoPrincipalDTO;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;

@Repository
public interface ForumDiscussaoRepository extends JpaRepository<ForumDiscussao, Long>{
	
	@Query("select new br.org.cidadessustentaveis.dto.ForumDiscussaoPrincipalDTO(f.id, f.ativo, f.dataHoraCriacao, f.titulo, f.numeroDeRespostas, f.numeroDeVisualizacao, usuario, f.publico) from ForumDiscussao f"
			+ " left join f.usuarioUltimaPostagem usuario")
	List<ForumDiscussaoPrincipalDTO> buscarDiscussoesToList();
	
	@Query("select new br.org.cidadessustentaveis.dto.ForumDiscussaoListDTO(f, usuario, prefeitura) from ForumDiscussao f"
            + " left join f.usuarioUltimaPostagem usuario"
            + " left join f.prefeitura prefeitura"
            + " order by f.dataHoraCriacao desc")
	List<ForumDiscussaoListDTO> buscarListaDiscussoes();
	
	@Query("select new br.org.cidadessustentaveis.dto.ForumDiscussaoListDTO(f, usuario, prefeitura) from ForumDiscussao f"
            + " left join f.usuarioUltimaPostagem usuario"
            + " left join f.prefeitura prefeitura"
            + " where prefeitura.id = :idPrefeitura")
	List<ForumDiscussaoListDTO> buscarListaDiscussoesPorIdPrefeitura(Long idPrefeitura);

}
