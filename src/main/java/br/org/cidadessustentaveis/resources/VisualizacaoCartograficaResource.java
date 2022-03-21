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
@RestController
@RequestMapping("/visualizacaoCartografica")
public class VisualizacaoCartograficaResource {

	@Autowired
	private IndicadorService indicadorService;
	
	@Autowired
	private VisualizacaoCartograficaService service;
	
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	
	@GetMapping("/inserirRelatorioVisualizacao/{nomeIndicador}/{idCidade}")
	public ResponseEntity<Void> inserir(
			@PathVariable("nomeIndicador") String nomeIndicador,
			@PathVariable("idCidade") Long idCidade) throws Exception {
		
		String EXPORTACAO = "Exportacao";
		
		Long idIndicador = indicadorService.buscarIndicadorPorNome(nomeIndicador).getId();
		
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null) {
			service.inserirRelatorioVisualizacaoCartografica(idIndicador, idCidade, usuarioContextUtil.getUsuario(), EXPORTACAO);
		} else {
			service.inserirRelatorioVisualizacaoCartografica(idIndicador, idCidade, null, EXPORTACAO);
		}
		
		return ResponseEntity.noContent().build();
	}
	
}
