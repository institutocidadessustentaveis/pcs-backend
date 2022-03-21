package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.repository.InstituicaoFonteRepository;

@Service
public class InstituicaoFonteService {
	
	@Autowired
	private InstituicaoFonteRepository repository;
	
	public List<InstituicaoFonte> buscarComboBoxInstituicaoFonte(Long idCidade) {
		return repository.findComboBoxInstituicaoFonte(idCidade);
	}
	
	public InstituicaoFonte inserirInstituicaoFonte(InstituicaoFonte instituicaoFonte) {
		instituicaoFonte = repository.save(instituicaoFonte);
		return instituicaoFonte;
	}
	
	public InstituicaoFonte getById(Long id) {
		Optional<InstituicaoFonte> instituicaoFonte = repository.findById(id);
		return instituicaoFonte.orElse(null);
	}
	
	public InstituicaoFonte getByNomeAndCidadeId(String nome, Long idCidade) {
		List<InstituicaoFonte> instituicaoFonte = repository.findByNomeAndCidadeId(nome, idCidade);
		if(instituicaoFonte.isEmpty()) {
			return null;
		}
		return instituicaoFonte.get(0);
	}
	
}
