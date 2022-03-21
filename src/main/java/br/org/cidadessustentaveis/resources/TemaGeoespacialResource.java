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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.TemaGeoespacialDTO;
import br.org.cidadessustentaveis.dto.TemaGeoespacialExibicaoDTO;
import br.org.cidadessustentaveis.services.TemaGeoespacialService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/tema-geoespacial")
public class TemaGeoespacialResource {
	
	@Autowired
	private TemaGeoespacialService temaGeoespacialService;

	@GetMapping("")
	public ResponseEntity<List<TemaGeoespacialDTO>> buscarTodos(){
		List<TemaGeoespacialDTO> dtos = temaGeoespacialService.buscarTodos();
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<TemaGeoespacialExibicaoDTO>> buscarTodosSimples(){
		List<TemaGeoespacialExibicaoDTO> dtos = temaGeoespacialService.buscarTodosSimples();
		return ResponseEntity.ok().body(dtos);
	}

	@GetMapping("/{id}")
	public TemaGeoespacialDTO buscarPorId( @PathVariable("id") Long id) {
		TemaGeoespacialDTO dto = temaGeoespacialService.buscarPorId(id);
		return dto;
	}
	
	
	

	@Secured({"ROLE_CADASTRAR_TEMA_GEOESPACIAL"})
	@PostMapping("")
	public void salvar( @RequestBody TemaGeoespacialDTO dto) {
		temaGeoespacialService.salvar(dto);		
	}
	
	@Secured({"ROLE_EDITAR_TEMA_GEOESPACIAL"})
	@PutMapping("")
	public void atualizar( @RequestBody TemaGeoespacialDTO dto) {
		temaGeoespacialService.atualizar(dto);				
	}

	@Secured({"ROLE_DELETAR_TEMA_GEOESPACIAL"})
	@DeleteMapping("{id}")
	public void remover(@PathVariable("id") Long id) {
		temaGeoespacialService.remover(id);
	}
}
