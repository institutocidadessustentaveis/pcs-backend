package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GrupoAcademicoCardDTO {

	 private Long id;
	 
	 private String nomeGrupo;
	 
	 public GrupoAcademicoCardDTO(Long id, String nomeGrupo) {
		 this.id = id;
		 this.nomeGrupo = nomeGrupo;
	 }
	 
}
