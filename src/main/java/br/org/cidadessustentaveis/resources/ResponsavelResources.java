package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ResponsavelDTO;
import br.org.cidadessustentaveis.model.administracao.Responsavel;
import br.org.cidadessustentaveis.services.ResponsavelService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/responsavel")
public class ResponsavelResources {

	@Autowired
	private ResponsavelService service;
	
	@GetMapping("/")	
	public ResponseEntity<List<ResponsavelDTO>> papeis(){
		
		
		
		List<Responsavel> area = service.buscaPapeis();		
		List<ResponsavelDTO> areaDTO = area.stream().map(obj -> new ResponsavelDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(areaDTO);
	}
	
	@PostMapping("/inserirResponsavel")
	public ResponseEntity<Void> inserirResponsavel(@Valid @RequestBody ResponsavelDTO responsavelDto) throws EmailException {
		Responsavel responsavel = service.inserirResponsavel(responsavelDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(responsavel.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}	
}