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

import br.org.cidadessustentaveis.dto.ForumDiscussaoDTO;
import br.org.cidadessustentaveis.dto.ForumDiscussaoListDTO;
import br.org.cidadessustentaveis.dto.ForumDiscussaoPrincipalDTO;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import br.org.cidadessustentaveis.services.ForumDiscussaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/forum-discussao")
public class ForumDiscussaoResource {

	@Autowired
	private ForumDiscussaoService service;

	@Secured({ "ROLE_CADASTRAR_DISCUSSAO" })
	@PostMapping("/salvar")
	public ForumDiscussao salvar(@RequestBody ForumDiscussaoDTO dto) throws Exception {
		ForumDiscussao forumDiscussao = service.salvar(dto);
		return forumDiscussao;
	}

	@GetMapping("/buscarDiscussoesToList")
	public ResponseEntity<List<ForumDiscussaoPrincipalDTO>> buscarDiscussoesToList() {
		List<ForumDiscussaoPrincipalDTO> forumDiscussaoPrincipalDTO = service.buscarDiscussoesToList();
		return ResponseEntity.ok().body(forumDiscussaoPrincipalDTO);
	}

	@GetMapping("/buscarDiscussoesFiltradas")
	public List<ForumDiscussaoPrincipalDTO> buscarDiscussoesFiltradas(@RequestParam String palavraChave,
			@RequestParam List<Long> idsTemas) throws Exception {
		return service.buscarDiscussoesFiltradas(palavraChave, idsTemas);
	}

	@Secured({ "ROLE_EXCLUIR_DISCUSSAO" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		service.excluir(id);
		return ResponseEntity.ok().build();
	}

	@Secured({ "ROLE_EDITAR_DISCUSSAO" })
	@PutMapping("/editar")
	public ResponseEntity<ForumDiscussaoDTO> alterar(@RequestBody ForumDiscussaoDTO forumDiscussaoDTO)
			throws Exception {
		ForumDiscussao forumDiscussao = service.alterar(forumDiscussaoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(forumDiscussao.getId())
				.toUri();
		return ResponseEntity.ok().location(uri).build();
	}

	@GetMapping("/buscarDiscussaoPorId/{id}")
	public ResponseEntity<ForumDiscussaoDTO> buscarPorId(@PathVariable("id") Long id) throws Exception {

		ForumDiscussaoDTO forumDiscussaoDTO = service.buscarDiscussaoPorId(id);
			if (forumDiscussaoDTO.getId() != null) {				
				/*if (forumDiscussaoDTO.getAtivo() == true) {				
					return ResponseEntity.ok().body(forumDiscussaoDTO);
				}
				else {					
					return ResponseEntity.status(416).build();
				}*/
				
				return ResponseEntity.ok().body(forumDiscussaoDTO);
			}
			else {
				return ResponseEntity.status(403).build();
			}
	}

	// atualiza na tabela forum_discussao as colunas
	// numero_de_respostas, usuario_ultima_postagem
	@PostMapping("/atualizarDiscussao")
	public ResponseEntity<ForumDiscussaoDTO> atualizarDiscussao(@RequestBody ForumDiscussaoDTO forumDiscussaoDTO)
			throws Exception {
		ForumDiscussao forumDiscussao = service.atualizarDiscussao(forumDiscussaoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(forumDiscussao.getId())
				.toUri();
		return ResponseEntity.ok().location(uri).build();
	}

	// Atualiza na tabela forum_discussao a coluna
	// numero_de_visualizacao
	@PostMapping("/atualizarVisualizacao")
	public ResponseEntity<ForumDiscussaoDTO> atualizarVisualizacao(@RequestBody ForumDiscussaoDTO forumDiscussaoDTO)
			throws Exception {
		ForumDiscussao forumDiscussao = service.atualizarVisualizacao(forumDiscussaoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(forumDiscussao.getId())
				.toUri();
		return ResponseEntity.ok().location(uri).build();
	}

	@Secured({ "ROLE_EDITAR_DISCUSSAO" })
	@GetMapping("/buscar/{id}")
	public ResponseEntity<ForumDiscussaoDTO> buscarForumDiscussaoEditar(@PathVariable("id") Long id) {
		ForumDiscussaoDTO forumDiscussaoDTO = service.buscarForumDiscussaoEditar(id);
		if (forumDiscussaoDTO.getId() != null) {
			
			return ResponseEntity.ok().body(forumDiscussaoDTO);
		}
		return ResponseEntity.status(403).build();
	}

	@GetMapping("/buscarListaDiscussoes")
	public ResponseEntity<List<ForumDiscussaoListDTO>> buscarListaDiscussoes() {
		List<ForumDiscussaoListDTO> forumDiscussaoListDTO = service.buscarListaDiscussoes();
		return ResponseEntity.ok().body(forumDiscussaoListDTO);
	}

	@GetMapping("/buscarListaDiscussoesPorIdPrefeitura/{idPrefeitura}")
	public ResponseEntity<List<ForumDiscussaoListDTO>> buscarListaDiscussoesPorIdPrefeitura(
			@PathVariable("idPrefeitura") Long idDiscussao) {
		List<ForumDiscussaoListDTO> forumDiscussaoListDTO = service.buscarListaDiscussoesPorIdPrefeitura(idDiscussao);
		return ResponseEntity.ok().body(forumDiscussaoListDTO);
	}
	
	@PostMapping("/atualizarUsuarioUltimaPostagem")
	public ResponseEntity<ForumDiscussaoDTO> atualizarUsuarioUltimaPostagem(@RequestBody ForumDiscussaoDTO forumDiscussaoDTO)
			throws Exception {
		ForumDiscussao forumDiscussao = service.atualizarUsuarioUltimaPostagem(forumDiscussaoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(forumDiscussao.getId())
				.toUri();
		return ResponseEntity.ok().location(uri).build();
	}

}
