package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.LinkedList;

import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProvinciaEstadoBuscaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id; //TEM DOIS IDS PARA NAO CONFLITAR COM DTO DO FRONT. OBJETIVO PARA NAO MUDAR TODO O FRONT
	
	private Long idProvinciaEstado;

	private String nome;

	private String sigla;

	private String populacao;

	public ProvinciaEstadoBuscaDTO(ProvinciaEstado obj) {
		this.idProvinciaEstado = obj.getId();
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.sigla = obj.getSigla();
		this.populacao = obj.getPopulacao();
	}

	public ProvinciaEstado toEntityInsert() {
		return new ProvinciaEstado (null, this.getNome(), this.getSigla(), 
									new Pais(), this.getPopulacao(), new LinkedList<>());
	}

	public ProvinciaEstado toEntityUpdate(ProvinciaEstado userRef) {
		userRef.setNome(this.nome);
		userRef.setPopulacao(this.populacao);
		return userRef;
	}

}
