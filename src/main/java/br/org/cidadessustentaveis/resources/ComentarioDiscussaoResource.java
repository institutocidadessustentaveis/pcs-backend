package br.org.cidadessustentaveis.resources;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ComentarioDTO;
import br.org.cidadessustentaveis.dto.ComentarioDiscussaoDTO;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;
import br.org.cidadessustentaveis.services.ComentarioDiscussaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/comentarioDiscussao")
public class ComentarioDiscussaoResource {

	@Autowired(required= true)
	private ComentarioDiscussaoService service;
	
	@Secured({ "ROLE_CADASTRAR_COMENTARIO_DISCUSSAO" })
	@PostMapping("/cadastrar")
	public ResponseEntity<ComentarioDiscussaoDTO> cadastrar(@RequestBody ComentarioDiscussaoDTO comentarioDiscussao) {
		service.inserir(comentarioDiscussao);
		return ResponseEntity.ok(comentarioDiscussao);
	}
	
	@Secured({ "ROLE_EDITAR_COMENTARIO_DISCUSSAO" })
	@PutMapping("/editar")
	public ResponseEntity<ComentarioDTO> alterar(@RequestBody  ComentarioDiscussaoDTO comentarioDiscussao) throws Exception {
		ComentarioDiscussao comentario = service.alterar(comentarioDiscussao);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comentario.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({ "ROLE_EXCLUIR_COMENTARIO_DISCUSSAO" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/buscarComentariosDiscussaoPorId/{id}")
	public ResponseEntity<ComentarioDiscussaoDTO> buscarComentariosDiscussaoPorId(@PathVariable("id") Long id){
		ComentarioDiscussaoDTO comentarioDiscussaoDTO = service.buscarComentarioDiscussaoPorId(id);
		return ResponseEntity.ok().body(comentarioDiscussaoDTO);
	}
	
	@GetMapping("/buscarComentariosDiscussaoPorIdDiscussao/{id}")
	public ResponseEntity<List<ComentarioDiscussaoDTO>> buscarComentariosDiscussaoPorIdDiscussao(@PathVariable("id") Long id){
		List<ComentarioDiscussaoDTO> comentarioDiscussaoDTO = service.buscarComentariosDiscussaoPorIdDiscussao(id);
		return ResponseEntity.ok().body(comentarioDiscussaoDTO);
	}

}
