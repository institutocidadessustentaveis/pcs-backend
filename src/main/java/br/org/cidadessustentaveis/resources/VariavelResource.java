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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.VariavelCompletaDTO;
import br.org.cidadessustentaveis.dto.VariavelDTO;
import br.org.cidadessustentaveis.dto.VariavelSimplesDTO;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.services.PrefeituraService;
import br.org.cidadessustentaveis.services.UsuarioService;
import br.org.cidadessustentaveis.services.VariavelService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/variavel")
public class VariavelResource {
	
	

	@Autowired
	private VariavelService variavelService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PrefeituraService prefeituraService;

	@Secured({"ROLE_VISUALIZAR_VARIAVEL"})
	@GetMapping()
	public ResponseEntity<List<VariavelDTO>> listar(Principal principal) {
		Prefeitura prefeitura = getPrefeitura(principal);
		List<Variavel> listaVariavel;
		if (prefeitura != null)
			listaVariavel = variavelService.listar(prefeitura);
		else
			listaVariavel = variavelService.listar();
		List<VariavelDTO> listaVariavelDto = listaVariavel.stream().map(obj -> new VariavelDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaVariavelDto);
	}
	
	@GetMapping("/pcsSimples")
	public ResponseEntity<List<VariavelSimplesDTO>> listarPCS() {
		List<VariavelSimplesDTO> listaVariavelDto = variavelService.buscarVariaveisPCSNumericas();
		return ResponseEntity.ok().body(listaVariavelDto);
	}
	
	@Secured({"ROLE_VISUALIZAR_VARIAVEL"})
	@GetMapping("/buscarTodas")
	public ResponseEntity<List<VariavelDTO>> listarTodas() {
		List<Variavel> listaVariavel = variavelService.listar();
		List<VariavelDTO> listaVariavelDto = listaVariavel.stream().map(obj -> new VariavelDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaVariavelDto);
	}
	
	@Secured({"ROLE_VISUALIZAR_VARIAVEL"})
	@GetMapping("/buscarPorPrefeitura/{idPrefeitura}")
	public ResponseEntity<List<VariavelDTO>> listarPorPrefeituraOrderByName(@PathVariable Long idPrefeitura) {
		List<Variavel> listaVariavel1 = variavelService.buscarSemPrefeituraOrderByName();
		List<Variavel> listaVariavel2 = variavelService.findByPrefeitura(idPrefeitura);
		List<VariavelDTO> listaVariavelDto1 = listaVariavel1.stream().map(obj -> new VariavelDTO(obj)).collect(Collectors.toList());
		List<VariavelDTO> listaVariavelDto2 = listaVariavel2.stream().map(obj -> new VariavelDTO(obj)).collect(Collectors.toList());
		List<VariavelDTO> listaVariavelDto3 = new ArrayList<>();
		listaVariavelDto3.addAll(listaVariavelDto1);
		listaVariavelDto3.addAll(listaVariavelDto2);
		return ResponseEntity.ok().body(listaVariavelDto3);
	}
	
	@Secured({"ROLE_VISUALIZAR_VARIAVEL"})
	@GetMapping("/carregaVariaveisAdmin")
	public ResponseEntity<List<VariavelDTO>> buscarSemPrefeituraOrderByName() {
		List<Variavel> listaVariavel = variavelService.buscarSemPrefeituraOrderByName();
		List<VariavelDTO> listaVariavelDto = listaVariavel.stream().map(obj -> new VariavelDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaVariavelDto);
	}

	@Secured({"ROLE_VISUALIZAR_VARIAVEL"})
	@GetMapping("/{id}")
	public ResponseEntity<Variavel> listarPorId(Principal principal, final @PathVariable("id") Long id) {
		Prefeitura prefeitura = getPrefeitura(principal);
		Variavel variavel;
		if (prefeitura != null) 
			variavel = variavelService.listarById(prefeitura, id);
		else
			variavel = variavelService.listarById(id);
		return ResponseEntity.ok().body(variavel);
	}

	@Secured({"ROLE_CADASTRAR_VARIAVEL"})
	@PostMapping()
	public ResponseEntity<VariavelDTO> inserir(Principal principal, @Valid @RequestBody VariavelDTO variavelDTO) {
		Prefeitura prefeitura = getPrefeitura(principal);
		Variavel variavel;
		if (prefeitura != null)
			variavel = variavelService.inserir(prefeitura, variavelDTO);
		else
			variavel = variavelService.inserir(variavelDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(variavel.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}

	@Secured({"ROLE_EDITAR_VARIAVEL"})
	@PutMapping(value = "/{id}")
	public ResponseEntity<VariavelDTO> alterar(Principal principal, final @PathVariable("id") Long id, @RequestBody VariavelDTO variavelDTO) throws Exception {
		Prefeitura prefeitura = getPrefeitura(principal);
		Variavel variavel;
		if (prefeitura != null)
			variavel = variavelService.alterar(prefeitura, id, variavelDTO);
		else
			variavel = variavelService.alterar(id, variavelDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(variavel.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}

	@GetMapping(value = "/alterarId")
	public void alterarId(Principal principal, final @RequestParam("id") Long id, final @RequestParam("novoId") Long novoId) throws Exception {
		variavelService.alterarId(id,novoId);
	}

	@Secured({"ROLE_DELETAR_VARIAVEL"})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(Principal principal, final @PathVariable("id") Long id) throws Exception {
		Prefeitura prefeitura = getPrefeitura(principal);
		if (prefeitura != null)
			variavelService.deletar(prefeitura, id);
		else
			variavelService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	private Prefeitura getPrefeitura(Principal principal) {
		Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
		Prefeitura prefeitura  = prefeituraService.buscarPrefeituraPorIdUsuario(usuarioLogado.getId());
		return prefeitura;
	}
	
	
	@GetMapping("/buscarVariaveisPcsParaCombo")
	public ResponseEntity<List<ItemComboDTO>> buscarVariaveisPcsParaCombo() {
		return ResponseEntity.ok().body(variavelService.buscarVariaveisPcsParaCombo());
	}


	@GetMapping("/consulta/{id}")
	public ResponseEntity<VariavelCompletaDTO> consultaPorId(@PathVariable("id") Long id) {
		Variavel variavel = this.variavelService.listarById(id);
		VariavelCompletaDTO dto = new VariavelCompletaDTO();
		dto = new VariavelCompletaDTO(variavel);
		return ResponseEntity.ok().body(dto);
	}

}
