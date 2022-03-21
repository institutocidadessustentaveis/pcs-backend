package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao01DTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao01;
	
@Repository
public interface InstitucionalDinamicoSecao01Repository extends JpaRepository<InstitucionalDinamicoSecao01, Long>{
	
	@Query("select h " +	
            "from InstitucionalDinamicoSecao01 h " +	
            "WHERE h.institucionalDinamico.id = ?1 and h.exibir = true order by h.indice")
	List<InstitucionalDinamicoSecao01> findByIdInstitucionalDinamico(Long id);	
	
	
	@Query("select new br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao01DTO(p.id, p.indice, p.titulo, p.exibir, p.tipo) " +	
            "from InstitucionalDinamicoSecao01 p " +	
            "WHERE p.institucionalDinamico.id = ?1 order by p.indice")
	List<InstitucionalDinamicoSecao01DTO> buscarListaInstitucionalDinamicoSecao01ResumidaPorId(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao01DTO(p) " +	
            "from InstitucionalDinamicoSecao01 p " +	
            "WHERE p.id = ?1")	
	InstitucionalDinamicoSecao01DTO buscarInstitucionalDinamicoSecao01Detalhe(Long id);	
	
}
