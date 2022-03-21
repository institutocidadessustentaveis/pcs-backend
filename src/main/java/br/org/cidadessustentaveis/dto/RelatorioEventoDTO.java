package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RelatorioEventoDTO implements Serializable {

	private static final long serialVersionUID = 4111739858205492640L;

	private Long id;

	private String titulo;

	private String data;

	private Integer npessoasSeguiram;
	
	private Integer npessoasCadastradas;

	private Integer npessoasAdicionaram;

	private Integer npessoasVisualizaram;
}
