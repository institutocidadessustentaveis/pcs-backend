package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CidadeComBoasPraticasDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCidade;
	
	private String nomeCidade;
	
	private String estadoSigla;
	
	private Double latitude;

	private Double longitude;
	
	private Long countBoasPraticas;
	
	private List<Feature> shapeZoneamento;

	private boolean visualizarComoPontos = true;
	
	

	public CidadeComBoasPraticasDTO(Cidade cidade, Long countBoasPraticas) {
		super();
		this.idCidade = cidade.getId();
		this.nomeCidade = cidade.getNome();
		this.latitude = cidade.getLatitude();
		this.longitude = cidade.getLongitude();
		this.countBoasPraticas = countBoasPraticas;
	}
	
	public CidadeComBoasPraticasDTO(Long id, Long countBoasPraticas) {
		this.idCidade = id;
		this.countBoasPraticas = countBoasPraticas;
	}

	public CidadeComBoasPraticasDTO(Long idCidade, String nomeCidade, Double latitude, Double longitude,
			Long countBoasPraticas) {
		super();
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
		this.latitude = latitude;
		this.longitude = longitude;
		this.countBoasPraticas = countBoasPraticas;
	}
	
	public CidadeComBoasPraticasDTO(Long idCidade, String nomeCidade, String estadoSigla, Long countBoasPraticas) {
		super();
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
		this.estadoSigla = estadoSigla;
		this.countBoasPraticas = countBoasPraticas;
	}
}