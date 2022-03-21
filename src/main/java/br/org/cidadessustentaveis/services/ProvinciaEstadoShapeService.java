package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.ProvinciaEstadoShape;
import br.org.cidadessustentaveis.repository.ProvinciaEstadoShapeRepository;

@Service
@CacheConfig(cacheNames={"shapes_estados"})
public class ProvinciaEstadoShapeService {

	@Autowired
	private ProvinciaEstadoShapeRepository dao;

	public ProvinciaEstadoShape buscarPorEstado(Long idEstado) {
		return dao.buscarPorEstado(idEstado);
	}

	@Cacheable
	public List<ProvinciaEstadoShape> buscarPorEstados(List<Long> idsEstados) {
		return dao.buscarPorIdsEstado(idsEstados);
	}

	public List<ProvinciaEstadoShape> findAll() {
		return dao.findAll();
	}

}
