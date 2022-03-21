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
public class IndicadoresPreenchidosSubdivisaoDTO {

	private IndicadorDTO indicador;

	private List<IndicadorPreenchidoSimplesDTO> preenchidos;
	
	private VariaveisPreenchidasPorAnoDTO variaveisPreenchidasPorAno;

	public IndicadoresPreenchidosSubdivisaoDTO(Indicador indicador, List<SubdivisaoIndicadorPreenchido> preenchidos,
			List<SubdivisaoVariavelPreenchida> variaveisPreenchidas, boolean ehSubdivisao) {
				this.indicador = new IndicadorDTO(indicador);
		
				this.preenchidos = new ArrayList<>();
				preenchidos.forEach(preenchido -> 
					this.preenchidos.add(
						new IndicadorPreenchidoSimplesDTO(
							preenchido.getId(), 
							preenchido.getIndicador().getId(),
							preenchido.getIndicador().getNome(), 
							preenchido.getIndicador().getDescricao(),
							preenchido.getAno(),
							preenchido.getDataPreenchimento(), 
							preenchido.getResultadoApresentacao(), 
							preenchido.getPrefeitura().getCidade().getId(), 
							preenchido.getPrefeitura().getCidade().getNome())));
				
				this.variaveisPreenchidasPorAno = null != variaveisPreenchidas ? new VariaveisPreenchidasPorAnoDTO(variaveisPreenchidas,true) : null;

	}
}