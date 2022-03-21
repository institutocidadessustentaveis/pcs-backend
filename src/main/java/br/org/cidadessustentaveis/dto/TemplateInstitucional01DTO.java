package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.institucional.TemplateInstitucional01;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TemplateInstitucional01DTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String tituloPrimeiraSecao;
	private String textoPrimeiraSecao;
	private String blockquotePrimeiraSecao;
	private String tituloSegundaSecao;
	private String txtSegundaSecao;
	private String txtBotao01;
	private String linkBotao01;
	private String txtBotao02;
	private String linkBotao02;
	private String txtBotao03;
	private String linkBotao03;
	private String txtBotao04;
	private String linkBotao04;
	
	public TemplateInstitucional01DTO(TemplateInstitucional01 obj) {
		this.id = obj.getId();
		this.tituloPrimeiraSecao = obj.getTituloPrimeiraSecao();
		this.textoPrimeiraSecao = obj.getTextoPrimeiraSecao();
		this.blockquotePrimeiraSecao = obj.getBlockquotePrimeiraSecao();
		this.tituloSegundaSecao = obj.getTituloSegundaSecao();
		this.txtSegundaSecao = obj.getTxtSegundaSecao();
		this.txtBotao01 = obj.getTxtBotao01();
		this.linkBotao01 = obj.getLinkBotao01();
		this.txtBotao02 = obj.getTxtBotao02();
		this.linkBotao02 = obj.getLinkBotao02();
		this.txtBotao03 = obj.getTxtBotao03();
		this.linkBotao03 = obj.getLinkBotao03();
		this.txtBotao04 = obj.getTxtBotao04();
		this.linkBotao04 = obj.getLinkBotao04();
	}
	
	public TemplateInstitucional01 toEntityInsert() {
		TemplateInstitucional01 objToInsert = new TemplateInstitucional01();
		
		objToInsert.setTituloPrimeiraSecao(this.tituloPrimeiraSecao);
		objToInsert.setTextoPrimeiraSecao(this.textoPrimeiraSecao);
		objToInsert.setBlockquotePrimeiraSecao(this.blockquotePrimeiraSecao);
		objToInsert.setTituloSegundaSecao(this.tituloSegundaSecao);
		objToInsert.setTxtSegundaSecao(this.txtSegundaSecao);
		objToInsert.setTxtBotao01(this.txtBotao01);
		objToInsert.setLinkBotao01(this.linkBotao01);
		objToInsert.setTxtBotao02(this.txtBotao02);
		objToInsert.setLinkBotao02(this.linkBotao02);
		objToInsert.setTxtBotao03(this.txtBotao03);
		objToInsert.setLinkBotao03(this.linkBotao03);
		objToInsert.setTxtBotao04(this.txtBotao04);
		objToInsert.setLinkBotao04(this.linkBotao04);
		
		return objToInsert;
	}
	
	public TemplateInstitucional01 toEntityUpdate(TemplateInstitucional01 objRef) {
		 objRef.setTituloPrimeiraSecao(this.tituloPrimeiraSecao);
		 objRef.setTextoPrimeiraSecao(this.textoPrimeiraSecao);
		 objRef.setBlockquotePrimeiraSecao(this.blockquotePrimeiraSecao);
		 objRef.setTituloSegundaSecao(this.tituloSegundaSecao);
		 objRef.setTxtSegundaSecao(this.txtSegundaSecao);
		 objRef.setTxtBotao01(this.txtBotao01);
		 objRef.setLinkBotao01(this.linkBotao01);
		 objRef.setTxtBotao02(this.txtBotao02);
		 objRef.setLinkBotao02(this.linkBotao02);
		 objRef.setTxtBotao03(this.txtBotao03);
		 objRef.setLinkBotao03(this.linkBotao03);
		 objRef.setTxtBotao04(this.txtBotao04);
		 objRef.setLinkBotao04(this.linkBotao04);
		 
		 return objRef;
	}

}
