package br.org.cidadessustentaveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;

@Repository
public interface AreaInteresseRepository extends JpaRepository<AreaInteresse, Long>{
	
	@Query("select area from AreaInteresse area where area.id in ?1")
	List<AreaInteresse> buscarAreasPorIds(List<Long> ids);

	List<AreaInteresse> findAllByOrderByNome();
	
}

	
