package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Permissao;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissaoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo funcionalidade não pode ser nulo")
	private FuncionalidadeDTO funcionalidade;
	
	@NotNull(message="Campo leitura não pode ser nulo")
	private Boolean leitura;
	
	@NotNull(message="Campo escrita não pode ser nulo")
	private Boolean habilitada;

	public PermissaoDTO(Permissao permissao) {
		this.id = permissao.getId();
		this.funcionalidade = new FuncionalidadeDTO(permissao.getFuncionalidade());
		this.habilitada = permissao.getHabilitada();
	}
	
	public Permissao toEntityInsert() {
		return new Permissao(null, this.funcionalidade.toEntityInsert(), this.habilitada);	
	}
	
	public Permissao toEntityUpdate(Permissao permissaoRef) {
		permissaoRef.setHabilitada(this.habilitada);
		return permissaoRef;
	}
	
	
}
