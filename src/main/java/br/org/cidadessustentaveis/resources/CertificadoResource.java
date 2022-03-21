package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.commons.mail.EmailException;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ArquivoDTO;
import br.org.cidadessustentaveis.dto.CertificadoDTO;
import br.org.cidadessustentaveis.model.capacitacao.Certificado;
import br.org.cidadessustentaveis.services.CertificadoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/certificado")
public class CertificadoResource {

	@Autowired(required= true)
	private CertificadoService service;
	
	@Secured({ "ROLE_CADASTRAR_CERTIFICADOS" })
	@PostMapping("/cadastrar")
	public ResponseEntity<CertificadoDTO> cadastrar( @RequestBody CertificadoDTO certificado) {
		service.inserir(certificado);
		return ResponseEntity.ok(certificado);
	}
	
	@PostMapping("/enviarPorEmail/{destinatario}/{nomeUsuario}")
	public ResponseEntity<Void> enviarPorEmail( @PathVariable("destinatario") String destinatario, @PathVariable("nomeUsuario") String nomeUsuario,
			@RequestBody String certificadoBase64) throws IOException, EmailException {
		service.enviarPorEmail(certificadoBase64, destinatario, nomeUsuario);
		return ResponseEntity.ok().build();
	}
	
	@Secured({"ROLE_EDITAR_CERTIFICADOS"})
	@PutMapping(value = "/editar/{id}")
	public ResponseEntity<CertificadoDTO> alterar(final @PathVariable("id") Long id,
													@RequestBody CertificadoDTO certificadoDTO) throws Exception {
		Certificado certificadoRef = service.alterar(certificadoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(certificadoRef.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({ "ROLE_EXCLUIR_CERTIFICADOS" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({"ROLE_VISUALIZAR_CERTIFICADOS"})
	@GetMapping("/buscarCertificadoPorId/{id}")
	public ResponseEntity<CertificadoDTO> buscarCertificadoPorId(@PathVariable("id") Long id){
		CertificadoDTO certificado = service.buscarCertificadoPorId(id);
		return ResponseEntity.ok().body(certificado);
	}
	
	@Secured({"ROLE_VISUALIZAR_CERTIFICADOS"})
	@GetMapping("/buscarCertificadoToList")
	public ResponseEntity<List<CertificadoDTO>> buscarCertificadoToList(){
		List<CertificadoDTO> certificadoDTO = service.buscarCertificadoToList();
		return ResponseEntity.ok().body(certificadoDTO);
	}
	
	@Secured({"ROLE_VISUALIZAR_CERTIFICADOS"})
	@GetMapping("/buscarCertificadoToListResumido")
	public ResponseEntity<List<CertificadoDTO>> buscarCertificadoToListResumido(){
		List<CertificadoDTO> certificadoDTO = service.buscarCertificadoToListResumido();
		return ResponseEntity.ok().body(certificadoDTO);
	}
	
	@GetMapping("/imagem/{id}")
	public ResponseEntity<ArquivoDTO> buscarImagemCertificado(final @PathVariable("id") Long id) throws Exception {
		CertificadoDTO certificadoDTO = service.buscarCertificadoPorId(id);
		ArquivoDTO imagem = new ArquivoDTO();
		if(certificadoDTO == null) {
			return ResponseEntity.notFound().build();
		}

		if(certificadoDTO.getImagem() != null) {
			imagem = certificadoDTO.getImagem();
		} else {
			imagem = null;
		}

		return ResponseEntity.ok().body(imagem);
	}
}
