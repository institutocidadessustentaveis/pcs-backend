package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.dto.CidadeComparativoDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.IndicadorComparativoDeCidadeDTO;
import br.org.cidadessustentaveis.dto.IndicadorcomparativomesmacidadeDTO;
import br.org.cidadessustentaveis.services.IndicadorComparativoDeCidadesService;
import br.org.cidadessustentaveis.services.IndicadorService;
import br.org.cidadessustentaveis.services.PainelIndicadorService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/buscarindicadorcomparativodecidades")
public class IndicadorComparativoDeCidadesResource {
	@Autowired
	private IndicadorComparativoDeCidadesService indicadorComparativoDeCidadeService;
	
	@Autowired
	private PainelIndicadorService service;
	@Autowired
	private IndicadorService indicadorService;
	
	@GetMapping("")
	public IndicadorComparativoDeCidadeDTO buscarIndicadorComparativoDeCidades(
			@RequestParam(name = "indicador") Long idIndicador,
			@RequestParam(name = "filtroCidades") String filtroCidades) {
		
		List<CidadeDTO> cidades = service.buscarCidadesPorIndicador(idIndicador);
		
		List<CidadeDTO> cidadesFiltradas = new ArrayList<CidadeDTO>();
		
		String[] idsCidadesFiltro  = filtroCidades.split("[,]");
		
		// Filtrar as cidades
		for (int i = 0; i < cidades.size(); i++) {   

			for(int x = 0; x < idsCidadesFiltro.length; x++) {
				if(cidades.get(i).getId() == Long.parseLong(idsCidadesFiltro[x])) {
					cidadesFiltradas.add(cidades.get(i));
				}
			}			
		}
		IndicadorComparativoDeCidadeDTO lista = indicadorComparativoDeCidadeService.buscarIndicadorComparativoDeCidades(idIndicador, cidadesFiltradas,null);		
		return lista;
	}
	
	@GetMapping("/cidades/{id}")
	public List<CidadeComparativoDTO> buscarCidadesPorIndicadorTela(@PathVariable("id") Long idIndicador) {

		List<Cidade> cidades = service.buscarCidadesPorIndicadorSemDTO(idIndicador);
		List<CidadeComparativoDTO> dto = cidades.stream()
													.distinct()
													.map((c) -> new CidadeComparativoDTO(c))
												.collect(Collectors.toList());
		
		return dto;
	}

	@GetMapping("/cidades/porNome")
	public List<CidadeComparativoDTO> buscarCidadesPorIndicadorEPorNome(@RequestParam("id") Long idIndicador,
																		@RequestParam("nome") String nome) {

		List<Cidade> cidades = service.buscarCidadesPorIndicadorEPorNome(idIndicador, nome);
		List<CidadeComparativoDTO> dto = cidades.stream()
													.distinct()
													.map((c) -> new CidadeComparativoDTO(c))
												.collect(Collectors.toList());
		return dto;
	}

	@GetMapping("/buscarindicadorporcidade/{id}")
	public List<IndicadorcomparativomesmacidadeDTO> buscarIndicadorPorCidade(@PathVariable("id") Long idCidade) {
		List<IndicadorcomparativomesmacidadeDTO> indicadores = indicadorService.listarTodosIndicadoresPorIdCidade(idCidade);
		return indicadores;
	}
	
	@Secured({"ROLE_VISUALIZAR_INDICADOR_PREENCHIDO"})
	@GetMapping("/buscarindicadorporcidadeporano/{id}/{ano}")
	public List<IndicadorcomparativomesmacidadeDTO> buscarIndicadorPorCidadePorAno(@PathVariable("id") Long idCidade, @PathVariable("ano") String ano) {
		List<IndicadorcomparativomesmacidadeDTO> indicadores = indicadorService.listarTodosIndicadoresPorIdCidadePorAno(idCidade,ano);
		return indicadores;
	}
	
}
