package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioIndicadoresPreenchidosDTO {
	
	private Short AnoInicio;
	
	private Short AnoFim;
	
	private String prefeitura;
	
	private String estado;
	
	private String estadoNomecompleto;
	
	private String indicador;	
	
	private String ods;
	
	private String eixo;
	
	LocalDateTime dataPreenchimento;
	
	private Long codigoIBGE;
	
	private Short anoIndicador;
	
	private Integer page;
	
	private Integer linesPerPage;
	
	private Long count;
	
	private String orderBy;
	
	private String direction;
	
	
	
	
	public RelatorioIndicadoresPreenchidosDTO(String prefeitura, String estado, String estadoNomecompleto, String indicador, String ods, String eixo, LocalDateTime dataPreenchimento, Long codigoIBGE, Short anoIndicador) {
		this.prefeitura = prefeitura;
		this.estado = estado;
		this.estadoNomecompleto = estadoNomecompleto;
		this.indicador = indicador;
		this.ods = ods;
		this.eixo = eixo;
		this.dataPreenchimento = dataPreenchimento;
		this.codigoIBGE = codigoIBGE;
		this.anoIndicador = anoIndicador;
	}
	
	public RelatorioIndicadoresPreenchidosDTO(RelatorioIndicadoresPreenchidosDTO sessao) {
		super();
		this.prefeitura = sessao.getPrefeitura();
		this.indicador = sessao.getIndicador();
		this.ods = sessao.getOds();
		this.eixo = sessao.getEixo();
	}
	
}
