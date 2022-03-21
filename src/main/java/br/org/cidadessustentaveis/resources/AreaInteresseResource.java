package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.AreaInteresseDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.services.AreaInteresseService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/areaInteresse")
public class AreaInteresseResource {

	@Autowired
	private AreaInteresseService service;
	
	@GetMapping("")	
	public ResponseEntity<List<AreaInteresseDTO>> areasInteresses(){
		List<AreaInteresse> area = service.buscaAreaInteresses();		
		List<AreaInteresseDTO> areaDTO = area.stream().map(obj -> new AreaInteresseDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(areaDTO);
	}
	
	}
	

