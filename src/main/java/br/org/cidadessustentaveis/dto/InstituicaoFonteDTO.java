package br.org.cidadessustentaveis.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class InstituicaoFonteDTO {
	private Long id;
	private String nome;
	
	
	public InstituicaoFonteDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

}
