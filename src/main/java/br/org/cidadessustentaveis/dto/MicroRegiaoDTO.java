package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class MicroRegiaoDTO {
	
	private Long id;
	private String nome;
	private MesorregiaoDTO mesorregiao;
	

}
