package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioQuantidadeIndicadorCadastradoDTO {

	private Long id;
	
	private String prefeitura;
	
	private Long codigoIBGE;
	
	private String estado;
	
	private String estadoSigla;
	
	private Integer ano;
	
	private Long quantidade;
	
	private String usuarioLogado;
	
	private Short anoInicio;
	
	private Short anoFim;
	
	public RelatorioQuantidadeIndicadorCadastradoDTO (String cidade, Long codigoIBGE, String estado,String estadoSigla, Integer ano, Long quantidade ) {
		this.prefeitura = cidade;
		this.codigoIBGE = codigoIBGE;
		this.estado = estado;
		this.estadoSigla = estadoSigla;
		this.ano = ano;
		this.quantidade = quantidade;
	}
}
