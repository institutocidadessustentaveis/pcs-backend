package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.TerceiraSecaoDTO;
import br.org.cidadessustentaveis.model.home.TerceiraSecao;
	
@Repository
public interface TerceiraSecaoRepository extends JpaRepository<TerceiraSecao, Long>{
		
	@Query("select h " +	
            "from TerceiraSecao h " +	
            "WHERE  h.home.id = ?1 and h.exibir = true order by h.indice")
	List<TerceiraSecao> findByIdHome(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.TerceiraSecaoDTO(t.id, t.indice, t.tituloPrincipal, t.exibir, t.tipo) " +	
            "from TerceiraSecao t " +	
            "WHERE t.home.id = ?1 order by t.indice")
	List<TerceiraSecaoDTO> buscarListaTerceiraSecaoResumidaPorId(Long id);
	
	@Query("select new br.org.cidadessustentaveis.dto.TerceiraSecaoDTO(t) " +	
            "from TerceiraSecao t " +	
            "WHERE t.id = ?1")
	TerceiraSecaoDTO buscarTerceiraSecaoDetalhe(Long id);	
}
	