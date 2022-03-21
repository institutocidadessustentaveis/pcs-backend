package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao03DTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao03;
	
@Repository
public interface InstitucionalDinamicoSecao03Repository extends JpaRepository<InstitucionalDinamicoSecao03, Long>{
	
	@Query("select h " +	
            "from InstitucionalDinamicoSecao03 h " +	
            "WHERE h.institucionalDinamico.id = ?1 and h.exibir = true order by h.indice")
	List<InstitucionalDinamicoSecao03> findByIdInstitucionalDinamico(Long id);	
	
	
	@Query("select new br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao03DTO(p.id, p.indice, p.titulo, p.exibir, p.tipo, p.corFundo) " +	
            "from InstitucionalDinamicoSecao03 p " +	
            "WHERE p.institucionalDinamico.id = ?1 order by p.indice")
	List<InstitucionalDinamicoSecao03DTO> buscarListaInstitucionalDinamicoSecao03ResumidaPorId(Long id);	
	
}
