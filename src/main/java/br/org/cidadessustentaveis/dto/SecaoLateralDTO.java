package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import br.org.cidadessustentaveis.model.home.SecaoLateral;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SecaoLateralDTO implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long indice;
	
	private String primeiroTituloPrincipal;
	private String primeiroTituloPrincipalCor;
	private String primeiroTituloPrincipalLink;
	private Boolean primeiroTituloPrincipalExibir;
	
	private String primeiraImagem;
	private String primeiraImagemLink;
	private String primeiraImagemTooltip;
	
	private String primeiroTitulo;
	private String primeiroTituloCor;
	private String primeiroTituloLink;
	
	private String primeiroTexto;
	private String primeiroTextoCor;
	private String primeiroTextoLink;
	
	
	private String segundaImagem;
	private String segundaImagemLink;
	private String segundaImagemTooltip;
		
	private String segundoTitulo;
	private String segundoTituloCor;
	private String segundoTituloLink;
	
	private String segundoTexto;
	private String segundoTextoCor;
	private String segundoTextoLink;
	
	
	private String segundoTituloPrincipal;
	private String segundoTituloPrincipalCor;
	private String segundoTituloPrincipalLink;
	private Boolean segundoTituloPrincipalExibir;
	
	private String itemTituloCor;
	private String itemTextoCor;
	
	
	private String primeiroItemTitulo;
	private String primeiroItemTexto;
	private String primeiroItemLink;
	
	private String segundoItemTitulo;
	private String segundoItemTexto;
	private String segundoItemLink;

	private String terceiroItemTitulo;
	private String terceiroItemTexto;
	private String terceiroItemLink;

	private String quartoItemTitulo;
	private String quartoItemTexto;
	private String quartoItemLink;
	
	
	private String terceiraImagem;
	private String terceiraImagemLink;
	private String terceiraImagemTooltip;
	
	private String terceiroTitulo;
	private String terceiroTituloCor;
	private String terceiroTituloLink;
	
	private String terceiroTexto;
	private String terceiroTextoCor;
	private String terceiroTextoLink;
	
	private String tipo;
	
	private Boolean exibir;
	private Boolean exibirBanner1;
	private Boolean exibirBanner2;
	private Boolean exibirBanner3;
	private Boolean exibirBanner4;
	
	public SecaoLateralDTO(Long id, Long indice, String primeirotituloPrincipal, Boolean exibir, String tipo) {
		this.id = id;
		this.indice = indice;
		this.primeiroTituloPrincipal = primeirotituloPrincipal;
		this.exibir = exibir;
		this.tipo = tipo;
	}	


	public SecaoLateralDTO(SecaoLateral obj) {
		this.id = obj.getId();
		this.indice = obj.getIndice();
		this.primeiroTituloPrincipal = obj.getPrimeiroTituloPrincipal();
		this.primeiroTituloPrincipalCor = obj.getPrimeiroTituloPrincipalCor();
		this.primeiroTituloPrincipalLink = obj.getPrimeiroTituloPrincipalLink();
		this.primeiroTituloPrincipalExibir = obj.getPrimeiroTituloPrincipalExibir();
		
		this.primeiraImagem = obj.getPrimeiraImagem();
		this.primeiraImagemLink = obj.getPrimeiraImagemLink();
		this.primeiraImagemTooltip = obj.getPrimeiraImagemTooltip();
		
		this.primeiroTitulo = obj.getPrimeiroTitulo();
		this.primeiroTituloCor = obj.getPrimeiroTituloCor();
		this.primeiroTituloLink = obj.getPrimeiroTituloLink();
		
		this.primeiroTexto = obj.getPrimeiroTexto();
		this.primeiroTextoCor = obj.getPrimeiroTextoCor();
		this.primeiroTextoLink = obj.getPrimeiroTextoLink();
		
		
		this.segundaImagem = obj.getSegundaImagem();
		this.segundaImagemLink = obj.getSegundaImagemLink();
		this.segundaImagemTooltip = obj.getSegundaImagemTooltip();
			
		this.segundoTitulo = obj.getSegundoTitulo();
		this.segundoTituloCor = obj.getSegundoTituloCor();
		this.segundoTituloLink = obj.getSegundoTituloLink();
		
		this.segundoTexto = obj.getSegundoTexto();
		this.segundoTextoCor = obj.getSegundoTextoCor();
		this.segundoTextoLink = obj.getSegundoTextoLink();
		
		
		this.segundoTituloPrincipal = obj.getSegundoTituloPrincipal();
		this.segundoTituloPrincipalCor = obj.getSegundoTituloPrincipalCor();
		this.segundoTituloPrincipalLink = obj.getSegundoTituloPrincipalLink();
		this.segundoTituloPrincipalExibir = obj.getSegundoTituloPrincipalExibir();
		
		this.itemTituloCor = obj.getItemTituloCor();
		this.itemTextoCor = obj.getItemTextoCor();
		
		
		this.primeiroItemTitulo = obj.getPrimeiroItemTitulo();
		this.primeiroItemTexto = obj.getPrimeiroItemTexto();
		this.primeiroItemLink = obj.getPrimeiroItemLink();
		
		this.segundoItemTitulo = obj.getSegundoItemTitulo();
		this.segundoItemTexto = obj.getSegundoItemTexto();
		this.segundoItemLink = obj.getSegundoItemLink();

		this.terceiroItemTitulo = obj.getTerceiroItemTitulo();
		this.terceiroItemTexto = obj.getTerceiroItemTexto();
		this.terceiroItemLink = obj.getTerceiroItemLink();

		this.quartoItemTitulo = obj.getQuartoItemTitulo();
		this.quartoItemTexto = obj.getQuartoItemTexto();
		this.quartoItemLink = obj.getQuartoItemLink();
		
		
		this.terceiraImagem = obj.getTerceiraImagem();
		this.terceiraImagemLink = obj.getTerceiraImagemLink();
		this.terceiraImagemTooltip = obj.getTerceiraImagemTooltip();
		
		this.terceiroTitulo = obj.getTerceiroTitulo();
		this.terceiroTituloCor = obj.getTerceiroTituloCor();
		this.terceiroTituloLink = obj.getTerceiroTituloLink();
		
		this.terceiroTexto = obj.getTerceiroTexto();
		this.terceiroTextoCor = obj.getTerceiroTextoCor();
		this.terceiroTextoLink = obj.getTerceiroTextoLink();
		
		this.exibir = obj.getExibir();
		this.exibirBanner1 = obj.getExibirBanner1();
		this.exibirBanner2 = obj.getExibirBanner2();
		this.exibirBanner3 = obj.getExibirBanner3();
		this.exibirBanner4 = obj.getExibirBanner4();

	}	
	
	public SecaoLateral toEntityInsert() {
		SecaoLateral objToInsert = new SecaoLateral();
		objToInsert.setIndice(this.indice);
	
	
		objToInsert.setPrimeiroTituloPrincipal(this.primeiroTituloPrincipal);
		objToInsert.setPrimeiroTituloPrincipalCor(this.primeiroTituloPrincipalCor);
		objToInsert.setPrimeiroTituloPrincipalLink(this.primeiroTituloPrincipalLink);
		objToInsert.setPrimeiroTituloPrincipalExibir(this.primeiroTituloPrincipalExibir);
		
		objToInsert.setPrimeiraImagem(this.primeiraImagem);
		objToInsert.setPrimeiraImagemLink(this.primeiraImagemLink);
		objToInsert.setPrimeiraImagemTooltip(this.primeiraImagemTooltip);
		
		objToInsert.setPrimeiroTitulo(this.primeiroTitulo);
		objToInsert.setPrimeiroTituloCor(this.primeiroTituloCor);
		objToInsert.setPrimeiroTituloLink(this.primeiroTituloLink);
		
		objToInsert.setPrimeiroTexto(this.primeiroTexto);
		objToInsert.setPrimeiroTextoCor(this.primeiroTextoCor);
		objToInsert.setPrimeiroTextoLink(this.primeiroTextoLink);
		
		
		objToInsert.setSegundaImagem(this.segundaImagem);
		objToInsert.setSegundaImagemLink(this.segundaImagemLink);
		objToInsert.setSegundaImagemTooltip(this.segundaImagemTooltip);
			
		objToInsert.setSegundoTitulo(this.segundoTitulo);
		objToInsert.setSegundoTituloCor(this.segundoTituloCor);
		objToInsert.setSegundoTituloLink(this.segundoTituloLink);
	
		
		objToInsert.setSegundoTexto(this.segundoTexto);
		objToInsert.setSegundoTextoCor(this.segundoTextoCor);
		objToInsert.setSegundoTextoLink(this.segundoTextoLink);
		
		
		objToInsert.setSegundoTituloPrincipal(this.segundoTituloPrincipal);
		objToInsert.setSegundoTituloPrincipalCor(this.segundoTituloPrincipalCor);
		objToInsert.setSegundoTituloPrincipalLink(this.segundoTituloPrincipalLink);
		objToInsert.setSegundoTituloPrincipalExibir(this.segundoTituloPrincipalExibir);
		
		objToInsert.setItemTituloCor(this.itemTituloCor);
		objToInsert.setItemTextoCor(this.itemTituloCor);
		
		
		objToInsert.setPrimeiroItemTitulo(this.primeiroItemTitulo);
		objToInsert.setPrimeiroItemTexto(this.primeiroItemTexto);
		objToInsert.setPrimeiroItemLink(this.primeiroItemLink);
		
		objToInsert.setSegundoItemTitulo(this.segundoItemTitulo);
		objToInsert.setSegundoItemTexto(this.segundoItemTexto);
		objToInsert.setSegundoItemLink(this.segundoItemLink);

		objToInsert.setTerceiroItemTitulo(this.terceiroItemTitulo);
		objToInsert.setTerceiroItemTexto(this.terceiroItemTexto);
		objToInsert.setTerceiroItemLink(this.terceiroItemLink);

		objToInsert.setQuartoItemTitulo(this.quartoItemTitulo);
		objToInsert.setQuartoItemTexto(this.quartoItemTexto);
		objToInsert.setQuartoItemLink(this.quartoItemLink);
		
		
		objToInsert.setTerceiraImagem(this.terceiraImagem);
		objToInsert.setTerceiraImagemLink(this.terceiraImagemLink);
		objToInsert.setTerceiraImagemTooltip(this.terceiraImagemTooltip);
		
		objToInsert.setTerceiroTitulo(this.terceiroTitulo);
		objToInsert.setTerceiroTituloCor(this.terceiroTituloCor);
		objToInsert.setTerceiroTituloLink(this.terceiroTituloLink);
		
		objToInsert.setTerceiroTexto(this.terceiroTexto);
		objToInsert.setTerceiroTextoCor(this.terceiroTextoCor);
		objToInsert.setTerceiroTextoLink(this.terceiroTextoLink);
		
		objToInsert.setExibir(this.exibir);
		objToInsert.setExibirBanner1(this.exibirBanner1);
		objToInsert.setExibirBanner2(this.exibirBanner2);
		objToInsert.setExibirBanner3(this.exibirBanner3);
		objToInsert.setExibirBanner4(this.exibirBanner4);
		
		return objToInsert;
	}
	
	public SecaoLateral toEntityUpdate(SecaoLateral objRef) {
		
		objRef.setIndice(this.indice);
		
		objRef.setPrimeiroTituloPrincipal(this.primeiroTituloPrincipal);
		objRef.setPrimeiroTituloPrincipalCor(this.primeiroTituloPrincipalCor);
		objRef.setPrimeiroTituloPrincipalLink(this.primeiroTituloPrincipalLink);
		objRef.setPrimeiroTituloPrincipalExibir(this.primeiroTituloPrincipalExibir);
		
		objRef.setPrimeiraImagem(this.primeiraImagem);
		objRef.setPrimeiraImagemLink(this.primeiraImagemLink);
		objRef.setPrimeiraImagemTooltip(this.primeiraImagemTooltip);
		
		objRef.setPrimeiroTitulo(this.primeiroTitulo);
		objRef.setPrimeiroTituloCor(this.primeiroTituloCor);
		objRef.setPrimeiroTituloLink(this.primeiroTituloLink);
		
		
		objRef.setPrimeiroTexto(this.primeiroTexto);
		objRef.setPrimeiroTextoCor(this.primeiroTextoCor);
		objRef.setPrimeiroTextoLink(this.primeiroTextoLink);
		
		
		objRef.setSegundaImagem(this.segundaImagem);
		objRef.setSegundaImagemLink(this.segundaImagemLink);
		objRef.setSegundaImagemTooltip(this.segundaImagemTooltip);
			
		objRef.setSegundoTitulo(this.segundoTitulo);
		objRef.setSegundoTituloCor(this.segundoTituloCor);
		objRef.setSegundoTituloLink(this.segundoTituloLink);
	
		
		objRef.setSegundoTexto(this.segundoTexto);
		objRef.setSegundoTextoCor(this.segundoTextoCor);
		objRef.setSegundoTextoLink(this.segundoTextoLink);
		
		
		objRef.setSegundoTituloPrincipal(this.segundoTituloPrincipal);
		objRef.setSegundoTituloPrincipalCor(this.segundoTituloPrincipalCor);
		objRef.setSegundoTituloPrincipalLink(this.segundoTituloPrincipalLink);
		objRef.setSegundoTituloPrincipalExibir(this.segundoTituloPrincipalExibir);
		
		objRef.setItemTituloCor(this.itemTituloCor);
		objRef.setItemTextoCor(this.itemTextoCor);
		
		
		objRef.setPrimeiroItemTitulo(this.primeiroItemTitulo);
		objRef.setPrimeiroItemTexto(this.primeiroItemTexto);
		objRef.setPrimeiroItemLink(this.primeiroItemLink);
		
		objRef.setSegundoItemTitulo(this.segundoItemTitulo);
		objRef.setSegundoItemTexto(this.segundoItemTexto);
		objRef.setSegundoItemLink(this.segundoItemLink);

		objRef.setTerceiroItemTitulo(this.terceiroItemTitulo);
		objRef.setTerceiroItemTexto(this.terceiroItemTexto);
		objRef.setTerceiroItemLink(this.terceiroItemLink);

		objRef.setQuartoItemTitulo(this.quartoItemTitulo);
		objRef.setQuartoItemTexto(this.quartoItemTexto);
		objRef.setQuartoItemLink(this.quartoItemLink);
		
		
		objRef.setTerceiraImagem(this.terceiraImagem);
		objRef.setTerceiraImagemLink(this.terceiraImagemLink);
		objRef.setTerceiraImagemTooltip(this.terceiraImagemTooltip);
		
		objRef.setTerceiroTitulo(this.terceiroTitulo);
		objRef.setTerceiroTituloCor(this.terceiroTituloCor);
		objRef.setTerceiroTituloLink(this.terceiroTituloLink);
		
		objRef.setTerceiroTexto(this.terceiroTexto);
		objRef.setTerceiroTextoCor(this.terceiroTextoCor);
		objRef.setTerceiroTextoLink(this.terceiroTextoLink);
		
		objRef.setExibir(this.exibir);
		objRef.setExibirBanner1(this.exibirBanner1);
		objRef.setExibirBanner2(this.exibirBanner2);
		objRef.setExibirBanner3(this.exibirBanner3);
		objRef.setExibirBanner4(this.exibirBanner4);
		
		return objRef;
	}
	
    public static SecaoLateralDTO create(SecaoLateral secaoLateral) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(secaoLateral, SecaoLateralDTO.class);
    }

}
