package br.org.cidadessustentaveis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.sistema.ParametroGeral;
import br.org.cidadessustentaveis.repository.ParametroGeralRepository;

@Service
public class ParametroGeralService {
	@Autowired
	private ParametroGeralRepository repository;
	
	public ParametroGeral buscar() {
		return repository.findEmailSugestaoBoaPratica();
	}

}
