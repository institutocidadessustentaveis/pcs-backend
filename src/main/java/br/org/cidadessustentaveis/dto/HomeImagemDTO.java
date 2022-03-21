package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.home.HomeImagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HomeImagemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    private String tipo;
    private String extensao;
    private String conteudo;
    private String nomeAutor;
    private String titulo;
    private String subtitulo;
    private String link;
    private Long indice;
    private Boolean exibirBusca;
    
    
    public HomeImagemDTO(HomeImagem homeImagemRef) {
    	this.id = homeImagemRef.getId();
    	this.tipo = homeImagemRef.getTipo();	
    	this.extensao = homeImagemRef.getExtensao();
    	this.conteudo = homeImagemRef.getConteudo();
    	this.nomeAutor = homeImagemRef.getNomeAutor();
    	this.titulo = homeImagemRef.getTitulo();
    	this.subtitulo = homeImagemRef.getSubtitulo();
    	this.link = homeImagemRef.getLink();
    	this.indice = homeImagemRef.getIndice();
    	this.exibirBusca = homeImagemRef.getExibirBusca();
    }
    
    public HomeImagemDTO(Long id, String tipo, String extensao, String nomeAutor, String titulo, String subtitulo, String link, Long indice, Boolean exibirBusca) {
    	this.id = id;
    	this.tipo = tipo;	
    	this.extensao = extensao;
    	this.nomeAutor = nomeAutor;
    	this.titulo = titulo;
    	this.subtitulo = subtitulo;
    	this.link = link;
    	this.indice = indice;
    	this.exibirBusca = exibirBusca;
    }
    
	public HomeImagem toEntityInsert() {
		return new HomeImagem(null, this.extensao, this.conteudo, this.tipo, this.nomeAutor, this.titulo, this.subtitulo, this.link, null, this.indice, this.exibirBusca);
	}
	
}