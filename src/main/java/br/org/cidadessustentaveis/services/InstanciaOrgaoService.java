package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.InstanciaOrgao;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.repository.InstanciaOrgaoRepository;

@Service
public class InstanciaOrgaoService {
	
	@Autowired
	private InstanciaOrgaoRepository repository;
	
	public List<InstanciaOrgao> buscarComboBoxInstanciaOrgao() {
		return repository.findComboBoxInstanciaOrgao();
	}
	
	public InstanciaOrgao inserirInstanciaOrgao(InstanciaOrgao instanciaOrgao) {
		instanciaOrgao = repository.save(instanciaOrgao);
		return instanciaOrgao;
	}

	public InstanciaOrgao buscarPorNome(String nomeInstanciaOrgao) {
		InstanciaOrgao instancia = repository.findByNome(nomeInstanciaOrgao);

		return instancia;
	}	
}
