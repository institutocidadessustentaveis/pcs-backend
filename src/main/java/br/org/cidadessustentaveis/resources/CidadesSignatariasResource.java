package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.DadosDownloadDTO;
import br.org.cidadessustentaveis.dto.VisualizarIndicadorDTO;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.services.CidadesSignatariasService;
import br.org.cidadessustentaveis.services.IndicadorService;
import br.org.cidadessustentaveis.services.VisualizacaoCartograficaService;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/cidadesSignatarias")
public class CidadesSignatariasResource {

	@Autowired
	private CidadesSignatariasService service;
	
	@GetMapping("/conteudoTxtFtp")
	public String conteudoTxtFtp() throws IOException {
			
		String textContent = service.conteudoTxtFtp();
		
		if(textContent != null ) {
			return textContent;
		} else {
			return null;
		}
		
	}
	
}
