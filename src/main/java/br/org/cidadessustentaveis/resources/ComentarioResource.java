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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ComentarioDTO;
import br.org.cidadessustentaveis.model.administracao.Comentario;
import br.org.cidadessustentaveis.services.ComentarioService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/comentario")
public class ComentarioResource {

	@Autowired(required= true)
	private ComentarioService service;
	
	@Secured({ "ROLE_CADASTRAR_COMENTARIO" })
	@PostMapping("/cadastrar")
	public ResponseEntity<ComentarioDTO> cadastrar(@RequestBody ComentarioDTO comentarioDTO) {
		service.inserir(comentarioDTO);
		return ResponseEntity.ok(comentarioDTO);
	}
	
	@Secured({ "ROLE_EDITAR_COMENTARIO" })
	@PutMapping("/editar")
	public ResponseEntity<ComentarioDTO> alterar(@RequestBody  ComentarioDTO comentarioDTO) throws Exception {
		Comentario comentario = service.alterar(comentarioDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comentario.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarComentarioPorId/{id}")
	public ResponseEntity<ComentarioDTO> buscarPorId(@PathVariable("id") Long id){
		ComentarioDTO comentario = service.buscarComentarioPorId(id);
		return ResponseEntity.ok().body(comentario);
	}
	
	@GetMapping("/buscarComentariosToList/{idUsuario}")
	public ResponseEntity<List<ComentarioDTO>> buscarComentariosToList(@PathVariable("idUsuario") Long idUsuario){
		List<ComentarioDTO> comentario = service.buscarComentariosToList(idUsuario);
		return ResponseEntity.ok().body(comentario);
	}
	
	@GetMapping("/buscarComentariosToListPublica")
	public ResponseEntity<List<ComentarioDTO>> buscarComentariosToListPublica(){
		List<ComentarioDTO> comentario = service.buscarComentariosToListPublica();
		return ResponseEntity.ok().body(comentario);
	}
	
	@Secured({ "ROLE_EXCLUIR_COMENTARIO" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/buscarComentarioFiltrado")
	public ResponseEntity<List<ComentarioDTO>> buscarComentarioFiltrado(@RequestParam String palavraChave, @RequestParam String dataInicial, @RequestParam String dataFinal,
			@RequestParam Long idCidade) throws Exception{
		List<ComentarioDTO> listaComentario = service.buscarComentarioFiltrado(palavraChave, dataInicial, dataFinal, idCidade);
		return ResponseEntity.ok().body(listaComentario);
	}
}
