package br.org.cidadessustentaveis.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter @NoArgsConstructor
public class FiltroGruposAcademicosDTO {
	
	private Long id;
	private Double latitude;
	private Double longitude;
	private String nomeGrupo;

	
	public FiltroGruposAcademicosDTO(Long id, Double latitude, Double longitude, String nomeGrupo) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.nomeGrupo = nomeGrupo;
	}
	
}
