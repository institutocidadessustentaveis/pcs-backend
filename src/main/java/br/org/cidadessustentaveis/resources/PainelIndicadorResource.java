package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.PainelCidadeIndicadoresDTO;
import br.org.cidadessustentaveis.dto.ProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.VariacaoReferenciasDTO;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.services.PainelIndicadorService;
import br.org.cidadessustentaveis.util.MandatoUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/painel")
public class PainelIndicadorResource {

	@Autowired
	private PainelIndicadorService service;
	
	@GetMapping("/mandatos")
	public ResponseEntity<List<List<String>>> buscarMandatos() {
		return ResponseEntity.ok().body(MandatoUtil.getMandatos());
	}	
	

	@GetMapping("/estados")
	public ResponseEntity<List<ProvinciaEstadoDTO>> buscarEstadosSignatarios() {
		List<ProvinciaEstadoDTO> estados = service.buscarEstadosSignatarias().stream()
																			 .map(estado -> new ProvinciaEstadoDTO(estado))
																			 .collect(Collectors.toList());
		return ResponseEntity.ok().body(estados);
	}
	@GetMapping("/indicadores")
	public ResponseEntity<List<List<String>>> buscarIndicadoresPorEixo(@RequestParam Long idEixo, @RequestParam Long  idCidade, @RequestParam Short  anoInicial, @RequestParam Short  anoFinal) {
		List<List<String>> tabela = service.buscarTabelaIndicadores(idEixo, idCidade, anoInicial, anoFinal);
		return ResponseEntity.ok().body(tabela);
	}
	
	@GetMapping("/variacaoReferencias")
	public ResponseEntity<List<VariacaoReferenciasDTO>> buscarReferenciasComVariacao(@RequestParam Long idIndicador){
		List<VariacaoReferenciasDTO> tabelaVariacaoReferencias = service.buscarReferenciasComVariacao(idIndicador);
		return ResponseEntity.ok().body(tabelaVariacaoReferencias);
	}
	
	@GetMapping("/indicadoresDaCidade")
	public ResponseEntity<List<List<String>>> buscarIndicadoresPorEixo(@RequestParam Long  idCidade, @RequestParam Short  anoInicial, @RequestParam Short  anoFinal) {
		List<List<String>> tabela = service.buscarTabelaIndicadoresDaCidade(idCidade, anoInicial, anoFinal);		
		return ResponseEntity.ok().body(tabela);
	}

	@GetMapping("/variaveis")
	public ResponseEntity<List<List<String>>> buscarTabelaVariaveis(@RequestParam Long idIndicador, @RequestParam Long  idCidade, @RequestParam Short  anoInicial, @RequestParam Short  anoFinal, @RequestParam Long  idSubdivisao) {
		List<List<String>> tabela = service.buscarTabelaVariaveis(idIndicador, idCidade, anoInicial, anoFinal, idSubdivisao);
		return ResponseEntity.ok().body(tabela);
	}

	@GetMapping("/cidades/estado/{idEstado}")
	public ResponseEntity<List<CidadeDTO>> buscarCidadesSignatarios(@PathVariable Long idEstado) {
		List<CidadeDTO> cidades = service.buscarCidadesSignatarias(idEstado).stream()
																			.map(signataria -> new CidadeDTO(signataria))
																			.collect(Collectors.toList());
		return ResponseEntity.ok().body(cidades);
	}
	
	
	@GetMapping("/cidades/indicador/{idIndicador}")
	public ResponseEntity<List<CidadeDTO>> buscarCidadesPorIndicador(@PathVariable Long idIndicador) {
		List<CidadeDTO> cidades = service.buscarCidadesPorIndicador(idIndicador);
		return ResponseEntity.ok().body(cidades);
	}



	@GetMapping("/indicadores/cidade/{idCidade}")
	public ResponseEntity<PainelCidadeIndicadoresDTO> buscarIndicadores(@PathVariable Long idCidade) {
		return ResponseEntity.ok().body(service.buscarIndicadoresPorCidade(idCidade));
	}
	
	@GetMapping("/cidades/{nome}")
	public ResponseEntity<List<CidadeDTO>> buscarCidadesPorNomeComIndicadoresPreenchidos(@PathVariable String nome) {
		List<CidadeDTO> cidades = service.buscarCidadesPorNomeComIndicadoresPreenchidos(nome.toLowerCase());
		return ResponseEntity.ok().body(cidades);
	}
	
	@Secured({"ROLE_VISUALIZAR_INDICADOR_PREENCHIDO"})
	@GetMapping("/anos/{idPrefeitura}")
	public ResponseEntity<List<Short>> buscarAnosIndicadoresPorPrefeitura(@PathVariable Long idPrefeitura){
		List<Short> lista = new ArrayList<>();
		lista = service.buscarAnosIndicadoresPorPrefeitura(idPrefeitura);
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping("/indicadores/cidade/{siglaEstado}/{nomeCidade}")
	public ResponseEntity<Long> buscarIndicadoresPorNomeEstadoCidade(@PathVariable String siglaEstado, @PathVariable String nomeCidade) {
		return ResponseEntity.ok().body(service.buscarIndicadoresPorNomeEstadoCidade(siglaEstado, nomeCidade));
	}
	
	@GetMapping("/indicadores/variavel")
	public ResponseEntity<List<Long>> buscarIdsIndicadoresPorVariavel(@RequestParam Long idVariavel) {
		List<Long> ids = service.buscarIdsIndicadoresPorVariavel(idVariavel);
		return ResponseEntity.ok().body(ids);
	}
	
	@GetMapping("/variavel/{nomeVariavel}")
	public ResponseEntity<List<Long>> buscarIdsIndicadoresPorNomeVariavel(@PathVariable String nomeVariavel) {
		List<Long> ids = service.buscarIdsIndicadoresPorNomeVariavel(nomeVariavel);
		return ResponseEntity.ok().body(ids);
	}

}
