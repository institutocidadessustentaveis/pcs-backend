package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ComparativoDeIndicadoresMesmaCidadeDTO {
	private String nomeCidade;
	/*private String descricao;
	private String formula;
	private String fonte;
	private String ods;
	private String meta;
	private Boolean numerico;
	*/
	private List<SerieHistoricaDTO> serieHistorica;
	private List<String> labels;
	private List<ChartComparaIndicadorDTO> chartData;
	private List<TreeMapChartDTO> treeMap;
	
}

