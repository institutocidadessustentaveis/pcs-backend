package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.AreaAtuacao;
import br.org.cidadessustentaveis.repository.AreaAtuacaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class AreaAtuacaoService {
	
	@Autowired
	private AreaAtuacaoRepository repo;
	
	public List<AreaAtuacao> buscaAreasAtuacoes(){
		return repo.findAll();	
	}
	
	public AreaAtuacao buscarPorId(Long id) {
		Optional<AreaAtuacao> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Area de atuação não encontrada!"));
	}
	
}
