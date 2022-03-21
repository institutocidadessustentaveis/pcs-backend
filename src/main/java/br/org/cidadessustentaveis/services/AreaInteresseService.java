package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.repository.AreaInteresseRepository;
import br.org.cidadessustentaveis.repository.ShapeFileRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class AreaInteresseService {
	
	@Autowired
	private AreaInteresseRepository repo;
	
	
	public List<AreaInteresse> buscaAreaInteresses(){
		 List<AreaInteresse> lista = repo.findAllByOrderByNome();
		 return lista;
	}
	
	public AreaInteresse buscarPorId(Long id) {
		Optional<AreaInteresse> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Area de interesse não encontrada!"));
	}
	
	public List<AreaInteresse> buscarAreasPorIds(List<Long> ids) {
		return repo.buscarAreasPorIds(ids);
	}
	
	
}
