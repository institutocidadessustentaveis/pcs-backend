package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.InstituicaoDTO;
import br.org.cidadessustentaveis.model.administracao.Instituicao;
import br.org.cidadessustentaveis.services.InstituicaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/instituicoes")
public class InstituicaoResource {

	@Autowired
	private InstituicaoService service;
	
	@GetMapping("/")	
	public ResponseEntity<List<InstituicaoDTO>> intituicoes(){
		List<Instituicao> instituicaoList = service.buscaInstituicoes();		
		List<InstituicaoDTO> instituicaoDTO = instituicaoList.stream().map(obj -> new InstituicaoDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(instituicaoDTO);
	}
	
}
