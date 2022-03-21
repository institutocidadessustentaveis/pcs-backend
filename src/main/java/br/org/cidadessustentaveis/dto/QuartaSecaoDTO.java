package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import br.org.cidadessustentaveis.model.home.QuartaSecao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QuartaSecaoDTO implements Serializable{

private static final long serialVersionUID = 1L;
	

	private Long id;
	private Long indice;
	private String tituloPrincipal;
	private String tituloPrincipalCor;
	private String tituloPrincipalLink;
	private String primeiroTitulo;
	private String primeiroTituloCor;
	private String primeiroTituloLink;
	private String primeiraImagem;
	private String primeiraImagemLink;
	private String primeiraImagemTooltip;
	private String primeiroBotaoTexto;
	private String primeiroBotaoTextoCor;
	private String primeiroBotaoLink;


	private String linhasCor;
	private Boolean exibir;
	
	private String tipo;
	
	public QuartaSecaoDTO(Long id, Long indice, String tituloPrincipal, Boolean exibir, String tipo) {
		this.id = id;
		this.indice = indice;
		this.tituloPrincipal = tituloPrincipal;
		this.exibir = exibir;
		this.tipo = tipo;
	}	




	public QuartaSecaoDTO(QuartaSecao obj) {
		this.id = obj.getId();
		this.indice = obj.getIndice();
		
		this.tituloPrincipal = obj.getTituloPrincipal();
		this.tituloPrincipalCor = obj.getTituloPrincipalCor();
		this.tituloPrincipalLink = obj.getTituloPrincipalLink();
		
		this.primeiroTitulo = obj.getPrimeiroTitulo();
		this.primeiroTituloCor = obj.getPrimeiroTituloCor();
		this.primeiroTituloLink = obj.getPrimeiroTituloLink();

		this.primeiraImagem = obj.getPrimeiraImagem();
		this.primeiraImagemLink = obj.getPrimeiraImagemLink();
		this.primeiraImagemTooltip = obj.getPrimeiraImagemTooltip();
		
		this.primeiroBotaoTexto = obj.getPrimeiroBotaoTexto();
		this.primeiroBotaoTextoCor = obj.getPrimeiroBotaoTextoCor();
		this.primeiroBotaoLink = obj.getPrimeiroBotaoLink();
		
		this.exibir = obj.getExibir();
	}	
	
	public QuartaSecao toEntityInsert() {
		QuartaSecao objToInsert = new QuartaSecao();
		objToInsert.setIndice(this.indice);
		objToInsert.setTituloPrincipal(this.tituloPrincipal);
		objToInsert.setTituloPrincipalCor(this.tituloPrincipalCor);
		objToInsert.setTituloPrincipalLink(this.tituloPrincipalLink);
		
		objToInsert.setPrimeiroTitulo(this.primeiroTitulo);
		objToInsert.setPrimeiroTituloCor(this.primeiroTituloCor);
		objToInsert.setPrimeiroTituloLink(this.primeiroTituloLink);

		objToInsert.setPrimeiraImagem(this.primeiraImagem);
		objToInsert.setPrimeiraImagemLink(this.primeiraImagemLink);
		objToInsert.setPrimeiraImagemTooltip(this.primeiraImagemTooltip);
		
		objToInsert.setPrimeiroBotaoTexto(this.getPrimeiroBotaoTexto());
		objToInsert.setPrimeiroBotaoTextoCor(this.getPrimeiroBotaoTextoCor());
		objToInsert.setPrimeiroBotaoLink(this.getPrimeiroBotaoLink());

		objToInsert.setExibir(this.exibir);
		return objToInsert;
	}
	
	public QuartaSecao toEntityUpdate(QuartaSecao objRef) {
		
		objRef.setIndice(this.indice);
		objRef.setTituloPrincipal(this.tituloPrincipal);
		objRef.setTituloPrincipalCor(this.tituloPrincipalCor);
		objRef.setTituloPrincipalLink(this.tituloPrincipalLink);
		
		objRef.setPrimeiroTitulo(this.primeiroTitulo);
		objRef.setPrimeiroTituloCor(this.primeiroTituloCor);
		objRef.setPrimeiroTituloLink(this.primeiroTituloLink);
		objRef.setPrimeiraImagem(this.primeiraImagem);
		objRef.setPrimeiraImagemLink(this.primeiraImagemLink);
		objRef.setPrimeiraImagemTooltip(this.primeiraImagemTooltip);
		
		objRef.setPrimeiroBotaoTexto(this.primeiroBotaoTexto);
		objRef.setPrimeiroBotaoTextoCor(this.primeiroBotaoTextoCor);
		objRef.setPrimeiroBotaoLink(this.primeiroBotaoLink);

		objRef.setExibir(this.exibir);
		
		return objRef;
	}
	
    public static QuartaSecaoDTO create(QuartaSecao quartaSecao) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(quartaSecao, QuartaSecaoDTO.class);
    }

}
