package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import br.org.cidadessustentaveis.dto.FuncionalidadeDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PerfilDTO;
import br.org.cidadessustentaveis.model.administracao.Funcionalidade;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Permissao;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.services.FuncionalidadeService;
import br.org.cidadessustentaveis.services.PerfilService;
import br.org.cidadessustentaveis.services.UsuarioService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/perfis")
public class PerfilResource {
	
	@Autowired
	private PerfilService service;
	
	@Autowired
	private FuncionalidadeService funcionalidadeService;
	@Autowired
	private UsuarioService usuarioService;

	@Secured({"ROLE_VISUALIZAR_PERFIL"})
	@GetMapping("/buscar")
	public ResponseEntity<List<PerfilDTO>> buscar(){
		List<Perfil> perfis = service.buscar();
		List<PerfilDTO> perfisDto = perfis.stream().map(obj -> new PerfilDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(perfisDto);
	}
	
	@GetMapping("/buscarPerfilGestaoPublica")
	public ResponseEntity<List<PerfilDTO>> buscarPerfilGestaoPublica(){
		List<Perfil> perfis = service.buscarPerfilGestaoPublica();
		List<PerfilDTO> perfisDto = perfis.stream().map(obj -> new PerfilDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(perfisDto);
	}
	
	@GetMapping("/buscarFuncionalidades")
	public ResponseEntity<List<FuncionalidadeDTO>> buscarFuncionalidades(){
		List<Funcionalidade> funcionalidades = funcionalidadeService.buscarFuncionalidades();
		List<FuncionalidadeDTO> funcionalidadesDto = funcionalidades.stream().map(obj -> new FuncionalidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(funcionalidadesDto);
	}
	@Secured({"ROLE_VISUALIZAR_PERFIL"})
	@GetMapping("/buscar/{id}")
	public ResponseEntity<PerfilDTO> buscarPorId(@PathVariable("id") Long id) {
		Perfil perfil = service.buscarPorId(id);
		List<Long> funcionalidadesPerfil = new ArrayList<>();
		perfil.getPermissoes().forEach(p -> funcionalidadesPerfil.add(p.getFuncionalidade().getId()));
		List<Funcionalidade> novasFuncionalidades = funcionalidadeService.buscarNotIn(funcionalidadesPerfil);
		novasFuncionalidades.stream().forEach(f -> perfil.getPermissoes().add(new Permissao(null, f, perfil, false)));
		perfil.getPermissoes().sort((p1, p2) -> p1.getFuncionalidade().getNome() .compareTo(p2.getFuncionalidade().getNome()));
		return ResponseEntity.ok().body(new PerfilDTO(perfil));
	}

	@Secured({"ROLE_EDITAR_PERFIL"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<Void> editar(@Valid @RequestBody PerfilDTO perfilDto, @PathVariable("id") Long idPerfil) {
		service.editar(perfilDto, idPerfil);
		return ResponseEntity.noContent().build();
	}

	@Secured({"ROLE_CADASTRAR_PERFIL"})
	@PostMapping("/cadastrar")
	public ResponseEntity<Void> inserir(@Valid @RequestBody PerfilDTO perfilDto) {
		Perfil perfil = service.inserir(perfilDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(perfil.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@Secured({"ROLE_DELETAR_PERFIL"})
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarPerfilAtivo")
	public ResponseEntity<List<PerfilDTO>> buscarPerfilAtivo(Principal principal) {
		List<PerfilDTO> listaPerfil = new ArrayList<PerfilDTO>();
		String email = principal.getName().toString().trim();
		Usuario usuario = usuarioService.buscarPorEmail(email);
		if(usuario != null) {
			for(Perfil itemPerfil : usuario.getCredencial().getListaPerfil()) {
				listaPerfil.add(new PerfilDTO(itemPerfil));
			}
		}
		return ResponseEntity.ok().body(listaPerfil);
	}
	
	@GetMapping("/buscarComboBoxPerfil")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxPerfil() {
		List<ItemComboDTO> perfis = service.buscarComboBoxPerfil();
		return ResponseEntity.ok().body(perfis);
	}
	
	

}
