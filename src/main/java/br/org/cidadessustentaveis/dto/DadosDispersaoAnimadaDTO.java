package br.org.cidadessustentaveis.dto;

import lombok.Data;

@Data
public class DadosDispersaoAnimadaDTO {
	private int ano;
	private String cidade;
	private Long idCidade;
	private Long populacaocidade;
	private Double valor;
	
	public DadosDispersaoAnimadaDTO(int ano, String cidade, Double valor, Long idCidade, Long populacaoCidade) {
		super();
		this.ano = ano;
		this.cidade = cidade;
		this.idCidade = idCidade;
		this.valor = valor;
		this.populacaocidade = populacaoCidade;
	}
	
}
