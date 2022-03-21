package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProvinciaEstadoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "Provincia/Estado é obrigatório")
	private String nome;
	
	private Pais pais;
	
	private String sigla;

	private String populacao;
	
	private List<Cidade> cidades;
	
	public ProvinciaEstadoDTO(ProvinciaEstado obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.pais = obj.getPais();
		this.populacao = obj.getPopulacao();
		this.cidades = obj.getCidades();
		this.sigla = obj.getSigla();
	}
	
	public ProvinciaEstado toEntityInsert() {
		if (this.getCidades() == null) {
		  this.cidades = new ArrayList<Cidade>();
		}
		return new ProvinciaEstado (null, this.getNome(), this.getSigla(), this.getPais(), this.getPopulacao(), this.getCidades());
	}
	
	public ProvinciaEstado toEntityUpdate(ProvinciaEstado userRef) {
		userRef.setNome(this.nome);
		userRef.setPais(this.pais);
		userRef.setPopulacao(this.populacao);
		userRef.setCidades(this.cidades);
		return userRef;
	}
	
	public ProvinciaEstadoDTO(BoaPratica boaPratica) {
		if(boaPratica.getEstado() != null) {
			this.id = boaPratica.getEstado().getId();
			this.nome = boaPratica.getEstado().getNome();
			this.sigla = boaPratica.getEstado().getSigla();
		}
	}
	
	public ProvinciaEstadoDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

}
