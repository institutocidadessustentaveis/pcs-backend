package br.org.cidadessustentaveis.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.repository.ArquivoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class ArquivoService {
	
	@Autowired
	private ArquivoRepository repository;
	
	public Arquivo buscarPorId(Long id) {
		Optional<Arquivo> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Arquivo não encontrado!"));
	}
	
	public void deletarArquivoPorId(Long idArquivo){
		Arquivo arquivo = buscarPorId(idArquivo);
		repository.delete(arquivo);
	}
	
	public Arquivo buscarReferencia(Long idArquivo){
		Arquivo arquivo = repository.getOne(idArquivo);
		return arquivo;
	}

	public Arquivo salvar(Arquivo arquivo) {
		return repository.save(arquivo);
	}

}
