package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariavelIndicadorDuplicadaDTO {
	private Long idIndicador;
	private Long idVariavel;
	private Long count;
	

}