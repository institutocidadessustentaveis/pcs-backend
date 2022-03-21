package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class IndicadorcomparativomesmacidadeDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nome;
	
	public IndicadorcomparativomesmacidadeDTO(Indicador indicador) {
		this.id = indicador.getId();
		this.nome = indicador.getNome();
	}
}
