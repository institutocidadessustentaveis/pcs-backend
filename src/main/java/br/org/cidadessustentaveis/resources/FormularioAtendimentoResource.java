package br.org.cidadessustentaveis.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.FormularioAtendimentoDTO;
import br.org.cidadessustentaveis.model.administracao.FormularioAtendimento;
import br.org.cidadessustentaveis.services.FormularioAtendimentoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/formulario-atendimento")
public class FormularioAtendimentoResource {
	
	@Autowired
	FormularioAtendimentoService service;
	
	@PostMapping("/salvar")
	public ResponseEntity<FormularioAtendimento> salvar(@RequestBody FormularioAtendimentoDTO dto) {
		FormularioAtendimento formulario = service.salvar(dto);
		return ResponseEntity.ok().body(formulario);
	}
	
	@GetMapping("/buscarTodosDto")
	@Secured({"ROLE_GERENCIAR_ATENDIMENTO"})
	public ResponseEntity<List<FormularioAtendimentoDTO>> buscarTodosDto(){
		List<FormularioAtendimentoDTO> listaDto = service.buscarTodos();
		return ResponseEntity.ok().body(listaDto);
	}

}
