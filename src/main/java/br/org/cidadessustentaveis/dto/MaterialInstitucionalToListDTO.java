package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Date;

import br.org.cidadessustentaveis.model.institucional.MaterialInstitucional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialInstitucionalToListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String titulo;
	private Date dtPublicacao;
	
	public MaterialInstitucionalToListDTO(MaterialInstitucional materialInstitucionalRef) {
		this.id = materialInstitucionalRef.getId();
		this.titulo = materialInstitucionalRef.getTitulo();
		this.dtPublicacao = materialInstitucionalRef.getDtPublicacao();
	}	
}