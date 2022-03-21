package br.org.cidadessustentaveis.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.ConfiguracaoComentarioTamanhoDTO;
import br.org.cidadessustentaveis.model.participacaoCidada.ConfiguracaoComentario;
import br.org.cidadessustentaveis.services.ConfiguracaoComentarioService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/configuracaocomentario")
public class ConfiguracaoComentarioResource {

	@Autowired
	private ConfiguracaoComentarioService service;

	@Secured({"ROLE_CONFIGURAR_COMENTARIO"})
	@PutMapping("/atualizartamanhocomentario")
	public void atualizar( @RequestBody Long tamanhoMaximoComentario) {
		service.atualizar(tamanhoMaximoComentario);		
	}
	
	@GetMapping("/tamanhocomentario")
	public ResponseEntity<ConfiguracaoComentarioTamanhoDTO> buscar() {
		ConfiguracaoComentario tamanhoComentario = service.buscar();
		return ResponseEntity.ok().body(new ConfiguracaoComentarioTamanhoDTO(tamanhoComentario));
	}

}
