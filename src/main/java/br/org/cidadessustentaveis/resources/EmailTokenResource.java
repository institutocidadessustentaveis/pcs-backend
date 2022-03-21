package br.org.cidadessustentaveis.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.EmailTokenDTO;
import br.org.cidadessustentaveis.model.administracao.EmailToken;
import br.org.cidadessustentaveis.services.EmailTokenService;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/emailToken")
public class EmailTokenResource {

	@Autowired
	private EmailTokenService service;
	
	@GetMapping("/buscarPorHash")	
	public ResponseEntity<EmailTokenDTO> areasAtuacoes(@RequestParam(name = "token") String token){
		EmailToken emailToken = service.getByHash(token);
		EmailTokenDTO dto = new EmailTokenDTO(emailToken);
		
		if(!dto.getAtivo()) {
			new ObjectNotFoundException("Token não encontrado!");
		}
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/isAtivoByIdAprovacaoPrefeitura/{id}")
	public ResponseEntity<Boolean> isAtivoByIdAprovacaoPrefeitura(@PathVariable(name = "id") Long id) {
		Boolean body = service.isAtivoByIdAprovacaoPrefeitura(id);
		return ResponseEntity.ok().body(body);
	}
	
}
