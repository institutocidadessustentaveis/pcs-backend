package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.boaspraticas.ImagemBoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ImagemBoaPraticaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    private String tipo;
    private String extensao;
    private String conteudo;
    private String nomeAutor;
    
    public ImagemBoaPraticaDTO(ImagemBoaPratica imagemBoaPraticaRef) {
    	this.id = imagemBoaPraticaRef.getId();
    	this.tipo = imagemBoaPraticaRef.getTipo();
    	this.extensao = imagemBoaPraticaRef.getExtensao();
    	this.conteudo = imagemBoaPraticaRef.getConteudo();
    	this.nomeAutor = imagemBoaPraticaRef.getNomeAutor();
    }
    
	public ImagemBoaPratica toEntityInsert() {
		return new ImagemBoaPratica(null, this.extensao, this.conteudo, this.tipo, this.nomeAutor, null);
	}
	
}