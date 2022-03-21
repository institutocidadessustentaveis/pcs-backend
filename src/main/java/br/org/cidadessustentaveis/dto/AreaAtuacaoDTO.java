package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.AreaAtuacao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AreaAtuacaoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nome;
	
	
	public AreaAtuacaoDTO(AreaAtuacao areaAtuacao) {
		this.id = areaAtuacao.getId();
		this.nome = areaAtuacao.getNome();
	}
	
}
