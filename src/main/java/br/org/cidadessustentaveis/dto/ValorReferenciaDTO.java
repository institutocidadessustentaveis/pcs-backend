package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ValorReferenciaDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Double valorde;
	private Double valorate;
	private String Label;
	private String fonteReferencia;
	private String cor;
	private Variavel variavel;

	public ValorReferenciaDTO(ValorReferencia variavelReferencia) {
		super();
		this.id = variavelReferencia.getId();
		this.valorde = variavelReferencia.getValorde();
		this.valorate = variavelReferencia.getValorate();
		this.Label = variavelReferencia.getLabel();
		this.fonteReferencia = variavelReferencia.getFonteReferencia();
		this.cor = variavelReferencia.getCor();
		this.variavel = variavelReferencia.getVariavel();
	}
	
	public ValorReferencia toEntityInsert() {
		return new ValorReferencia(null,this.valorde, this.valorate, this.Label, this.fonteReferencia, this.cor, this.variavel);	
	}
	
	public ValorReferencia toEntityUpdate(ValorReferencia objRef) {
		objRef.setValorde(valorde);
		objRef.setValorate(valorate);
		objRef.setLabel(Label);
		objRef.setFonteReferencia(fonteReferencia);
		objRef.setCor(cor);
		objRef.setVariavel(variavel);
		return objRef;
	}
}
