package br.org.cidadessustentaveis.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.PropostaMunicipioDTO;
import br.org.cidadessustentaveis.services.PropostaMunicipioService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/proposta-municipio")
public class PropostaMunicipioResource {

	@Autowired
	private PropostaMunicipioService service;
	
	@PostMapping
	public ResponseEntity<String> visualizar(@RequestBody PropostaMunicipioDTO dto ){
		try{
			service.salvar(dto);
			return ResponseEntity.ok().build();
		} catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
}
