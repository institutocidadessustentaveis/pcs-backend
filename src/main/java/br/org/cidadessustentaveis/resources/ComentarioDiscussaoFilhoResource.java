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
import br.org.cidadessustentaveis.dto.ComentarioDiscussaoFilhoDTO;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussaoFilho;
import br.org.cidadessustentaveis.services.ComentarioDiscussaoFilhoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/comentarioDiscussaoFilho")
public class ComentarioDiscussaoFilhoResource {
	
	@Autowired
	ComentarioDiscussaoFilhoService service;

	@Secured({ "ROLE_CADASTRAR_COMENTARIO_DISCUSSAO" })
	@PostMapping("/cadastrar") 
	public ResponseEntity<ComentarioDiscussaoFilhoDTO> cadastrar(@RequestBody ComentarioDiscussaoFilhoDTO comentarioDiscussao) {
		service.inserir(comentarioDiscussao);
		return ResponseEntity.ok(comentarioDiscussao);
	}

	@GetMapping("/buscarComentariosFilhosPorIdComentarioPai/{idComentarioPai}")
	public ResponseEntity<List<ComentarioDiscussaoFilhoDTO>> buscarComentariosFilhosPorIdComentarioPai(@PathVariable Long idComentarioPai) {
		List<ComentarioDiscussaoFilhoDTO> listaComentarios = service.buscarComentariosFilhosPorIdComentarioPai(idComentarioPai);
		return ResponseEntity.ok().body(listaComentarios);
	}
	
	@Secured({ "ROLE_EDITAR_COMENTARIO_DISCUSSAO" })
	@PutMapping("/editar")
	public ResponseEntity<ComentarioDTO> alterar(@RequestBody  ComentarioDiscussaoFilhoDTO comentarioDiscussao) throws Exception {
		ComentarioDiscussaoFilho comentario = service.alterar(comentarioDiscussao);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comentario.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({ "ROLE_EXCLUIR_COMENTARIO_DISCUSSAO" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	

}
