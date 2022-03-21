package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.home.HomeBarra;
	
@Repository
public interface HomeBarraRepository extends JpaRepository<HomeBarra, Long>{
	
	@Query("select h " +	
            "from HomeBarra h " +	
            "WHERE h.id = ?1")
	HomeBarra findByIdHomeBarra(Long id);
	
}
