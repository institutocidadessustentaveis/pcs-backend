package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioContagemBoasPraticasDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String contagem;

	public RelatorioContagemBoasPraticasDTO(Long id, String nome, Long contagem) {
		super();
		this.id = id;
		this.nome = nome;
		this.contagem = contagem.toString();
	}
	
	
}
