package br.org.cidadessustentaveis.resources;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.SugestaoBoaPraticaDTO;
import br.org.cidadessustentaveis.model.boaspraticas.SugestaoBoasPraticas;
import br.org.cidadessustentaveis.services.SugestaoBoasPraticasService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/sugestaoboaspraticas")
public class SugestaoBoasPraticasResource {

	@Autowired
	private SugestaoBoasPraticasService service;//////////

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Secured({"ROLE_CADASTRAR_SUGESTAO_BOAS_PRATICAS"})
	@PostMapping("/inserirSugestaoBoasPraticas")
	public ResponseEntity<?> inserir(@RequestBody SugestaoBoasPraticas sugestaoBoasPraticas) {
		SugestaoBoasPraticas sugestao;
		try {
			sugestao = service.inserirSugestaoBoasPraticas(sugestaoBoasPraticas);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sugestao.getId()).toUri();
			return ResponseEntity.created(uri).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	@Secured({"ROLE_VISUALIZAR_SUGESTAO_BOA_PRATICA"})
	@GetMapping("/buscar")
	public ResponseEntity<Page<SugestaoBoasPraticas>> buscar(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPerPage", defaultValue = "50") Integer linesPerPage) {
		Page<SugestaoBoasPraticas> sugestoesBoaspraticas = service.buscar(page, linesPerPage);
		return ResponseEntity.ok().body(sugestoesBoaspraticas);
	}
	
	@Secured({"ROLE_VISUALIZAR_SUGESTAO_BOA_PRATICA"})
	@GetMapping("/buscaPorId/{id}")
	public ResponseEntity<SugestaoBoaPraticaDTO> buscaPorId(@PathVariable("id") Long id) {
		SugestaoBoasPraticas sugestaoBoaPratica = service.buscarPorId(id).get();
		return ResponseEntity.ok().body(new SugestaoBoaPraticaDTO(sugestaoBoaPratica));
	}
	
	@Secured({"ROLE_DELETAR_SUGESTAO_BOA_PRATICA"})
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}	
	
}
