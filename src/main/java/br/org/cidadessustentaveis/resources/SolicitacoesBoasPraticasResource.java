package br.org.cidadessustentaveis.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.SolicitacoesBoasPraticasDTO;
import br.org.cidadessustentaveis.recaptcha.InvalidRecaptchaException;
import br.org.cidadessustentaveis.recaptcha.Recaptcha;
import br.org.cidadessustentaveis.recaptcha.RecaptchaRequest;
import br.org.cidadessustentaveis.services.SolicitacoesBoasPraticasService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/solicitacoesBoasPraticas")
public class SolicitacoesBoasPraticasResource {

	@Autowired(required= true)
	private SolicitacoesBoasPraticasService service;
	
	@Value("${recaptha.secret}")
	private String recaptchaSecret;
	
	@Secured({ "ROLE_CADASTRAR_SOLICITACAO_BOA_PRATICA" })
	@PostMapping("/cadastrar")
	public ResponseEntity<SolicitacoesBoasPraticasDTO> cadastrar(@RequestBody SolicitacoesBoasPraticasDTO solicitacoesBoasPraticasDTO, @RequestParam String tokenRecaptcha) {
		RecaptchaRequest recaptchaRequest = new RecaptchaRequest(recaptchaSecret, tokenRecaptcha);
		Recaptcha recaptcha = new Recaptcha(recaptchaRequest);

		if(!recaptcha.validate()) {
			throw new InvalidRecaptchaException("CAPTCHA não preenchido");
		}
		
		service.inserir(solicitacoesBoasPraticasDTO);
		return ResponseEntity.ok(solicitacoesBoasPraticasDTO);
	}
	
	
	@GetMapping("/buscarSolicitacaoPorId/{id}")
	public ResponseEntity<SolicitacoesBoasPraticasDTO> buscarPorId(@PathVariable("id") Long id){
		SolicitacoesBoasPraticasDTO solicitacao = service.buscarSolicitacaoPorId(id);
		return ResponseEntity.ok().body(solicitacao);
	}
	
	@GetMapping("/buscarSolicitacoesToList")
	public ResponseEntity<List<SolicitacoesBoasPraticasDTO>> buscarSolicitacoesToList(){
		List<SolicitacoesBoasPraticasDTO> solicitacao = service.buscarSolicitacoesBoasPraticasToList();
		return ResponseEntity.ok().body(solicitacao);
	}
	
}
