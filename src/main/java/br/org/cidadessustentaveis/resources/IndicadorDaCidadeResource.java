package br.org.cidadessustentaveis.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.IndicadorDaCidadeDTO;
import br.org.cidadessustentaveis.services.IndicadorDaCidadeService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/indicadordacidade")
public class IndicadorDaCidadeResource {
	@Autowired
	private IndicadorDaCidadeService indicadorDaCidadeService;

	@GetMapping("")
	public IndicadorDaCidadeDTO buscarIndicadorDaCidade(
			@RequestParam(name = "indicador") Long idIndicador,
			@RequestParam(name = "sigla") String sigla, 
			@RequestParam(name = "cidade") String nomeCidade) {
		
		IndicadorDaCidadeDTO lista = indicadorDaCidadeService.buscarIndicadorDaCidade(idIndicador, sigla, nomeCidade);
		
		return lista;
	}
	
	
}
