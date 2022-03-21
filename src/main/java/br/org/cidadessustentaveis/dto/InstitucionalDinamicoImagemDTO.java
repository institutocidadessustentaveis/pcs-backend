package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoImagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InstitucionalDinamicoImagemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    private String conteudo;		
	private Long indice;
	private String nomeAutor;
	private String subtitulo;
	private String titulo;
	private String link;
    
    
    public InstitucionalDinamicoImagemDTO(InstitucionalDinamicoImagem imagem) {
    	this.id = imagem.getId();
    	this.conteudo = imagem.toBase64();
    	this.nomeAutor = imagem.getNomeAutor();
    	this.indice = imagem.getIndice();
    	this.subtitulo = imagem.getSubtitulo();
    	this.titulo = imagem.getTitulo();
    	this.link = imagem.getLink();

    }
    
    public InstitucionalDinamicoImagemDTO(Long id) {
    	this.id = id;
    }
    
	public InstitucionalDinamicoImagem toEntityInsert() {
		InstitucionalDinamicoImagem institucionalDinamicoImagem = new InstitucionalDinamicoImagem(this.conteudo);	
		institucionalDinamicoImagem.setNomeAutor(this.nomeAutor);
		institucionalDinamicoImagem.setIndice(this.indice);
		institucionalDinamicoImagem.setSubtitulo(this.subtitulo);
		institucionalDinamicoImagem.setTitulo(this.titulo);
		institucionalDinamicoImagem.setLink(this.link);
		return institucionalDinamicoImagem;
	}
	
}