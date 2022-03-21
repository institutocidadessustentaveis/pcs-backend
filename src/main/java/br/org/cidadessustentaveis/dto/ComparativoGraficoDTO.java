package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComparativoGraficoDTO {

	private String mandato;
	private Integer inicioMandato;
	private Integer fimMandato;
	private List<String> labels;
	private List<LineChartDataDTO> valores;
	private List<TreeMapAnoDTO> treeMap;
	
}
