package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.institucional.Institucional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InstitucionalVisualizacaoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

	private String link_pagina;

	private String titulo;

	private String subtitulo;

	public InstitucionalVisualizacaoDTO(Institucional obj) {
		this.id = obj.getId();
		this.link_pagina = obj.getLink_pagina();
		this.titulo = obj.getTitulo();
		this.subtitulo = obj.getSubtitulo();
	}

}
