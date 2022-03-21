package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteSource;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MaterialInstitucionalDTO;
import br.org.cidadessustentaveis.dto.MaterialInstitucionalToListDTO;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.institucional.MaterialInstitucional;
import br.org.cidadessustentaveis.services.MaterialInstitucionalService;
import br.org.cidadessustentaveis.util.PDFUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/materialinstitucional")
public class MaterialInstitucionalResource {
	
	@Autowired
	private MaterialInstitucionalService service;
	
	@GetMapping("/buscarToList")
	public ResponseEntity<List<MaterialInstitucionalToListDTO>> buscarToList(){
		List<MaterialInstitucional> boasPraticas = service.buscar();
		List<MaterialInstitucionalToListDTO> boasPraticasDto = boasPraticas.stream().map(obj -> new MaterialInstitucionalToListDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(boasPraticasDto);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<MaterialInstitucionalDTO> buscarPorId(@PathVariable("id") Long id) {
		MaterialInstitucional materialInstitucional = service.buscarPorId(id);
		MaterialInstitucionalDTO a = new MaterialInstitucionalDTO(materialInstitucional);
		return ResponseEntity.ok().body(a);
	}
	
	@GetMapping("/buscarPorPublicacao/{id}")
	public ResponseEntity<MaterialInstitucionalDTO> buscarPorPublicacaoId(@PathVariable("id") Long id) {
		MaterialInstitucional materialInstitucional = service.buscarPorPublicacaoId(id);
		MaterialInstitucionalDTO a = new MaterialInstitucionalDTO(materialInstitucional);
		return ResponseEntity.ok().body(a);
	}

	@Secured({"ROLE_CADASTRAR_MATERIAL_INSTITUCIONAL"})
	@PostMapping("/inserir")
	public ResponseEntity<Void> inserir(@RequestBody MaterialInstitucionalDTO materialInstitucionalDto) {
		MaterialInstitucional materialInstitucional = service.inserir(materialInstitucionalDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(materialInstitucional.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@Secured({"ROLE_DELETAR_MATERIAL_INSTITUCIONAL"})
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<Void> excluirBoaPratica(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_MATERIAL_INSTITUCIONAL"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<MaterialInstitucionalDTO> alterar(final @PathVariable("id") Long id, @RequestBody MaterialInstitucionalDTO materialInstitucionalDTO) throws Exception {
		MaterialInstitucional boaPratica = service.alterar(id, materialInstitucionalDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(boaPratica.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarParaCombo")
	public ResponseEntity<List<ItemComboDTO>> buscarParaCombo(){
		List<ItemComboDTO> itens = service.buscarParaCombo();
		return ResponseEntity.ok().body(itens);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadArquivos( @PathVariable("id") Long idArquivo, HttpServletRequest request) throws IOException{
		
		Arquivo arq = service.downloadArquivos(idArquivo);
		
		String mediaType = arq.getExtensao().replace("data:", "");
		mediaType = mediaType.replace(";base64", "");
		
		byte[] pdfBytes = PDFUtils.B64Decode(arq.getConteudo());
		String nomeRealArquivo = arq.getNomeArquivo();
		FileOutputStream fos = new FileOutputStream(nomeRealArquivo);
	    fos.write(pdfBytes);
	    fos.flush();
	    fos.close();    
	    File arquivo = new File(nomeRealArquivo);
	    byte[] fileContent = Files.readAllBytes(arquivo.toPath());
	    arquivo.delete();
		
		InputStream targetStream = ByteSource.wrap(fileContent).openStream();
		InputStreamResource resource = new InputStreamResource(targetStream);	
	

		return 	ResponseEntity.ok().contentType(MediaType.parseMediaType(mediaType)).body(resource);
		
	}

}
