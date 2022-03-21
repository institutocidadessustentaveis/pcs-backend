package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GrupoAcademicoComboDTO {

	 private Long id;
	 
	 private String nomeGrupo;
	 
	 public GrupoAcademicoComboDTO(Long id, String nomeGrupo) {
		 this.id = id;
		 this.nomeGrupo = nomeGrupo;
	 }
	 
}
