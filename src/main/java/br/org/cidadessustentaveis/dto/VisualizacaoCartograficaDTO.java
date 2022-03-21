package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.sistema.VisualizacaoCartografica;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VisualizacaoCartograficaDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String indicador;
	
	private String estado;

	private String cidade;
	
	private int qtdeVisualizacao;
	
	private int qtdeIndicadorExportacao;
	
	private String usuario;
	
	private String usuarioLogado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	private LocalDateTime data; 
	
	private String acao;

	/*public VisualizacaoCartograficaDTO(VisualizacaoCartografica visualizacaoCartografica) {
		super();
		this.id = visualizacaoCartografica.getId();
		this.indicador = visualizacaoCartografica.getIndicador();
		this.cidade = visualizacaoCartografica.getCidade();
		//this.qtdeVisualizacao = visualizacaoCartografica.getQtdeVisualizacao();
		//this.qtdeIndicadorExportacao = visualizacaoCartografica.getQtdeIndicadorExportacao();
		this.data = visualizacaoCartografica.getData();
		//this.usuarioLogado = visualizacaoCartografica.getUsuarioLogado();
	}*/
	
	public VisualizacaoCartografica toEntityInsert() {
		return new VisualizacaoCartografica(null, null, null, data, estado, null, acao);	
	}
	
	/*public VisualizacaoCartografica toEntityUpdate(VisualizacaoCartografica objRef) {
		objRef.setIndicador(indicador);
		objRef.setCidade(cidade);
		objRef.setData(data);
		return objRef;
	}*/
	
	public VisualizacaoCartograficaDTO(Long id, String indicador, String cidade, String estado, LocalDateTime data, String usuario, String acao) {
		this.id = id;
		this.indicador = indicador;
		this.cidade = cidade;
		this.estado = estado;
		this.data = data;
		this.usuario = usuario;
		this.acao = acao;
		//this.qtdeIndicadorExportacao = qtdeIndicadorExportacao.toString();
	}
	
}
