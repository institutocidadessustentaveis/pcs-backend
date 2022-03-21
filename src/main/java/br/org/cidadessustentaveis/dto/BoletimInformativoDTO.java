package br.org.cidadessustentaveis.dto;

import java.util.List;

import br.org.cidadessustentaveis.model.noticias.BoletimTemplate01;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoletimInformativoDTO {
	
	private String titulo;
	
	private List<NoticiaInformacaoLivreDTO> noticiasInformacoesLivres;
	
	private BoletimTemplate01DTO boletimTemplate01;
	
	public BoletimInformativoDTO(BoletimTemplate01 boletimTemplate01) {
		this.boletimTemplate01.setId(boletimTemplate01.getId());
		this.boletimTemplate01.setTitulo(boletimTemplate01.getTitulo());
		this.boletimTemplate01.setTextoIntroducao(boletimTemplate01.getTextoIntroducao());
		this.boletimTemplate01.setTituloPrimeiroBanner(boletimTemplate01.getTituloPrimeiroBanner());
		this.boletimTemplate01.setTextoPrimeiroBanner(boletimTemplate01.getTextoPrimeiroBanner());
		this.boletimTemplate01.setTextoBotao01(boletimTemplate01.getTextoBotao01());
		this.boletimTemplate01.setImagemPrimeiroBanner(boletimTemplate01.getImagemPrimeiroBanner() != null ? new ArquivoDTO(boletimTemplate01.getImagemPrimeiroBanner()) : null);
		this.boletimTemplate01.setTituloChamada01(boletimTemplate01.getTituloChamada01());
		this.boletimTemplate01.setSubtituloChamada01(boletimTemplate01.getSubtituloChamada01());
		this.boletimTemplate01.setTextoChamada01(boletimTemplate01.getTextoChamada01());
		this.boletimTemplate01.setTituloChamada02(boletimTemplate01.getTituloChamada02());
		this.boletimTemplate01.setSubtituloChamada02(boletimTemplate01.getSubtituloChamada02());
		this.boletimTemplate01.setTextoChamada02(boletimTemplate01.getTextoChamada02());
		this.boletimTemplate01.setImagemPrincipal(boletimTemplate01.getImagemPrincipal() != null ? new ArquivoDTO(boletimTemplate01.getImagemPrincipal()) : null);
		this.boletimTemplate01.setTituloImagemPrincipal(boletimTemplate01.getTituloImagemPrincipal());
		this.boletimTemplate01.setLegendaImagemPrincipal(boletimTemplate01.getLegendaImagemPrincipal());
		this.boletimTemplate01.setTituloChamada03(boletimTemplate01.getTituloChamada03());
		this.boletimTemplate01.setSubtituloChamada03(boletimTemplate01.getSubtituloChamada03());
		this.boletimTemplate01.setTextoChamada03(boletimTemplate01.getTextoChamada03());
		this.boletimTemplate01.setTituloChamada04(boletimTemplate01.getTituloChamada04());
		this.boletimTemplate01.setSubtituloChamada04(boletimTemplate01.getSubtituloChamada04());
		this.boletimTemplate01.setTextoChamada04(boletimTemplate01.getTextoChamada04());
		this.boletimTemplate01.setImagemSegundoBanner(boletimTemplate01.getImagemSegundoBanner() != null ? new ArquivoDTO(boletimTemplate01.getImagemSegundoBanner()) : null);
		this.boletimTemplate01.setTituloSegundoBanner(boletimTemplate01.getTituloSegundoBanner());
		this.boletimTemplate01.setTextoSegundoBanner(boletimTemplate01.getTextoSegundoBanner());
		this.boletimTemplate01.setTextoBotao02(boletimTemplate01.getTextoBotao02());
	}

}
