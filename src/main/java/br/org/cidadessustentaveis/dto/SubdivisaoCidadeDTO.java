package br.org.cidadessustentaveis.dto;

import lombok.Data;

@Data
public class SubdivisaoCidadeDTO {
    private Long id;	
    private Long cidade;
    private String nome;
    private Long subdivisaoPai;
    private Long tipoSubdivisao;
}
	