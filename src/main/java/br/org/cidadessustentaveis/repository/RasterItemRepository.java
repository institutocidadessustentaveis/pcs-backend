package br.org.cidadessustentaveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.planjementoIntegrado.RasterItem;

@Repository
public interface RasterItemRepository extends JpaRepository<RasterItem, Long>{
	
	
	@Query(value = "select max(r.id) from raster_itens r", nativeQuery = true)
	Long findMaxId();
}

