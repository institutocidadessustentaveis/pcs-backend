package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NavItemPaginaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idMenuPagina;
	private String modulo;
	private String nome;
	
}	