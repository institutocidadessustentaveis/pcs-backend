package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CidadeQtIndicadorPreenchidoDTO {
	private String cidade;
	private String provinciaEstado;
	private Long qtdIndicadoresPreenchidos;
	private Double latitude;
	private Double longitude;
	private Long idCidade;

	public CidadeQtIndicadorPreenchidoDTO(String cidade, String provinciaEstado,
										  Double latitude, Double longitude, Long idCidade) {
		this.cidade = cidade;
		this.provinciaEstado = provinciaEstado;
		this.qtdIndicadoresPreenchidos = 0l;
		this.latitude = latitude;
		this.longitude = longitude;
		this.idCidade = idCidade;
	}

}
