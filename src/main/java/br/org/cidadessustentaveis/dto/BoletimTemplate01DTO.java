package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.noticias.BoletimTemplate01;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class BoletimTemplate01DTO {
	
	private Long id;
	
	private String titulo;
	
	private String textoIntroducao;
	private String tituloPrimeiroBanner;
	private String textoPrimeiroBanner;
	private String textoBotao01;
	private ArquivoDTO imagemPrimeiroBanner;
	
	private String tituloChamada01;
	private String subtituloChamada01;
	private String textoChamada01;
	private String tituloChamada02;
	private String subtituloChamada02;
	private String textoChamada02;
	
	private ArquivoDTO imagemPrincipal;
	private String tituloImagemPrincipal;
	private String legendaImagemPrincipal;
	
	private String tituloChamada03;
	private String subtituloChamada03;
	private String textoChamada03;
	private String tituloChamada04;
	private String subtituloChamada04;
	private String textoChamada04;
	
	private ArquivoDTO imagemSegundoBanner;
	private String tituloSegundoBanner;
	private String textoSegundoBanner;
	private String textoBotao02;
	
	private String textoFinal;
	private String urlAPI;
	
	public BoletimTemplate01 toEntityInsert(BoletimTemplate01DTO boletimTemplate) {
		return new BoletimTemplate01(
				null,
				titulo,
				textoIntroducao, tituloPrimeiroBanner, textoPrimeiroBanner, textoBotao01, imagemPrimeiroBanner != null ? imagemPrimeiroBanner.toEntityInsert() : null,
				tituloChamada01, subtituloChamada01, textoChamada01, tituloChamada02, subtituloChamada02, textoChamada02,
				imagemPrincipal != null ? imagemPrincipal.toEntityInsert() : null, tituloImagemPrincipal, legendaImagemPrincipal,
				tituloChamada03, subtituloChamada03, textoChamada03, tituloChamada04, subtituloChamada04, textoChamada04,
				imagemSegundoBanner != null ? imagemSegundoBanner.toEntityInsert() : null, tituloSegundoBanner, textoSegundoBanner, textoBotao02,
				textoFinal);
	}
	
	public BoletimTemplate01 toEntityUpdate(BoletimTemplate01 boletim) {
		boletim.setTitulo(this.titulo);
		boletim.setTextoIntroducao(this.textoIntroducao);
		boletim.setTituloPrimeiroBanner(this.tituloPrimeiroBanner);
		boletim.setTextoPrimeiroBanner(this.textoPrimeiroBanner);
		boletim.setTextoBotao01(this.textoBotao01);
		boletim.setImagemPrimeiroBanner(this.imagemPrimeiroBanner != null ? this.imagemPrimeiroBanner.toEntityInsert() : null);
		boletim.setTituloChamada01(this.tituloChamada01);
		boletim.setSubtituloChamada01(this.subtituloChamada01);
		boletim.setTextoChamada01(this.textoChamada01);
		boletim.setTituloChamada02(this.tituloChamada02);
		boletim.setSubtituloChamada02(this.subtituloChamada02);
		boletim.setTextoChamada02(this.textoChamada02);
		boletim.setImagemPrincipal(this.imagemPrincipal != null ? this.imagemPrincipal.toEntityInsert() : null);
		boletim.setTituloImagemPrincipal(this.tituloImagemPrincipal);
		boletim.setLegendaImagemPrincipal(this.legendaImagemPrincipal);
		boletim.setTituloChamada03(this.tituloChamada03);
		boletim.setSubtituloChamada03(this.subtituloChamada03);
		boletim.setTextoChamada03(this.textoChamada03);
		boletim.setTituloChamada04(this.tituloChamada04);
		boletim.setSubtituloChamada04(this.subtituloChamada04);
		boletim.setTextoChamada04(this.textoChamada04);
		boletim.setImagemSegundoBanner(this.imagemSegundoBanner != null ? this.imagemSegundoBanner.toEntityInsert() : null);
		boletim.setTituloSegundoBanner(this.tituloSegundoBanner);
		boletim.setTextoSegundoBanner(this.textoSegundoBanner);
		boletim.setTextoBotao02(this.textoBotao02);
		boletim.setTextoFinal(this.textoFinal);
		
		return boletim;
	}

	public BoletimTemplate01DTO(BoletimTemplate01 boletim) {
		this.id = boletim.getId();
		this.titulo = boletim.getTitulo();
		this.textoIntroducao = boletim.getTextoIntroducao();
		this.tituloPrimeiroBanner = boletim.getTituloPrimeiroBanner();
		this.textoPrimeiroBanner = boletim.getTextoPrimeiroBanner();
		this.textoBotao01 = boletim.getTextoBotao01();
		this.imagemPrimeiroBanner = boletim.getImagemPrimeiroBanner() != null ? new ArquivoDTO(boletim.getImagemPrimeiroBanner()) : null;
		this.tituloChamada01 = boletim.getTituloChamada01();
		this.subtituloChamada01 = boletim.getSubtituloChamada01();
		this.textoChamada01 = boletim.getTextoChamada01();
		this.tituloChamada02 = boletim.getTituloChamada02();
		this.subtituloChamada02 = boletim.getSubtituloChamada02();
		this.textoChamada02 = boletim.getTextoChamada02();
		this.imagemPrincipal = boletim.getImagemPrincipal() != null ? new ArquivoDTO(boletim.getImagemPrincipal()) : null;
		this.tituloImagemPrincipal = boletim.getTituloImagemPrincipal();
		this.legendaImagemPrincipal = boletim.getLegendaImagemPrincipal();
		this.tituloChamada03 = boletim.getTituloChamada03();
		this.subtituloChamada03 = boletim.getSubtituloChamada03();
		this.textoChamada03 = boletim.getTextoChamada03();
		this.tituloChamada04 = boletim.getTituloChamada04();
		this.subtituloChamada04 = boletim.getSubtituloChamada04();
		this.textoChamada04 = boletim.getTextoChamada04();
		this.imagemSegundoBanner = boletim.getImagemSegundoBanner() != null ? new ArquivoDTO(boletim.getImagemSegundoBanner()) : null;
		this.tituloSegundoBanner = boletim.getTituloSegundoBanner();
		this.textoSegundoBanner =boletim. getTextoSegundoBanner();
		this.textoBotao02 = boletim.getTextoBotao02();
		this.textoFinal = boletim.getTextoFinal();
	}

	public BoletimTemplate01DTO(Long id, String titulo, String textoIntroducao, String tituloPrimeiroBanner,
			String textoPrimeiroBanner, String textoBotao01, ArquivoDTO imagemPrimeiroBanner, String tituloChamada01,
			String subtituloChamada01, String textoChamada01, String tituloChamada02, String subtituloChamada02,
			String textoChamada02, ArquivoDTO imagemPrincipal, String tituloImagemPrincipal,
			String legendaImagemPrincipal, String tituloChamada03, String subtituloChamada03, String textoChamada03,
			String tituloChamada04, String subtituloChamada04, String textoChamada04, ArquivoDTO imagemSegundoBanner,
			String tituloSegundoBanner, String textoSegundoBanner, String textoBotao02, String textoFinal) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.textoIntroducao = textoIntroducao;
		this.tituloPrimeiroBanner = tituloPrimeiroBanner;
		this.textoPrimeiroBanner = textoPrimeiroBanner;
		this.textoBotao01 = textoBotao01;
		this.imagemPrimeiroBanner = imagemPrimeiroBanner;
		this.tituloChamada01 = tituloChamada01;
		this.subtituloChamada01 = subtituloChamada01;
		this.textoChamada01 = textoChamada01;
		this.tituloChamada02 = tituloChamada02;
		this.subtituloChamada02 = subtituloChamada02;
		this.textoChamada02 = textoChamada02;
		this.imagemPrincipal = imagemPrincipal;
		this.tituloImagemPrincipal = tituloImagemPrincipal;
		this.legendaImagemPrincipal = legendaImagemPrincipal;
		this.tituloChamada03 = tituloChamada03;
		this.subtituloChamada03 = subtituloChamada03;
		this.textoChamada03 = textoChamada03;
		this.tituloChamada04 = tituloChamada04;
		this.subtituloChamada04 = subtituloChamada04;
		this.textoChamada04 = textoChamada04;
		this.imagemSegundoBanner = imagemSegundoBanner;
		this.tituloSegundoBanner = tituloSegundoBanner;
		this.textoSegundoBanner = textoSegundoBanner;
		this.textoBotao02 = textoBotao02;
		this.textoFinal = textoFinal;
	}
	
	
	

}
