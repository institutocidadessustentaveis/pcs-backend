package br.org.cidadessustentaveis.resources;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.BuscarDTO;
import br.org.cidadessustentaveis.services.BuscarService;
import springfox.documentation.annotations.ApiIgnore;	

@ApiIgnore	
@CrossOrigin
@RestController	
@RequestMapping("/buscar")
@Validated
public class BuscarResource {
		
	@Autowired
	private BuscarService buscaService;

	@GetMapping("/buscarPorPalavraChave")
	public BuscarDTO buscarPorPalavraChave(@RequestParam("q") String keywords) throws ParseException {
		BuscarDTO buscarDTO = buscaService.buscarPorPalavraChave(keywords);
		return buscarDTO;
	}
}
