package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MetaObjetivoDesenvolvimentoSustentavelDTO;
import br.org.cidadessustentaveis.dto.ODSOrdenadoDTO;
import br.org.cidadessustentaveis.dto.ObjetivoDesenvolvimentoSustentavelDTO;
import br.org.cidadessustentaveis.dto.OdsPagInstitucionalDTO;
import br.org.cidadessustentaveis.dto.OdsParaComboDTO;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.services.MetaObjetivoDesenvolvimentoSustentavelService;
import br.org.cidadessustentaveis.services.ObjetivoDesenvolvimentoSustentavelService;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/ods")
public class ObjetivoDesenvolvimentoSustentavelResource {

	@Autowired
	private ObjetivoDesenvolvimentoSustentavelService odsService;
	
	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelService metaService;
	
	@GetMapping()
	public ResponseEntity<List<ObjetivoDesenvolvimentoSustentavelDTO>> listar() {
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = odsService.listar();
		List<ObjetivoDesenvolvimentoSustentavelDTO> listaOdsDto = listaOds.stream().map(obj -> new ObjetivoDesenvolvimentoSustentavelDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaOdsDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ODSOrdenadoDTO> listarPorId(final @PathVariable("id") Long id) {
		ObjetivoDesenvolvimentoSustentavel ods = odsService.listarPorId(id);

		List<MetaObjetivoDesenvolvimentoSustentavel> metas =
													ods.getMetas().stream()
																	.sorted(Comparator.comparing(
																				MetaObjetivoDesenvolvimentoSustentavel
																											::getNumero))
																.collect(Collectors.toList());

		ODSOrdenadoDTO dto = new ODSOrdenadoDTO(ods);
		dto.setMetas(metas);

		return ResponseEntity.ok().body(dto);
	}

	@Secured({ "ROLE_CADASTRAR_ODS" })
	@PostMapping()
	public ResponseEntity<ObjetivoDesenvolvimentoSustentavelDTO> inserir(@Valid @RequestBody ObjetivoDesenvolvimentoSustentavelDTO odsDTO) throws IOException {
        odsDTO.setIcone(ImageUtils.compressImage(odsDTO.getIcone()));
        odsDTO.setIconeReduzido(ImageUtils.compressImage(odsDTO.getIconeReduzido()));
		ObjetivoDesenvolvimentoSustentavel ods = odsService.inserir(odsDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ods.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}

	@Secured({ "ROLE_EDITAR_ODS" })
	@PutMapping(value = "/{id}")
	public ResponseEntity<ObjetivoDesenvolvimentoSustentavelDTO> alterar(final @PathVariable("id") Long id, @RequestBody ObjetivoDesenvolvimentoSustentavelDTO odsDTO) throws Exception {
		odsDTO.setIcone(ImageUtils.compressImage(odsDTO.getIcone()));
		odsDTO.setIconeReduzido(ImageUtils.compressImage(odsDTO.getIconeReduzido()));
		ObjetivoDesenvolvimentoSustentavel ods = odsService.alterar(id, odsDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ods.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({ "ROLE_EDITAR_ODS" })
	@PutMapping(value = "/{idOds}/metas/{idMeta}")
	public ResponseEntity<ObjetivoDesenvolvimentoSustentavelDTO> alterarMeta(@PathVariable("idOds") final Long idOds, @PathVariable("idMeta") final Long idMeta, @RequestBody MetaObjetivoDesenvolvimentoSustentavelDTO metaDTO) throws Exception {
		MetaObjetivoDesenvolvimentoSustentavel meta = odsService.alterarMeta(idOds, idMeta, metaDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(meta.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}

	@Secured({ "ROLE_DELETAR_ODS" })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		odsService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({ "ROLE_DELETAR_ODS" })
	@DeleteMapping(value = "/{idOds}/metas/{idMeta}")
	public ResponseEntity<Void> deletarMeta(@PathVariable("idOds") final Long idOds, @PathVariable("idMeta") final Long idMeta) throws Exception {
		odsService.deletarMeta(idOds, idMeta);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value="/buscarOdsParaPaginaInstitucional")
	public ResponseEntity<List<OdsPagInstitucionalDTO>> buscarOdsParaPaginaInstitucional() {
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = odsService.listarTodosOrdenadoPorNumero();
		List<OdsPagInstitucionalDTO> listaOdsDto = listaOds.stream()
                                                                .map(obj -> new OdsPagInstitucionalDTO(obj))
                                                            .collect(Collectors.toList());
		listaOdsDto.forEach(ods -> ods.setIcone(""));
		return ResponseEntity.ok().body(listaOdsDto);
	}
	
	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarIconeOds(final @PathVariable("id") Long id,
												 @RequestParam(required = false) boolean reduzido) throws Exception {
		ObjetivoDesenvolvimentoSustentavel ods = odsService.listarPorId(id);

		if(ods == null) {
			return ResponseEntity.notFound().build();
		}

		String imagem = ods.getIcone();

		if(reduzido) {
			imagem = ods.getIconeReduzido();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("public");
		headers.set("ETag", ImageUtils.generateImageETag(imagem));

		return new ResponseEntity<byte[]>(Base64.decode(imagem.getBytes()), headers, HttpStatus.OK);
	}
	
	@GetMapping("/buscarPorEixo/{idEixo}")
	public List<ItemComboDTO> buscarPorId(@PathVariable("idEixo") Long id) {
		List<ItemComboDTO> objetivoDesenvolvimentoSustentavel = odsService.buscarPorIdEixo(id);
		return objetivoDesenvolvimentoSustentavel;
	}
	
	@GetMapping("/buscarOdsCombo")
	public List<ItemComboDTO> buscarOdsCombo() {
		List<ItemComboDTO> objetivoDesenvolvimentoSustentavel = odsService.buscarOdsParaCombo();
		return objetivoDesenvolvimentoSustentavel;
	}

	@GetMapping("/buscarOdsParaCombo")
	public ResponseEntity<List<ItemComboDTO>> buscarOdsParaCombo() {
		List<ItemComboDTO> listaOds = odsService.buscarOdsParaCombo();
		return ResponseEntity.ok().body(listaOds);
	}
	@GetMapping("/buscarOdsParaComboComMetas")
	public ResponseEntity<List<OdsParaComboDTO>> buscarOdsParaComboComMetas() {
		List<OdsParaComboDTO> listaOds = odsService.buscarOdsParaComboComMetas();
		return ResponseEntity.ok().body(listaOds);

	}
	
	@GetMapping("/buscarPorListaIds")
	public ResponseEntity<List<ObjetivoDesenvolvimentoSustentavelDTO>> buscarPorListaIds(@RequestParam ("idsOds") List<Long> idsOds) {
		List<ObjetivoDesenvolvimentoSustentavelDTO> listaOds = odsService.buscarPorListaIds(idsOds);
		return ResponseEntity.ok().body(listaOds);
	}
	
	@GetMapping("/buscarMetaOdsPorIdOdsItemCombo")
	public ResponseEntity<List<ItemComboDTO>> buscarMetaOdsPorIdOdsItemCombo(@RequestParam ("idsOds") List<Long> idsOds) {
		List<ItemComboDTO> listaMetaOds = metaService.buscarMetaOdsPorIdOdsItemCombo(idsOds);
		return ResponseEntity.ok().body(listaMetaOds);
	}

}
