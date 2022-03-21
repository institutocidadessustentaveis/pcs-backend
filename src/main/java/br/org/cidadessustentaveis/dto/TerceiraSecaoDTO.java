package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import br.org.cidadessustentaveis.model.home.TerceiraSecao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TerceiraSecaoDTO implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long indice;
	
	private String tituloPrincipal;
	private String tituloPrincipalCor;
	private String tituloPrincipalLink;
	
	
	
	private String primeiroTitulo;
	private String primeiroTituloCor;
	private String primeiroTituloLink;
	private String primeiroTexto;
	private String primeiroTextoCor;
	private String primeiroTextoLink;
	private String primeiraImagem;
	private String primeiraImagemLink;
	private String primeiraImagemTooltip;
	
	private String segundoTitulo;
	private String segundoTituloCor;
	private String segundoTituloLink;
	private String segundoTexto;
	private String segundoTextoCor;
	private String segundoTextoLink;
	private String segundaImagem;
	private String segundaImagemLink;
	private String segundaImagemTooltip;


	private String linhasCor;
	private Boolean exibir;
	
	private String tipo;
	
	public TerceiraSecaoDTO(Long id, Long indice, String tituloPrincipal, Boolean exibir, String tipo) {
		this.id = id;
		this.indice = indice;
		this.tituloPrincipal = tituloPrincipal;
		this.exibir = exibir;
		this.tipo = tipo;
	}	



	public TerceiraSecaoDTO(TerceiraSecao obj) {
		this.id = obj.getId();
		this.indice = obj.getIndice();
		
		this.tituloPrincipal = obj.getTituloPrincipal();
		this.tituloPrincipalCor = obj.getTituloPrincipalCor();
		this.tituloPrincipalLink = obj.getTituloPrincipalLink();
		
		this.primeiroTitulo = obj.getPrimeiroTitulo();
		this.primeiroTituloCor = obj.getPrimeiroTituloCor();
		this.primeiroTituloLink = obj.getPrimeiroTituloLink();
		this.primeiroTexto = obj.getPrimeiroTexto();
		this.primeiroTextoCor = obj.getPrimeiroTextoCor();
		this.primeiroTextoLink = obj.getPrimeiroTextoLink();
		this.primeiraImagem = obj.getPrimeiraImagem();
		this.primeiraImagemLink = obj.getPrimeiraImagemLink();
		this.primeiraImagemTooltip = obj.getPrimeiraImagemTooltip();
		
		this.segundoTitulo = obj.getSegundoTitulo();
		this.segundoTituloCor = obj.getSegundoTituloCor();
		this.segundoTituloLink = obj.getSegundoTituloLink();
		this.segundoTexto = obj.getSegundoTexto();
		this.segundoTextoCor = obj.getSegundoTextoCor();
		this.segundoTextoLink = obj.getSegundoTextoLink();
		this.segundaImagem = obj.getSegundaImagem();
		this.segundaImagemLink = obj.getSegundaImagemLink();
		this.segundaImagemTooltip = obj.getSegundaImagemTooltip();
	
		this.exibir = obj.getExibir();
	}	
	
	public TerceiraSecao toEntityInsert() {
		TerceiraSecao objToInsert = new TerceiraSecao();
		objToInsert.setIndice(this.indice);
		objToInsert.setTituloPrincipal(this.tituloPrincipal);
		objToInsert.setTituloPrincipalCor(this.tituloPrincipalCor);
		objToInsert.setTituloPrincipalLink(this.tituloPrincipalLink);
		
		objToInsert.setPrimeiroTitulo(this.primeiroTitulo);
		objToInsert.setPrimeiroTituloCor(this.primeiroTituloCor);
		objToInsert.setPrimeiroTituloLink(this.primeiroTituloLink);
		objToInsert.setPrimeiroTexto(this.primeiroTexto);
		objToInsert.setPrimeiroTextoCor(this.primeiroTextoCor);
		objToInsert.setPrimeiroTextoLink(this.primeiroTextoLink);
		objToInsert.setPrimeiraImagem(this.primeiraImagem);
		objToInsert.setPrimeiraImagemLink(this.primeiraImagemLink);
		objToInsert.setPrimeiraImagemTooltip(this.primeiraImagemTooltip);
		
		objToInsert.setSegundoTitulo(this.segundoTitulo);
		objToInsert.setSegundoTituloCor(this.segundoTituloCor);
		objToInsert.setSegundoTituloLink(this.segundoTituloLink);
		objToInsert.setSegundoTexto(this.segundoTexto);
		objToInsert.setSegundoTextoCor(this.segundoTextoCor);
		objToInsert.setSegundoTextoLink(this.segundoTextoLink);
		objToInsert.setSegundaImagem(this.segundaImagem);
		objToInsert.setSegundaImagemLink(this.segundaImagemLink);
		objToInsert.setSegundaImagemTooltip(this.segundaImagemTooltip);
		

		objToInsert.setExibir(this.exibir);
		return objToInsert;
	}
	
	public TerceiraSecao toEntityUpdate(TerceiraSecao objRef) {
		
		objRef.setIndice(this.indice);
		objRef.setTituloPrincipal(this.tituloPrincipal);
		objRef.setTituloPrincipalCor(this.tituloPrincipalCor);
		objRef.setTituloPrincipalLink(this.tituloPrincipalLink);
		
		objRef.setPrimeiroTitulo(this.primeiroTitulo);
		objRef.setPrimeiroTituloCor(this.primeiroTituloCor);
		objRef.setPrimeiroTituloLink(this.primeiroTituloLink);
		objRef.setPrimeiroTexto(this.primeiroTexto);
		objRef.setPrimeiroTextoCor(this.primeiroTextoCor);
		objRef.setPrimeiroTextoLink(this.primeiroTextoLink);
		objRef.setPrimeiraImagem(this.primeiraImagem);
		objRef.setPrimeiraImagemLink(this.primeiraImagemLink);
		objRef.setPrimeiraImagemTooltip(this.primeiraImagemTooltip);
		
		objRef.setSegundoTitulo(this.segundoTitulo);
		objRef.setSegundoTituloCor(this.segundoTituloCor);
		objRef.setSegundoTituloLink(this.segundoTituloLink);
		objRef.setSegundoTexto(this.segundoTexto);
		objRef.setSegundoTextoCor(this.segundoTextoCor);
		objRef.setSegundoTextoLink(this.segundoTextoLink);
		objRef.setSegundaImagem(this.segundaImagem);
		objRef.setSegundaImagemLink(this.segundaImagemLink);
		objRef.setSegundaImagemTooltip(this.segundaImagemTooltip);
		
		objRef.setExibir(this.exibir);
		
		return objRef;
	}
	
    public static TerceiraSecaoDTO create(TerceiraSecao terceiraSecao) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(terceiraSecao, TerceiraSecaoDTO.class);
    }

}
