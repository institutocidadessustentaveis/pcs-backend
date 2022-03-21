package br.org.cidadessustentaveis.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.services.RecalculoService;
import springfox.documentation.annotations.ApiIgnore;
@Deprecated
@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/recalculo")
public class RecalculoResource {
	@Autowired
	RecalculoService recalculoSerice;
	
	@GetMapping("/recalcular")
	public void recalcular(@RequestParam Boolean pcs , @RequestParam Boolean prefeitura, @RequestParam boolean numerico, @RequestParam boolean texto ){		
		List<Long> indicadores = recalculoSerice.recalcularTudo(pcs, prefeitura,numerico, texto);
		for(Long id : indicadores) {
			System.out.println("Mandei: "+id);
			recalculoSerice.preencherNovosIndicadores(id);
			System.out.println("Mandei: "+id);
		}
	}

}

