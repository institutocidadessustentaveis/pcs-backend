package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.noticias.InformacaoLivre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InformacaoLivreDTO {
	
	private Long id;
	
	private String tituloNoticia;
	
	private String url;
	
	public InformacaoLivreDTO (InformacaoLivre informacaoLivre) {
		this.id = informacaoLivre.getId();
		this.tituloNoticia = informacaoLivre.getTituloNoticia();
		this.url = informacaoLivre.getUrl();
	}
	
	public InformacaoLivre toEntityInsert (InformacaoLivreDTO informacaoLivreDTO) {
		InformacaoLivre informacao = new InformacaoLivre().builder().id(this.id)
				.tituloNoticia(this.tituloNoticia)
				.url(this.url).build();
		
		return informacao;
	}

}
