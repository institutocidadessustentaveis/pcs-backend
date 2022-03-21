package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import br.org.cidadessustentaveis.model.home.PrimeiraSecao;
import br.org.cidadessustentaveis.model.home.SegundaSecao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrimeiraSecaoDTO implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long indice;
	private String primeiroTitulo;
	private String primeiroTituloCor;
	private String primeiroTituloLink;
	private String primeiroTexto;
	private String primeiroTextoCor;
	private String primeiroTextoLink;
	
	private String primeiraImagem;
	private String nomeAutorPrimeiraImagem;

	private String linhasCor;
	private Boolean exibir;
	private String tipo;



	public PrimeiraSecaoDTO(PrimeiraSecao obj) {
		this.id = obj.getId();
		this.indice = obj.getIndice();
		this.primeiroTitulo = obj.getPrimeiroTitulo();
		this.primeiroTituloCor = obj.getPrimeiroTituloCor();
		this.primeiroTituloLink = obj.getPrimeiroTituloLink();
		
		this.primeiroTexto = obj.getPrimeiroTexto();
		this.primeiroTextoCor = obj.getPrimeiroTextoCor();
		this.primeiroTextoLink = obj.getPrimeiroTextoLink();
		
		this.primeiraImagem = obj.getPrimeiraImagem();
		this.nomeAutorPrimeiraImagem = obj.getNomeAutorPrimeiraImagem();
		
		this.linhasCor = obj.getLinhasCor();
		this.exibir = obj.getExibir();
	}	
	
	public PrimeiraSecaoDTO(Long id, Long indice, String primeiroTitulo, Boolean exibir, String tipo) {
		this.id = id;
		this.indice = indice;
		this.primeiroTitulo = primeiroTitulo;
		this.exibir = exibir;
		this.tipo = tipo;
	}	
	
	public PrimeiraSecao toEntityInsert() {
		PrimeiraSecao objToInsert = new PrimeiraSecao();
		objToInsert.setIndice(this.indice);
		objToInsert.setPrimeiroTitulo(this.primeiroTitulo);
		objToInsert.setPrimeiroTituloCor(this.primeiroTituloCor);
		objToInsert.setPrimeiroTituloLink(this.primeiroTituloLink);
		
		objToInsert.setPrimeiroTexto(this.primeiroTexto);
		objToInsert.setPrimeiroTextoCor(this.primeiroTextoCor);
		objToInsert.setPrimeiroTextoLink(this.primeiroTextoLink);
		
		objToInsert.setPrimeiraImagem(this.primeiraImagem);
		objToInsert.setNomeAutorPrimeiraImagem(this.nomeAutorPrimeiraImagem);

		objToInsert.setLinhasCor(this.linhasCor);
		objToInsert.setExibir(this.exibir);
		return objToInsert;
	}
	
	public PrimeiraSecao toEntityUpdate(PrimeiraSecao objRef) {
		objRef.setIndice(this.indice);
		objRef.setPrimeiroTitulo(this.primeiroTitulo);
		objRef.setPrimeiroTituloCor(this.primeiroTituloCor);
		objRef.setPrimeiroTituloLink(this.primeiroTituloLink);
		
		objRef.setPrimeiroTexto(this.primeiroTexto);
		objRef.setPrimeiroTextoCor(this.primeiroTextoCor);
		objRef.setPrimeiroTextoLink(this.primeiroTextoLink);
		
		objRef.setPrimeiraImagem(this.primeiraImagem);
		objRef.setNomeAutorPrimeiraImagem(this.nomeAutorPrimeiraImagem);
		
		objRef.setLinhasCor(this.linhasCor);
		objRef.setExibir(this.exibir);
		
		return objRef;
	}
	
    public static PrimeiraSecaoDTO create(PrimeiraSecao primeiraSecao) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(primeiraSecao, PrimeiraSecaoDTO.class);
    }

}
