package br.org.cidadessustentaveis.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.BoletimTemplate01DTO;
import br.org.cidadessustentaveis.model.noticias.BoletimTemplate01;
import br.org.cidadessustentaveis.repository.BoletimTemplate01Repository;
import br.org.cidadessustentaveis.repository.NewsletterRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class BoletimTemplate01Service {
	
	@Autowired
	BoletimInformativoService boletimService;
	
	@Autowired
	NewsletterRepository newsLetterRpository;
	
	@Autowired
	BoletimTemplate01Repository template01Repository;
	
	public Long salvarTemplate01(BoletimTemplate01DTO boletimTemplate01DTO) throws Exception {
		BoletimTemplate01 template01 = boletimTemplate01DTO.toEntityInsert(boletimTemplate01DTO);
		
		return boletimService.gravarDadosDoBoletimTemplate01(template01);
	}
	
	public BoletimTemplate01 buscarPorId(Long id) {
		Optional<BoletimTemplate01> template01 = template01Repository.findById(id);
		return template01.orElseThrow(() -> new ObjectNotFoundException("Boletim não encontrado!"));
	}
	
	public void editarBoletimTemplate01(BoletimTemplate01DTO boletimTemplate01DTO) throws Exception {
		BoletimTemplate01 template01 = boletimTemplate01DTO.toEntityUpdate(buscarPorId(boletimTemplate01DTO.getId()));
		
		template01Repository.save(template01);
	}
}
