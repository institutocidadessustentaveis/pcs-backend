package br.org.cidadessustentaveis.dto;


import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaVariavel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor
public class FiltroVariaveisPorMunicipios {
	
	private Long idConsulta;
	
	private String nomeConsulta;

	private Long idVariavelSelecionada;
	
	private String valorPreenchido;
	
	private Long anoSelecionado;
	
	private boolean visualizarComoPontos = true;
	
	public FiltroVariaveisPorMunicipios(ConsultaVariavel consultaVariaveis) {
		this.idConsulta = consultaVariaveis.getId();
		this.nomeConsulta = consultaVariaveis.getNome();
		this.idVariavelSelecionada = consultaVariaveis.getIdVariavel();
		this.valorPreenchido =  consultaVariaveis.getValorPreenchido();
		this.anoSelecionado = consultaVariaveis.getAnoSelecionado();
		this.visualizarComoPontos = consultaVariaveis.getVisualizarComoPontos();
	}

}
