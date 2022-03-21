package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
public class MenuPaginaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String label;
	
	private String modulo;
	
}              
