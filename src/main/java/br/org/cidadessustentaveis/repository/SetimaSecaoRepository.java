package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.SetimaSecaoDTO;
import br.org.cidadessustentaveis.model.home.SetimaSecao;
	
@Repository
public interface SetimaSecaoRepository extends JpaRepository<SetimaSecao, Long>{
	
	@Query("select h " +	
            "from SetimaSecao h " +	
            "WHERE h.home.id = ?1 and h.exibir = true order by h.indice")
	List<SetimaSecao> findByIdHome(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.SetimaSecaoDTO(s.id, s.indice, s.tituloPrincipal, s.exibir, s.tipo) " +	
            "from SetimaSecao s " +	
            "WHERE s.home.id = ?1 order by s.indice")
	List<SetimaSecaoDTO> buscarListaSetimaSecaoResumidaPorId(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.SetimaSecaoDTO(s) " +	
            "from SetimaSecao s " +	
            "WHERE s.id = ?1")
	SetimaSecaoDTO buscarSetimaSecaoDetalhe(Long id);	
		
}
