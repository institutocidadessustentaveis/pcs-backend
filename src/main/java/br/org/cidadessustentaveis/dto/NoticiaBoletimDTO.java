package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class NoticiaBoletimDTO {

	private Long id;

	private String  titulo;

	private String autor;
	
	private String usuario;

	private String url;

	private LocalDateTime dataHoraCriacao;
	
	private LocalDateTime dataHoraPublicacao;

	public NoticiaBoletimDTO(Noticia noticia) {
		this.id = noticia.getId();
		this.titulo = noticia.getTitulo();
		this.autor = noticia.getAutor();
		this.usuario = noticia.getUsuario();
		this.url = noticia.getUrl();
		this.dataHoraCriacao = noticia.getDataHoraCriacao();
		this.dataHoraPublicacao = noticia.getDataHoraPublicacao();
	}
	
	public NoticiaBoletimDTO(Long id, String titulo, String autor, String usuario, String url, LocalDateTime dataHoraCriacao, LocalDateTime dataHoraPublicacao) {
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.usuario = usuario;
		this.url = url;
		this.dataHoraCriacao = dataHoraCriacao;
		this.dataHoraPublicacao = dataHoraPublicacao;
	} 

	public Noticia toEntity() {		
		Noticia noticia = Noticia.builder()
				.id(this.id)
				.titulo(this.titulo)
				.autor(this.autor)
				.usuario(this.usuario)
				.url(this.url)
				.dataHoraCriacao(this.dataHoraCriacao)
				.dataHoraPublicacao(this.dataHoraPublicacao)
				.build();
		return noticia;
	}

}
