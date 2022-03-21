package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Mandato2DTO implements Serializable{

	private static final long serialVersionUID = 4356711170784527834L;

	private String periodo;
	
	private Short anoInicioMandato;
	
	private Short anoFimMandato;
	
	private List<ValorPorCidadeFiltradoDTO> listaValoresPorCidade;
}
