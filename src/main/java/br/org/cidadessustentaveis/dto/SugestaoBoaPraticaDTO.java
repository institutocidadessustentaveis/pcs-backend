package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.boaspraticas.SugestaoBoasPraticas;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SugestaoBoaPraticaDTO {

	private Long id;

	private String titulo;

	private String descricao;

	private String nomeUsuario;

	public SugestaoBoaPraticaDTO(SugestaoBoasPraticas obj) {
		this.id = obj.getId();
		this.titulo = obj.getTitulo();
		this.descricao = obj.getDescricao();
		this.nomeUsuario = obj.getUsuario().getNome();
	}
	
	public SugestaoBoasPraticas toEntityInsert() {
		return new SugestaoBoasPraticas(null, this.getTitulo(), this.getDescricao(), this.getNomeUsuario());
	}
	
	public SugestaoBoasPraticas toEntityUpdate(SugestaoBoasPraticas userRef) {
		userRef.setId(this.id);
		userRef.setTitulo(this.titulo);
		userRef.setDescricao(this.descricao);
		userRef.getUsuario().setNome(this.nomeUsuario);
		return userRef;
	}
}
