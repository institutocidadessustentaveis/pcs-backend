package br.org.cidadessustentaveis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.participacaoCidada.Pergunta;
import br.org.cidadessustentaveis.repository.PerguntaRepository;

@Service
public class PerguntaService {
	@Autowired
	private PerguntaRepository repository;
	
	public void excluirPerguntasSemSecao(){
		List<Long> ids = this.repository.perguntasSemSecao();
		ids.forEach(id -> this.repository.deleteById(id));
	}

	public Pergunta findById(Long idPergunta) {
		Pergunta pergunta = this.repository.getOne(idPergunta);
		return pergunta;
	}
}
