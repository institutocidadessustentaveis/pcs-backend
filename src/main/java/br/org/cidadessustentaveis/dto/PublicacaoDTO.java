package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.institucional.MaterialInstitucional;
import br.org.cidadessustentaveis.model.institucional.Publicacao;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PublicacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String imagem;
	private String titulo;
	private String texto;
	private String link;
	private Long idImagem;
	private Long idTemplate03;
	private Long idMaterialApoio;
	private Long idMaterialInstitucional;
	private List<Long> idsArquivos;
	private Integer ordemExibicao;
	private String tooltipTitulo;
	private String tooltipTexto;

	public PublicacaoDTO(Publicacao publicacao) {
		this.id = publicacao.getId();
		this.titulo = publicacao.getTitulo();
		this.texto = publicacao.getTexto();
		this.link = publicacao.getLink();
		this.idImagem = publicacao.getImagem() != null ? publicacao.getImagem().getId() : null;
		this.idTemplate03 = publicacao.getTemplate() != null ? publicacao.getTemplate().getId() : null;
		this.imagem = publicacao.getImagem() != null ? publicacao.getImagem().toBase64() : null;
		this.idMaterialApoio = publicacao.getMaterialApoio() != null ?  publicacao.getMaterialApoio().getId(): null;
		this.idMaterialInstitucional = publicacao.getMaterialInstitucional() != null ?  publicacao.getMaterialInstitucional().getId(): null;
		this.tooltipTexto = publicacao.getTooltipTexto();
		this.tooltipTitulo = publicacao.getTooltipTitulo();
		this.ordemExibicao = publicacao.getOrdemExibicao();
	}
	
	public PublicacaoDTO(MaterialInstitucional materialInstitucional, Integer ordemExibicao) {
		this.titulo = materialInstitucional.getTitulo();
		this.texto = materialInstitucional.getSubtitulo();
		this.link = materialInstitucional.getId().toString();
		this.idsArquivos = materialInstitucional.getArquivos() != null ? materialInstitucional.getArquivos().stream().map(Arquivo::getId).collect(Collectors.toList()) : null;
	}
	
	public PublicacaoDTO(MaterialApoio materialApoio, Integer ordemExibicao) {
		this.imagem = materialApoio.getImagemCapa() != null ? materialApoio.getImagemCapa().getConteudo() : null;
		this.titulo = materialApoio.getTitulo();
		this.texto = materialApoio.getSubtitulo();
		this.link = materialApoio.getId().toString();
	}

}