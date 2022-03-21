package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.ObservacaoVariavelDTO;
import br.org.cidadessustentaveis.dto.VariavelJaPreenchidaSimplesDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaDuplicadaDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaMapaDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaMunicipioDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaSimplesDTO;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.services.IndicadorPreenchidoService;
import br.org.cidadessustentaveis.services.VariavelPreenchidaService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/variavelPreenchida")
public class VariavelPreenchidaResource {

	@Autowired
	private VariavelPreenchidaService variavelPreenchidaService;
	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	
	//@Secured({"ROLE_VISUALIZAR_VARIAVEL"})
	@GetMapping("/buscaPorAnoeCidade/{ano}/{idCidade}/{idVariavel}")
	public VariavelPreenchidaDTO buscaPorAnoeCidade(final @PathVariable("ano") Long ano, final @PathVariable("idCidade") Long idCidade, final @PathVariable("idVariavel") Long idVariavel) {
		
		VariavelPreenchidaDTO variavelPreenchida = variavelPreenchidaService.buscaVariavelPreenchidaPorAnoeCidade(ano, idCidade, idVariavel);
		
		return variavelPreenchida;
				
	}
	@GetMapping("/buscaPorAnoeCidade/{ano}/{idVariavel}")
	public ResponseEntity<VariavelPreenchidaDTO> buscaPorAnoVariavel(final @PathVariable("ano") Long ano,  final @PathVariable("idVariavel") Long idVariavel)
			throws Exception {
		
		try {
			VariavelPreenchidaDTO variavelPreenchida = variavelPreenchidaService.buscaVariavelPreenchidaPorAnoVariavel(ano, idVariavel);
			return ResponseEntity.ok().body(variavelPreenchida);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}		
	}
	
	@GetMapping("/buscaPorAnoCidade/{ano}/{idVariavel}/{idSubdivisao}")
	public VariavelPreenchidaDTO buscaPorAnoVariavelSubdivisao(final @PathVariable("ano") Long ano,  final @PathVariable("idVariavel") Long idVariavel, final @PathVariable("idSubdivisao") Long idSubdivisao)
			throws Exception {
		
		VariavelPreenchidaDTO variavelPreenchida = variavelPreenchidaService.buscaVariavelPreenchidaPorAnoVariavelSubdivisao(ano, idVariavel, idSubdivisao);
		
		return variavelPreenchida;
				
	}

	@GetMapping("/buscaPorSubdivisaoAnoCidade/{subdivisao}{ano}/{idVariavel}")
	public VariavelPreenchidaDTO buscaPorAnoVariavel(final @PathVariable("subdivisao") Long subdivisao, final @PathVariable("ano") Long ano,  final @PathVariable("idVariavel") Long idVariavel)
			throws Exception {
		
		VariavelPreenchidaDTO variavelPreenchida = variavelPreenchidaService.buscaVariavelPreenchidaPorAnoVariavel(ano, idVariavel);
		
		return variavelPreenchida;
				
	}
	
	@GetMapping("/variavel")
	public List<VariavelPreenchidaMunicipioDTO> buscaPorAnoeCidade(@RequestParam ("idVariavel") Long idVariavel, @RequestParam ("cidades") List<Long> idCidades) {
		List<VariavelPreenchidaMunicipioDTO> variavelPreenchida = variavelPreenchidaService.buscarVariavelPreenchidaPorVariavel(idVariavel,idCidades);
		return variavelPreenchida;
				
	}
	
	@GetMapping("/serieHistorica")
	public List<List<String>>buscarSerieHistorica(@RequestParam Long idIndicador,@RequestParam Long idCidade,@RequestParam Short anoInicial, @RequestParam Short anoFinal) {
		List<List<String>> tabela = variavelPreenchidaService.serieHistorica(idIndicador, idCidade, anoInicial, anoFinal);
		
		
		return tabela;
				
	}

	@GetMapping("/excluirDuplicadas")
	public void excluirDuplicadas() {
		List<VariavelPreenchidaDuplicadaDTO> duplicadas = variavelPreenchidaService.buscarDuplicadas();
		for(VariavelPreenchidaDuplicadaDTO duplicada : duplicadas) {
			System.out.println(duplicada.toString());
			List<VariavelPreenchida> vps = variavelPreenchidaService.findByVariavelIdAndAnoAndPrefeituraid(duplicada.getIdVariavel(), duplicada.getIdPrefeitura(), duplicada.getAno());
			for(int i = 0; i < vps.size(); i++) {
				if(i != 0) {
					VariavelPreenchida vp = vps.get(i);
					variavelPreenchidaService.deletar(vp.getId());
				}
			}
		}
	}

	@DeleteMapping("/{id}")
	public void deletarVariavelPreenchida(@PathVariable Long id) throws AuthenticationException {
		variavelPreenchidaService.deletarIncluindoIndicadoresPreenchidos(id);
	}



	@GetMapping("/carregarComboAnosPreenchidos")
	public ResponseEntity<List<Short>> carregarComboAnosPreenchidos(){
		return ResponseEntity.ok().body(variavelPreenchidaService.carregarComboAnosPreenchidos());
	}

	@GetMapping("/buscarCidadesComVariavelPreenchida")
	public ResponseEntity<List<VariavelPreenchidaMapaDTO>> buscarCidadesComVariavelPreenchida(@RequestParam ("idVariavel") Long idVariavelSelecionada, @RequestParam ("valorPreenchido") String valorPreenchido,
			@RequestParam ("anoSelecionado") Short anoSelecionado, @RequestParam ("visualizarComoPontos") boolean visualizarComoPontos) {
		return ResponseEntity.ok().body(variavelPreenchidaService.buscarCidadesComVariavelPreenchida(idVariavelSelecionada,valorPreenchido, anoSelecionado, visualizarComoPontos));

	}
	
	@GetMapping("/buscarObservacaoVariavel")
	public ResponseEntity<List<ObservacaoVariavelDTO>> buscarObservacaoVariavel(@RequestParam ("idIndicador") Long idIndicador, @RequestParam ("idCidade") Long idCidade) {
		return ResponseEntity.ok().body(variavelPreenchidaService.buscarObservacaoVariavel(idIndicador, idCidade));

	}

	@GetMapping("/listarPorIndicadorAndCidade")
	public ResponseEntity<List<VariavelPreenchidaSimplesDTO>> listaPorIndicadorAndCidade(@RequestParam ("idIndicador") Long idIndicador, @RequestParam ("idCidade") Long idCidade){
		List<VariavelPreenchida> lista = variavelPreenchidaService.listarPorIndicadorAndCidade(idIndicador, idCidade);
		List<VariavelPreenchidaSimplesDTO> listaDTO = new ArrayList<>();
		for (VariavelPreenchida vp : lista) {
			listaDTO.add(new VariavelPreenchidaSimplesDTO(vp));
		}
		return ResponseEntity.ok().body(listaDTO);
	}
	
	@GetMapping("/listarPorIndicadorCidadeSubdivisao")
	public ResponseEntity<List<VariavelPreenchidaSimplesDTO>> listaPorIndicadorCidadeSubdivisao(@RequestParam ("idIndicador") Long idIndicador,@RequestParam ("idCidade") Long idCidade, @RequestParam ("idSubdivisao") Long idSubdivisao){
		List<SubdivisaoVariavelPreenchida> lista = variavelPreenchidaService.buscarPorIndicadorCidadeSubdivisao(idIndicador, idCidade, idSubdivisao);
		List<VariavelPreenchidaSimplesDTO> listaDTO = new ArrayList<>();
		for (SubdivisaoVariavelPreenchida vp : lista) {
			listaDTO.add(new VariavelPreenchidaSimplesDTO(vp));
		}
		return ResponseEntity.ok().body(listaDTO);
	}
	
	@GetMapping("/recalcularIDH")
	public void recalcularIDH() {
		variavelPreenchidaService.recalcularIDH();
	}
	
	@GetMapping("/carregarComboAnosPreenchidosPorVariavel")
	public ResponseEntity<List<Short>> carregarComboAnosPreenchidosPorVariavel(@RequestParam ("idVariavel") Long idVariavel){
		return ResponseEntity.ok().body(variavelPreenchidaService.carregarComboAnosPreenchidosPorIdVariavel(idVariavel));
	}


	@GetMapping()
	public List<VariavelJaPreenchidaSimplesDTO> variaveisParaPreencher() throws Exception {
		List<VariavelJaPreenchidaSimplesDTO> variaveis = new ArrayList<>();
		variaveis = this.variavelPreenchidaService.variaveisParaPreencher();
		return variaveis;
	}

	@GetMapping("/{id}")
	public List<VariavelJaPreenchidaSimplesDTO> variaveisParaPreencherSubdivisao(@PathVariable Long id) throws Exception {
		List<VariavelJaPreenchidaSimplesDTO> variaveis = new ArrayList<>();
		variaveis = this.variavelPreenchidaService.variaveisParaPreencher(id);
		return variaveis;
	}

	@PostMapping()
	public VariavelPreenchidaDTO preencherVariavel(@RequestBody VariavelPreenchidaDTO dto) throws Exception {
		if(dto.getSubdivisao() != null){
			SubdivisaoVariavelPreenchida vp = this.variavelPreenchidaService.preencherComSubdivisao(dto);
			this.indicadorPreenchidoService.recalcularIndicadoresPorVariavelCidadeAnoSubdivisao(vp.getVariavel(), vp.getPrefeitura().getCidade(), vp.getAno(),dto.getSubdivisao());
		} else {
			VariavelPreenchida vp = this.variavelPreenchidaService.preencher(dto);
			this.indicadorPreenchidoService.recalcularIndicadoresPorVariavelCidadeAno(vp.getVariavel(), vp.getPrefeitura().getCidade(), vp.getAno());
		}
		return dto;
	}

	@GetMapping("/serie/{id}")
	public List<VariavelPreenchidaSimplesDTO> variaveisParaPreenchingseriePreenchida(@PathVariable Long id) throws Exception {
		List<VariavelPreenchidaSimplesDTO> variaveis = new ArrayList<>();
		variaveis = this.variavelPreenchidaService.seriePreenchida(id);
		return variaveis;
	}
	@GetMapping("/serie/{id}/{subdivisao}")
	public List<VariavelPreenchidaSimplesDTO> variaveisParaPreenchingSeriePreenchidaSubdivisao(@PathVariable Long id, @PathVariable Long subdivisao) throws Exception {
		List<VariavelPreenchidaSimplesDTO> variaveis = new ArrayList<>();
		variaveis = this.variavelPreenchidaService.seriePreenchidaSubdivisao(id, subdivisao);
		return variaveis;
	}
}
