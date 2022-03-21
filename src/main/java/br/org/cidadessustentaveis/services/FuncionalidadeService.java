package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.Funcionalidade;
import br.org.cidadessustentaveis.repository.FuncionalidadeRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class FuncionalidadeService {
	
	@Autowired
	private FuncionalidadeRepository repository;
	
	public List<Funcionalidade> buscarFuncionalidades() {
		return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
	}
	
	public Funcionalidade buscarPorId(Long id) {
		Optional<Funcionalidade> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Funcionalidade não encontrada!"));
	}
	
	public List<Funcionalidade> buscarNotIn(List<Long > ids) {
		if(ids.isEmpty()) {
			ids.add(0l);
		}
		return repository.findByIdNotIn(ids);
	}

}
