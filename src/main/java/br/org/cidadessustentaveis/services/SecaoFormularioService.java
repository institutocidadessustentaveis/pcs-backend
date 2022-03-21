package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.repository.SecaoFormularioRepository;

@Service
public class SecaoFormularioService {
	@Autowired
	private SecaoFormularioRepository repository;
	
	public void excluirSecoesSemFormulario(){
		List<Long> ids = this.repository.secoesSemFormulario();
		ids.forEach(id -> this.repository.deleteById(id));
	}
}
