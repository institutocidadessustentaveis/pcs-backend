package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FatorDesigualdadeDTO {
	private Short ano;
	private Double maximo;
	private Double minimo;
	private Double desigualdade;
	private String subdivisaoMaximo;
	private String subdivisaoMinimo;	
}