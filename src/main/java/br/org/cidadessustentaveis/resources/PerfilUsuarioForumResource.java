package br.org.cidadessustentaveis.resources;

import java.net.URI;

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
import br.org.cidadessustentaveis.dto.PerfilUsuarioForumDTO;
import br.org.cidadessustentaveis.model.administracao.PerfilUsuarioForum;
import br.org.cidadessustentaveis.services.PerfilUsuarioForumService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/perfilUsuarioForum")
public class PerfilUsuarioForumResource {


	@Autowired(required= true)
	private PerfilUsuarioForumService service;
	
	@Secured({ "ROLE_CADASTRAR_PERFIL_USUARIO_FORUM" })
	@PostMapping("/cadastrar")
	public ResponseEntity<PerfilUsuarioForumDTO> cadastrar(@RequestBody PerfilUsuarioForumDTO perfilUsuarioForum) {
		service.inserir(perfilUsuarioForum);
		return ResponseEntity.ok(perfilUsuarioForum);
	}
	
	@Secured({ "ROLE_EDITAR_PERFIL_USUARIO_FORUM" })
	@PutMapping("/editar")
	public ResponseEntity<PerfilUsuarioForumDTO> alterar(@RequestBody  PerfilUsuarioForumDTO perfilUsuarioForumDTO) throws Exception {
		PerfilUsuarioForum perfilUsuarioForum = service.alterar(perfilUsuarioForumDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(perfilUsuarioForum.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarPerfilUsuarioForumPorId/{id}")
	public ResponseEntity<PerfilUsuarioForumDTO> buscarPorId(@PathVariable("id") Long id){
		PerfilUsuarioForumDTO perfilUsuarioForumDTO = service.buscarPerfilPorId(id);
		return ResponseEntity.ok().body(perfilUsuarioForumDTO);
	}
	
	@GetMapping("/buscarPerfilPorIdUsuario/{id}")
	public ResponseEntity<PerfilUsuarioForumDTO> buscarPerfilPorIdUsuario(@PathVariable("id") Long id){
		PerfilUsuarioForumDTO perfilUsuarioForumDTO = service.buscarPerfilPorIdUsuario(id);
		return ResponseEntity.ok().body(perfilUsuarioForumDTO);
	}
	
	@Secured({ "ROLE_EXCLUIR_PERFIL_USUARIO_FORUM" })
	@DeleteMapping("deletar/{id}")
	public ResponseEntity<Void> deletar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
}
