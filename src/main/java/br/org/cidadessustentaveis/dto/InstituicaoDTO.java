package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.Instituicao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class InstituicaoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nome;
	
	private String tipo;
	
	public InstituicaoDTO(Instituicao instituicao) {
		this.id = instituicao.getId();
		this.nome = instituicao.getNome();
		this.tipo = instituicao.getTipo();
	}
	
}
