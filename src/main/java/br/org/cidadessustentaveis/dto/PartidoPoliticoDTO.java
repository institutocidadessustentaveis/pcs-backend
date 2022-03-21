package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.PartidoPolitico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PartidoPoliticoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	@NotNull(message = "Obrigatório preenchimento do número.")
	private String nome;
	@NotNull(message = "Obrigatório preenchimento da sigla do Partido.")
	private String siglaPartido;
	
	@NotNull(message = "Obrigatório preenchimento do número da legenda do Partido.")
	private int numeroLegenda;
	
	public PartidoPoliticoDTO(PartidoPolitico partido) {
		this.id = partido.getId();
		this.nome = partido.getNome();
		this.siglaPartido = partido.getSiglaPartido();
		this.numeroLegenda = partido.getNumeroLegenda();
	}
	
	public PartidoPolitico toEntityInsert() {
	  return new PartidoPolitico(null, this.nome, this.siglaPartido, this.numeroLegenda);
	}
	
	public PartidoPolitico toEntityUpdate(PartidoPolitico partido) {
	  partido.setNome(this.nome);
	  partido.setSiglaPartido(this.siglaPartido);
	  partido.setNumeroLegenda(this.numeroLegenda);
	  return partido;
	}

}
