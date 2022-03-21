package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import br.org.cidadessustentaveis.model.home.SetimaSecao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SetimaSecaoDTO implements Serializable{

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

	private String linhasCor;
	private Boolean exibir;
	
	private String tipo;

	public SetimaSecaoDTO(Long id, Long indice, String tituloPrincipal, Boolean exibir, String tipo) {
		this.id = id;
		this.indice = indice;
		this.tituloPrincipal = tituloPrincipal;
		this.exibir = exibir;
		this.tipo = tipo;
	}	

	public SetimaSecaoDTO(SetimaSecao obj) {	
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
	
		this.exibir = obj.getExibir();
	}	
	
	public SetimaSecao toEntityInsert() {
		SetimaSecao objToInsert = new SetimaSecao();
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
		
		objToInsert.setExibir(this.exibir);
		return objToInsert;
	}
	
	public SetimaSecao toEntityUpdate(SetimaSecao objRef) {
		
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
		
		objRef.setExibir(this.exibir);
		
		return objRef;
	}
	
    public static SetimaSecaoDTO create(SetimaSecao setimaSecao) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(setimaSecao, SetimaSecaoDTO.class);
    }

}
