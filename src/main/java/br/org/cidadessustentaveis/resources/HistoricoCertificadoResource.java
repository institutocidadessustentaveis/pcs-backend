package br.org.cidadessustentaveis.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.HistoricoCertificadoDTO;
import br.org.cidadessustentaveis.services.HistoricoCertificadoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/historico-certificado")
public class HistoricoCertificadoResource {

	@Autowired(required= true)
	private HistoricoCertificadoService service;
	
	@Secured({ "ROLE_EMITIR_CERTIFICADOS" })
	@PostMapping("/cadastrar")
	public ResponseEntity<HistoricoCertificadoDTO> cadastrar( @RequestBody HistoricoCertificadoDTO historicoCertificado) {
		service.inserir(historicoCertificado);
		return ResponseEntity.ok(historicoCertificado);
	}
	
	@Secured({ "ROLE_EXCLUIR_HISTORICO_CERTIFICADOS" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({"ROLE_VISUALIZAR_HISTORICO_CERTIFICADOS"})
	@GetMapping("/buscarHistoricoCertificadoPorId/{id}")
	public ResponseEntity<HistoricoCertificadoDTO> buscarCertificadoPorId(@PathVariable("id") Long id){
		HistoricoCertificadoDTO historicoCertificadoDTO = service.buscarHistoricoCertificadoPorId(id);
		return ResponseEntity.ok().body(historicoCertificadoDTO);
	}
	
	@Secured({"ROLE_VISUALIZAR_HISTORICO_CERTIFICADOS"})
	@GetMapping("/buscarHistoricoCertificadoToList")
	public ResponseEntity<List<HistoricoCertificadoDTO>> buscarHistoricoCertificadoToList(){
		List<HistoricoCertificadoDTO> historicoCertificadoDTO = service.buscarHistoricoCertificadoToList();
		return ResponseEntity.ok().body(historicoCertificadoDTO);
	}
}
