package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import org.wololo.geojson.Feature;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class IndicadorPreenchidoMapaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCidade;
	private String nomeCidade;
	private String nomeIndicador;
	private Double latitude;
	private Double longitude;
	private String valorPreenchido;
	private List<Feature> shapeZoneamento;
	private boolean visualizarComoPontos = true;

	public IndicadorPreenchidoMapaDTO(Long idCidade, String nomeCidade, String nomeIndicador, Double latitude,
			Double longitude, String valorPreenchido) {
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
		this.nomeIndicador = nomeIndicador;
		this.latitude = latitude;
		this.longitude = longitude;
		this.valorPreenchido = valorPreenchido;
	}
	
}