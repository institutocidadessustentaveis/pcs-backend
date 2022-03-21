package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.locationtech.jts.io.ParseException;
import org.opengis.referencing.FactoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wololo.geojson.Feature;
import org.xml.sax.SAXException;

import com.google.common.io.ByteSource;

import br.org.cidadessustentaveis.dto.ConfirmacaoCriacaoShapeFileDTO;
import br.org.cidadessustentaveis.dto.CriacaoShapeDTO;
import br.org.cidadessustentaveis.dto.MesclagemAtributoDTO;
import br.org.cidadessustentaveis.dto.ShapeFileDTO;
import br.org.cidadessustentaveis.dto.ShapeFileDetalheDTO;
import br.org.cidadessustentaveis.dto.ShapeFileVisualizarDetalheDTO;
import br.org.cidadessustentaveis.dto.ShapeListagemMapaDTO;
import br.org.cidadessustentaveis.dto.ShapesFiltroPalavraChaveDTO;
import br.org.cidadessustentaveis.dto.ShapesPaginacaoDTO;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.services.DownloadsExportacoesService;
import br.org.cidadessustentaveis.services.ShapeFileService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/shapefile")
public class ShapeFileResource {
	
	@Autowired
	private ShapeFileService shapeFileService;
	
	@Autowired
	private DownloadsExportacoesService downloadsService;
	
	@GetMapping("/buscarTodosDto")
	public ResponseEntity<List<ShapeFileDTO>> buscarTodosDto(){
		List<ShapeFileDTO> shapefiles = shapeFileService.buscarTodosDto();
		return ResponseEntity.ok().body(shapefiles);
	}
	
	@PostMapping("/editarArquivosShape")
	public boolean editarArquivosShape(@RequestParam MultipartFile arquivoShp,
													  @RequestParam MultipartFile arquivoDbf,
													  @RequestParam MultipartFile arquivoShx, @RequestParam String cidadeDTO) throws Exception {
		ConfirmacaoCriacaoShapeFileDTO dtoShape = shapeFileService.editarArquivosShape(arquivoShp,
																					arquivoDbf, arquivoShx, cidadeDTO);
		URI uri = ServletUriComponentsBuilder
									.fromCurrentRequest()
											.path("/{id}").buildAndExpand(dtoShape.getShapeFile().getId()).toUri();
		return true;
	}

	@Secured({"ROLE_CADASTRAR_SHAPEFILE_RASTER"})
	@PostMapping("/inserir")
	public ResponseEntity<CriacaoShapeDTO> inserir(@RequestParam String shapefile,
													  @RequestParam MultipartFile arquivoShp,
													  @RequestParam MultipartFile arquivoDbf,
													  @RequestParam MultipartFile arquivoShx) throws Exception {
		ConfirmacaoCriacaoShapeFileDTO dtoShape = shapeFileService.salvarShapeFile(shapefile, arquivoShp,
																					arquivoDbf, arquivoShx);
		CriacaoShapeDTO dto = new CriacaoShapeDTO(dtoShape.getShapePertenceAPrefeitura(),
													dtoShape.getTemIntersecacoNaAreaDaPrefeitura(), dtoShape.getShapeFile().getId());
		URI uri = ServletUriComponentsBuilder
									.fromCurrentRequest()
											.path("/{id}").buildAndExpand(dtoShape.getShapeFile().getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@Secured({"ROLE_DELETAR_SHAPEFILE_RASTER"})
	@DeleteMapping("/excluirShapeFilePorId/{id}")
	public ResponseEntity<Void> excluirShapeFilePorId(@PathVariable("id") Long id) {
		shapeFileService.excluirShapeFilePorId(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/inserirRaster")
	public ResponseEntity<Void> inserirRaster(@RequestParam String shapefile, @RequestParam MultipartFile arquivoTiff)
                                                                                                    throws Exception {
		
		shapeFileService.salvarRasterFile(shapefile,arquivoTiff);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarShapeFilePorId/{id}")
    public ResponseEntity<ShapeFileDetalheDTO> buscarShapeFilePorId(@PathVariable("id") Long id) {
          ShapeFileDetalheDTO shapeFileDto = shapeFileService.buscarShapeFilePorId(id);
          return ResponseEntity.ok().body(shapeFileDto);
    }
	
	@GetMapping("/buscarShapeFilePorIdValidacao/{id}")
    public ResponseEntity<ShapeFileDetalheDTO> buscarShapeFilePorIdValidacao(@PathVariable("id") Long id) {
          ShapeFileDetalheDTO shapeFileDto = shapeFileService.buscarShapeFilePorIdValidacao(id);
          return ResponseEntity.ok().body(shapeFileDto);
    }

    @GetMapping("/filtrar")
	public ResponseEntity<ShapesPaginacaoDTO> filtrar(
							@RequestParam(name = "titulo", required = false) String titulo,
							@RequestParam(name = "ano", required = false) Integer ano,
							@RequestParam(name = "sistemaDeReferencia", required = false) String sistemaDeReferencia,
							@RequestParam(name = "tipo", required = false) String tipo,
							@RequestParam(name = "nivelTerritorial", required = false) String nivelTerritorial,
							@RequestParam(name = "temaGeospacial", required = false) Long temaGeoespacial,
							@RequestParam(required = false, defaultValue = "0") Integer page,
							@RequestParam(required = false, defaultValue = "5") Integer itemsPerPage,
							@RequestParam(name = "orderBy", defaultValue = "titulo") String orderBy,
							@RequestParam(name = "direction", defaultValue = "DESC") String direction) throws Exception {
    	
    	ShapesPaginacaoDTO dtoFiltrada = shapeFileService.filtrarShapes(titulo, ano, sistemaDeReferencia, tipo,
																nivelTerritorial, temaGeoespacial, page, itemsPerPage, orderBy,
																direction);		

		return ResponseEntity.ok(dtoFiltrada);
	}
    
    @GetMapping("/filtrarPorPalavraChave")
	public ResponseEntity<ShapesFiltroPalavraChaveDTO> filtrarPorPalavraChave(
							@RequestParam(name = "filtro", required = false) String filtro,
							@RequestParam(name = "origem", required = false) String origem,
							@RequestParam(name = "idPais", required = false) Long idPais,
							@RequestParam(name = "idsEstados", required = false) List<Long> idsEstados,
							@RequestParam(name = "idsCidades", required = false) List<Long> idsCidades,
							@RequestParam(name = "temaGeoespacial", required = false) Long idTema,
							@RequestParam(required = false, defaultValue = "0") Integer page,
							@RequestParam(required = false, defaultValue = "5") Integer itemsPerPage,
							@RequestParam(name = "orderBy", defaultValue = "titulo") String orderBy,
							@RequestParam(name = "direction", defaultValue = "DESC") String direction) throws Exception {
    	
    	ShapesFiltroPalavraChaveDTO shapes = shapeFileService.filtrarPorPalavraChave(filtro, origem, idPais, idsEstados, idsCidades, idTema, page, itemsPerPage, orderBy, direction);		

		return ResponseEntity.ok(shapes);
	}

	@GetMapping("/buscarComPaginacao")
	public ResponseEntity<ShapesPaginacaoDTO> buscarComPaginacao(
										 @RequestParam(required = false, defaultValue = "0") Integer page,
										 @RequestParam(required = false, defaultValue = "5") Integer itemsPerPage,
										 @RequestParam(name = "orderBy", defaultValue = "titulo") String orderBy,
										 @RequestParam(name = "direction", defaultValue = "DESC") String direction) throws Exception {
		 

		return ResponseEntity.ok(shapeFileService.buscarComPaginacao(page, itemsPerPage, orderBy, direction));
	}

	@GetMapping("/buscarShapesListagemMapa")
	public ResponseEntity<List<ShapeListagemMapaDTO>> buscarShapesListagemMapa() {
		List<ShapeListagemMapaDTO> shapes = shapeFileService.buscarShapesListagemMapa();
		return ResponseEntity.ok(shapes);
	}

    @GetMapping(value = "/downloadShapeFile/{id}.png", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> buscarPngShape(@PathVariable("id") Long id, HttpServletResponse response)
																				throws FactoryException,
																						ParseException,
																						IOException,
																						ParserConfigurationException,
																						SAXException {
		response.setContentType("image/png");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +
															shapeFileService.buscarNomeArquivoShape(id) +
														 ".png\"");

		return ResponseEntity.ok(shapeFileService.buscarPngShape(id));
	}

	@GetMapping(value = "/downloadShapeFile/{id}.geojson")
	public ResponseEntity<byte[]> buscarGeoJson(@PathVariable("id") Long id, HttpServletResponse response)
																				throws UnsupportedEncodingException {
		response.setContentType("application/vnd.geo+json");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +
															shapeFileService.buscarNomeArquivoShape(id) +
															".geojson\"");

		return ResponseEntity.ok(shapeFileService.buscarGeoJsonShape(id));
	}

	@GetMapping(value = "/downloadShapeFile/{id}.geotiff")
	public ResponseEntity<byte[]> buscarTiff(@PathVariable("id") Long id, HttpServletResponse response)
																				throws IOException,
																						ParserConfigurationException,
																						SAXException {
		response.setContentType("image/tiff");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +
															shapeFileService.buscarNomeArquivoShape(id) +
														  ".tif\"");

		return ResponseEntity.ok(shapeFileService.buscarGeoTiffShape(id));
	}

	@GetMapping(value = "/downloadShapeFile/{id}.shp")
	public ResponseEntity<byte[]> buscarArquivoShapefile(@PathVariable("id") Long id, HttpServletResponse response)
																									throws Exception {
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +
															shapeFileService.buscarNomeArquivoShape(id) +
														  ".zip\"");

		return ResponseEntity.ok(shapeFileService.buscarShapefileShape2(id));
	}

	@Secured({"ROLE_CADASTRAR_SHAPEFILE_RASTER"})
	@PutMapping("/publicar/{id}")
	public ResponseEntity<ShapeFileDTO> publicar(@PathVariable("id") Long id) throws Exception {
		ShapeFile shapeFile = shapeFileService.publicar(id);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(shapeFile.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	

	@PostMapping("/exportarShapeFile")
	public ResponseEntity<byte[]> exportarShapeFile(@RequestBody List<Feature> listaFeature, @RequestParam(defaultValue = "camadas") String nome) throws Exception { //HttpServletResponse response

	return 	ResponseEntity.ok()
			.contentType(MediaType.parseMediaType("application/octet-stream"))
			.header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"shapefile\"")
			.body(shapeFileService.exportShapefileFromFeatures(listaFeature, nome));
	}

	
	@PostMapping("/exportarShapeFileCidades")
	public ResponseEntity<byte[]> exportarShapeFileCidades(@RequestBody List<Long> idsCidadesBoasPraticas) throws Exception { //HttpServletResponse response

	return 	ResponseEntity.ok()
			.contentType(MediaType.parseMediaType("application/octet-stream"))
			.header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"shapefile\"")
			.body(shapeFileService.exportShapefileFromIdsCidades(idsCidadesBoasPraticas));
	}
	
	@PostMapping("/mesclarAtributos")
	public Integer mesclarAtributos(@RequestBody MesclagemAtributoDTO dto, Principal principal) throws Exception { //HttpServletResponse response
		int quantidade = shapeFileService.mesclarAtributos(dto, principal);
		return quantidade;		
	}

	@GetMapping(path = "/downloadAtributos/{idShapeFile}",  produces = { "application/vnd.ms-excel" })
	public ResponseEntity<Resource> downloadAtributos(@PathVariable ("idShapeFile") Long idShapeFile, Principal principal) throws IOException{
		File file = shapeFileService.gerarArquivoAtributos(idShapeFile);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		file.delete();
		InputStream targetStream = ByteSource.wrap(fileContent).openStream();
		InputStreamResource resource = new InputStreamResource(targetStream);		
        downloadsService.gravarLog((principal != null ? principal.getName() : ""), "atributos_"+idShapeFile+".xlsx");
		return 	ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"atributos_"+idShapeFile+".xlsx\"")
				.body(resource);
	}
	

	@Secured({"ROLE_CADASTRAR_SHAPEFILE_RASTER"})
	@PostMapping("/editarShapeFile")
	public ResponseEntity<ShapeFileDTO> editarShapeFile(@RequestBody ShapeFileDTO shapefile) throws Exception {
		ShapeFile shapeFile = shapeFileService.editarShapeFile(shapefile);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(shapeFile.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarFileNameShapeFilePorId/{idShapeFile}")
    public ResponseEntity<ShapeFileDTO> buscarFileNameShapeFilePorId(@PathVariable("idShapeFile") Long idShapeFile) {
		ShapeFileDTO shapeFileDTO = shapeFileService.buscarFileNameShapeFilePorId(idShapeFile);
        return ResponseEntity.ok().body(shapeFileDTO);
    }

	@Secured({"ROLE_CADASTRAR_SHAPEFILE_RASTER"})
	@PostMapping("/inserirSubdivisao")
	public ResponseEntity<CriacaoShapeDTO> inserirSubdivicaoCidade(@RequestParam String subDivisao,
													  @RequestParam MultipartFile arquivoZip) throws Exception {
		ConfirmacaoCriacaoShapeFileDTO dtoShape = shapeFileService.salvarShapeFileSubdivisao(subDivisao, arquivoZip);
		CriacaoShapeDTO dto = new CriacaoShapeDTO(dtoShape.getShapePertenceAPrefeitura(),
				dtoShape.getTemIntersecacoNaAreaDaPrefeitura(), dtoShape.getShapeFile().getId());
		
		URI uri = ServletUriComponentsBuilder
									.fromCurrentRequest()
											.path("/{id}").buildAndExpand(dtoShape.getShapeFile().getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@GetMapping("/buscarShapeFileIdPorSubdivisaoId/{id}")
	public ResponseEntity<Long> buscarShapeFileIdPorSubdivisaoId(@PathVariable Long id) throws Exception{
		Long idShapeFile = shapeFileService.buscarShapeFileIdPorSubdivisaoId(id);
		return ResponseEntity.ok().body(idShapeFile);
	}
	
	@Secured({"ROLE_CADASTRAR_SHAPEFILE_RASTER"})
	@PostMapping("/inserirKml")
	public ResponseEntity<CriacaoShapeDTO> inserir(@RequestParam String shapefile,
													  @RequestParam MultipartFile arquivoKml) throws Exception {
		ConfirmacaoCriacaoShapeFileDTO dtoShape = shapeFileService.salvarKml(shapefile, arquivoKml);
		CriacaoShapeDTO dto = new CriacaoShapeDTO(dtoShape.getShapePertenceAPrefeitura(),
													dtoShape.getTemIntersecacoNaAreaDaPrefeitura(), dtoShape.getShapeFile().getId());
		URI uri = ServletUriComponentsBuilder
									.fromCurrentRequest()
											.path("/{id}").buildAndExpand(dtoShape.getShapeFile().getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	
	@GetMapping("/buscarShapeFileVisualizarDetalheDTOPorIdShapeFile/{idShapeFile}")
	public ResponseEntity<ShapeFileVisualizarDetalheDTO> buscarShapeFileVisualizarDetalheDTOPorIdShapeFile(@PathVariable("idShapeFile") Long idShapeFile) throws Exception {
		ShapeFileVisualizarDetalheDTO shapeFile = shapeFileService.buscarShapeFileVisualizarDetalheDTOPorIdShapeFile(idShapeFile);
		return ResponseEntity.ok().body(shapeFile);
	}
	
	@GetMapping("/buscarOdsDoShapeFileId/{idShapeFile}")
	public ResponseEntity<List<ObjetivoDesenvolvimentoSustentavel>> buscarOdsDoShapeFileId(@PathVariable("idShapeFile") Long idShapeFile){
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = shapeFileService.buscarOdsDoShapeFileId(idShapeFile);
		return ResponseEntity.ok().body(listaOds);	
	}
	
	@GetMapping(value = "/downloadShapeFileCGEE/{camadaCGEE}.shp")
	public ResponseEntity<byte[]> buscarArquivoShapefileCGEE(@PathVariable("camadaCGEE") String camadaCGEE, HttpServletResponse response)
																									throws Exception {
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +
															shapeFileService.buscarNomeArquivoShapeCGEE(camadaCGEE) +
														  ".zip\"");

		return ResponseEntity.ok(shapeFileService.buscarShapefileCGEE(camadaCGEE));
	}
}
