package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.institucional.MaterialInstitucional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialInstitucionalDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String titulo;
	private String subtitulo;
	private String autor;
	private List<String> linksRelacionados;
	private List<String> tagPalavrasChave;
	private String corpoTexto;
	private Date dtPublicacao;
	private List<ArquivoDTO> arquivos;
	
	public MaterialInstitucionalDTO(MaterialInstitucional materialInstitucionalRef) {
		this.id = materialInstitucionalRef.getId();
		this.titulo = materialInstitucionalRef.getTitulo();
		this.subtitulo = materialInstitucionalRef.getSubtitulo();
		this.autor = materialInstitucionalRef.getAutor();
		this.linksRelacionados = Arrays.asList(materialInstitucionalRef.getLinksRelacionados().split("!&"));
		this.tagPalavrasChave = Arrays.asList(materialInstitucionalRef.getTagPalavrasChave().split("!&"));
		this.corpoTexto = materialInstitucionalRef.getCorpoTexto();
		this.dtPublicacao = materialInstitucionalRef.getDtPublicacao();
		this.arquivos = materialInstitucionalRef.getArquivos().stream().map(arquivo -> new ArquivoDTO(arquivo)).collect(Collectors.toList());
	}
	
	public MaterialInstitucional toEntityInsert() {
		return new MaterialInstitucional(this.id, this.titulo, this.subtitulo, this.autor, this.linksRelacionados.stream().map( item -> item).collect( Collectors.joining( "!&" )), this.tagPalavrasChave.stream().map( item -> item).collect( Collectors.joining( "!&" )), this.corpoTexto, new Date(), this.arquivos.stream().map(arquivo -> arquivo.toEntityInsert()).collect(Collectors.toList()));
	}

	public MaterialInstitucional toEntityUpdate(MaterialInstitucional materialInstitucionalRef) {
		materialInstitucionalRef.setTitulo(this.titulo);
		materialInstitucionalRef.setSubtitulo(this.subtitulo);
		materialInstitucionalRef.setAutor(this.autor);
		materialInstitucionalRef.setLinksRelacionados(this.linksRelacionados.stream().map( item -> item).collect( Collectors.joining( "!&" )));
		materialInstitucionalRef.setTagPalavrasChave(this.tagPalavrasChave.stream().map( item -> item).collect( Collectors.joining( "!&" )));
		materialInstitucionalRef.setCorpoTexto(this.corpoTexto);
		return materialInstitucionalRef;
	}
	
}