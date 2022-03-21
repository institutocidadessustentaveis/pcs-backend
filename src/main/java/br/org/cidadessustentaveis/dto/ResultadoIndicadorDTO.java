package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResultadoIndicadorDTO {

	private String nome;
	
	private String cor;
	
	private String resultado;
	
	private String label;
	
	private List<ResultadoVariavelDTO> resultadoVariaveis;
	
}
