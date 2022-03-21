package br.org.cidadessustentaveis.resources;

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
import br.org.cidadessustentaveis.services.IndicadorService;
import br.org.cidadessustentaveis.services.VisualizacaoCartograficaService;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/visualizarindicador")
public class VisualizarIndicadorResource {

	@Autowired
	private IndicadorService indicadorService;
	
	@GetMapping("/indicador/{id}")
	public ResponseEntity<VisualizarIndicadorDTO> buscarPorId(@PathVariable("id") final Long id) {
	  return ResponseEntity.ok().body(indicadorService.buscarVisualizarIndicador(id));
	}
	
	
}
