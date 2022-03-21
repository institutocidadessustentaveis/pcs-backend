package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import br.org.cidadessustentaveis.dto.ExibirProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.ProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ProvinciaEstadoPaginacaoDTO;
import br.org.cidadessustentaveis.dto.ProviniciaEstadoBuscaDTO;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.services.ProvinciaEstadoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/provinciaEstado")
public class ProvinciaEstadoResource {

	@Autowired
	private ProvinciaEstadoService service;

	@Secured({"ROLE_CADASTRAR_ESTADO"})
	@PostMapping("/inserirProvinciaEstado")
	public ResponseEntity<Void> inserir(@RequestBody ProvinciaEstadoDTO provinciaEstadoDto) {
		ProvinciaEstado provinciaEstado = service.inserirProvinciaEstado(provinciaEstadoDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(provinciaEstado.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<ExibirProvinciaEstadoDTO> buscarPorId(@PathVariable("id") Long id) {
		ProvinciaEstado provinciaEstado = service.buscarPorId(id);
		return ResponseEntity.ok().body(new ExibirProvinciaEstadoDTO(provinciaEstado));
	}

	@GetMapping("/buscarTodos")
	public ResponseEntity<List<ExibirProvinciaEstadoDTO>> buscar() {
		List<ExibirProvinciaEstadoDTO> provinciaEstado = service.buscar();
		return ResponseEntity.ok().body(provinciaEstado);
	}
	
	@GetMapping("/buscarTodosBrasil")
	public ResponseEntity<List<ExibirProvinciaEstadoDTO>> buscarBrasil() {
		List<ProvinciaEstado> provinciaEstado = service.buscarEstadosDoBrasil();
		List<ExibirProvinciaEstadoDTO> provinciaEstadoDto = provinciaEstado.stream().map(obj -> new ExibirProvinciaEstadoDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(provinciaEstadoDto);
	}
	
	@GetMapping("/buscarComboBoxEstado")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBox() {
		List<ProvinciaEstado> provinciaEstado = service.buscarComboBox();
		List<ItemComboDTO> provinciaEstadoDto = provinciaEstado.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(provinciaEstadoDto);
	}

	// ?linesPerPage=2&page=0&orderBy=nome&direction=DESC
	@GetMapping("/buscar")
	public ResponseEntity<ProvinciaEstadoPaginacaoDTO> findComPaginacao(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPerPage", defaultValue = "22") Integer linesPerPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {

		Page<ProvinciaEstado> provinciaEstado = service.buscarComPaginacao(page, linesPerPage, orderBy, direction);
		List<ProviniciaEstadoBuscaDTO> provinciaEstadoDto = provinciaEstado.map(obj -> new ProviniciaEstadoBuscaDTO(obj)).getContent();
		ProvinciaEstadoPaginacaoDTO dto = new ProvinciaEstadoPaginacaoDTO(provinciaEstadoDto, service.contar());
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping("/buscar/{nome}")
	public ResponseEntity<ProvinciaEstadoPaginacaoDTO> buscarPorNome(
				@PathVariable String nome,
				@RequestParam(name = "page", defaultValue = "0") Integer page,
				@RequestParam(name = "linesPerPage", defaultValue = "10") Integer linesPerPage) {
		List<ProvinciaEstado> provinciaEstado = service.buscarPorNomeLike(nome, page, linesPerPage);
		List<ProviniciaEstadoBuscaDTO> provinciaEstadoDto = provinciaEstado.stream().map(obj -> new ProviniciaEstadoBuscaDTO(obj)).collect(Collectors.toList());
		ProvinciaEstadoPaginacaoDTO dto = new ProvinciaEstadoPaginacaoDTO(provinciaEstadoDto, service.contar());
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/buscarIdPorNome/{nome}")
	public ResponseEntity<ProviniciaEstadoBuscaDTO> buscarIdPorNome(
				@PathVariable String nome) {
		ProviniciaEstadoBuscaDTO idProvinciaEstado = (ProviniciaEstadoBuscaDTO) service.buscarIdPorNome(nome);
		return ResponseEntity.ok().body(idProvinciaEstado);
	}


	@Secured({"ROLE_EDITAR_ESTADO"})
	@PutMapping("/{id}")
	public ResponseEntity<Void> editar(@Valid @RequestBody ProvinciaEstadoDTO provinciaEstadoDto,
			@PathVariable("id") Long id) {
		service.editar(provinciaEstadoDto, id);
		return ResponseEntity.noContent().build();
	}

	@Secured({"ROLE_DELETAR_ESTADO"})
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarPorPais/{idPais}")
	public ResponseEntity<List<ProvinciaEstadoDTO>> buscarPorPais(@PathVariable("idPais") Long idPais) {
		List<ProvinciaEstado> provinciaEstado = service.buscarPorPais(idPais);
		List<ProvinciaEstadoDTO> provinciaEstadoDto = provinciaEstado.stream().map(obj -> new ProvinciaEstadoDTO(obj))
				.collect(Collectors.toList());
		
		provinciaEstadoDto.stream().forEach(x -> x.setCidades(null));
		provinciaEstadoDto.stream().forEach(x -> x.setPais(null));
		return ResponseEntity.ok().body(provinciaEstadoDto);
	}
	
	@GetMapping("/buscarPorPaisCombo/{idPais}")
	public ResponseEntity<List<ItemComboDTO>> buscarPorPaisCombo(@PathVariable("idPais") Long idPais) {		
		List<ItemComboDTO> estadoCombo = service.buscarPorPaisResumido(idPais);				
		return ResponseEntity.ok().body(estadoCombo);
	}
	
	@GetMapping("/buscarPorPaisAutoComplete/{idPais}")
	public ResponseEntity<List<String>> buscarPorPaisAutoComplete(@PathVariable("idPais") Long idPais) {
		List<String> provinciaEstado = service.buscarPorPaisAutoComplete(idPais);
		return ResponseEntity.ok().body(provinciaEstado);
	}
	
	@GetMapping("/buscarComboBoxEstadoBrasil")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxEstadoBrasil() {
		List<ProvinciaEstado> provinciaEstado = service.buscarComboBoxEstadoBrasil();
		List<ItemComboDTO> provinciaEstadoDto = provinciaEstado.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(provinciaEstadoDto);
	}


}
