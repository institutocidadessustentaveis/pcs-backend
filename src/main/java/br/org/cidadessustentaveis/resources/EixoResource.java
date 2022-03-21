package br.org.cidadessustentaveis.resources;

import java.net.URI;
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

import br.org.cidadessustentaveis.dto.EixoDTO;
import br.org.cidadessustentaveis.dto.EixoListagemDTO;
import br.org.cidadessustentaveis.dto.EixoParaComboDTO;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.services.EixoService;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/eixo")
public class EixoResource {

	@Autowired
	private EixoService eixoService;
	
	@GetMapping()
	public ResponseEntity<List<EixoDTO>> listar() {
		List<Eixo> listaEixo = eixoService.listar();
		List<EixoDTO> listaEixoDto = listaEixo.stream().map(obj -> new EixoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaEixoDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Eixo> listarPorId(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(eixoService.listarById(id));
	}

	@Secured({"ROLE_CADASTRAR_EIXO"})
	@PostMapping()
	public ResponseEntity<EixoDTO> inserir(@Valid @RequestBody EixoDTO eixoDTO) {
		Eixo eixo = eixoService.inserir(eixoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eixo.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}

	@Secured({"ROLE_EDITAR_EIXO"})
	@PutMapping(value = "/{id}")
	public ResponseEntity<EixoDTO> alterar(final @PathVariable("id") Long id, @RequestBody EixoDTO eixoDTO) throws Exception {
		Eixo eixo = eixoService.alterar(id, eixoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eixo.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}

	@Secured({"ROLE_DELETAR_EIXO"})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		eixoService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarEixosCombo")
	public ResponseEntity<List<EixoParaComboDTO>> buscarEixosCombo(@RequestParam(required = false, defaultValue = "true") Boolean comListaIndicadores) {
		List<EixoParaComboDTO> listaEixoDto = eixoService.buscarEixosCombo(comListaIndicadores);
		return ResponseEntity.ok().body(listaEixoDto);
	}
	
	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagemEixo(final @PathVariable("id") Long id) throws Exception {
		Eixo eixo = eixoService.listarById(id);
		String base64Imagem = "";
		if(eixo == null) {
			return ResponseEntity.notFound().build();
		}

		if(eixo.getIcone() != null) {
			base64Imagem = eixo.getIcone();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("public");
		headers.set("ETag", ImageUtils.generateBase64ImageETag(base64Imagem));

		return new ResponseEntity<byte[]>(Base64.decode(base64Imagem.getBytes()), headers, HttpStatus.OK);
	}
	
	@GetMapping("/buscarEixosDto")
	public ResponseEntity<List<EixoDTO>> buscarEixosDto() {
		List<EixoDTO> listaEixoDto = eixoService.buscarEixosDto();
		return ResponseEntity.ok().body(listaEixoDto);
	}
	
	@GetMapping("/buscarEixosList")
	public ResponseEntity<List<EixoListagemDTO>> buscarEixosList() {
		List<EixoListagemDTO> listaEixoDto = eixoService.buscarEixosList();
		return ResponseEntity.ok().body(listaEixoDto);
	}

}
