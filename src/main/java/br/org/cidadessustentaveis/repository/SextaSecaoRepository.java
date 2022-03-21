package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.SextaSecaoDTO;
import br.org.cidadessustentaveis.model.home.SextaSecao;
	
@Repository
public interface SextaSecaoRepository extends JpaRepository<SextaSecao, Long>{
		
	@Query("select h " +	
            "from SextaSecao h " +	
            "WHERE  h.home.id = ?1 and h.exibir = true order by h.indice")
	List<SextaSecao> findByIdHome(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.SextaSecaoDTO(t.id, t.indice, t.tituloPrincipal, t.exibir, t.tipo) " +	
            "from SextaSecao t " +	
            "WHERE t.home.id = ?1 order by t.indice")
	List<SextaSecaoDTO> buscarListaSextaSecaoResumidaPorId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.SextaSecaoDTO(t) " +	
            "from SextaSecao t " +	
            "WHERE t.id = ?1")	
	SextaSecaoDTO buscarSextaSecaoDetalhe(Long id);	
}
		