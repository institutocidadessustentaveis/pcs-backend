package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

import br.org.cidadessustentaveis.dto.MapaTematicoDTO;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MapaTematico;
import br.org.cidadessustentaveis.services.MapaTematicoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/mapaTematico")
public class MapaTematicoResource {
	
	@Autowired
	private MapaTematicoService service;


	@Secured({"ROLE_CADASTRAR_MAPA_TEMATICO"})
	@PostMapping("/inserirMapaTematico")
	public ResponseEntity<Void> inserir(@RequestBody MapaTematicoDTO mapaTematicoDTO) throws IOException {

		service.inserirMapaTematico(mapaTematicoDTO);
					
		return ResponseEntity.noContent().build();	
	}
	
	@GetMapping("/buscarMapasTematico/{idShapeFile}")
	public ResponseEntity<List<MapaTematicoDTO>> buscarConsultasBoaPratica(@PathVariable("idShapeFile") Long idShapeFile){
		List<MapaTematico> listaMapasTematicos = service.buscarMapasTematicos(idShapeFile);
		List<MapaTematicoDTO> listaMapasTematicosDTO = listaMapasTematicos.stream().map(obj -> new MapaTematicoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaMapasTematicosDTO);	
	}
	
	@GetMapping("/buscarMapaTematicoExibirAuto/{idShapeFile}")
	public ResponseEntity<MapaTematicoDTO> buscarMapaTematicoExibirAuto(@PathVariable("idShapeFile") Long idShapeFile){
		MapaTematico mapaTematico = service.mapaExibirAuto(idShapeFile);
		if (mapaTematico != null) {
			MapaTematicoDTO mapaTematicoDTO = new MapaTematicoDTO(mapaTematico);
			return ResponseEntity.ok().body(mapaTematicoDTO);
		}
		return  ResponseEntity.noContent().build();	
	}
	
	@Secured({"ROLE_DELETAR_MAPA_TEMATICO"})
	@DeleteMapping("/excluirMapaTematico/{id}")
	public ResponseEntity<Void> excluirMapaTematico(@PathVariable("id") Long id) {
		service.deletarMapaTematico(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_DELETAR_MAPA_TEMATICO"})
	@PutMapping("/editarExibirAuto/{id}/{exibirAuto}/{shapeId}")
	public ResponseEntity<Void> editarExibirAuto(@PathVariable("id") Long idMapa, @PathVariable("exibirAuto") boolean exibirAuto, @PathVariable("shapeId") Long shapeId) { 
		service.editarExibirAuto(idMapa, exibirAuto, shapeId);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_DELETAR_MAPA_TEMATICO"})
	@PutMapping("/editarExibirLegenda/{id}/{exibirLegenda}/{shapeId}")
	public ResponseEntity<Void> editarExibirLegenda(@PathVariable("id") Long idMapa, @PathVariable("exibirLegenda") boolean exibirLegenda, @PathVariable("shapeId") Long shapeId) { 
		service.editarExibirLegenda(idMapa, exibirLegenda, shapeId);
		return ResponseEntity.noContent().build();
	}
	
}
