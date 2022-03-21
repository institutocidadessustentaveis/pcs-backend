package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.Instituicao;
import br.org.cidadessustentaveis.repository.InstituicaoRepository;

@Service
public class InstituicaoService {
	
	@Autowired
	private InstituicaoRepository repo;
	
	public List<Instituicao> buscaInstituicoes(){
		return repo.findAll();	
	}
	
}
