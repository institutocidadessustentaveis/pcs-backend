package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.IndicadorDaCidadeDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;

@Service
public class IndicadorDaCidadeService {

	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private IndicadorService indicadorService;
	@Autowired
	private CalculadoraFormulaUtil calculadoraFormulaUtil;
	@Autowired
	private PrefeituraService prefeituraService;
	@Autowired
	private VariavelPreenchidaService variavelPreenchidaService;

	public IndicadorDaCidadeDTO buscarIndicadorDaCidade(Long idIndicador, String sigla, String nomeCidade) {
		
		if(!nomeCidade.equals("")) {
			nomeCidade = nomeCidade.replace("-", " ");
		}
	
		Cidade cidade = cidadeService.findByNomeAndProvinciaEstadoSigla(nomeCidade, sigla);		
		Prefeitura prefeitura = prefeituraService.buscarAtualPorCidade(cidade.getId());
		Indicador indicador  = indicadorService.listarById(idIndicador);
		List<String> fontes = variavelPreenchidaService.buscarFontesPreenchidas(idIndicador,cidade.getId());
		LocalDate anoInicioMandato = prefeitura.getInicioMandato();
		LocalDate anoFimMandato = prefeitura.getFimMandato();
	
		Short AnoInicioMandatoShort = (short)anoInicioMandato.getYear();
		Short AnoFimMandatoShort = (short)anoFimMandato.getYear();
		
		calculadoraFormulaUtil.formatarFormula(indicador);
		String meta = indicador.getMetaODS() != null ? indicador.getMetaODS().getNumero()+" - "+indicador.getMetaODS().getDescricao() : "Meta não vinculada";
		Long idODS = indicador.getOds() != null ? indicador.getOds().getId() : null;
		String odsTitulo = indicador.getOds() != null ? indicador.getOds().getTitulo() : "Ods não vinculado";
		Integer numeroODS = indicador.getOds() != null ? indicador.getOds().getNumero() : null;
		List<List<String>> serieHistorica = variavelPreenchidaService.serieHistorica(
				indicador.getId(),
				cidade.getId(),
				AnoInicioMandatoShort,
				AnoFimMandatoShort
				);
		
		IndicadorDaCidadeDTO dto = IndicadorDaCidadeDTO.builder()
				.indicador(indicador.getNome())
				.descricao(indicador.getDescricao())
				.formula(indicador.getFormulaResultado())
				.fontes(fontes)
				.meta(meta)
				.odsId(idODS)
				.serieHistorica(serieHistorica)
				.ods(odsTitulo)
				.numeroODS(numeroODS)
				.idCidade(cidade.getId()+"")
				.idIndicador(indicador.getId())
				.nomeCidade(cidade.getNome())
				.build();	

		return dto;
	}
	
	public List<String> gerarValoresSerieHistoricaAno(List<String> cabecalho, IndicadorPreenchido ip){
		List<String> listaValores = new ArrayList<>();
		String[] linha = new String[cabecalho.size()];
		linha[0] = ip.getAno()+"";
		for(VariavelPreenchida vp : ip.getVariaveisPreenchidas()) {
			int indice = cabecalho.indexOf(vp.getVariavel().getNome());
			linha[indice] = vp.getValor()+"";
		}
		linha[cabecalho.size()-1] =  ip.getResultadoReferencia() != null ? ip.getResultadoReferencia()+"" : ip.getResultado();
		listaValores = Arrays.asList(linha);
		return listaValores;
	}

}
