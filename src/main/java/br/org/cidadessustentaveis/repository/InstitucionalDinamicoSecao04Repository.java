package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao04DTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao04;
		
@Repository
public interface InstitucionalDinamicoSecao04Repository extends JpaRepository<InstitucionalDinamicoSecao04, Long>{
		
	@Query("select h " +	
            "from InstitucionalDinamicoSecao04 h " +	
            "WHERE h.institucionalDinamico.id = ?1 and h.exibir = true order by h.indice")
	List<InstitucionalDinamicoSecao04> findByIdInstitucionalDinamico(Long id);	
	
	
	@Query("select new br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao04DTO(p) " +	
            "from InstitucionalDinamicoSecao04 p " +	
            "WHERE p.institucionalDinamico.id = ?1 order by p.indice")
	List<InstitucionalDinamicoSecao04DTO> buscarListaInstitucionalDinamicoSecao04ResumidaPorId(Long id);		
	
	@Query("select new br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao04DTO(p) " +	
            "from InstitucionalDinamicoSecao04 p " +	
            "WHERE p.id = ?1")	
	InstitucionalDinamicoSecao04DTO buscarInstitucionalDinamicoSecao04Detalhe(Long id);	
	
}
