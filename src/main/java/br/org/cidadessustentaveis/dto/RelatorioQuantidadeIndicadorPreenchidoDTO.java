package br.org.cidadessustentaveis.dto;


import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class RelatorioQuantidadeIndicadorPreenchidoDTO implements Comparable<RelatorioQuantidadeIndicadorPreenchidoDTO>{

	private Long id;
	
	private String prefeitura;
	
	private Long codigoIBGE;
	
	private String estado;
	
	private String estadoSigla;
	
	private Long quantidade;
	
	private String usuarioLogado;
	
	private Short ano;
	
	private Short anoInicio;
	
	private Short anoFim;
	
	private Long populacao;
	
	public RelatorioQuantidadeIndicadorPreenchidoDTO(String prefeitura, Long codigoIBGE, String estado, String estadoSigla, Short ano, Long quantidade, Long populacao) {
		this.prefeitura = prefeitura;
		this.codigoIBGE = codigoIBGE;
		this.estado = estado;
		this.estadoSigla = estadoSigla;
		this.quantidade = quantidade;
		this.ano = ano;
		this.populacao = populacao;
	}

	@Override
	public int compareTo(RelatorioQuantidadeIndicadorPreenchidoDTO relatorio) {
		return Comparator.comparing(RelatorioQuantidadeIndicadorPreenchidoDTO::getPrefeitura)
	              .thenComparingInt(RelatorioQuantidadeIndicadorPreenchidoDTO::getAno)
	              .compare(this, relatorio);
	}
	
	
}
