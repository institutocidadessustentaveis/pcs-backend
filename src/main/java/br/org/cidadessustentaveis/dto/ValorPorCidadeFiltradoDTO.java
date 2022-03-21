package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ValorPorCidadeFiltradoDTO implements Serializable{

	private static final long serialVersionUID = -2522519978361722589L;
	
	private Long idCidade;
	
	private String cidade;
	
	private Double latitude;
	
	private Double longitude;
	
	private String periodoMandato;
	
	private String resultadoAno1;
	
	private String resultadoAno2;
	
	private String resultadoAno3;
	
	private String resultadoAno4;
	
	private String indicadorMultiplo;
}
