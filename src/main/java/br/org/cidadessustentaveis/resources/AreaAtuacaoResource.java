package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.AreaAtuacaoDTO;
import br.org.cidadessustentaveis.model.administracao.AreaAtuacao;
import br.org.cidadessustentaveis.services.AreaAtuacaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/areaAtuacao")
public class AreaAtuacaoResource {

	@Autowired
	private AreaAtuacaoService service;
	
	@GetMapping("/")	
	public ResponseEntity<List<AreaAtuacaoDTO>> areasAtuacoes(){
		List<AreaAtuacao> area = service.buscaAreasAtuacoes();		
		List<AreaAtuacaoDTO> areaDTO = area.stream().map(obj -> new AreaAtuacaoDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(areaDTO);
	}
	
}
