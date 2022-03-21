package br.org.cidadessustentaveis.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.services.AjusteGeralService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/ajuste-geral")
public class AjusteGeralResource {
	
	@Autowired
	AjusteGeralService service;
	
	@PostMapping("/inserir-ajuste")
	@Secured({"ROLE_CADASTRAR_AJUSTE_GERAL"})
	public ResponseEntity inserirAjuste(@RequestBody AjusteGeralDTO ajusteGeralDTO) {
		service.inserirAjuste(ajusteGeralDTO);
		return ResponseEntity.ok(201);
	}
	
	@GetMapping("/buscarAjustePorLocalApp")
	public ResponseEntity<AjusteGeralDTO> buscarAjustePorLocalApp(@RequestParam String localApp) {
		AjusteGeralDTO ajuste = service.buscarAjustePorLocalApp(localApp);
		return ResponseEntity.ok().body(ajuste);
	}
	
	@GetMapping("/buscarListaAjustes")
	public ResponseEntity<List<AjusteGeralDTO>> buscarListaAjustes(@RequestParam String localApp) {
		List<AjusteGeralDTO> ajuste = service.buscarListaAjustes(localApp);
		return ResponseEntity.ok().body(ajuste);
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity deletar(@PathVariable Long id) {
		service.deletar(id);
		return ResponseEntity.ok(201);
	}
}
