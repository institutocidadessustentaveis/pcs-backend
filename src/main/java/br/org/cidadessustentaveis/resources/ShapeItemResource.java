package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.dto.AtributoDTO;
import br.org.cidadessustentaveis.dto.ConfirmacaoCriacaoShapeFileDTO;
import br.org.cidadessustentaveis.dto.CriacaoShapeDTO;
//10.1.1.158/IACIT/EPS/cidades-sustentaveis-backend.git
import br.org.cidadessustentaveis.dto.ShapeFileMergedDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoDTO;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
//10.1.1.158/IACIT/EPS/cidades-sustentaveis-backend.git
import br.org.cidadessustentaveis.services.ShapeItemService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/shapeitens")
public class ShapeItemResource {


	@Autowired
	private ShapeItemService shapeItemService;	


	@GetMapping("/buscarTodosFeatures")
	public ResponseEntity<List<Feature>> buscarTodosFeatures() {
		List<Feature> lista = shapeItemService.findAll();
		return ResponseEntity.ok(lista);
	}

	@GetMapping("/buscarFeaturesPorShapeId/{id}")
	public ResponseEntity<List<Feature>> buscarFeaturesPorShapeId(@PathVariable("id") Long id) {
		List<Feature> lista = shapeItemService.findPorShapeFile(id);
		return ResponseEntity.ok(lista);
	}

	@Secured({"ROLE_CADASTRAR_SHAPEFILE_RASTER"})
	@PostMapping("/inserirNovaCamada")
	public ResponseEntity<CriacaoShapeDTO> inserirNovaCamada(@RequestBody ShapeFileMergedDTO shapeFileMergedDTO) throws Exception  {
		ConfirmacaoCriacaoShapeFileDTO dtoConfirmacao = shapeItemService.salvarShape(shapeFileMergedDTO);
		CriacaoShapeDTO dto = new CriacaoShapeDTO(dtoConfirmacao.getShapePertenceAPrefeitura(),
				dtoConfirmacao.getTemIntersecacoNaAreaDaPrefeitura(),dtoConfirmacao.getShapeFile().getId());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dtoConfirmacao.getShapeFile().getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
//	@Secured({"ROLE_"})
	@PutMapping("/editarPorIdShapeFile/{idShapeFile}")
	public ResponseEntity<Void> editarPorIdShapeFile(final @PathVariable("idShapeFile") Long idShapeFile, @RequestBody List<Feature> shapes) throws Exception {
		shapeItemService.editarPorIdShapeFile(shapes, idShapeFile);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idShapeFile).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
//	@Secured({"ROLE_"})
	@PutMapping("/editarAtributos/{idShape}")
	public ResponseEntity<Void> editarAtributos(final @PathVariable("idShape") Long idShape, @RequestBody List<AtributoDTO> atributos) throws Exception {
		shapeItemService.editarAtributos(idShape, atributos);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idShape).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({"ROLE_CADASTRAR_SHAPEFILE_RASTER"})
	@PostMapping("/inserirSubdivisaoShapeFile")
	public ResponseEntity<CriacaoShapeDTO> inserirSubdivisaoShapeFile(@RequestBody SubdivisaoDTO subdivisaoDTO) throws Exception  {
		ConfirmacaoCriacaoShapeFileDTO dtoConfirmacao = shapeItemService.salvarShapeSubdivisao(subdivisaoDTO);
		CriacaoShapeDTO dto = new CriacaoShapeDTO(dtoConfirmacao.getShapePertenceAPrefeitura(),
				dtoConfirmacao.getTemIntersecacoNaAreaDaPrefeitura(),dtoConfirmacao.getShapeFile().getId());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dtoConfirmacao.getShapeFile().getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

}
