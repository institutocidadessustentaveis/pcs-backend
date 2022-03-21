package br.org.cidadessustentaveis.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor
public class FiltroMaterialApoioDTO {

	private Long idMaterialApoio;

	public FiltroMaterialApoioDTO(Long idMaterialApoio) {
		this.idMaterialApoio = idMaterialApoio;
	}
	
	
	
}
