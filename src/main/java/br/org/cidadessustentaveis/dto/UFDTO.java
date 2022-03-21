package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UFDTO {
	
	private Long id;
	private String sigla;
	private String nome;
	private RegiaoDTO regiao;

}
