package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.org.cidadessustentaveis.dto.PremiacaoPrefeituraDTO;
import br.org.cidadessustentaveis.model.administracao.PremiacaoPrefeitura;
import br.org.cidadessustentaveis.services.PremiacaoPrefeituraService;
import springfox.documentation.annotations.ApiIgnore;	

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/premiacaoPrefeitura")
public class PremiacaoPrefeituraResource {	

	@Autowired
	private PremiacaoPrefeituraService premiacaoPrefeituraService;	

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Secured({"ROLE_ADMINISTRAR_PREMIACOES","ROLE_VISUALIZAR_PREMIACOES"})
	@GetMapping("/buscarTodos")
	public ResponseEntity<List<PremiacaoPrefeituraDTO>> buscar() {

		List<PremiacaoPrefeitura> premiacoesPrefeitura = premiacaoPrefeituraService.buscar();
		List<PremiacaoPrefeituraDTO> premiacoesPrefeituraDto = premiacoesPrefeitura.stream().map(obj -> new PremiacaoPrefeituraDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(premiacoesPrefeituraDto);
	}
	
	@Secured({"ROLE_ADMINISTRAR_PREMIACOES"})
	@GetMapping("/{id}")
	public ResponseEntity<PremiacaoPrefeituraDTO> buscarPorId(@PathVariable("id") Long id) {
		PremiacaoPrefeitura premiacaoPrefeitura = premiacaoPrefeituraService.buscarPorId(id);
		return ResponseEntity.ok().body(new PremiacaoPrefeituraDTO(premiacaoPrefeitura));
	}
	
	@Secured({"ROLE_ADMINISTRAR_PREMIACOES"})
	@GetMapping("/buscarCidadesInscritas/{id}")
	public ResponseEntity<List<PremiacaoPrefeituraDTO>> buscarCidadesInscritas(@PathVariable("id") Long id) {
		List<PremiacaoPrefeitura> premiacoesPrefeitura = premiacaoPrefeituraService.buscarCidadesInscritas(id);
		List<PremiacaoPrefeituraDTO> premiacoesPrefeituraDto = premiacoesPrefeitura.stream().map(obj -> new PremiacaoPrefeituraDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(premiacoesPrefeituraDto);
	}
	
}
