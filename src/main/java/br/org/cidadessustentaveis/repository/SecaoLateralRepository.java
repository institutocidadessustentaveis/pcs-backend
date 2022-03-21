package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.SecaoLateralDTO;
import br.org.cidadessustentaveis.model.home.SecaoLateral;
		
@Repository
public interface SecaoLateralRepository extends JpaRepository<SecaoLateral, Long>{
	
	@Query("select h " +	
            "from SecaoLateral h " +	
			"WHERE  h.home.id = ?1 and h.exibir = true order by h.indice")
	List<SecaoLateral> findByIdHome(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.SecaoLateralDTO(s.id, s.indice, s.primeiroTituloPrincipal, s.exibir, s.tipo) " +	
            "from SecaoLateral s " +	
            "WHERE s.home.id = ?1 order by s.indice")
	List<SecaoLateralDTO> buscarListaSecaoLateralResumidaPorId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.SecaoLateralDTO(s) " +	
            "from SecaoLateral s " +	
            "WHERE s.id = ?1")
	SecaoLateralDTO buscarSecaoLateralDetalhe(Long id);	
		
}
		