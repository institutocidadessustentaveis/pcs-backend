package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetasDetalhadoHistorico;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlanoDeMetaHistoricoAnosDTO {
	
	private String metaAnualPrimeiroAno;
	
	private String metaAnualSegundoAno;
	
	private String metaAnualTerceiroAno;
	
	private String metaAnualQuartoAno;
	
	private Long idIndicador;
	
	public PlanoDeMetaHistoricoAnosDTO (PlanoDeMetasDetalhadoHistorico planoDeMetasDetalhadoHistorico) {
		this.metaAnualPrimeiroAno = planoDeMetasDetalhadoHistorico.getMetaAnualPrimeiroAno();
		this.metaAnualSegundoAno = planoDeMetasDetalhadoHistorico.getMetaAnualSegundoAno();
		this.metaAnualTerceiroAno = planoDeMetasDetalhadoHistorico.getMetaAnualTerceiroAno();
		this.metaAnualQuartoAno = planoDeMetasDetalhadoHistorico.getMetaAnualQuartoAno();
		this.idIndicador = planoDeMetasDetalhadoHistorico.getIndicador().getId();
	}
	
	public PlanoDeMetaHistoricoAnosDTO(String metaAnualPrimeiroAno, String metaAnualSegundoAno, String metaAnualTerceiroAno, String metaAnualQuartoAno, Long idIndicador) {
		this.metaAnualPrimeiroAno = metaAnualPrimeiroAno;
		this.metaAnualSegundoAno = metaAnualSegundoAno;
		this.metaAnualTerceiroAno = metaAnualTerceiroAno;
		this.metaAnualQuartoAno = metaAnualQuartoAno;
		this.idIndicador = idIndicador;
	}
	
}
