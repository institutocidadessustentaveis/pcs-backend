package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EixoListagemDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nome;
	
	private Boolean possuiImagem;

	public EixoListagemDTO(Long id, String nome, String possuiImagem) {
		super();
		this.id = id;
		this.nome = nome;
		this.possuiImagem = possuiImagem != null ? true : false;
	}
	
}
