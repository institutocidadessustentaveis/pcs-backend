package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.institucional.Publicacao;
import br.org.cidadessustentaveis.model.institucional.TemplateInstitucional03;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TemplateInstitucional03DTO implements Serializable{

	private static final long serialVersionUID = 4398569683594829277L;
	private Long id;
	private String tituloSecaoTexto;
	private String secaoTexto;
	private String tituloPrimeiraSecao;
	private String textoPrimeiraSecao;
	private String imagemPrimeiraSecao;
	private String tituloSegundaSecao;
	private Boolean verMaisPCS;
	private Boolean verMaisInstituicao;
	private String tituloCatalogo1;
	private String tituloCatalogo2;
	private List<PublicacaoDTO> publicacoes = new ArrayList<PublicacaoDTO>();
	private List<PublicacaoDTO> publicacoes2 = new ArrayList<PublicacaoDTO>();
	
	public TemplateInstitucional03DTO(TemplateInstitucional03 templateRef) {
		this.id = templateRef.getId();
		this.secaoTexto = templateRef.getSecaoTexto();
		this.tituloSecaoTexto = templateRef.getTituloSecaoTexto();
		this.tituloPrimeiraSecao = templateRef.getTituloPrimeiraSecao();
		this.textoPrimeiraSecao = templateRef.getTextoPrimeiraSecao();
		this.imagemPrimeiraSecao = templateRef.getImagemPrimeiraSecao() != null ? templateRef.getImagemPrimeiraSecao().toBase64() : null;
		this.tituloSegundaSecao = templateRef.getTituloSegundaSecao();
		this.tituloCatalogo1 = templateRef.getTituloCatalogo1();
		this.tituloCatalogo2 = templateRef.getTituloCatalogo2();
		this.verMaisPCS = templateRef.getVerMaisPCS();
		this.verMaisInstituicao = templateRef.getVerMaisInstituicao();
		
		for(Publicacao publicacao : templateRef.getPublicacoes() ) {
			this.getPublicacoes().add(new PublicacaoDTO(publicacao));
		}
		for(Publicacao publicacao : templateRef.getPublicacoes2() ) {
			this.getPublicacoes2().add(new PublicacaoDTO(publicacao));
		}
	}
	
	public TemplateInstitucional03 toEntityInsert() {
		TemplateInstitucional03 templateInstitucional03 = new TemplateInstitucional03();
		
		templateInstitucional03.setTituloSecaoTexto(tituloSecaoTexto);
		templateInstitucional03.setSecaoTexto(secaoTexto);
		templateInstitucional03.setTituloPrimeiraSecao(tituloPrimeiraSecao);
		templateInstitucional03.setTextoPrimeiraSecao(textoPrimeiraSecao);
		templateInstitucional03.setTituloSegundaSecao(tituloSegundaSecao);
		templateInstitucional03.setVerMaisInstituicao(verMaisInstituicao);
		templateInstitucional03.setVerMaisPCS(verMaisPCS);
		templateInstitucional03.setTituloCatalogo1(tituloCatalogo1);
		templateInstitucional03.setTituloCatalogo2(tituloCatalogo2);
		return templateInstitucional03;
	}
	
	public TemplateInstitucional03 toEntityUpdate(TemplateInstitucional03 objRef) {
		objRef.setTituloSecaoTexto(this.tituloSecaoTexto);
		objRef.setSecaoTexto(this.secaoTexto);
		objRef.setTituloPrimeiraSecao(this.tituloPrimeiraSecao);
		objRef.setTextoPrimeiraSecao(this.textoPrimeiraSecao);
		objRef.setTituloSegundaSecao(this.tituloSegundaSecao);
		objRef.setVerMaisInstituicao(verMaisInstituicao);
		objRef.setVerMaisPCS(verMaisPCS);
		objRef.setTituloCatalogo1(tituloCatalogo1);
		objRef.setTituloCatalogo2(tituloCatalogo2);
		return objRef;
	}

}
