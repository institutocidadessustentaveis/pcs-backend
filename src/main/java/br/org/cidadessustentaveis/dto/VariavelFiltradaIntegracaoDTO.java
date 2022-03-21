package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VariavelFiltradaIntegracaoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nome;
	
	private String descricao;
	
	private String tipo;
	
	private String unidade;
	
	private String nomeCidade;

}
