package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder		
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VisualizarIndicadorDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String nome;
	
	private String descricao;
	
	private String formula;
	
	private String disponibilidadeDeDados;
	
	private String metaODS;
	
	private Long odsId;
	
	private Integer odsNumero;
	
	private String odsTitulo;

	private String fonte;
	
	CalculadoraFormulaUtil cal;
	
	
	public VisualizarIndicadorDTO(Indicador indicador){
		this.id =  indicador.getId();
		this.nome = indicador.getNome();
		this.descricao = indicador.getDescricao();
		cal.formatarFormula(indicador);
		this.formula = indicador.getFormulaResultado();
		this.disponibilidadeDeDados = "VERIFICAR INFORMAÇÃO";
		this.metaODS = indicador.getMetaODS().getDescricao();
		this.odsId = indicador.getOds().getId();
		this.odsNumero = indicador.getOds().getNumero();
		this.odsTitulo = indicador.getOds().getTitulo();
		this.fonte = "VERIFICAR INFORMAÇÃO";
	}
	
}
