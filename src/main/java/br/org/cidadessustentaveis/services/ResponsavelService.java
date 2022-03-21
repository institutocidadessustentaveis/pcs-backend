package br.org.cidadessustentaveis.services;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ResponsavelDTO;
import br.org.cidadessustentaveis.model.administracao.Responsavel;
import br.org.cidadessustentaveis.repository.ResponsavelRepository;

@Service
public class ResponsavelService {

	@Autowired
	private ResponsavelRepository repo;
	
	public List<Responsavel> buscaPapeis(){
		return repo.findAll();	
	}

	public Responsavel inserirResponsavel(@Valid ResponsavelDTO responsavelDto) {
		Responsavel responsavel = responsavelDto.toEntityInsertResponsavel();
		responsavel = repo.save(responsavel);
		return responsavel;

	}
	
}
