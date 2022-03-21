package br.org.cidadessustentaveis.resources;

import java.net.URI;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.RespostaAtendimentoDTO;
import br.org.cidadessustentaveis.model.administracao.RespostaAtendimento;
import br.org.cidadessustentaveis.services.FormularioAtendimentoService;
import br.org.cidadessustentaveis.services.RespostaAtendimentoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/respostaAtendimento")
public class RespostaAtendimentoResource {
	
	@Autowired
	private RespostaAtendimentoService service;
	
	@Autowired
	private FormularioAtendimentoService formularioAtendimentoService;
	
	@PostMapping("/salvar")
	@Secured({"ROLE_GERENCIAR_ATENDIMENTO"})
	public ResponseEntity<RespostaAtendimento> salvar(@RequestBody RespostaAtendimentoDTO respostaDTO) {
		RespostaAtendimento resposta = null;
		try {
			resposta = service.salvar(respostaDTO);
			formularioAtendimentoService.changeFormularioParaRespondido(resposta.getFormularioAtendimento().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(resposta.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/buscarRespostaAtendimentoPorIdFormulario/{id}")
	@Secured({"ROLE_GERENCIAR_ATENDIMENTO"})
	public ResponseEntity<RespostaAtendimentoDTO> buscarRespostaAtendimentoPorIdFormulario(@PathVariable Long id) {
		RespostaAtendimentoDTO respostaDto = service.buscarRespostaAtendimentoPorIdFormulario(id);
		return ResponseEntity.ok().body(respostaDto);
	}

}
