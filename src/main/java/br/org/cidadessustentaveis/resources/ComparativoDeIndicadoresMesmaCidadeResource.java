package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.ComparativoDeIndicadoresMesmaCidadeDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.repository.PrefeituraRepository;
import br.org.cidadessustentaveis.services.CidadeService;
import br.org.cidadessustentaveis.services.ComparativoDeIndicadoresMesmaCidadesService;
import br.org.cidadessustentaveis.services.IndicadorPreenchidoService;
import br.org.cidadessustentaveis.services.IndicadorService;
import br.org.cidadessustentaveis.services.PainelIndicadorService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/buscarComparativoDeIndicadoresMesmaCidade")
public class ComparativoDeIndicadoresMesmaCidadeResource {
	@Autowired
	private ComparativoDeIndicadoresMesmaCidadesService comparativoDeIndicadoresMesmaCidadesService;
	
	@Autowired
	private PainelIndicadorService service;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private PrefeituraRepository prefeituraRepository;
	
	@Autowired
	private IndicadorService indicadorService;
	
	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;

	@GetMapping("")
	public ComparativoDeIndicadoresMesmaCidadeDTO buscarComparativoDeIndicadoresMesmaCidade(
			@RequestParam(name = "cidade") Long idCidade,
			@RequestParam(name = "sigla") String sigla, 
			@RequestParam(name = "filtroIndicadores") String filtroIndicadores) {
		
		Cidade cidade = cidadeService.buscarPorId(idCidade);
		
		List<IndicadorPreenchido> indicadores = new ArrayList<IndicadorPreenchido>();
		indicadores = indicadorPreenchidoService.buscarPorCidade(cidade);
				
		List<IndicadorPreenchido> indicadoresFiltrados = new ArrayList<IndicadorPreenchido>();
		
		String[] idsIndicadoresFiltro  = filtroIndicadores.split("[,]");
		
		// Filtrar os indicadores
		for (int i = 0; i < indicadores.size(); i++) {   
			// Converte lista de indicadores para lista de indicadoresFiltrados
			for(int x = 0; x < idsIndicadoresFiltro.length; x++) {
				if(indicadores.get(i).getIndicador().getId() == Long.parseLong(idsIndicadoresFiltro[x])) {
					indicadoresFiltrados.add(indicadores.get(i));
				}
			}
		}
		ComparativoDeIndicadoresMesmaCidadeDTO lista = comparativoDeIndicadoresMesmaCidadesService.buscarComparativoDeIndicadoresMesmaCidade(idCidade, indicadoresFiltrados);
		
		return lista;
	}
	
	@GetMapping("/cidades/{id}")
	public List<CidadeDTO> buscarCidadesPorIndicadorTela(@PathVariable("id") Long idIndicador) {
		List<CidadeDTO> cidades = service.buscarCidadesPorIndicador(idIndicador);
		return cidades;
	}
	
	
	
}
