package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class VariavelPreenchidaDuplicadaDTO {
	private Long idVariavel;
	private Long idPrefeitura;
	private Short ano;
	private Long quantidade;
	
}
