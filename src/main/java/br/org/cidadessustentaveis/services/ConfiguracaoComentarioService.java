package br.org.cidadessustentaveis.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.participacaoCidada.ConfiguracaoComentario;
import br.org.cidadessustentaveis.repository.ConfiguracaoComentarioRepository;

@Service
public class ConfiguracaoComentarioService {
	@Autowired
	private ConfiguracaoComentarioRepository repository;

	public ConfiguracaoComentario buscar() {
		return repository.findTamanhoComentario();
	}
	
	public void atualizar(Long tamanhoMaximoComentario) {
		ConfiguracaoComentario configuracaoComentario = repository.findTamanhoComentario();
		Long idConfiguracao = configuracaoComentario.getId();
		Optional<ConfiguracaoComentario> configuracaoComentarioOptional = repository.findById(idConfiguracao);
		ConfiguracaoComentario configuracao = configuracaoComentarioOptional.get();
		configuracao.setTamanhoComentario(tamanhoMaximoComentario);
		this.repository.save(configuracao);
		
	}
	


}
