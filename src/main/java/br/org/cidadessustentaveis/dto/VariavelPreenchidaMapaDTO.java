package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import org.wololo.geojson.Feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class VariavelPreenchidaMapaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCidade;
	private String nomeCidade;
	private String nomeVariavel;
	private Double latitude;
	private Double longitude;
	private Double valorNumericoPreenchido;
	private String valorTextoPreenchido;
	private List<Feature> shapeZoneamento;
	private boolean visualizarComoPontos = true;

	public VariavelPreenchidaMapaDTO(Long idCidade, String nomeCidade, String nomeVariavel, Double latitude,
			Double longitude, Double valorNumericoPreenchido, String valorTextoPreenchido) {
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
		this.nomeVariavel = nomeVariavel;
		this.latitude = latitude;
		this.longitude = longitude;
		this.valorNumericoPreenchido = valorNumericoPreenchido;
		this.valorTextoPreenchido = valorTextoPreenchido != null ? valorTextoPreenchido : valorNumericoPreenchido.toString();
	}
	
}