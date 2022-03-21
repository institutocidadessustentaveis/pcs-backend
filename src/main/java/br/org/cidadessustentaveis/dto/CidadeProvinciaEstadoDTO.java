package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class CidadeProvinciaEstadoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
		
	private String nome;
	
	private String sigla;
	
	private ProvinciaEstadoPaisDTO pais;
	
	public CidadeProvinciaEstadoDTO (ProvinciaEstado provinciaEstado) {
	  this.id = provinciaEstado.getId();
	  this.nome = provinciaEstado.getNome();
	  this.sigla = provinciaEstado.getSigla();

	  if(provinciaEstado.getPais() != null) {
		  this.pais = new ProvinciaEstadoPaisDTO(provinciaEstado.getPais());
	  }
	}
	
}
