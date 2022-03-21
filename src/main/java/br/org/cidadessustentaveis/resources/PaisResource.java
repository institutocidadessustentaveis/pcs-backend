package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import br.org.cidadessustentaveis.dto.PaisBuscaDTO;
import br.org.cidadessustentaveis.dto.PaisComEstadoDTO;
import br.org.cidadessustentaveis.dto.PaisDTO;
import br.org.cidadessustentaveis.dto.PaisesPaginacaoDTO;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.services.PaisService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/pais")
public class PaisResource {

	@Autowired
	private PaisService service;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Secured({"ROLE_CADASTRAR_PAIS"})
	@PostMapping("/inserirPais")
	public ResponseEntity<Void> inserir(@RequestBody PaisDTO paisDto) {
		Pais pais = service.inserirPais(paisDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pais.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PaisDTO> buscarPorId(@PathVariable("id") Long id) {
		Pais pais = service.buscarPorId(id);
		return ResponseEntity.ok().body(new PaisDTO(pais));
	}

	@GetMapping("/buscarTodos")
	public ResponseEntity<List<PaisDTO>> buscar() {

		List<Pais> paises = service.buscar();
		List<PaisDTO> paisesDto = paises.stream().map(obj -> new PaisDTO(obj)).collect(Collectors.toList());
		
		paisesDto.stream().forEach(x -> x.setEstados(null));
		return ResponseEntity.ok().body(paisesDto);
	}
	
	@GetMapping("/buscarTodosPaisEstado")
	public ResponseEntity<List<PaisComEstadoDTO>> buscarTodosPaisEstado() {
		List<Pais> paises = service.buscar();
		List<PaisComEstadoDTO> paisesDto = paises.stream().map(obj -> new PaisComEstadoDTO(obj)).collect(Collectors.toList());
	
		return ResponseEntity.ok().body(paisesDto);
	}
	
	@GetMapping("/buscarPaisesPorContinente/{continente}")
	public ResponseEntity<List<PaisComEstadoDTO>> buscarPaisesPorContinente(@PathVariable("continente") String continente) {
		List<Pais> paises = service.buscarPaisesPorContinente(continente);
		List<PaisComEstadoDTO> paisesDto = paises.stream().map(obj -> new PaisComEstadoDTO(obj)).collect(Collectors.toList());
	
		return ResponseEntity.ok().body(paisesDto);
	}

	// ?linesPerPage=2&page=0&orderBy=nome&direction=DESC
	@GetMapping("/buscar")
	public ResponseEntity<PaisesPaginacaoDTO> findComPaginacao(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPerPage", defaultValue = "50") Integer linesPerPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {

		Page<Pais> paises = service.buscarComPaginacao(page, linesPerPage, orderBy, direction);
		List<PaisBuscaDTO> paisesDto = paises.stream()
											.map(obj -> new PaisBuscaDTO(obj))
											.collect(Collectors.toList());
		PaisesPaginacaoDTO dto = new PaisesPaginacaoDTO(paisesDto, service.contar());
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping("/buscar/{nome}")
	public ResponseEntity<PaisesPaginacaoDTO> buscarPorNome(
				@PathVariable String nome, 
				@RequestParam(name = "page", defaultValue = "0") Integer page,
				@RequestParam(name = "linesPerPage", defaultValue = "10") Integer linesPerPage) {
		List<Pais> paises = service.buscarPorNomeLike(nome.toLowerCase(), page, linesPerPage);
		List<PaisBuscaDTO> paisesDto = paises.stream()
												.map(obj -> new PaisBuscaDTO(obj))
												.collect(Collectors.toList());
		PaisesPaginacaoDTO dto = new PaisesPaginacaoDTO(paisesDto, service.contar());
		return ResponseEntity.ok().body(dto);
	}

	@Secured({"ROLE_EDITAR_PAIS"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<Void> editar(@Valid @RequestBody PaisDTO paisDto, @PathVariable("id") Long id) {
		service.editar(paisDto, id);
		return ResponseEntity.noContent().build();
	}

	@Secured({"ROLE_DELETAR_PAIS"})
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarPaisesCombo")
	public ResponseEntity<List<ItemComboDTO>> buscarPaisesCombo() {
		List<ItemComboDTO> paises = service.buscarPaisesCombo();
		return ResponseEntity.ok().body(paises);
	}
	

}
