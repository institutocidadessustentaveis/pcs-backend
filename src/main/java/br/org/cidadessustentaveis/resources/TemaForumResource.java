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

import br.org.cidadessustentaveis.dto.TemaForumDTO;
import br.org.cidadessustentaveis.model.biblioteca.TemaForum;
import br.org.cidadessustentaveis.services.TemaForumService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/temaforum")
public class TemaForumResource {

	@Autowired
	private TemaForumService temaForumService;
	
	@GetMapping("/buscarListaTemaForum")
	public ResponseEntity<List<TemaForumDTO>> buscarListaTemaForum(){
		List<TemaForumDTO> temaForumListDTO = temaForumService.buscarListaTemaForum();
		return ResponseEntity.ok().body(temaForumListDTO);
	}
	
	@Secured({ "ROLE_CADASTRAR_TEMA_FORUM" })
	@PostMapping("/cadastrar")	
	public TemaForumDTO cadastrar(@RequestBody TemaForumDTO temaForumDTO) throws Exception {
		temaForumService.cadastrar(temaForumDTO);
		return temaForumDTO;
	}
	
	@Secured({ "ROLE_EDITAR_TEMA_FORUM" })
	@PutMapping("/editar")
	public ResponseEntity<TemaForumDTO> editar(@RequestBody TemaForumDTO temaForumDTO) throws Exception {
		TemaForum temaForum = temaForumService.editar(temaForumDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(temaForum.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({ "ROLE_EXCLUIR_TEMA_FORUM" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> excluir( @PathVariable Long id) {
		temaForumService.excluir(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<TemaForumDTO> buscarTemaForumParaEditar(@PathVariable("id") Long id) {
		TemaForumDTO temaForumDTO = temaForumService.buscarTemaForumParaEditar(id);
		return ResponseEntity.ok().body(temaForumDTO);
	}
}
