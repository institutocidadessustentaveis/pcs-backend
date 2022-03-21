package br.org.cidadessustentaveis.resources;

import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.FormularioPreenchidoDTO;
import br.org.cidadessustentaveis.dto.RespostaFormularioDTO;
import br.org.cidadessustentaveis.services.FormularioPreenchidoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/formulario-preenchido")
public class FormularioPreenchidoResource {
	
	@Autowired
	private FormularioPreenchidoService formularioPreenchidoService;
	
	@PostMapping()	
	public void salvar(@RequestBody List<RespostaFormularioDTO> respostas, @RequestParam("formulario") String formulario ) throws AuthenticationException, org.apache.tomcat.websocket.AuthenticationException {
		formularioPreenchidoService.salvar(formulario,respostas);
	}
		
	@GetMapping("/exportarFormularioPreenchido/{id}")
	public ResponseEntity<List<FormularioPreenchidoDTO>> exportarFormularioPreenchido(@PathVariable("id") Long id) throws Exception {

		List<FormularioPreenchidoDTO> formulario = formularioPreenchidoService.exportarFormularioPreenchido(id);
		
		return ResponseEntity.ok().body(formulario);
	}
	

}
