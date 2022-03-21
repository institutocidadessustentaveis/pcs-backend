package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.planjementoIntegrado.RasterItem;
import br.org.cidadessustentaveis.repository.RasterItemRepository;	

@Service
@CacheConfig(cacheNames={"cache"})
public class RasterItemService {

	
	@Autowired
	private RasterItemRepository daoRasterItem;

	public List<RasterItem> findAll() {
		List<RasterItem>lista = daoRasterItem.findAll();
		return lista;
	}
}
