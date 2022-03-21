package br.org.cidadessustentaveis.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VariacaoReferenciasDTO {
	private Double valorde;
	private Double valorate;
	private String Label;
	public VariacaoReferenciasDTO (Double valorde, Double valorate, String Label) {
		this.valorde = valorde;
		this.valorate = valorate;
		this.Label = Label;
	}
}
