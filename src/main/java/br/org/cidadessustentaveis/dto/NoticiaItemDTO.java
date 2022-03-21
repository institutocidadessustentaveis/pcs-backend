package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @Getter @Setter
public class NoticiaItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;	

	private Long idNoticia;
	private String titulo;
	private String subtitulo;
	private String autor;
	private String usuario;
	private LocalDateTime dataHoraCriacao;
	private LocalDateTime dataHoraPublicacao;
	private String palavraChave;
	private String url;
	private String urlImagem;

	public NoticiaItemDTO(Long idNoticia, String titulo, String subtitulo, String autor, String usuario,
						  LocalDateTime dataHoraCriacao, LocalDateTime dataHoraPublicacao, String palavraChave,
						  String url) {
		this.idNoticia = idNoticia;
		this.titulo = titulo;
		this.subtitulo = subtitulo;
		this.autor = autor;
		this.usuario = usuario;
		this.dataHoraCriacao = dataHoraCriacao;
		this.dataHoraPublicacao = dataHoraPublicacao;
		this.palavraChave = palavraChave;
		this.url = url;
	}

	public NoticiaItemDTO( Noticia noticia ) {
		this.idNoticia = noticia.getId();
		this.titulo = noticia.getTitulo();
		this.subtitulo = noticia.getSubtitulo();
		this.autor = noticia.getAutor();
		this.usuario = noticia.getUsuario();
		this.dataHoraCriacao = noticia.getDataHoraCriacao();
		this.dataHoraPublicacao = noticia.getDataHoraPublicacao();
		this.url = noticia.getUrl();
	}
	
	public NoticiaItemDTO(Long idNoticia) {
		this.idNoticia = idNoticia;
	}
}

