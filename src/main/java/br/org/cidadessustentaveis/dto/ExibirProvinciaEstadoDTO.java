package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ExibirProvinciaEstadoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
		
	private String nome;
	
	private String sigla;
	
	private List<Cidade> cidades;
	
	private String populacao;
	
	private ProvinciaEstadoPaisDTO pais;
	
	private String nomePais;
	
	public ExibirProvinciaEstadoDTO (ProvinciaEstado provinciaEstado) {
	  this.id = provinciaEstado.getId();
	  this.nome = provinciaEstado.getNome();
	  this.sigla = provinciaEstado.getSigla();
	  this.populacao = provinciaEstado.getPopulacao();
	  this.pais = new ProvinciaEstadoPaisDTO(provinciaEstado.getPais());
	  if(provinciaEstado.getPais() !=  null) {
		  this.nomePais = provinciaEstado.getPais().getNome();
	  }
	}
	
	
}
