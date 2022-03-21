package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;

import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PaisBuscaDTO {

	private Long id;

	private String nome;

	private String continente;

	private String populacao;

	public PaisBuscaDTO(Pais obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.continente = obj.getContinente();
		this.populacao = obj.getPopulacao();
	}
	
	public Pais toEntityInsert() {
		return new Pais(null, this.getNome(), this.getContinente(), this.getPopulacao(), new ArrayList<ProvinciaEstado>());
	}
	
	public Pais toEntityUpdate(Pais userRef) {
		userRef.setNome(this.nome);
		userRef.setContinente(this.continente);
		userRef.setPopulacao(this.populacao);
		return userRef;
	}
}
