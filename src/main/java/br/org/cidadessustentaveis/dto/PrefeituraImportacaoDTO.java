package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PrefeituraImportacaoDTO {

	private String nome;
	private String email;
	private String telefone;
	private String celular;
	private String cargo;
	private String ibge;
	private String partido;
	private Boolean receberInfo;
	private Boolean signataria;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private String arquivo;
}