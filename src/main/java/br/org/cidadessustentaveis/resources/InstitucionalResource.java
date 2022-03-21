package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.org.cidadessustentaveis.dto.InstitucionalVisualizacaoDTO;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.InstitucionalDTO;
import br.org.cidadessustentaveis.model.administracao.ArquivoInstitucional;
import br.org.cidadessustentaveis.model.institucional.Institucional;
import br.org.cidadessustentaveis.services.InstitucionalService;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/institucional")
public class InstitucionalResource {

	@Autowired
	private InstitucionalService institucionalService;

	@GetMapping()
	public ResponseEntity<List<InstitucionalVisualizacaoDTO>> listar() {
		List<Institucional> listaInstitucional = institucionalService.listar();
		List<InstitucionalVisualizacaoDTO> listaInstitucionalDto =
												listaInstitucional.stream()
																	.map(obj -> new InstitucionalVisualizacaoDTO(obj))
																.collect(Collectors.toList());
		return ResponseEntity.ok().body(listaInstitucionalDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Institucional> listarPorId(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(institucionalService.listarById(id));
	}

	@Secured({"ROLE_CADASTRAR_PAGINA_INSTITUCIONAL"})
	@PostMapping()
	public ResponseEntity<InstitucionalDTO> inserir(@Valid @RequestBody InstitucionalDTO institucionalDTO)
																									throws Exception {
		Institucional institucional = institucionalService.inserir(institucionalDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
																	.buildAndExpand(institucional.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}

	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/{id}")
	public ResponseEntity<InstitucionalDTO> alterar(final @PathVariable("id") Long id,
													@RequestBody InstitucionalDTO institucionalDTO) throws Exception {
		Institucional institucional = institucionalService.alterar(id, institucionalDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
												.path("/{id}").buildAndExpand(institucional.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}

	@Secured({"ROLE_DELETAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		institucionalService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarPaginaInstitucionalPorLink/{link}")
	public ResponseEntity<InstitucionalDTO> buscarPaginaInstitucionalPorLink(final @PathVariable("link") String link) {
		InstitucionalDTO pagina = institucionalService.buscarPaginaInstitucionalPorLink(link);
		return ResponseEntity.ok().body(pagina);
	}
	
	@GetMapping("/existePaginaInstitucionalComLink/{link}")
	public ResponseEntity<InstitucionalDTO> existePaginaInstitucionalComLink(final @PathVariable("link") String link) {
		InstitucionalDTO pagina = institucionalService.existePaginaInstitucionalComLink(link);
		return ResponseEntity.ok().body(pagina);
	}
	
	@GetMapping("/buscarArquivoInstitucionalPorId/{idArquivo}")
	public ResponseEntity<ArquivoInstitucional> buscarArquivoInstitucionalPorId(
																	final @PathVariable("idArquivo") Long idArquivo) {
		return ResponseEntity.ok().body(institucionalService.listarArquivoPorId(idArquivo));
	}

	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagemPaginalInstitucional(@RequestHeader(value = "User-Agent") String userAgent, final @PathVariable("id") Long id)
																					throws IOException,
																							NoSuchAlgorithmException {
		Institucional institucional = institucionalService.listarById(id);

		if(institucional == null) {
			return ResponseEntity.notFound().build();
		}

		String imagem =  ImageUtils.isRobot(userAgent) ? 
				ImageUtils.compressImageSocialMediaB64(institucional.getImagemPrincipal()) : institucional.getImagemPrincipal();
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("public");
		headers.set("ETag", ImageUtils.generateImageETag(imagem));

		return new ResponseEntity<byte[]>(
				
				Base64.decode(imagem.getBytes()),
											headers, HttpStatus.OK);
	}
	
	@GetMapping("/buscarParaEdicao/{id}")
	public ResponseEntity<InstitucionalDTO> buscarParaEdicao(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(institucionalService.buscarParaEdicao(id));
	}

	@GetMapping("/criarPaginasMenu")
	public ResponseEntity<Void> criarPaginasMenu() {
	    institucionalService.criarPaginasMenu();
	    return ResponseEntity.ok().build();
    }

}
