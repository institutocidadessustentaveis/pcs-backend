package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class BuscaItemDTO {

	private Long id;

	private String  label;

	private String url;


	public BuscaItemDTO(Long id, String titulo,String url) {
		this.id = id;
		this.label = titulo;
		this.url = url;
	}

	public Noticia toEntity() {		
		Noticia noticia = Noticia.builder()
				.id(this.id)
				.titulo(this.label)
				.url(this.url)
				.build();
		return noticia;
	}

}
