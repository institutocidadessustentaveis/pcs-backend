package br.org.cidadessustentaveis.dto;

import java.util.List;

import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class IndicadorComparativoDeCidadeDTO {
	private String indicador;
	private String descricao;
	private String formula;
	private List<InstituicaoFonte> fontes;
	private String ods;
	private Integer odsNumero;
	private String meta;
	private Boolean numerico;
	private List<SerieHistoricaDTO> serieHistorica;
	private List<String> labels;
	private List<LineChartDataDTO> chartData;
	private List<TreeMapChartDTO> treeMap;
	private List<ComparativoGraficoDTO> graficos;
	private List<String> labelsDispersaoAnimada;
	private List<String> cidadesDispersaoAnimada;
	private List<DadosDispersaoAnimadaDTO> dispersao; 
	
}

