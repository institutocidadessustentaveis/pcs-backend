package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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

import com.google.common.io.ByteSource;

import br.org.cidadessustentaveis.dto.CombosMaterialApoioDTO;
import br.org.cidadessustentaveis.dto.FiltroMaterialApoioDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MaterialApoioDTO;
import br.org.cidadessustentaveis.dto.MaterialApoioDetalhadoDTO;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;
import br.org.cidadessustentaveis.services.MaterialApoioService;
import br.org.cidadessustentaveis.util.ImageUtils;
import br.org.cidadessustentaveis.util.PDFUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/materialapoio")
public class MaterialApoioResource {
	
	@Autowired
	private MaterialApoioService service;
	
	@GetMapping("/carregarCombosMaterialApoio")
	public ResponseEntity<CombosMaterialApoioDTO> carregarCombosMaterialApoio(){
		CombosMaterialApoioDTO dto = service.carregarCombosMaterialApoio();
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/buscarMaterialDeApoioPorId/{id}")
	public ResponseEntity<MaterialApoioDTO> buscarMaterialDeApoioPorId(@PathVariable("id") Long id){
		MaterialApoioDTO dto = service.buscarMaterialDeApoioPorId(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/buscarMaterialDeApoioDetalhadoPorId/{id}")
	public ResponseEntity<MaterialApoioDetalhadoDTO> buscarMaterialDeApoioDetalhadoPorId(@PathVariable("id") Long id){
		MaterialApoioDetalhadoDTO dto = service.buscarMaterialDeApoioDetalhadoPorId(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/buscarMateriaisDeApoio")
	public ResponseEntity<List<MaterialApoioDTO>> buscar(){
		List<MaterialApoioDTO> dtos = service.buscarMateriaisDeApoio();
		return ResponseEntity.ok().body(dtos);
	}
	
	@Secured({"ROLE_CADASTRAR_MATERIAL_APOIO"})
	@PostMapping("/inserir")
	public ResponseEntity<Void> inserir(@Valid @RequestBody MaterialApoioDTO materialApoioDTO) throws Exception {
		MaterialApoio materialApoio = service.inserirMaterialDeApoio(materialApoioDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(materialApoio.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@Secured({"ROLE_DELETAR_MATERIAL_APOIO"})
	@DeleteMapping("/excluirMaterialApoio/{id}")
	public ResponseEntity<Void> excluirMaterialApoio(@PathVariable("id") Long id) {
		service.excluirMaterialApoio(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_MATERIAL_APOIO"})
	@PutMapping("/editar")
	public ResponseEntity<MaterialApoioDTO> alterar(@RequestBody MaterialApoioDTO materialApoioDTO) throws Exception {
		MaterialApoio materialApoio = service.alterar(materialApoioDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(materialApoio.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@GetMapping("/buscarParaCombo")
	public ResponseEntity<List<ItemComboDTO>> buscarParaCombo(){
		List<ItemComboDTO> itens = service.buscarParaCombo();
		return ResponseEntity.ok().body(itens);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadArquivos( @PathVariable("id") Long idMaterialApoio, HttpServletRequest request) throws IOException{
		
		Arquivo arq = service.buscarArquivoPublicacao(idMaterialApoio);
		
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
	
	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagem(final @PathVariable("id") Long id) throws Exception {
        MaterialApoio materialApoio = service.buscarPorId(id);

        if(materialApoio == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(materialApoio.getImagemCapa().getConteudo()));

        return new ResponseEntity<byte[]>(Base64.decode(materialApoio.getImagemCapa().getConteudo().getBytes()), headers, HttpStatus.OK);
    }
	
	@GetMapping("/buscarMaterialApoioFiltrado")
	public ResponseEntity<List<FiltroMaterialApoioDTO>> buscarEventosFiltrados(@RequestParam Long idAreaInteresse, @RequestParam Long idEixo, @RequestParam Long idOds, @RequestParam Long idMetasOds,
			@RequestParam Long idIndicador, @RequestParam Long idCidade, @RequestParam Long idProvinciaEstado, @RequestParam String regiao, @RequestParam Long idPais, @RequestParam String continente, @RequestParam Long populacaoMin,
			@RequestParam Long populacaoMax, @RequestParam String palavraChave
			) throws Exception{
		List<FiltroMaterialApoioDTO> listaMaterialApoio = service.buscarMaterialApoioFiltrado(idAreaInteresse, idEixo, idOds, idMetasOds, idIndicador, idCidade, idProvinciaEstado, regiao, idPais, continente, populacaoMin, populacaoMax, palavraChave );
		return ResponseEntity.ok().body(listaMaterialApoio);
	}

}
