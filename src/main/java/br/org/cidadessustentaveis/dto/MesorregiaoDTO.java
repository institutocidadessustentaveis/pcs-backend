package br.org.cidadessustentaveis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class MesorregiaoDTO {
	
	private Long id;
	private String nome;
	@JsonAlias("UF")
	private UFDTO uf;

}
