package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.home.Home;
import br.org.cidadessustentaveis.model.home.HomeImagem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HomeDTO implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo Link da Home não pode ser nulo")
	private String link_pagina;
	
	@NotNull(message="Campo titulo da Home não pode ser nulo")
	private String titulo;
		
	private List<HomeImagemDTO> galeriaDeImagens;

//	private PrimeiraSecaoDTO primeiraSecao;
	
//	private List<SegundaSecaoDTO> listaSegundaSecao;
	
//	private TerceiraSecaoDTO terceiraSecao;
	
	private QuartaSecaoDTO quartaSecao;
	
	private QuintaSecaoDTO quintaSecao;
	
	private SecaoLateralDTO secaoLateral;
	
	private HomeBarraDTO homeBarra;
	
	private Boolean exibir;
	
	public HomeDTO(Home obj) {
		this.id = obj.getId();
		this.link_pagina = obj.getLink_pagina();
		this.titulo = obj.getTitulo();
		this.exibir = obj.getExibir();
		List<HomeImagemDTO> imagensGaleria = new ArrayList<>();
		for (HomeImagem imagem : obj.getImagens()) {
			imagensGaleria.add(new HomeImagemDTO(imagem));
		}
		this.setGaleriaDeImagens(imagensGaleria.isEmpty()?null:imagensGaleria);
		
		if(obj.getHomeBarra() != null) {
			this.homeBarra = new HomeBarraDTO(obj.getHomeBarra()); 
		}
		
//		this.primeiraSecao = obj.getPrimeiraSecao() != null ? new PrimeiraSecaoDTO(obj.getPrimeiraSecao()) : null;
		
//		this.segundaSecao = obj.getSegundaSecao() != null ? new SegundaSecaoDTO(obj.getSegundaSecao()) : null;
		
//		this.terceiraSecao = obj.getTerceiraSecao() != null ? new TerceiraSecaoDTO(obj.getTerceiraSecao()) : null;
		
		this.quartaSecao = obj.getQuartaSecao() != null ? new QuartaSecaoDTO(obj.getQuartaSecao()) : null;
		
		this.quintaSecao = obj.getQuintaSecao() != null ? new QuintaSecaoDTO(obj.getQuintaSecao()) : null;
		
		this.quintaSecao = obj.getQuintaSecao() != null ? new QuintaSecaoDTO(obj.getQuintaSecao()) : null;
		
		this.secaoLateral = obj.getSecaoLateral() != null ? new SecaoLateralDTO(obj.getSecaoLateral()) : null;
	}
	
	public HomeDTO(Long id, String titulo, String subtitulo,String link_pagina) {
		this.id = id;
		this.link_pagina = link_pagina;
		this.titulo = titulo;
	}
	
	public Home toEntityInsert() {
		Home objToInsert = new Home();
		objToInsert.setLink_pagina(this.link_pagina);
		objToInsert.setTitulo(this.titulo);
		objToInsert.setExibir(this.exibir);
		List<HomeImagem> imagensGaleria = new ArrayList<>();
		for (HomeImagemDTO imagemDTO : this.getGaleriaDeImagens()) {
			HomeImagem homeImagemAux = imagemDTO.toEntityInsert();
			homeImagemAux.setHome(objToInsert);
			imagensGaleria.add(homeImagemAux);
		}
		objToInsert.setImagens(imagensGaleria.isEmpty()?null:imagensGaleria);
		objToInsert.setHomeBarra(this.homeBarra != null ? this.homeBarra.toEntityInsert() : null);
//		objToInsert.setPrimeiraSecao(this.primeiraSecao != null ? this.primeiraSecao.toEntityInsert() : null);
//		objToInsert.setSegundaSecao(this.segundaSecao != null ? this.segundaSecao.toEntityInsert() : null);
//		objToInsert.setTerceiraSecao(this.terceiraSecao != null ? this.terceiraSecao.toEntityInsert() : null);
		objToInsert.setQuartaSecao(this.quartaSecao != null ? this.quartaSecao.toEntityInsert() : null);
		objToInsert.setQuintaSecao(this.quintaSecao != null ? this.quintaSecao.toEntityInsert() : null);
		objToInsert.setSecaoLateral(this.secaoLateral != null ? this.secaoLateral.toEntityInsert() : null);
		
		return objToInsert;
	}
	
	public Home toEntityUpdate(Home objRef) {
		 objRef.setLink_pagina(this.link_pagina);
		 objRef.setTitulo(this.titulo);
		 objRef.setExibir(this.exibir);
		 if(objRef.getHomeBarra() != null) {
			 objRef.setHomeBarra(this.homeBarra.toEntityUpdate(objRef.getHomeBarra())); 
		 }else {
			 objRef.setHomeBarra(this.homeBarra.toEntityInsert()); 
		 }
		return objRef;
	}
	

	
	public HomeDTO(Long idHome, String link_pagina, String titulo, Home home) {
		this.id = idHome;
		this.link_pagina = link_pagina;
		this.titulo = titulo;
		this.exibir = home.getExibir();
		if(home.getHomeBarra() != null) {
			this.homeBarra = new HomeBarraDTO(); 
			this.homeBarra.setId(home.getHomeBarra().getId());
		}
		
//		if(home.getPrimeiraSecao() != null) {
//			this.primeiraSecao = new PrimeiraSecaoDTO(); 
//			this.primeiraSecao.setId(home.getPrimeiraSecao().getId());
//		}
		
//		if(home.getSegundaSecao() != null) {
//			this.segundaSecao = new SegundaSecaoDTO(); 
//			this.segundaSecao.setId(home.getSegundaSecao().getId());
//		}
		
//		if(home.getTerceiraSecao() != null) {
//			this.terceiraSecao = new TerceiraSecaoDTO(); 
//			this.terceiraSecao.setId(home.getTerceiraSecao().getId());
//		}
		
		if(home.getQuartaSecao() != null) {
			this.quartaSecao = new QuartaSecaoDTO(); 
			this.quartaSecao.setId(home.getQuartaSecao().getId());
		}
		
		if(home.getQuintaSecao() != null) {
			this.quintaSecao = new QuintaSecaoDTO(); 
			this.quintaSecao.setId(home.getQuintaSecao().getId());
		}
		
		if(home.getSecaoLateral() != null) {
			this.secaoLateral = new SecaoLateralDTO(); 
			this.secaoLateral.setId(home.getSecaoLateral().getId());
		}
		
	}
	
	public HomeDTO(Long id, String link_pagina , String titulo, Boolean exibir) {
		this.id = id;
		this.link_pagina = link_pagina;
		this.titulo = titulo;
		this.exibir = exibir;
	}


}
