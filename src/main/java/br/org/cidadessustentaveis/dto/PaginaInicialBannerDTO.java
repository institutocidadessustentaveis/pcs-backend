package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @Getter @Setter @AllArgsConstructor
public class PaginaInicialBannerDTO implements Serializable {

	private static final long serialVersionUID = 1L;	

	private Long idNoticia = null;
	private Long idBoaPratica = null;
	private Long idInstitucional = null;
	
	private String titulo = null;
	private String subtitulo = null;
	private String linkInstitucional = null;

}

