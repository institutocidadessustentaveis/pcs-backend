package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PartidoPoliticoDTO;
import br.org.cidadessustentaveis.model.administracao.PartidoPolitico;
import br.org.cidadessustentaveis.services.PartidoPoliticoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/partidoPolitico")
public class PartidoPoliticoResource {

	@Autowired
	private PartidoPoliticoService partidoService;

	@GetMapping()
	public ResponseEntity<List<PartidoPoliticoDTO>> listar() {
		List<PartidoPolitico> listaPartidos = partidoService.listar();
		List<PartidoPoliticoDTO> listaPartidosDto = listaPartidos.stream().map(obj -> new PartidoPoliticoDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(listaPartidosDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PartidoPolitico> listarPorId(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(partidoService.listarPorId(id));
	}

	@PostMapping()
	public ResponseEntity<PartidoPoliticoDTO> inserir(@Valid @RequestBody PartidoPoliticoDTO partidoDTO) {
		PartidoPolitico partido = partidoService.inserir(partidoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(partido.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<PartidoPoliticoDTO> alterar(final @PathVariable("id") Long id,
			@RequestBody PartidoPoliticoDTO partidoDTO) throws Exception {
		PartidoPolitico partido = partidoService.alterar(id, partidoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(partido.getId())
				.toUri();
		return ResponseEntity.ok().location(uri).build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		partidoService.deletar(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/buscaritemcombo")
	public ResponseEntity<List<ItemComboDTO>> buscarItemCombo() {
		List<ItemComboDTO> itemCombo = partidoService.buscarItemCombo();
		return ResponseEntity.ok().body(itemCombo);
	}

}
