package br.org.cidadessustentaveis.dto;

import java.util.List;

import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class IndicadorDaCidadeDTO {
	private String indicador;
	private String descricao;
	private String formula;
	private List<String> fontes;
	private Long odsId;
	private String ods;
	private Integer numeroODS;
	private String meta;
	private List<List<String>> serieHistorica;
	private List<String> labels;
	private LineChartDataDTO chartData;
	private List<TreeMapChartDTO> treeMap;
	private String idCidade;
	private Long idIndicador;
	private String nomeCidade;
	
}

