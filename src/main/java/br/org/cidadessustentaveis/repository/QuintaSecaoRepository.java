package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.org.cidadessustentaveis.dto.QuintaSecaoDTO;
import br.org.cidadessustentaveis.model.home.QuintaSecao;
	
@Repository
public interface QuintaSecaoRepository extends JpaRepository<QuintaSecao, Long>{
	
	@Query("select h " +	
            "from QuintaSecao h " +	
			"WHERE  h.home.id = ?1 and h.exibir = true order by h.indice")
	List<QuintaSecao> findByIdHome(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.QuintaSecaoDTO(q.id, q.indice, q.tituloPrincipal, q.exibir, q.tipo) " +	
            "from QuintaSecao q " +	
            "WHERE q.home.id = ?1 order by q.indice")
	List<QuintaSecaoDTO> buscarListaQuintaSecaoResumidaPorId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.QuintaSecaoDTO(q) " +	
            "from QuintaSecao q " +	
            "WHERE q.id = ?1")
	QuintaSecaoDTO buscarQuintaSecaoDetalhe(Long id);	
		
	
}
	