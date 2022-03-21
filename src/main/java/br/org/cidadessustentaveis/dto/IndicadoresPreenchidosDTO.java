package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class IndicadoresPreenchidosDTO {

	private IndicadorDTO indicador;

	private List<IndicadorPreenchidoDTO> preenchidos;
	
	private VariaveisPreenchidasPorAnoDTO variaveisPreenchidasPorAno;

	public IndicadoresPreenchidosDTO(Indicador indicador, List<IndicadorPreenchido> preenchidos, List<VariavelPreenchida> variaveisPreenchidas) {
		this.indicador = new IndicadorDTO(indicador);
		
		this.preenchidos = new ArrayList<>();
		preenchidos.forEach(preenchido -> this.preenchidos.add(new IndicadorPreenchidoDTO(preenchido.getId(),preenchido.getAno(), preenchido.getIndicador().getId(), preenchido.getJustificativa())));
		
		this.variaveisPreenchidasPorAno = null != variaveisPreenchidas ? new VariaveisPreenchidasPorAnoDTO(variaveisPreenchidas) : null;
	}

	public IndicadoresPreenchidosDTO(Indicador indicador, List<SubdivisaoIndicadorPreenchido> preenchidos,
			List<SubdivisaoVariavelPreenchida> variaveisPreenchidas, boolean ehSubdivisao) {
				this.indicador = new IndicadorDTO(indicador);
		
				this.preenchidos = new ArrayList<>();
				preenchidos.forEach(preenchido -> this.preenchidos.add(new IndicadorPreenchidoDTO(preenchido.getId(),preenchido.getAno(), preenchido.getIndicador().getId(), preenchido.getJustificativa())));
				
				this.variaveisPreenchidasPorAno = null != variaveisPreenchidas ? new VariaveisPreenchidasPorAnoDTO(variaveisPreenchidas,true) : null;

	}
}

@Getter @Setter
class VariaveisPreenchidasPorAnoDTO {

	private Map<Short, List<VariavelPreenchidaDTO>> variaveisPreenchidas;
	
	public VariaveisPreenchidasPorAnoDTO(List<VariavelPreenchida> preenchidas) {
		this.variaveisPreenchidas = new HashMap<>();
		preenchidas.forEach(preenchida -> {
			if ( null == this.variaveisPreenchidas.get(preenchida.getAno()) ) {
				this.variaveisPreenchidas.put(preenchida.getAno(), new ArrayList<>());
			}
			this.variaveisPreenchidas.get(preenchida.getAno()).add(new VariavelPreenchidaDTO(preenchida));
		});
	}

	public VariaveisPreenchidasPorAnoDTO(List<SubdivisaoVariavelPreenchida> preenchidas, boolean ehSubdivisao) {
		this.variaveisPreenchidas = new HashMap<>();
		preenchidas.forEach(preenchida -> {
			if ( null == this.variaveisPreenchidas.get(preenchida.getAno()) ) {
				this.variaveisPreenchidas.put(preenchida.getAno(), new ArrayList<>());
			}
			this.variaveisPreenchidas.get(preenchida.getAno()).add(new VariavelPreenchidaDTO(preenchida));
		});
	}
	
}