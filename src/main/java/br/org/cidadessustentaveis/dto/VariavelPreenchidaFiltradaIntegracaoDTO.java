package br.org.cidadessustentaveis.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VariavelPreenchidaFiltradaIntegracaoDTO {

	private Long id;

	private Long idVariavel;
	
	private Short ano;
	
	private Double valor;
	
	private String nomeVariavel;

	private String observacao;

	private String instituicaoFonte;

	private String nomeCidade;
}
