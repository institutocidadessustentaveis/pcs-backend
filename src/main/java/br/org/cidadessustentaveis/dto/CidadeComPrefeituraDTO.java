package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class CidadeComPrefeituraDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nomeCidade;
	
	private Long codigoIbge;
	
	private String nomeProvinciaEstado;
	
	private String estadoSigla;
	
	private Long populacao;
	
	private Long idCidade;
	
	public CidadeComPrefeituraDTO(String nomeCidade, Long codigoIbge, String nomeProvinciaEstado, String estadoSigla, Long populacao, Long idCidade) {
		this.nomeCidade = nomeCidade;
		this.codigoIbge = codigoIbge;
		this.nomeProvinciaEstado = nomeProvinciaEstado;
		this.estadoSigla = estadoSigla;
		this.populacao = populacao;
		this.idCidade = idCidade;
	}
}
