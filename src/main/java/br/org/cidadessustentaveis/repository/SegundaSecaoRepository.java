package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.org.cidadessustentaveis.dto.SegundaSecaoDTO;
import br.org.cidadessustentaveis.model.home.SegundaSecao;
	
@Repository
public interface SegundaSecaoRepository extends JpaRepository<SegundaSecao, Long>{
	
	@Query("select h " +	
            "from SegundaSecao h " +	
            "WHERE h.home.id = ?1 and h.exibir = true order by h.indice")
	List<SegundaSecao> findByIdHome(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.SegundaSecaoDTO(s.id, s.indice, s.tituloPrincipal, s.exibir, s.tipo) " +	
            "from SegundaSecao s " +	
            "WHERE s.home.id = ?1 order by s.indice")
	List<SegundaSecaoDTO> buscarListaSegundaSecaoResumidaPorId(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.SegundaSecaoDTO(s) " +	
            "from SegundaSecao s " +	
            "WHERE s.id = ?1")
	SegundaSecaoDTO buscarSegundaSecaoDetalhe(Long id);	
		
}
