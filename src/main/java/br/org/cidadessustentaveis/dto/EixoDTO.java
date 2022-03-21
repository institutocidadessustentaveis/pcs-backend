package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EixoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo icone não pode ser nulo")
	private String icone;
	
	@NotNull(message="Campo nome do eixo não pode ser nulo")
	private String nome;
	
	private String link;
	
	private Set<ObjetivoDesenvolvimentoSustentavel> listaODS;

	public EixoDTO(Eixo eixo) {
		super();
		this.id = eixo.getId();
		this.icone = eixo.getIcone();
		this.nome = eixo.getNome();
		this.listaODS = eixo.getListaODS();
		this.link = eixo.getLink();
	}
	
	public Eixo toEntityInsert() {
		return new Eixo(null,this.icone, this.nome, this.link, this.listaODS);
	}
	
	public Eixo toEntityUpdate(Eixo objRef) {
		objRef.setIcone(icone);
		objRef.setNome(nome);
		objRef.setLink(link);
		objRef.setListaODS(listaODS);
		return objRef;
	}

//	Usado na query buscarEixosDto
	public EixoDTO(Long id, String nome, String link) {
		this.id = id;
		this.nome = nome;
		this.link = link;
	}
}
