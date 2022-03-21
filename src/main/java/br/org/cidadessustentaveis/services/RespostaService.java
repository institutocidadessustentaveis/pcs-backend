package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.participacaoCidada.Resposta;
import br.org.cidadessustentaveis.repository.RespostaRepository;

@Service
public class RespostaService {
	@Autowired
	private RespostaRepository repository;
	
	public void excluirRespostasSemPergunta(){
		List<Long> ids = this.repository.respostasSemPerguntas();
		ids.forEach(id -> this.repository.deleteById(id));
	}

	public Resposta getById(Long idResposta) {
		Resposta resposta = this.repository.getOne(idResposta);
		return resposta;
	}
}
