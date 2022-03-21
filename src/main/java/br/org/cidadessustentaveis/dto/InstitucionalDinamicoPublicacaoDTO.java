package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoPublicacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InstitucionalDinamicoPublicacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String imagem;
	private String titulo;
	private String texto;
	private String link;
	private Long idImagem;
	private Long idInstitucionalDinamicoSecao03;	
	private Integer ordemExibicao;
	private String tooltipTitulo;
	private String tooltipTexto;

	public InstitucionalDinamicoPublicacaoDTO(InstitucionalDinamicoPublicacao publicacao) {
		this.id = publicacao.getId();
		this.titulo = publicacao.getTitulo();
		this.texto = publicacao.getTexto();
		this.link = publicacao.getLink();
		this.idImagem = publicacao.getImagem() != null ? publicacao.getImagem().getId() : null;
		this.idInstitucionalDinamicoSecao03 = publicacao.getInstitucionalDinamicoSecao03() != null ? publicacao.getInstitucionalDinamicoSecao03().getId() : null;
		this.imagem = publicacao.getImagem() != null ? publicacao.getImagem().toBase64() : null;
		this.tooltipTexto = publicacao.getTooltipTexto();
		this.tooltipTitulo = publicacao.getTooltipTitulo();
		this.ordemExibicao = publicacao.getOrdemExibicao();
	}
	

}