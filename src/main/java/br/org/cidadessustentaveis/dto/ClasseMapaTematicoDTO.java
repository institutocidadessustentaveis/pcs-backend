package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClasseMapaTematicoDTO implements Serializable{

private static final long serialVersionUID = 1L;

	private String color;
	private String descricao;
	private Long maximo;
	private Long minimo;	
	private Long number;
	private String value;

}
