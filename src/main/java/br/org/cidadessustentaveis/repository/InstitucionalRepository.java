package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.institucional.Institucional;

@Repository
public interface InstitucionalRepository extends JpaRepository<Institucional, Long>{

	@Query("SELECT pagina FROM Institucional pagina WHERE pagina.link_pagina like ?1")
	Institucional findByLink(String link);
	
	@Query("SELECT pagina.id FROM Institucional pagina WHERE pagina.link_pagina like ?1")
	Long findByLinkOnlyId(String link);

	List<Institucional> findAllByOrderByTituloAsc();
	
	@Query("select i " +
            "from Institucional i " +
            "order by i.id desc")
	List<Institucional> carregarUltimasInstitucional(Pageable a);

	Institucional findByTemplate03Id(Long idTemplate03);

}
