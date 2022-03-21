package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;

import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BibliotecaSimplesDTO {

	private Long id;
	
	private String autor;
	
	private Long cidade;
	
	private LocalDate dataPublicacao;
	
	private String idioma;
	
	private String instituicao;
	
	private Long paisPublicacao;
	
	private String palavraChave;
	
	private String descricao;
	
	private String subtitulo;
	
	private String tipoMaterial;
	
	private String tituloPublicacao;
	
	private Long estado;

	private boolean possuiImagem = false;
	
	public BibliotecaSimplesDTO(Biblioteca biblioteca) {
		this.id = biblioteca.getId();
		this.autor = biblioteca.getAutor();
		this.cidade = biblioteca.getCidade() != null ? biblioteca.getCidade().getId() : null;
		this.dataPublicacao = biblioteca.getDataPublicacao();
		this.idioma = biblioteca.getIdioma();
		this.instituicao = biblioteca.getInstituicao();
		this.paisPublicacao = biblioteca.getPaisPublicacao() != null ? biblioteca.getPaisPublicacao().getId() : null;
		this.palavraChave = biblioteca.getPalavraChave();
		this.descricao = biblioteca.getDescricao();
		this.subtitulo = biblioteca.getSubtitulo();
		this.tipoMaterial = biblioteca.getTipoMaterial();
		this.tituloPublicacao = biblioteca.getTituloPublicacao();
		this.estado = biblioteca.getEstado() != null ?  biblioteca.getEstado().getId(): null;
		this.possuiImagem = biblioteca.getImagemCapa() != null ? true : false;
	}

	
}