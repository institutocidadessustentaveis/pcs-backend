package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.AvaliacaoVariavelDTO;
import br.org.cidadessustentaveis.dto.AvaliacaoVariavelPreenchidaDTO;
import br.org.cidadessustentaveis.dto.AvaliacaoVariavelPreenchidaDetalhesDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaDTO;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.services.IndicadorPreenchidoService;
import br.org.cidadessustentaveis.services.PrefeituraService;
import br.org.cidadessustentaveis.services.VariavelPreenchidaService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/avaliacaoVariavel")
public class AvaliacaoVariavelResource {

	@Autowired
	private IndicadorPreenchidoService service;
	@Autowired
	private PrefeituraService prefeituraService;
	@Autowired
	private VariavelPreenchidaService variavelPreenchidaService;
	
	@Secured({ "ROLE_AVALIACAO_VARIAVEIS" })
	@GetMapping()
	public ResponseEntity<List<AvaliacaoVariavelDTO>> listar() {
		List<AvaliacaoVariavelDTO> listaAvaliacaoVariavelCidade = new ArrayList<>();
		List<AvaliacaoVariavelDTO> listaAvaliacaoVariavelCidadeData = new ArrayList<>();
		List<AvaliacaoVariavelDTO> lista = new ArrayList<>();
		try {

			listaAvaliacaoVariavelCidade = service.findByAvaliacaoVariavelPorCidade();
			listaAvaliacaoVariavelCidadeData = service.findByAvaliacaoVariavelPorCidadeData();
			for (AvaliacaoVariavelDTO itemAvaliacaoVariavel : listaAvaliacaoVariavelCidade) {
				AvaliacaoVariavelDTO avaliacaoVariavel = new AvaliacaoVariavelDTO();
				avaliacaoVariavel.setId(itemAvaliacaoVariavel.getId());
				avaliacaoVariavel.setCidade(itemAvaliacaoVariavel.getCidade());
				avaliacaoVariavel.setQtdVariaveis(itemAvaliacaoVariavel.getQtdVariaveis());
				
				AvaliacaoVariavelDTO item= listaAvaliacaoVariavelCidadeData.stream().filter(x -> x.getId() == itemAvaliacaoVariavel.getId()).findFirst().orElse(null);
				if(item != null) {
					avaliacaoVariavel.setDataPreenchimento(item.getDataPreenchimento());
					avaliacaoVariavel.setDataAvaliacao(item.getDataAvaliacao());
				}
				avaliacaoVariavel
						.setStatus(itemAvaliacaoVariavel.getQtdVariaveis() > 0 ? "Aguardando Avaliação" : "Avaliado");
				lista.add(avaliacaoVariavel);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Mensagem de erro" + ex);
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_AVALIACAO_VARIAVEIS" })
	@GetMapping("/prefeitura/{id}")
	public ResponseEntity<List<AvaliacaoVariavelPreenchidaDTO>> buscarPreenchidoPorPrefeitura(@PathVariable Long id) {
		List<AvaliacaoVariavelPreenchidaDTO> listaAvaliacaoVariavelPreenchidaDTO = new ArrayList<AvaliacaoVariavelPreenchidaDTO>();
		List<AvaliacaoVariavelPreenchidaDetalhesDTO> listaAvaliacaoVariavelPreenchidaDetalhesDTO = new ArrayList<AvaliacaoVariavelPreenchidaDetalhesDTO>();
		
		Prefeitura prefeitura = prefeituraService.buscarPorId(id);
		List<IndicadorPreenchido> listaIndicadorPreenchido = service.buscarPorPrefeitura(prefeitura);

		for (IndicadorPreenchido itemIndicadorPreenchido : listaIndicadorPreenchido) {
			boolean possuiVariaveisParaAvaliar = false;
			AvaliacaoVariavelPreenchidaDTO avaliacaoVariavelPreenchidaDTO = new AvaliacaoVariavelPreenchidaDTO();
			avaliacaoVariavelPreenchidaDTO.setId(itemIndicadorPreenchido.getId());
			avaliacaoVariavelPreenchidaDTO.setPrefeitura(prefeitura.getCidade().getNome());
			avaliacaoVariavelPreenchidaDTO.setPrefeito(prefeitura.getNome());
			avaliacaoVariavelPreenchidaDTO.setNomeIndicador(itemIndicadorPreenchido.getIndicador().getNome());
			avaliacaoVariavelPreenchidaDTO.setAnoIndicador(itemIndicadorPreenchido.getAno().toString());

			for (Variavel itemVariavel : itemIndicadorPreenchido.getIndicador().getVariaveis()) {
				if (itemVariavel.getTipo().trim().toUpperCase().equals("TEXTO LIVRE")) {
					AvaliacaoVariavelPreenchidaDetalhesDTO avaliacaoVariavelPreenchidaDetalhesDTO = new AvaliacaoVariavelPreenchidaDetalhesDTO();
					avaliacaoVariavelPreenchidaDetalhesDTO.setIdIndicadorPreenchido(itemIndicadorPreenchido.getId());
					avaliacaoVariavelPreenchidaDetalhesDTO.setIdVariavel(itemVariavel.getId());
					avaliacaoVariavelPreenchidaDetalhesDTO.setNomeVariavel(itemVariavel.getNome());
					avaliacaoVariavelPreenchidaDetalhesDTO.setDescricaoVariavel(itemVariavel.getDescricao());
					if(itemIndicadorPreenchido.getVariaveisPreenchidas().stream().filter(x -> x.getVariavel().getId() == itemVariavel.getId() && x.getStatus().equals("Aguardando Avaliação")).findFirst().isPresent()) {
						avaliacaoVariavelPreenchidaDetalhesDTO.setIdVariavelPreenchida(itemIndicadorPreenchido.getVariaveisPreenchidas().stream().filter(x -> x.getVariavel().getId() == itemVariavel.getId()).findFirst().get().getId());
					}else {
						break;
					}
					avaliacaoVariavelPreenchidaDetalhesDTO.setValorPrefeitura((itemIndicadorPreenchido.getVariaveisPreenchidas().stream().filter(x -> x.getVariavel().getId() == itemVariavel.getId()).findFirst().get().getValorTexto()));
					avaliacaoVariavelPreenchidaDetalhesDTO.setReferencia(itemVariavel.getVariavelReferencia());
					listaAvaliacaoVariavelPreenchidaDetalhesDTO.add(avaliacaoVariavelPreenchidaDetalhesDTO);
					possuiVariaveisParaAvaliar = true;
				}
			}

			avaliacaoVariavelPreenchidaDTO.setAvaliacaoVariavelPreenchidaDetalhesDTO(listaAvaliacaoVariavelPreenchidaDetalhesDTO);
			listaAvaliacaoVariavelPreenchidaDetalhesDTO = new ArrayList<>();
			if(possuiVariaveisParaAvaliar) {
				listaAvaliacaoVariavelPreenchidaDTO.add(avaliacaoVariavelPreenchidaDTO);
			}
		}

		return ResponseEntity.ok().body(listaAvaliacaoVariavelPreenchidaDTO);
	}
	
	@Secured({ "ROLE_AVALIACAO_VARIAVEIS" })
	@PutMapping("/editar/{id}")
	public ResponseEntity<Void> editar(@Valid @RequestBody VariavelPreenchidaDTO variavelPreenchidaDTO, @PathVariable("id") Long id) {
		variavelPreenchidaService.editar(variavelPreenchidaDTO, id);
		return ResponseEntity.noContent().build();
	}
}
