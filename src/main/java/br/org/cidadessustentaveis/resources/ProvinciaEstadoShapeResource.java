package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.ProvinciaEstadoShapeDTO;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstadoShape;
import br.org.cidadessustentaveis.services.ProvinciaEstadoShapeService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/shape/estado")
public class ProvinciaEstadoShapeResource {

	@Autowired
	private ProvinciaEstadoShapeService shapeService;

	@GetMapping("/{idEstado}")
	public ResponseEntity<ProvinciaEstadoShapeDTO> buscarPorEstado(@PathVariable Long idEstado) {
		ProvinciaEstadoShape shape = shapeService.buscarPorEstado(idEstado);
		
		if(shape != null) {
			return ResponseEntity.ok(new ProvinciaEstadoShapeDTO(shape));
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("/buscarListaShapesPorEstados")
	public ResponseEntity<List<ProvinciaEstadoShapeDTO>> buscarPorEstado(@RequestParam List<Long> id) {
		List<ProvinciaEstadoShape> shapes = shapeService.buscarPorEstados(id);
		List<ProvinciaEstadoShapeDTO> dto = shapes.stream()
													.map(s -> new ProvinciaEstadoShapeDTO(s))
												.collect(Collectors.toList());
		return ResponseEntity.ok(dto);
	}

	@GetMapping
	public ResponseEntity<List<ProvinciaEstadoShapeDTO>> findAll() {
		List<ProvinciaEstadoShape> shapes = shapeService.findAll();
		List<ProvinciaEstadoShapeDTO> dto = shapes.stream()
													.map(s -> new ProvinciaEstadoShapeDTO(s))
												.collect(Collectors.toList());
		return ResponseEntity.ok(dto);
	}

}
