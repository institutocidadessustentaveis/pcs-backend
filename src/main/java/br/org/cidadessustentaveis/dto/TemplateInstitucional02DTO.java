package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.institucional.TemplateInstitucional02;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TemplateInstitucional02DTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String tituloPrimeiraSecao;
	private String textoPrimeiraSecao;
	private String imagemPrimeiraSecao;
	private String tituloSegundaSecao;
	private String txtSegundaSecao;
	private String corFundoSegundaSecao;
	private String tituloTerceiraSecao;
	private String txtTerceiraSecao;
	private String imagemTerceiraSecao;
	private String tituloQuartaSecao;
	private String txtQuartaSecao;
	private String txtBotao01;
	private String linkBotao01;
	private String nomeAutorImagemPrimeiraSecao;
	private String nomeAutorImagemTerceiraSecao;
	
	public TemplateInstitucional02DTO(TemplateInstitucional02 obj) {
		this.id = obj.getId();
		this.tituloPrimeiraSecao = obj.getTituloPrimeiraSecao();
		this.textoPrimeiraSecao = obj.getTextoPrimeiraSecao();
		this.imagemPrimeiraSecao = obj.getImagemPrimeiraSecao();
		this.tituloSegundaSecao = obj.getTituloSegundaSecao();
		this.txtSegundaSecao = obj.getTxtSegundaSecao();
		this.corFundoSegundaSecao = obj.getCorFundoSegundaSecao();
		this.tituloTerceiraSecao = obj.getTituloTerceiraSecao();
		this.txtTerceiraSecao = obj.getTxtTerceiraSecao();
		this.imagemTerceiraSecao = obj.getImagemTerceiraSecao();
		this.tituloQuartaSecao = obj.getTituloQuartaSecao();
		this.txtQuartaSecao = obj.getTxtQuartaSecao();
		this.txtBotao01 = obj.getTxtBotao01();
		this.linkBotao01 = obj.getLinkBotao01();
		this.nomeAutorImagemPrimeiraSecao = obj.getNomeAutorImagemPrimeiraSecao();
		this.nomeAutorImagemTerceiraSecao = obj.getNomeAutorImagemTerceiraSecao();
	}	
	
	public TemplateInstitucional02 toEntityInsert() {
		TemplateInstitucional02 objToInsert = new TemplateInstitucional02();
		
		objToInsert.setTituloPrimeiraSecao(this.tituloPrimeiraSecao);
		objToInsert.setTextoPrimeiraSecao(this.textoPrimeiraSecao);
		objToInsert.setImagemPrimeiraSecao(this.imagemPrimeiraSecao);
		objToInsert.setTituloSegundaSecao(this.tituloSegundaSecao);
		objToInsert.setTxtSegundaSecao(this.txtSegundaSecao);
		objToInsert.setCorFundoSegundaSecao(this.corFundoSegundaSecao);
		objToInsert.setTituloTerceiraSecao(this.tituloTerceiraSecao);
		objToInsert.setTxtTerceiraSecao(this.txtTerceiraSecao);
		objToInsert.setImagemTerceiraSecao(this.imagemTerceiraSecao);
		objToInsert.setTituloQuartaSecao(this.tituloQuartaSecao);
		objToInsert.setTxtQuartaSecao(this.txtQuartaSecao);
		objToInsert.setTxtBotao01(this.txtBotao01);
		objToInsert.setLinkBotao01(this.linkBotao01);
		objToInsert.setNomeAutorImagemPrimeiraSecao(this.nomeAutorImagemPrimeiraSecao);
		objToInsert.setNomeAutorImagemTerceiraSecao(this.nomeAutorImagemTerceiraSecao);
		return objToInsert;
	}
	
	public TemplateInstitucional02 toEntityUpdate(TemplateInstitucional02 objRef) {
		objRef.setTituloPrimeiraSecao(this.tituloPrimeiraSecao);
		objRef.setTextoPrimeiraSecao(this.textoPrimeiraSecao);
		objRef.setImagemPrimeiraSecao(this.imagemPrimeiraSecao);
		objRef.setTituloSegundaSecao(this.tituloSegundaSecao);
		objRef.setTxtSegundaSecao(this.txtSegundaSecao);
		objRef.setCorFundoSegundaSecao(this.corFundoSegundaSecao);
		objRef.setTituloTerceiraSecao(this.tituloTerceiraSecao);
		objRef.setTxtTerceiraSecao(this.txtTerceiraSecao);
		objRef.setImagemTerceiraSecao(this.imagemTerceiraSecao);
		objRef.setTituloQuartaSecao(this.tituloQuartaSecao);
		objRef.setTxtQuartaSecao(this.txtQuartaSecao);
		objRef.setTxtBotao01(this.txtBotao01);
		objRef.setLinkBotao01(this.linkBotao01);
		objRef.setNomeAutorImagemPrimeiraSecao(this.nomeAutorImagemPrimeiraSecao);
		objRef.setNomeAutorImagemTerceiraSecao(this.nomeAutorImagemTerceiraSecao);
		return objRef;
	}

}
