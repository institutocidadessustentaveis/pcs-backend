package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import br.org.cidadessustentaveis.model.home.QuartaSecao;
import br.org.cidadessustentaveis.model.home.QuintaSecao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QuintaSecaoDTO implements Serializable{

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
	
	
	private String primeiroSubTitulo;
	private String primeiroSubTituloCor;
	private String primeiroSubTituloLink;
	
	
	private String segundoTitulo;
	private String segundoTituloCor;
	private String segundoTituloLink;
	
	private String segundoTexto;
	private String segundoTextoCor;
	private String segundoTextoLink;
	
	
	private String segundoSubTitulo;
	private String segundoSubTituloCor;
	private String segundoSubTituloLink;
	

	private String linhasCor;
	private Boolean exibir;	
	
	private String tipo;

	
	public QuintaSecaoDTO(Long id, Long indice, String tituloPrincipal, Boolean exibir, String tipo) {
		this.id = id;
		this.indice = indice;
		this.tituloPrincipal = tituloPrincipal;
		this.exibir = exibir;
		this.tipo = tipo;
	}	

	public QuintaSecaoDTO(QuintaSecao obj) {
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
		
		this.primeiroSubTitulo = obj.getPrimeiroSubTitulo();
		this.primeiroSubTituloCor = obj.getPrimeiroSubTituloCor();
		this.primeiroSubTituloLink = obj.getPrimeiroSubTituloLink();
		
		this.segundoTitulo = obj.getSegundoTitulo();
		this.segundoTituloCor = obj.getSegundoTituloCor();
		this.segundoTituloLink = obj.getSegundoTituloLink();
		this.segundoTexto = obj.getSegundoTexto();
		this.segundoTextoCor = obj.getSegundoTextoCor();
		this.segundoTextoLink = obj.getSegundoTextoLink();
		
		this.segundoSubTitulo = obj.getSegundoSubTitulo();
		this.segundoSubTituloCor = obj.getSegundoSubTituloCor();
		this.segundoSubTituloLink = obj.getSegundoSubTituloLink();
	
		this.exibir = obj.getExibir();
	}	
	
	public QuintaSecao toEntityInsert() {
		QuintaSecao objToInsert = new QuintaSecao();
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
		
		objToInsert.setPrimeiroSubTitulo(this.primeiroSubTitulo);
		objToInsert.setPrimeiroSubTituloCor(this.primeiroSubTituloCor);
		objToInsert.setPrimeiroSubTituloLink(this.primeiroSubTituloLink);
		
		
		objToInsert.setSegundoTitulo(this.segundoTitulo);
		objToInsert.setSegundoTituloCor(this.segundoTituloCor);
		objToInsert.setSegundoTituloLink(this.segundoTituloLink);
		objToInsert.setSegundoTexto(this.segundoTexto);
		objToInsert.setSegundoTextoCor(this.segundoTextoCor);
		objToInsert.setSegundoTextoLink(this.segundoTextoLink);
		
		objToInsert.setSegundoSubTitulo(this.segundoSubTitulo);
		objToInsert.setSegundoSubTituloCor(this.segundoSubTituloCor);
		objToInsert.setSegundoSubTituloLink(this.segundoSubTituloLink);
		

		objToInsert.setExibir(this.exibir);
		return objToInsert;
	}
	
	public QuintaSecao toEntityUpdate(QuintaSecao objRef) {
		
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

		objRef.setPrimeiroSubTitulo(this.primeiroSubTitulo);
		objRef.setPrimeiroSubTituloCor(this.primeiroSubTituloCor);
		objRef.setPrimeiroSubTituloLink(this.primeiroSubTituloLink);
		
		
		objRef.setSegundoTitulo(this.segundoTitulo);
		objRef.setSegundoTituloCor(this.segundoTituloCor);
		objRef.setSegundoTituloLink(this.segundoTituloLink);
		objRef.setSegundoTexto(this.segundoTexto);
		objRef.setSegundoTextoCor(this.segundoTextoCor);
		objRef.setSegundoTextoLink(this.segundoTextoLink);
		
		objRef.setSegundoSubTitulo(this.segundoSubTitulo);
		objRef.setSegundoSubTituloCor(this.segundoSubTituloCor);
		objRef.setSegundoSubTituloLink(this.segundoSubTituloLink);

		
		objRef.setExibir(this.exibir);
		
		return objRef;
	}
	
    public static QuintaSecaoDTO create(QuintaSecao quintaSecao) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(quintaSecao, QuintaSecaoDTO.class);
    }

}
