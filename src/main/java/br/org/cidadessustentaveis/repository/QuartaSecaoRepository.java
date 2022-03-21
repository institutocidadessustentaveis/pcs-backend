package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.org.cidadessustentaveis.dto.QuartaSecaoDTO;
import br.org.cidadessustentaveis.model.home.QuartaSecao;
	
@Repository
public interface QuartaSecaoRepository extends JpaRepository<QuartaSecao, Long>{
	
	@Query("select h " +	
            "from QuartaSecao h " +	
			"WHERE  h.home.id = ?1 and h.exibir = true order by h.indice")
	List<QuartaSecao> findByIdHome(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.QuartaSecaoDTO(q.id, q.indice, q.tituloPrincipal, q.exibir, q.tipo) " +	
            "from QuartaSecao q " +	
            "WHERE q.home.id = ?1 order by q.indice")
	List<QuartaSecaoDTO> buscarListaQuartaSecaoResumidaPorId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.QuartaSecaoDTO(q) " +	
            "from QuartaSecao q " +	
            "WHERE q.id = ?1")
	QuartaSecaoDTO buscarQuartaSecaoDetalhe(Long id);	
		
}
	