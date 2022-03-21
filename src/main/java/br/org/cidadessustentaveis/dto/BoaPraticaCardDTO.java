package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BoaPraticaCardDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String titulo;
	private String subtitulo;
	
	public BoaPraticaCardDTO(BoaPratica objRef) {
		this.id = objRef.getId();
		this.titulo = objRef.getTitulo();
		this.subtitulo = objRef.getSubtitulo();
	}	
}