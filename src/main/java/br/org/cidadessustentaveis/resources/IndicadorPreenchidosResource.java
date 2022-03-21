package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.CidadeComboDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CidadeMapaDTO;
import br.org.cidadessustentaveis.dto.CidadeQtIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.ComparativoGraficoDTO;
import br.org.cidadessustentaveis.dto.FatorDesigualdadeDTO;
import br.org.cidadessustentaveis.dto.IndicadorComparativoDeCidadeDTO;
import br.org.cidadessustentaveis.dto.IndicadorParaComboDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoMapaDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoStatusDTO;
import br.org.cidadessustentaveis.dto.IndicadoresPreenchidosDTO;
import br.org.cidadessustentaveis.dto.IndicadoresPreenchidosSubdivisaoDTO;
import br.org.cidadessustentaveis.dto.ResultadoIndicadorDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.TabelaComparativoCidadeDTO;
import br.org.cidadessustentaveis.dto.TabelaIndicadorDTO;
import br.org.cidadessustentaveis.dto.TreeMapAnoDTO;
import br.org.cidadessustentaveis.dto.TreeMapChartDTO;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.services.IndicadorComparativoDeCidadesService;
import br.org.cidadessustentaveis.services.IndicadorPreenchidoService;
import br.org.cidadessustentaveis.services.IndicadorService;
import br.org.cidadessustentaveis.services.PrefeituraService;
import br.org.cidadessustentaveis.services.UsuarioService;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/indicador/preenchidos")
public class IndicadorPreenchidosResource {
	
	@Autowired
	private IndicadorPreenchidoService service;
	
	@Autowired
	private IndicadorService indicadorService;
	
	@Autowired
	private CalculadoraFormulaUtil calculadora;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private IndicadorComparativoDeCidadesService comparativoService;
	


	@GetMapping
	public ResponseEntity<List<IndicadorPreenchidoDTO>> buscarPreenchidos(Principal principal) {
        List<IndicadorPreenchidoDTO> preenchidos = new ArrayList<>();
        
		Prefeitura prefeitura = getPrefeitura(principal);
        List<IndicadorPreenchido> encontrados = prefeitura != null ? service.buscarPorPrefeitura(prefeitura) : service.buscar();
		encontrados.forEach(indicador -> preenchidos.add(new IndicadorPreenchidoDTO(indicador)));
        
		return ResponseEntity.ok().body(preenchidos);
	}

	@GetMapping("/ano/{ano}")
	public ResponseEntity<List<IndicadorPreenchidoStatusDTO>> buscarPreenchidosPorAno(Principal principal, @PathVariable Short ano) {
		Prefeitura prefeitura = getPrefeitura(principal);
		List<Indicador> indicadores;
		if (prefeitura != null) {
			indicadores = indicadorService.listarTodosParaPrefeitura(prefeitura);
		}
		else {
			indicadores = indicadorService.listar();
		}
        List<IndicadorPreenchidoStatusDTO> preenchidos = new ArrayList<>();
        indicadores.forEach(indicador -> {
        	List<IndicadorPreenchido> encontrados;
        	if ( prefeitura != null){
        		Optional<IndicadorPreenchido> preenchido = service.buscarPorAno(prefeitura, indicador, ano);
        		encontrados = preenchido.isPresent() ? Collections.singletonList(preenchido.get()) : Collections.emptyList();
        	} else {
        		encontrados = service.buscarPorAno(indicador, ano);
        	}
        	
        	if ( encontrados.isEmpty() ) {
        		List<IndicadorPreenchido> indicadoresPreenchidos = service.findByPrefeituraAndIndicador(prefeitura, indicador);
        		if(!indicadoresPreenchidos.isEmpty()) {
        			preenchidos.add(new IndicadorPreenchidoStatusDTO(indicador, indicadoresPreenchidos, "Preenchido (anos anteriores)"));
        		} else {
        			preenchidos.add(new IndicadorPreenchidoStatusDTO(indicador));
        		}    		
        	} else {
        		for (IndicadorPreenchido encontrado : encontrados) {
        			preenchidos.add(new IndicadorPreenchidoStatusDTO(indicador, encontrado));
				}
        	}

        });
        
		return ResponseEntity.ok().body(preenchidos);
	}

	@GetMapping("/subdivisao/{subdivisao}/{ano}")
	public ResponseEntity<List<IndicadorPreenchidoStatusDTO>> buscarPreenchidosPorSubdivisaoAno(Principal principal, @PathVariable Long subdivisao, @PathVariable Short ano) {
		Prefeitura prefeitura = getPrefeitura(principal);
		List<Indicador> indicadores;
		if (prefeitura != null) {
			indicadores = indicadorService.listarTodosParaPrefeitura(prefeitura);
		}
		else {
			indicadores = indicadorService.listar();
		}
        List<IndicadorPreenchidoStatusDTO> preenchidos = new ArrayList<>();
        indicadores.forEach(indicador -> {

        	List<SubdivisaoIndicadorPreenchido> encontrados = new ArrayList<SubdivisaoIndicadorPreenchido>();
			Long idCidade = prefeitura.getCidade().getId();
			SubdivisaoIndicadorPreenchido preenchido = service.buscarPorCidadeIndicadorSubdivisaoAno(idCidade, indicador,subdivisao, ano);
			if(preenchido != null) {
				encontrados.add(preenchido);
			}
        	
        	if ( encontrados.isEmpty() ) {
        		List<SubdivisaoIndicadorPreenchido> indicadoresPreenchidos = service.buscarPorSubdivisaoEIndicadorECidade(subdivisao, indicador.getId(), idCidade);
            	
        		if(!indicadoresPreenchidos.isEmpty()) {
        			preenchidos.add(new IndicadorPreenchidoStatusDTO(indicador, indicadoresPreenchidos, "Preenchido (anos anteriores)", true));
        		} else {
        			preenchidos.add(new IndicadorPreenchidoStatusDTO(indicador));
        		} 
        	}
        	else {
        		for (SubdivisaoIndicadorPreenchido encontrado : encontrados) {
        			preenchidos.add(new IndicadorPreenchidoStatusDTO(indicador, encontrado, true));
				}
        	}

        });
        
		return ResponseEntity.ok().body(preenchidos);
	}



	@GetMapping("/{id}")
	public ResponseEntity<IndicadorPreenchidoDTO> buscarPreenchidoPorId(@PathVariable Long id) {
        IndicadorPreenchido preenchido2 = service.buscarPorId(id);
		IndicadorPreenchidoDTO preenchido = new IndicadorPreenchidoDTO(preenchido2);
        
		return ResponseEntity.ok().body(preenchido);
	}
	

	@GetMapping("/indicador")
	public ResponseEntity<List<IndicadorPreenchidoSimplesDTO>> buscarPreenchidoPorIndicador(@RequestParam ("idIndicador") Long idIndicador, @RequestParam ("cidades") List<Long> idCidades) {
		Indicador indicador = indicadorService.listarById(idIndicador);
		calculadora.formatarFormula(indicador);
		
        List<IndicadorPreenchidoSimplesDTO> preenchidos = service.listarPorIndicadorECidades(idIndicador, idCidades);       
		
		return ResponseEntity.ok().body(preenchidos);
	}

	@GetMapping("/indicador/{id}")
	public ResponseEntity<IndicadoresPreenchidosDTO> buscarPreenchidoPorIndicador(Principal principal, @PathVariable Long id) {
		Prefeitura prefeitura = getPrefeitura(principal);
		Indicador indicador = indicadorService.listarById(id);
		calculadora.formatarFormula(indicador);
		
        List<IndicadorPreenchido> preenchidos = service.buscarPorIndicador(prefeitura, indicador);
        List<VariavelPreenchida> variaveisPreenchidas = service.buscarVariaveisPreenchidasPorIndicador(prefeitura, indicador);
		
		IndicadoresPreenchidosDTO indicadoresPreenchidosDTO = new IndicadoresPreenchidosDTO(indicador, preenchidos, variaveisPreenchidas);
		
		return ResponseEntity.ok().body(indicadoresPreenchidosDTO);
	}

	@GetMapping("/indicador/{id}/{subdivisao}")
	public ResponseEntity<IndicadoresPreenchidosDTO> buscarPreenchidoPorIndicadorSubdivisao(Principal principal, @PathVariable Long id, @PathVariable Long subdivisao) {
		Prefeitura prefeitura = getPrefeitura(principal);
		Long idCidade = prefeitura.getCidade().getId();
		Indicador indicador = indicadorService.listarById(id);
		calculadora.formatarFormula(indicador);
		

        List<SubdivisaoIndicadorPreenchido> preenchidos = this.service.buscarPorSubdivisaoEIndicadorECidade(subdivisao, indicador.getId(), idCidade);
        List<SubdivisaoVariavelPreenchida> variaveisPreenchidas = this.service.buscarVariaveisPreenchidasPorIndicador(idCidade, indicador.getId(), subdivisao);
		
		IndicadoresPreenchidosDTO indicadoresPreenchidosDTO = new IndicadoresPreenchidosDTO(indicador, preenchidos, variaveisPreenchidas,true);
		
		return ResponseEntity.ok().body(indicadoresPreenchidosDTO);
	}

	@GetMapping("/resultado/{id}")
	public ResponseEntity<ResultadoIndicadorDTO> buscarResultadoIndicadorPorId(Principal principal, @PathVariable Long id) {
		Prefeitura prefeitura = getPrefeitura(principal);
		ResultadoIndicadorDTO resultadoIndicador = prefeitura != null ? service.searchResultadoIndicadorPorId(prefeitura, id) : service.searchResultadoIndicadorPorId(id);
		return ResponseEntity.ok().body(resultadoIndicador);
	}

	@Secured({ "ROLE_PREENCHER_INDICADOR" })
	@PostMapping
	public ResponseEntity<IndicadorPreenchidoDTO> preencherIndicador(Principal principal, @Valid @RequestBody IndicadorPreenchidoDTO dto) {
		Prefeitura prefeitura = getPrefeitura(principal);
		if (prefeitura != null){
	        IndicadorPreenchido entity = service.preencherIndicador(prefeitura, dto);
	        dto.setId(entity.getId());
	        if(entity != null) {
	    		return ResponseEntity.ok().body(dto);
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@Secured({ "ROLE_PREENCHER_INDICADOR" })
	@PutMapping
	public ResponseEntity<IndicadorPreenchidoDTO> editar(Principal principal, @Valid @RequestBody IndicadorPreenchidoDTO dto) {
		Prefeitura prefeitura = getPrefeitura(principal);
		if (prefeitura != null) {
			IndicadorPreenchido entity = service.editar(prefeitura, dto);
	        if(entity != null) {
	        	return ResponseEntity.ok().location(gerarURItoEntity(entity)).build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@Secured({ "ROLE_PREENCHER_INDICADOR" })
	@PostMapping("/{idSubdivisao}")
	public ResponseEntity<IndicadorPreenchidoDTO> preencherIndicadorSubdivisao( @Valid @RequestBody IndicadorPreenchidoDTO dto, @PathVariable Long idSubdivisao) {
		SubdivisaoIndicadorPreenchido entity = service.preencherIndicadorSubdivisao(dto, idSubdivisao);
		dto.setId(entity.getId());
		if(entity != null) {
			return ResponseEntity.ok().body(dto);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/qtdPreenchidosCidade")
	public List<CidadeQtIndicadorPreenchidoDTO> qtdPreenchidosCidade(){
		List<CidadeQtIndicadorPreenchidoDTO> lista = new ArrayList<>();
		lista = service.contarQuantidadeIndicadoresPreenchidos();
		return lista;
	}

	@GetMapping("/indicadoresJaPreenchidos")
	public List<IndicadorParaComboDTO> indicadoresJaPreenchidos(@RequestParam Long estado, @RequestParam Long cidade, @RequestParam Integer populacao, @RequestParam Long eixo,@RequestParam Long ods){
		List<IndicadorParaComboDTO> dtos = service.buscarIndicadorJaPreenchido(estado, cidade, populacao, eixo, ods);		
		
		dtos = dtos.stream().sorted(Comparator.comparing(IndicadorParaComboDTO::getNome)).collect(Collectors.toList()); 
		return dtos;
	}

	@GetMapping("/tabela/indicadores")
	public List<TabelaIndicadorDTO> indicadoresJaPreenchidos(@RequestParam Long indicador , @RequestParam List<Long> cidades){
		return service.buscarPorIndicadorParaTabela(indicador, cidades);
	}
	
	@GetMapping("/mapa/indicadores")
	public List<CidadeMapaDTO> indicadoresJaPreenchidosMapa(@RequestParam Long indicador, @RequestParam List<Long> cidades){		
		return service.buscarPorIndicadorParaMapa(indicador, cidades );
	}
	
	@GetMapping("/cidades")
	public List<CidadeDTO> cidadesQuePreencheram(@RequestParam Long indicador){		
		return service.buscarCidadesPreencheramSimplificado(indicador);
	}
	
	
	@GetMapping("/grafico/indicadores")
	public IndicadorComparativoDeCidadeDTO indicadoresJaPreenchidosGrafico(@RequestParam Long indicador,@RequestParam List<Long> cidades,@RequestParam Integer formulaidx){				
		List<CidadeDTO> cidadeDTOS = new ArrayList<>();
		for(Long id : cidades) {
			CidadeDTO dto = new CidadeDTO(id);	
			cidadeDTOS.add(dto);
		}
		IndicadorComparativoDeCidadeDTO dto = comparativoService.buscarIndicadorComparativoDeCidades(indicador,cidadeDTOS , formulaidx );
		
		List<ComparativoGraficoDTO> listaMandatos = new ArrayList<>();
		ComparativoGraficoDTO mandato = new ComparativoGraficoDTO();
		
		List<TreeMapAnoDTO> listaAnos = new ArrayList<>();
		List<TreeMapAnoDTO> listaAnos2 = new ArrayList<>();
		TreeMapAnoDTO ano = new TreeMapAnoDTO();
		
		List<TreeMapChartDTO> listaValores = new ArrayList<>();
		TreeMapChartDTO valor = new TreeMapChartDTO();
		
		for (ComparativoGraficoDTO m : dto.getGraficos()) {
			int anoMandato = m.getInicioMandato().intValue();
			List<TreeMapAnoDTO> listaAnosAux = m.getTreeMap();
			for (TreeMapAnoDTO a : listaAnosAux) {
				if(a.getAno().intValue() == anoMandato) {
					listaValores.add(a.getValores().get(0));
					a.setValores(new ArrayList<>());
					a.setAno(a.getAno());
					a.setValores(listaValores);
				}
				else {
					anoMandato += 1;
				}
			}
			m.setTreeMap(m.getTreeMap().stream().filter(distinctByKey(TreeMapAnoDTO::getAno)).collect(Collectors.toList()));
			for (TreeMapAnoDTO tree : m.getTreeMap()) {
				tree.setValores(tree.getValores().stream().sorted(Comparator.comparing(TreeMapChartDTO::getValue).reversed()).collect(Collectors.toList()));
			}
		}
		
		return dto;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        
        return t -> seen.add(keyExtractor.apply(t));
    }

	private URI gerarURItoEntity(IndicadorPreenchido entity) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
		                                       .path("/{id}")
		                                       .buildAndExpand(entity.getId()).toUri();
		return uri;
	}
	
	private Prefeitura getPrefeitura(Principal principal) {
		Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
		Prefeitura prefeitura  = prefeituraService.buscarPrefeituraPorIdUsuario(usuarioLogado.getId());
		return prefeitura;
	}
	
	@GetMapping("/tabela/indicadoresvisualizaindicador")
	public List<TabelaIndicadorDTO> indicadoresJaPreenchidosVisualizarIndicador(@RequestParam Long indicador, @RequestParam Long estado, @RequestParam Long cidade, @RequestParam Long populacao){
		List<TabelaIndicadorDTO> lista = new ArrayList<>();
		lista = service.filtrarIndicadoresECidadesPaginaInicialIndicadores(estado, cidade, null, populacao, null, null, indicador);
		if(lista != null && lista.size() > 0) {
			return lista;
		}
		return null;
	}
	
	@GetMapping("/mapa/indicadoresvisualizaindicador")
	public List<CidadeMapaDTO> indicadoresJaPreenchidosMapaVisualizarIndicador(@RequestParam Long indicador, @RequestParam Long estado, @RequestParam Long cidade, @RequestParam Long populacao){		
		List<CidadeMapaDTO> lista = new ArrayList<>();
		lista = service.buscarPorIndicadorParaMapaVisualizarIndicador(indicador, estado, cidade, populacao);
		if(lista != null && lista.size() > 0) {
			return lista;
		}
		return null;
	}
	
	@GetMapping("/grafico/indicadoresvisualizaindicador")
	public IndicadorComparativoDeCidadeDTO indicadoresJaPreenchidosGraficoVisualizarIndicador(@RequestParam Long indicador, @RequestParam Long estado, @RequestParam Long cidade, @RequestParam Long populacao, @RequestParam Integer indexFormula){
		IndicadorComparativoDeCidadeDTO dto = new IndicadorComparativoDeCidadeDTO();
		dto = comparativoService.buscarIndicadorComparativoDeCidadesVisualizaIndicador(indicador, estado, cidade, populacao, indexFormula);
		if(dto.getGraficos() != null) {
			return dto;
		}
		return null;
	}
	
	@GetMapping("/tabela/indicadorescomparativocidades")
	public TabelaComparativoCidadeDTO indicadoresJaPreenchidosComparativoCidades(@RequestParam Long indicador, @RequestParam List<Long> cidades){
		TabelaComparativoCidadeDTO tabelaComparativoCidades = service.buscarPorIndicadorParaTabelaComparativoCidades(indicador, cidades);
		return tabelaComparativoCidades;
	}
	
	@GetMapping("/mapa/indicadorescomparativocidades")
	public List<CidadeMapaDTO> indicadoresJaPreenchidosMapaComparativoCidades(@RequestParam Long indicador, @RequestParam List<Long> cidades){
		List<CidadeMapaDTO> mapaComparativoCidade = service.buscarPorIndicadorParaMapaComparativoCidades(indicador, cidades);
		return mapaComparativoCidade;
	}
	
	@GetMapping("/grafico/indicadorescomparativocidades")
	public IndicadorComparativoDeCidadeDTO indicadoresJaPreenchidosGraficoComparativoCidades(@RequestParam Long indicador, @RequestParam List<Long> cidades){			
			IndicadorComparativoDeCidadeDTO dto = comparativoService.buscarIndicadorComparativoDeCidadesComparativoCidades(indicador,cidades);
			return dto;
	}
	
	
	@GetMapping("/filtrarCidadesPaginaInicialIndicadores")
	public List<CidadeComboDTO> filtrarCidadesPaginaInicialIndicadores(@RequestParam Long idEstado,
			@RequestParam Long populacaoMin, @RequestParam Long populacaoMax, @RequestParam Long idEixo, 
			@RequestParam Long idOds){
		return service.filtrarCidadesPaginaInicialIndicadores(idEstado, populacaoMin, populacaoMax, idEixo, idOds);
	}
	
	@GetMapping("/filtrarIndicadoresPaginaInicialIndicadores")
	public List<TabelaIndicadorDTO> filtrarIndicadoresPaginaInicialIndicadores(@RequestParam Long idEstado, @RequestParam Long idCidade,
			@RequestParam Long populacaoMin, @RequestParam Long populacaoMax, @RequestParam Long idEixo, 
			@RequestParam Long idOds, @RequestParam Long idIndicador){
		return service.filtrarIndicadoresPaginaInicialIndicadores(idEstado, idCidade, populacaoMin, populacaoMax, idEixo, idOds, idIndicador);
	}
	
	@GetMapping("/filtrarMapaResultadoPaginaInicialIndicadores")
	public List<CidadeMapaDTO> filtrarMapaResultadoPaginaInicialIndicadores(@RequestParam Long idEstado,
																			@RequestParam Long idCidade,
																			@RequestParam Long populacaoMin,
																			@RequestParam Long populacaoMax,
																			@RequestParam Long idEixo,
																			@RequestParam Long idOds,
																			@RequestParam Long idIndicador,
																			@RequestParam Short anoInicialMandato){
		return service.filtrarMapaResultadoPaginaInicialIndicadores(idEstado, idCidade, populacaoMin, populacaoMax,
																	idEixo, idOds, idIndicador, anoInicialMandato);
	}
	
	@GetMapping("/mapa/buscarCidadesPorIndicadorMandato")
	public List<CidadeMapaDTO> buscarCidadesPorIndicadorMandato(@RequestParam Long indicador, @RequestParam List<Long> cidades, @RequestParam Short anoInicialMandato){		
		return service.buscarCidadesPorIndicadorMandato(indicador, cidades ,anoInicialMandato);
	}
	
	@GetMapping("/recalcular")
	public void recalcular(@RequestParam(defaultValue = "true") Boolean pcs , @RequestParam(defaultValue = "false") Boolean prefeitura, @RequestParam(defaultValue = "true") boolean numerico, @RequestParam(defaultValue = "true") boolean texto, @RequestParam(defaultValue = "2016") int anoInicial ){		
		service.recalcularTudo(pcs, prefeitura,numerico, texto, anoInicial);
	}
	
	@GetMapping("/recalcularIndicador")
	public void recalcular(@RequestParam Long idIndicador){		
		service.recalcular(idIndicador, 2005);
	}
	
	@GetMapping("/buscarCidadesComIndicadorPreenchido")
	public ResponseEntity<List<IndicadorPreenchidoMapaDTO>> buscarCidadesComIndicadorPreenchido(@RequestParam ("idIndicador") Long idIndicador, @RequestParam ("idEixo") Long idEixo,
			@RequestParam ("idOds") Long idOds,
			@RequestParam ("idCidade") Long idCidade,
			@RequestParam ("popuMin") Long popuMin,
			@RequestParam ("popuMax") Long popuMax,
			@RequestParam ("valorPreenchido") String valorPreenchido, 
			@RequestParam ("ano") Short ano,
			@RequestParam ("formula") Long idxFormula,
			@RequestParam ("visualizarComoPontos") boolean visualizarComoPontos) throws Exception {
		return ResponseEntity.ok().body(service.buscarCidadesComIndicadorPreenchido(idIndicador,idEixo, idOds, idCidade, popuMin, popuMax, valorPreenchido, ano, idxFormula, visualizarComoPontos));
	}

	@GetMapping("/calcularResultadoApresentacao")
	public void calcularResultadoApresentacao() {
		service.calcularResultadoApresentacao();		
		
	}

	@GetMapping("/calcularValorTexto")
	public void calcularValorTexto() {
		service.calcularValorTexto();		
		
	}
	

	@GetMapping("/subdivisao/livre/{idIndicador}/{idSubdivisao}")
	public ResponseEntity<IndicadoresPreenchidosSubdivisaoDTO> buscarPreenchidosPorIndicadorSubdivisao(@PathVariable Long idIndicador, @PathVariable Long idSubdivisao) {
		Indicador indicadorEscolhido = indicadorService.listarById(idIndicador);
		calculadora.formatarFormula(indicadorEscolhido);

        List<SubdivisaoIndicadorPreenchido> preenchidos = this.service.buscarPorSubdivisaoEIndicadorECidade(idSubdivisao, indicadorEscolhido.getId(), null);
        List<SubdivisaoVariavelPreenchida> variaveisPreenchidas = this.service.buscarVariaveisPreenchidasPorIndicador(null, indicadorEscolhido.getId(), idSubdivisao);
		
		IndicadoresPreenchidosSubdivisaoDTO indicadoresPreenchidosDTO = new IndicadoresPreenchidosSubdivisaoDTO(indicadorEscolhido, preenchidos, variaveisPreenchidas,true);
		
		return ResponseEntity.ok().body(indicadoresPreenchidosDTO);
	}

	@GetMapping("/subdivisao/fator-desigualdade/{idIndicador}/{idCidade}/{nivel}")
	public ResponseEntity<List<FatorDesigualdadeDTO>> fatorDesigualdade(@PathVariable Long idIndicador, @PathVariable Long idCidade, @PathVariable Long nivel) {
		List<FatorDesigualdadeDTO> dtos = this.service.calcularFatorDesigualdade(idIndicador, idCidade, nivel);
		return ResponseEntity.ok().body(dtos);
	}

	
	@GetMapping("/subdivisao/nivel/{idIndicador}/{idCidade}/{nivel}/{ano}")
	public ResponseEntity<List<SubdivisaoIndicadorPreenchidoDTO>> buscarPorInidicadorCidadeNivel(@PathVariable Long idIndicador, @PathVariable Long idCidade, @PathVariable Long nivel,@PathVariable Short ano) {
		
		List<SubdivisaoIndicadorPreenchido> preenchidos = 
			this.service.subdvisaoIndicadorPreenchidoPorIndicadorCidadeNivelAno(idIndicador, idCidade, nivel, ano);
		
		List<SubdivisaoIndicadorPreenchidoDTO> dtos = new ArrayList<>();
		if(preenchidos != null){
			for(SubdivisaoIndicadorPreenchido sip : preenchidos){
				SubdivisaoIndicadorPreenchidoDTO dto = new SubdivisaoIndicadorPreenchidoDTO(sip);
				dtos.add(dto);
			}
		}
		return ResponseEntity.ok().body(dtos);
	}
}

