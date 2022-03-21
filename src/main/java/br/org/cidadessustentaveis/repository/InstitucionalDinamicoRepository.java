package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamico;
	
@Repository
public interface InstitucionalDinamicoRepository extends JpaRepository<InstitucionalDinamico, Long>{

	@Query("SELECT inst FROM InstitucionalDinamico inst WHERE inst.link_pagina like ?1")
	InstitucionalDinamico findByLink(String link);
	
	@Query("SELECT inst.id FROM InstitucionalDinamico inst WHERE inst.link_pagina like ?1")
	Long findByLinkOnlyId(String link);
	
    @Query("SELECT " +
            "new br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO(inst.id, inst.titulo, inst.link_pagina, inst.exibir) " +
            "FROM " +
            "    InstitucionalDinamico inst order by inst.titulo")
	List<InstitucionalDinamicoDTO> findAllByOrderByTituloAsc();
	
	@Query("select i " +
            "from InstitucionalDinamico i " +	
            "order by i.id desc")
	List<InstitucionalDinamico> carregarUltimasInstitucionalDinamico(Pageable a);

    @Query("SELECT " +
            "new br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO(inst) " +
            "FROM " +
            "    InstitucionalDinamico inst " +
            "WHERE " +
            "     inst.link_pagina like ?1 and inst.exibir = true ")
    InstitucionalDinamicoDTO findAllIdsByLink(String link);
    
    
    @Query("SELECT " +
            "new br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO(inst) " +
            "FROM " +
            "    InstitucionalDinamico inst " +
            "WHERE " +
            "     inst.id = :idInstitucionalDinamico ")
    InstitucionalDinamicoDTO findAllIdsByIdInstitucionalDinamico(Long idInstitucionalDinamico);

}
