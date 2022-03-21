package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FormularioResumidoDTO {
	private Long id;
	private String nome;
	private String descricao;
	private String link;
	
	public FormularioResumidoDTO(Long id, String nome, String descricao, String link) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.link = link;	
	}

}
