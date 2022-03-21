package br.org.cidadessustentaveis.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor
public class FiltroBibliotecaPlanoLeisRegulamentacaoDTO {

	private Long idBiblioteca;
	private Double latitude;
	private Double longitude;
	private Long idCidade;
	private String nomeCidade;
	private String tituloPublicacao;
	private String descricao;
	private Long idEstado;
	private Long idPais;

	public FiltroBibliotecaPlanoLeisRegulamentacaoDTO(Long idBiblioteca, Double latitude, Double longitude, Long idCidade, 
			String nomeCidade, String tituloPublicacao, String descricao, Long idEstado, Long idPais) {
		this.idBiblioteca = idBiblioteca;
		this.latitude = latitude;
		this.longitude = longitude;
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
		this.tituloPublicacao = tituloPublicacao;
		this.descricao = descricao;
		this.idEstado = idEstado;
		this.idPais = idPais;
	}
	
	
	
}
