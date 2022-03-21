package br.org.cidadessustentaveis.resources;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.AlertaEventoDTO;
import br.org.cidadessustentaveis.model.eventos.AlertaEvento;
import br.org.cidadessustentaveis.services.AlertaEventoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/alertaEvento")
public class AlertaEventoResource {

	@Autowired
	private AlertaEventoService service;
	
	@GetMapping("/{evento}")
	public ResponseEntity<List<AlertaEventoDTO>> buscarPorEvento( @PathVariable Long evento, Principal principal){
		List<AlertaEvento> alertas = service.findByEventoId(evento);
		List<AlertaEventoDTO> dtos = new ArrayList<>();
		for(AlertaEvento alerta : alertas) {
			AlertaEventoDTO dto = new AlertaEventoDTO(alerta);
			dtos.add(dto);
		}
		
		return ResponseEntity.ok(dtos);
	}
	//@Secured({ "ROLE_VISUALIZAR_ALERTA" })
	@PostMapping
	public ResponseEntity<Long> salvar( @RequestBody AlertaEventoDTO dto, Principal principal){
		AlertaEvento alerta = service.salvar(dto);
		return ResponseEntity.ok(alerta.getId());
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Long> excluir(final @PathVariable("id") Long id){
		service.excluir(id);
		return ResponseEntity.ok(id);
	}
	
	
}
