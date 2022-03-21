package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter @Setter @AllArgsConstructor
public class VariavelJaPreenchidaSimplesDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String nome;
		
	private Boolean preenchida = false;

	public VariavelJaPreenchidaSimplesDTO(Long id, String nome){
		this.id = id;
		this.nome = nome;
	}
}
