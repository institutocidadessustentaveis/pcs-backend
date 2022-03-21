package br.org.cidadessustentaveis.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.ParametroGeralEmailSugestaoBoaPraticaDTO;
import br.org.cidadessustentaveis.model.sistema.ParametroGeral;
import br.org.cidadessustentaveis.services.ParametroGeralService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/parametrogeral")
public class ParametroGeralResource {

	@Autowired
	private ParametroGeralService service;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/emailsugestaoboaspraticas")
	public ResponseEntity<ParametroGeralEmailSugestaoBoaPraticaDTO> buscar() {
		ParametroGeral parametro = service.buscar();
		return ResponseEntity.ok().body(new ParametroGeralEmailSugestaoBoaPraticaDTO(parametro));
	}
	
	
	
}
