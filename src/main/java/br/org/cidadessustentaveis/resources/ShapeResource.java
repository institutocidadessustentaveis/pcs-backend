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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ShapeDTO;
import br.org.cidadessustentaveis.model.administracao.Shape;
import br.org.cidadessustentaveis.services.ShapeService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/shape")
public class ShapeResource {

	@Autowired
	private ShapeService shapeService;
	
	@GetMapping()
	public ResponseEntity<List<ShapeDTO>> listar() {
		List<Shape> listaShape = shapeService.listar();
		List<ShapeDTO> listaShapeDto = listaShape.stream().map(obj -> new ShapeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaShapeDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Shape> listarPorId(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(shapeService.listarById(id));
	}

	//@Secured({"ROLE_CADASTRAR_EIXO"})
	@PostMapping()
	public ResponseEntity<Shape> inserir(@Valid @RequestBody ShapeDTO shape) {
		Shape shaperetorno = shapeService.inserir(shape.toEntityInsert());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(shaperetorno.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}
	

	//@Secured({"ROLE_EDITAR_EIXO"})
	/*
	@PutMapping(value = "/{id}")
	public ResponseEntity<ShapeDTO> alterar(final @PathVariable("id") Long id, @RequestBody ShapeDTO shapeDTO) throws Exception {
		Shape shape = shapeService.alterar(id, shapeDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(shape.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	*/

	//@Secured({"ROLE_DELETAR_EIXO"})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		shapeService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	

}
