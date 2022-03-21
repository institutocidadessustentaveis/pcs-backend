package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.org.cidadessustentaveis.recaptcha.InvalidRecaptchaException;
import br.org.cidadessustentaveis.recaptcha.Recaptcha;
import br.org.cidadessustentaveis.recaptcha.RecaptchaRequest;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.AlterarSenhaDTO;
import br.org.cidadessustentaveis.dto.EventosFiltradosDTO;
import br.org.cidadessustentaveis.dto.FiltroUsuarioDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.UsuarioCadastroDTO;
import br.org.cidadessustentaveis.dto.UsuarioDTO;
import br.org.cidadessustentaveis.dto.UsuarioPerfisCidadeDTO;
import br.org.cidadessustentaveis.dto.UsuarioSimplesDTO;
import br.org.cidadessustentaveis.model.administracao.Credencial;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.CredencialRepository;
import br.org.cidadessustentaveis.services.UsuarioService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

	@Autowired
	private CredencialRepository credencialRepository;

	@Value("${recaptha.secret}")
	private String recaptchaSecret;

	@Secured({"ROLE_USUARIOS"})
	@GetMapping("/buscarTodos")
	public ResponseEntity<List<UsuarioDTO>> buscar() {
		List<Usuario> usuarios = service.buscar();
		List<UsuarioDTO> usuariosDto = usuarios.stream().map(obj -> new UsuarioDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(usuariosDto);
	}

	@Secured({"ROLE_USUARIOS"})
	@GetMapping("/buscar")
	public ResponseEntity<List<UsuarioCadastroDTO>> findComPaginacao(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {

		Page<Usuario> usuarios = service.buscarComPaginacao(page, linesPerPage, orderBy, direction);
		List<UsuarioCadastroDTO> usuariosDto = usuarios.stream().map(obj -> new UsuarioCadastroDTO(obj)).collect(Collectors.toList());
		usuariosDto.get(0).setTotalElements(usuarios.getTotalElements());
		return ResponseEntity.ok().body(usuariosDto);
	}
	
	@Secured({"ROLE_USUARIOS"})
	@GetMapping("/buscarFiltrado")
	public ResponseEntity<List<FiltroUsuarioDTO>> findFiltrado(@RequestParam String nome, @RequestParam Long cidade, 
			@RequestParam Long perfil, @RequestParam String organizacao) {
		List<FiltroUsuarioDTO> usuarios = service.buscarFiltrado(nome, cidade, perfil, organizacao);
		return ResponseEntity.ok().body(usuarios);
	}
	
	@Secured({"ROLE_USUARIOS"})
	@GetMapping("/buscarPrefeitura")
	public ResponseEntity<List<UsuarioCadastroDTO>> findComPaginacaoPrefeitura(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction,
			@RequestParam(name = "nome") String nome,
			@RequestParam(name = "uf") String uf,
			@RequestParam(name = "cidade") String cidade,
			@RequestParam(name = "perfil") List<Long> perfil) {

		if(nome == null || nome.isEmpty()) {
			nome = "%";
		}
		if(uf == null || uf.isEmpty()) {
			uf = "%";
		}
		if(cidade == null || cidade.isEmpty()) {
			cidade = "%";
		}
		
		Page<Usuario> usuarios = service.buscarComPaginacaoPrefeitura(page, linesPerPage, orderBy, direction, nome, uf, cidade, perfil);
		
		List<UsuarioCadastroDTO> usuariosDto = usuarios.stream().map(obj -> new UsuarioCadastroDTO(obj)).collect(Collectors.toList());
		if(!usuariosDto.isEmpty()) {
			usuariosDto.get(0).setTotalElements(usuarios.getTotalElements());
		}
		
		return ResponseEntity.ok().body(usuariosDto);
	}
	
	@Secured({"ROLE_USUARIOS"})
	@GetMapping("/buscarPrefeituraFiltrada")
	public ResponseEntity<List<UsuarioCadastroDTO>> findPrefeituraFiltrada(
			@RequestParam(name = "nome") String nome,
			@RequestParam(name = "uf") String uf,
			@RequestParam(name = "cidade") String cidade,
			@RequestParam(name = "perfil") List<Long> perfil) {

		if(nome == null || nome.isEmpty()) {
			nome = "%";
		}
		if(uf == null || uf.isEmpty()) {
			uf = "%";
		}
		if(cidade == null || cidade.isEmpty()) {
			cidade = "%";
		}
		
		List<UsuarioCadastroDTO> usuarios = service.buscarUsuarioPrefeituraFiltrado(nome, uf, cidade, perfil);
		
		/*List<UsuarioCadastroDTO> usuariosDto = usuarios.stream().map(obj -> new UsuarioCadastroDTO(obj)).collect(Collectors.toList());
		if(!usuariosDto.isEmpty()) {
			usuariosDto.get(0).setTotalElements(usuarios.get);
		}*/
		
		return ResponseEntity.ok().body(usuarios);
	}

	@Secured({"ROLE_VISUALIZAR_USUARIO"})
	@GetMapping("/buscar/{id}")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable("id") Long id) {
		Usuario usuario = service.buscarPorId(id);
		return ResponseEntity.ok().body(new UsuarioDTO(usuario));
	}
	

	@Secured({"ROLE_VISUALIZAR_USUARIO"})
	@GetMapping("/simples/{id}")
	public ResponseEntity<UsuarioSimplesDTO> buscarPorIdSimples(@PathVariable("id") Long id) {
		Usuario usuario = service.buscarPorId(id);
		return ResponseEntity.ok().body(new UsuarioSimplesDTO(usuario));
	}
	
	@Secured({"ROLE_VISUALIZAR_USUARIO"})
	@GetMapping("/buscarPerfisCidade/{id}")
	public ResponseEntity<UsuarioPerfisCidadeDTO> buscarPerfisCidade(@PathVariable("id") Long id) {
		Usuario usuario = service.buscarPorId(id);
		UsuarioPerfisCidadeDTO usuarioPerfisCidadeDTO = new UsuarioPerfisCidadeDTO(usuario);
		return ResponseEntity.ok().body(usuarioPerfisCidadeDTO);
	}
	
	@GetMapping("/buscarUsuarioLogado")
	public ResponseEntity<UsuarioDTO> buscarUsuarioLogado() {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = service.buscarPorEmailCredencial(user);
		return ResponseEntity.ok().body(new UsuarioDTO(usuario));
	}


	@PostMapping("/inserirResponsavel")
	public void inserirResponsavel(@RequestBody List<UsuarioDTO> usuariosDto) throws Exception{
		service.inserirResponsavel(usuariosDto);
	}
	

	@PostMapping("/inserirResponsavelSimples")
	public void inserirResponsavel(@RequestBody UsuarioSimplesDTO usuarioDTO) throws Exception {
		service.inserirResponsavel(usuarioDTO);
	}
	

	@PutMapping("/alterarResponsavelSimples")
	public void alterarResponsavel(@RequestBody UsuarioSimplesDTO usuarioDTO) throws Exception {
		service.alterarResponsavel(usuarioDTO);
	}
	
	@Secured({"ROLE_CADASTRAR_USUARIO"})
	@PostMapping("/inserirGestorPlataforma")
	public ResponseEntity<Void> inserirGestorPlataforma(@Valid @RequestBody UsuarioDTO usuarioDto) throws EmailException {
		Usuario usuario = service.inserirGestorPlataforma(usuarioDto);		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
		
	@PostMapping("/inserircidadao")
	public ResponseEntity<Void> inserirCidadao(@Valid @RequestBody UsuarioDTO usuarioDto,
											   @RequestParam String tokenRecaptcha) throws EmailException {

		RecaptchaRequest recaptchaRequest = new RecaptchaRequest(recaptchaSecret, tokenRecaptcha);
		Recaptcha recaptcha = new Recaptcha(recaptchaRequest);

		if(!recaptcha.validate()) {
			throw new InvalidRecaptchaException("CAPTCHA não preenchido");
		}

		Usuario usuario = service.inserirCidadao(usuarioDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/existe/{email}")
	public ResponseEntity<Boolean> buscarPorId(@PathVariable("email") String email) {
		Credencial credencial = credencialRepository.findByLoginIgnoreCaseAndSnExcluido(email,false);
		Boolean usuarioExiste = null;
		if (credencial != null) {
			usuarioExiste = Boolean.TRUE;
		} else {
			usuarioExiste = Boolean.FALSE;
		}
		return ResponseEntity.ok().body(usuarioExiste);
	}

	@Secured({"ROLE_EDITAR_USUARIO"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<Void> editar(@Valid @RequestBody UsuarioDTO usuarioDto, @PathVariable("id") Long id) {
		service.editar(usuarioDto, id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_BLOQUEAR_FORUM"})
	@PutMapping("/editarBloqueadoForum/{id}")
	public ResponseEntity<Void> editarBloqueadoForum(@Valid @RequestBody UsuarioDTO usuario, @PathVariable("id") Long id) {
		service.editarBloqueadoForum(usuario, id);
		return ResponseEntity.noContent().build();
	}

	@Secured({"ROLE_DELETAR_USUARIO"})
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/trocarsenha")
	public ResponseEntity<Boolean> trocarSenha(Authentication authentication,
			@RequestBody AlterarSenhaDTO novaSenhaDTO) {
		String username = authentication.getName();
		Usuario usuario = service.alterarSenha(novaSenhaDTO.getSenha(), username, novaSenhaDTO.getNovaSenha());
		if (usuario != null) {
			return ResponseEntity.ok().body(true);
		} else {
			return ResponseEntity.ok().body(false);
		}
	}

	@PostMapping("/esquecisenha")
	public ResponseEntity<Boolean> esqueciSenha(@Valid @RequestBody String email) throws EmailException {
		boolean usuarioNaoExiste = false;
		Credencial credencial = credencialRepository.findByLoginIgnoreCaseAndSnExcluido(email, false);
		if (credencial != null)
			usuarioNaoExiste = true;
		if (usuarioNaoExiste == true)
			usuarioNaoExiste = service.esqueciSenha(email);

		return ResponseEntity.ok().body(usuarioNaoExiste);

	}
	
	@PutMapping("/criarNovaSenha")
	public ResponseEntity<Boolean> criarNovaSenha(Authentication authentication,
			@RequestBody AlterarSenhaDTO novaSenhaDTO) {
		Usuario usuario = service.criarNovaSenha(novaSenhaDTO.getSenha(), novaSenhaDTO.getUsuario(), novaSenhaDTO.getNovaSenha());
		if (usuario != null) {
			return ResponseEntity.ok().body(true);
		} else {
			return ResponseEntity.ok().body(false);
		}
	}
	
	@PutMapping("/cadastrarSenha")
	public ResponseEntity<Boolean> cadastrarSenha(Authentication authentication,
			@RequestBody AlterarSenhaDTO cadSenhaDTO) {
		Usuario usuario = service.cadastrarSenha( cadSenhaDTO.getUsuario(), cadSenhaDTO.getSenha(), cadSenhaDTO.getNovaSenha() );
		if (usuario != null) {
			return ResponseEntity.ok().body(true);
		} else {
			return ResponseEntity.ok().body(false);
		}
	}
	
	@GetMapping("/buscarPorEmail/{email}")
	public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable("email") String email) {
		Usuario usuario = service.buscarPorEmail(email);

		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(new UsuarioDTO(usuario));
	}
	
	@PutMapping("/editarUsuarioLogado/{id}")
	public ResponseEntity<Void> editarUsuarioLogado(@Valid @RequestBody UsuarioDTO usuarioDto, @PathVariable("id") Long id) {
		service.editarUsuarioLogado(usuarioDto, id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarComboBoxUsuario")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxUsuario() {
		List<Usuario> usuarios = service.buscarComboBoxUsuario();
		List<ItemComboDTO> usuarioDto = usuarios.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(usuarioDto);
	}
	
	@GetMapping("/buscarComboBoxUsuarioSemPrefeitura")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxUsuarioSemPrefeitura() {
		List<ItemComboDTO> usuarioDto = service.buscarComboBoxUsuarioSemPrefeitura();
		return ResponseEntity.ok().body(usuarioDto);
	}
	
	@GetMapping("/buscarComboBoxUsuarioDePrefeitura")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxUsuarioDePrefeitura() {
		List<ItemComboDTO> usuarioDto = service.buscarComboBoxUsuarioDePrefeitura();
		return ResponseEntity.ok().body(usuarioDto);
	}
	
	@GetMapping("/buscarPorPerfil/{idPerfil}")
	public ResponseEntity<List<UsuarioDTO>> buscarPorPerfil(@PathVariable("idPerfil") Long idPerfil) {
		List<UsuarioDTO> usuario = service.buscarPorPerfil(idPerfil);

		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(usuario);
	}
	
	@GetMapping("/buscarListaUsuariosPorPrefeitura/{idPrefeitura}")
	public ResponseEntity<List<UsuarioDTO>> buscarListaUsuariosPorPrefeitura(@PathVariable Long idPrefeitura) {
		List<UsuarioDTO> listUsuario = service.buscarListaUsuariosPorPrefeitura(idPrefeitura);
		return ResponseEntity.ok().body(listUsuario);
	}
	
	@GetMapping("/criarUsuariosPrefeitos")
	public ResponseEntity criarUsuariosPrefeitosScript() {
		service.criarUsuariosPrefeitosScript();
		return ResponseEntity.ok(201);
	}

	@Secured({"ROLE_DELETAR_USUARIO_RESPONSAVEL"})
	@DeleteMapping("/deletar-responsavel/{id}")
	public ResponseEntity<Void> deletarResponsavel(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
