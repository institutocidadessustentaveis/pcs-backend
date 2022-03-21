package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.HomeDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO;
import br.org.cidadessustentaveis.model.home.Home;
	
@Repository
public interface HomeRepository extends JpaRepository<Home, Long>{

	@Query("SELECT home FROM Home home WHERE home.link_pagina like ?1")
	Home findByLink(String link);
	
	@Query("SELECT home.id FROM Home home WHERE home.link_pagina like ?1")
	Long findByLinkOnlyId(String link);

	
	@Query("select i " +
            "from Home i " +	
            "order by i.id desc")
	List<Home> carregarUltimasHome(Pageable a);

    @Query("SELECT " +
            "new br.org.cidadessustentaveis.dto.HomeDTO(h.id, h.link_pagina, h.titulo,h) " +
            "FROM " +
            "    Home h " +
            "WHERE " +
            "     h.link_pagina like ?1 ")
    HomeDTO findAllIdsByLink(String link);
    
    
    @Query("SELECT " +
            "new br.org.cidadessustentaveis.dto.HomeDTO(h.id, h.link_pagina, h.titulo, h) " +
            "FROM " +
            "    Home h " +
            "WHERE " +
            "     h.id = :idHome ")
    HomeDTO findAllIdsByIdHome(Long idHome);
    
    
    @Query("SELECT " +
            "new br.org.cidadessustentaveis.dto.HomeDTO(home.id, home.link_pagina, home.titulo, home.exibir) " +
            "FROM " +
            "    Home home order by home.titulo")
	List<HomeDTO> findAllByOrderByTituloAsc();

}
