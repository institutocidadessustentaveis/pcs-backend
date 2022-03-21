package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.dto.TemaForumDTO;
import br.org.cidadessustentaveis.model.biblioteca.TemaForum;

@Repository
public interface TemaForumRepository extends JpaRepository<TemaForum, Long>{

	@Query("select new br.org.cidadessustentaveis.dto.TemaForumDTO(t) from TemaForum t")
	List<TemaForumDTO> buscarListaTemaForum();
}