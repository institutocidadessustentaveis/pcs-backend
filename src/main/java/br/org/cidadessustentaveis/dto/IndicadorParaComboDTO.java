package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class IndicadorParaComboDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nome;

	private boolean numerico;
	
	public IndicadorParaComboDTO(Indicador indicador) {
		this.id = indicador.getId();
		this.nome = indicador.getNome();
		this.numerico = indicador.isNumerico();
	}
	
	public IndicadorParaComboDTO(Long idIndicador, String nome) {
		this.id = idIndicador;
		this.nome = nome;
	}
	
}
