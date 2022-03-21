package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao02DTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao02;
	
@Repository
public interface InstitucionalDinamicoSecao02Repository extends JpaRepository<InstitucionalDinamicoSecao02, Long>{
	
	@Query("select h " +	
            "from InstitucionalDinamicoSecao02 h " +	
            "WHERE h.institucionalDinamico.id = ?1 and h.exibir = true order by h.indice")
	List<InstitucionalDinamicoSecao02> findByIdInstitucionalDinamico(Long id);	
	
	
	@Query("select new br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao02DTO(p.id, p.indice, p.titulo, p.exibir, p.tipo, p.corFundo) " +	
            "from InstitucionalDinamicoSecao02 p " +	
            "WHERE p.institucionalDinamico.id = ?1 order by p.indice")
	List<InstitucionalDinamicoSecao02DTO> buscarListaInstitucionalDinamicoSecao02ResumidaPorId(Long id);	
	
	@Query("select new br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao02DTO(p) " +	
            "from InstitucionalDinamicoSecao02 p " +	
            "WHERE p.id = ?1")	
	InstitucionalDinamicoSecao02DTO buscarInstitucionalDinamicoSecao02Detalhe(Long id);	
	
}
