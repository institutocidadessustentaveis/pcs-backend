package br.org.cidadessustentaveis.resources;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.data.ows.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.BoaPraticaFiltradaIntegracaoDTO;
import br.org.cidadessustentaveis.dto.IndicadorIntegracaoDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoIntegracaoDTO;
import br.org.cidadessustentaveis.dto.ShapeFileOpenEndPointDTO;
import br.org.cidadessustentaveis.dto.VariavelFiltradaIntegracaoDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaFiltradaIntegracaoDTO;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.services.BoaPraticaService;
import br.org.cidadessustentaveis.services.IndicadorPreenchidoService;
import br.org.cidadessustentaveis.services.IndicadorService;
import br.org.cidadessustentaveis.services.RelatorioApiPublicaService;
import br.org.cidadessustentaveis.services.ShapeFileService;
import br.org.cidadessustentaveis.services.VariavelPreenchidaService;
import br.org.cidadessustentaveis.services.VariavelService;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import br.org.cidadessustentaveis.util.ProfileUtil;
import io.swagger.annotations.ApiOperation;

@CrossOrigin()
@RestController
@RequestMapping("/integracao")
public class IntegracaoResource {

	@Autowired
	private ShapeFileService shapeFileService;
	
	@Autowired
	private BoaPraticaService boaPraticaService;

	@Autowired
	private IndicadorService indicadorService;

	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	
	@Autowired
	private VariavelService variavelService;
	
	@Autowired
	private VariavelPreenchidaService variavelPreenchidaService;
	
	@Autowired
	private CalculadoraFormulaUtil calculadoraFormulaUtil;
	
	@Autowired
	ProfileUtil profileUtil;
	
	@Autowired
	private RelatorioApiPublicaService relatorioApiPublicaService;

	//Caso esse metódo receba uma nova versão, será preciso atualizar na service qual versão chamar
	@GetMapping("/v1/camadas")
    public ResponseEntity<List<ShapeFileOpenEndPointDTO>> buscarShapeFileOpenEndPointDTO(HttpServletRequest request) {
		List<ShapeFileOpenEndPointDTO> shapeFileDTO = shapeFileService.buscarShapeFileOpenEndPointDTO();
		
		try {
			relatorioApiPublicaService.salvarRegistro("buscarShapeFileOpenEndPointDTO", request.getHeader("Origin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return ResponseEntity.ok().body(shapeFileDTO);
    }
	
	@GetMapping(value = "/v1/camadas/{id}.geojson")
	public ResponseEntity<byte[]> buscarGeoJson(@PathVariable("id") Long id, 
			HttpServletResponse response,
			HttpServletRequest request)
																				throws UnsupportedEncodingException {
		response.setContentType("application/vnd.geo+json");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +
															shapeFileService.buscarNomeArquivoShape(id) +
															".geojson\"");

		try {
			relatorioApiPublicaService.salvarRegistro("buscarGeoJson", request.getHeader("Origin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(shapeFileService.buscarGeoJsonShapePublicarIsTrueNaoSalvaHistorico(id));
	}

	@ApiOperation(value="Busca boas práticas filtradas")
	@GetMapping("/buscarBoasPraticasFiltradas")	
	public ResponseEntity<List<BoaPraticaFiltradaIntegracaoDTO>> buscarBoasPraticasFiltradas(
			@RequestParam(required = false) String nomeBoaPratica,
			@RequestParam(required = false) String nomeCidade, 
			HttpServletRequest request){
		List<BoaPraticaFiltradaIntegracaoDTO> lista = boaPraticaService.buscarBoasPraticasFiltradasIntegracao(nomeBoaPratica, nomeCidade);
		
		try {
			relatorioApiPublicaService.salvarRegistro("Busca boas práticas filtradas", request.getHeader("Origin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body(lista);		
	}
	
	@ApiOperation(value="Busca indicadores filtrados")
	@GetMapping("/buscarIndicadoresFiltrados")
	public ResponseEntity<List<IndicadorIntegracaoDTO>> buscarTodosFiltrados(
			@RequestParam(required = false) String nome,
			HttpServletRequest request) {
		List<IndicadorIntegracaoDTO> listaIndicadores = indicadorPreenchidoService.filtrarIndicadoresIntegracao(nome);
		for (IndicadorIntegracaoDTO indicadorIntegracaoDTO : listaIndicadores) {		
			Indicador indicador = indicadorService.listarById(indicadorIntegracaoDTO.getId());
			calculadoraFormulaUtil.formatarFormula(indicador);
			indicadorIntegracaoDTO.setFormulaResultado(indicador.getFormulaResultado());
			indicadorIntegracaoDTO.setUrlPlataforma(profileUtil.getProperty("profile.frontend") + "/visualizarindicador/" + indicador.getId());
		}
		
		try {
			relatorioApiPublicaService.salvarRegistro("Busca indicadores filtrados", request.getHeader("Origin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body(listaIndicadores);
		
	}

	@ApiOperation(value="Busca cidades com indicadores preenchidos")
	@GetMapping("/buscarCidadesComIndicadorPreenchido")
	public ResponseEntity<List<IndicadorPreenchidoIntegracaoDTO>> buscarCidadesComIndicadorPreenchido(
			@RequestParam (required = false) Long populacaoMinima,
			@RequestParam (required = false) Long populacaoMaxima,
			@RequestParam (required = false) String nomeCidade,
			@RequestParam (required = false) String nomeIndicador,
			HttpServletRequest request) {
		
		try {
			relatorioApiPublicaService.salvarRegistro("Busca cidades com indicadores preenchidos", request.getHeader("Origin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body(indicadorPreenchidoService.buscarCidadesComIndicadorPreenchidoIntegracao(populacaoMinima, populacaoMaxima, nomeCidade, nomeIndicador));
	}
	
	@ApiOperation(value="Busca variáveis filtradas")
	@GetMapping("/buscarVariaveisFiltradas")	
	public ResponseEntity<List<VariavelFiltradaIntegracaoDTO>> buscarVariaveisFiltradas(
			@RequestParam(required = false) String nomeVariavel,
			@RequestParam(required = false) String nomeCidade,
			HttpServletRequest request){
		
		try {
			relatorioApiPublicaService.salvarRegistro("Busca variáveis filtradas", request.getHeader("Origin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body(variavelService.buscarVariaveisFiltradasIntegracao(nomeVariavel, nomeCidade));		
	}
	
	@ApiOperation(value="Busca variáveis preenchidas filtradas")
	@GetMapping("/buscarVariaveisPreenchidasFiltradas")
	public ResponseEntity<List<VariavelPreenchidaFiltradaIntegracaoDTO>> buscaPorAnoVariavel(
			@RequestParam(required = false) Short ano,  
			@RequestParam(required = false) String nomeVariavel,
			@RequestParam(required = false) String nomeCidade,
			HttpServletRequest request) {
		
		try {
			relatorioApiPublicaService.salvarRegistro("Busca variáveis preenchidas filtradas", request.getHeader("Origin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			return ResponseEntity.ok().body(variavelPreenchidaService.buscaVariavelPreenchidaPorAnoVariavelIntegracao(ano, nomeVariavel, nomeCidade));	
	}
}
