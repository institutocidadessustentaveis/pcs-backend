package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Funcionalidade;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class FuncionalidadeDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Long id;
	
	@NotNull
	private String nome;

	public FuncionalidadeDTO(Funcionalidade funcionalidade) {
		super();
		this.id = funcionalidade.getId();
		this.nome = funcionalidade.getNome();
	}

	public Funcionalidade toEntityInsert() {
		return new Funcionalidade(this.id, this.nome, null);
	}

}
