package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;

@Data
public class LineChartDataDTO {
	private String label;
	private List<Double> valor;
	
	public LineChartDataDTO() {

	}
	
	public LineChartDataDTO(String label, List<Double> valor) {
		super();
		this.label = label;
		this.valor = valor;
	}
	
	
}
