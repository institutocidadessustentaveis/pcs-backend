package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetasDetalhado;
import br.org.cidadessustentaveis.util.NumeroUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanoDeMetasDetalhadoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long idIndicador;
	
	private String nomeIndicador;
	
	private String descricaoIndicador;

	private String ultimoValorIndicador;
	
	private String ultimoValorIndicadorApresentacao;
	
	private String statusUltimoValor;
	
	private String corUltimoValor;

	private String ods;
	
	private String idOds;

	private String metaOds;
	
	private String metaOdsNumero;
	
	private String metaFinal;
	
	private String valorPreenchidoPrimeiroAno;
	
	private String valorPreenchidoSegundoAno;
	
	private String valorPreenchidoTerceiroAno;
		
	private String valorPreenchidoQuartoAno;
	
	private String valorPreenchidoPrimeiroAnoApresentacao;
	
	private String valorPreenchidoSegundoAnoApresentacao;
	
	private String valorPreenchidoTerceiroAnoApresentacao;
		
	private String valorPreenchidoQuartoAnoApresentacao;

	private String metaAnualPrimeiroAno;
	
	private String metaAnualSegundoAno;
	
	private String metaAnualTerceiroAno;
	
	private String metaAnualQuartoAno;

	private String metaAnualPrimeiroAnoApresentacao;
	
	private String metaAnualSegundoAnoApresentacao;
	
	private String metaAnualTerceiroAnoApresentacao;
	
	private String metaAnualQuartoAnoApresentacao;
	
	private Float variacaoPrimeiroAno;/**/
	
	private Float variacaoSegundoAno;
	
	private Float variacaoTerceiroAno;
	
	private Float variacaoQuartoAno;
	
	private Double orcamentoPrevisto;
	
	private Double orcamentoExecutado;
	
	private String justificativa;
	
	private String planoParaAlcancarProposta;
	
	public PlanoDeMetasDetalhadoDTO(PlanoDeMetasDetalhado planoDeMetasDetalhadoRef) {
		this.id = planoDeMetasDetalhadoRef.getId();
		this.idIndicador = planoDeMetasDetalhadoRef.getIndicador().getId();
		this.nomeIndicador = planoDeMetasDetalhadoRef.getIndicador().getNome();
		this.descricaoIndicador = planoDeMetasDetalhadoRef.getIndicador().getDescricao();
		
		
		this.ods = planoDeMetasDetalhadoRef.getIndicador().getOds() != null ? 
				planoDeMetasDetalhadoRef.getIndicador().getOds().getTitulo() : "";
		this.idOds = planoDeMetasDetalhadoRef.getIndicador().getOds() != null ? 
				planoDeMetasDetalhadoRef.getIndicador().getOds().getId()+"" : "";
		
		if(planoDeMetasDetalhadoRef.getIndicador().getMetaODS() != null) {
			this.metaOds = planoDeMetasDetalhadoRef.getIndicador().getMetaODS().getNumero() + " - " + planoDeMetasDetalhadoRef.getIndicador().getMetaODS().getDescricao();
		}
		this.metaOdsNumero = planoDeMetasDetalhadoRef.getIndicador().getMetaODS() != null ? 
				planoDeMetasDetalhadoRef.getIndicador().getMetaODS().getNumero() : "";
		this.metaAnualPrimeiroAno = planoDeMetasDetalhadoRef.getMetaAnualPrimeiroAno();
		this.metaAnualSegundoAno = planoDeMetasDetalhadoRef.getMetaAnualSegundoAno();
		this.metaAnualTerceiroAno = planoDeMetasDetalhadoRef.getMetaAnualTerceiroAno();
		this.metaAnualQuartoAno = planoDeMetasDetalhadoRef.getMetaAnualQuartoAno();
		
		try {
			this.metaAnualPrimeiroAnoApresentacao = NumeroUtil.decimalToString(NumeroUtil.toDouble(planoDeMetasDetalhadoRef.getMetaAnualPrimeiroAno())) ;
		} catch (Exception e) {
			e.printStackTrace();
			this.metaAnualPrimeiroAnoApresentacao = "";
		}
		try {
			this.metaAnualSegundoAnoApresentacao = NumeroUtil.decimalToString(NumeroUtil.toDouble(planoDeMetasDetalhadoRef.getMetaAnualSegundoAno())) ;
		} catch (Exception e) {
			e.printStackTrace();
			this.metaAnualSegundoAnoApresentacao = "";
		}
		try {
			this.metaAnualTerceiroAnoApresentacao = NumeroUtil.decimalToString(NumeroUtil.toDouble(planoDeMetasDetalhadoRef.getMetaAnualTerceiroAno())) ;
		} catch (Exception e) {
			e.printStackTrace();
			this.metaAnualTerceiroAnoApresentacao = "";
		}
		try {
			this.metaAnualQuartoAnoApresentacao = NumeroUtil.decimalToString(NumeroUtil.toDouble(planoDeMetasDetalhadoRef.getMetaAnualQuartoAno())) ;
		} catch (Exception e) {
			e.printStackTrace();
			this.metaAnualQuartoAnoApresentacao = "";
		}

		this.orcamentoPrevisto = planoDeMetasDetalhadoRef.getOrcamentoPrevisto();
		this.orcamentoExecutado = planoDeMetasDetalhadoRef.getOrcamentoExecutado();
		this.metaFinal = planoDeMetasDetalhadoRef.getMetaAnualQuartoAno();
		this.planoParaAlcancarProposta = planoDeMetasDetalhadoRef.getPlanoParaAlcancarProposta();
	}
	
}
