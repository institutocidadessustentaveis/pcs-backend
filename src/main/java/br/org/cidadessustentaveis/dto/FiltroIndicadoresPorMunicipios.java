package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaIndicador;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor
public class FiltroIndicadoresPorMunicipios {
	
	private Long idConsulta;
	
	private String nomeConsulta;
	
	private Long idIndicador;

	private Long idVariavelSelecionada;

	private Long idEixo;
	
	private Long idOds;
	
	private Long idCidade;
	
	private String valorPreenchido;

	private Long anoSelecionado;

	private Long popuMin;
	
	private Long popuMax;
	
	private boolean visualizarComoPontos = true;
	
	public FiltroIndicadoresPorMunicipios(ConsultaIndicador consultaIndicador) {
		this.idConsulta = consultaIndicador.getId();
		this.nomeConsulta = consultaIndicador.getNome();
		this.idIndicador = consultaIndicador.getIdIndicador();
		this.idVariavelSelecionada = consultaIndicador.getIdVariavel();
		this.idEixo = consultaIndicador.getIdEixo();
		this.idOds = consultaIndicador.getIdOds();
		this.idCidade = consultaIndicador.getIdCidade();
		this.valorPreenchido = consultaIndicador.getValorPreenchido();
		this.anoSelecionado = consultaIndicador.getAnoSelecionado();
		this.popuMin = consultaIndicador.getPopuMin();
		this.popuMax = consultaIndicador.getPopuMax();
		this.visualizarComoPontos = consultaIndicador.getVisualizarComoPontos();
	}

}
