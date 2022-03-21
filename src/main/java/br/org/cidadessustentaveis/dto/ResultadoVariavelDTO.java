package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResultadoVariavelDTO {

	private String nome;
	
	private String cor;
	
	private String label;

}
