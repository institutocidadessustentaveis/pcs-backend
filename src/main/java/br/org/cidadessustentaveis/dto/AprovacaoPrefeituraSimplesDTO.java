package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AprovacaoPrefeituraSimplesDTO {

	private Long id;
	private LocalDate inicioMandato;
	private LocalDate fimMandato;
}
