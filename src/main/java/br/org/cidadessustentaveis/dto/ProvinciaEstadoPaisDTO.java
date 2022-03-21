package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.Pais;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProvinciaEstadoPaisDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nome;

	private String continente;

	private String populacao;
	
	public ProvinciaEstadoPaisDTO(final Pais pais) {
	  this.id = pais.getId();
	  this.nome = pais.getNome();
	  this.continente = pais.getContinente();
	  this.populacao = pais.getPopulacao();
	}

}
