package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PaisDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "Pais é obrigtório")
	private String nome;


	private String continente;

	private String populacao;
	
	private List<ProvinciaEstado> estados;
		

	public PaisDTO(Pais obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.continente = obj.getContinente();
		this.populacao = obj.getPopulacao();
		this.estados = obj.getEstados();
	}
	
	public Pais toEntityInsert() {
		return new Pais(null, this.getNome(), this.getContinente(), this.getPopulacao(), new ArrayList<ProvinciaEstado>());
	}
	
	public Pais toEntityUpdate(Pais userRef) {
		userRef.setNome(this.nome);
		userRef.setContinente(this.continente);
		userRef.setPopulacao(this.populacao);
		userRef.setEstados(this.estados);
		return userRef;
	}
	
	
	public PaisDTO(BoaPratica boaPratica) {
		if(boaPratica.getEstado() != null && boaPratica.getEstado().getPais() != null) {
			this.id = boaPratica.getEstado().getPais().getId();
			this.nome = boaPratica.getEstado().getPais().getNome();
		}
	}
	
}
