package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.institucional.Institucional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InstitucionalDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo Link da página não pode ser nulo")
	private String link_pagina;
	
	@NotNull(message="Campo titulo do institucional não pode ser nulo")
	private String titulo;
	
	private String caminhoMigalhas;

	@NotNull(message="Campo subtitulo do institucional não pode ser nulo")
	private String subtitulo;
	
	private String txtBotaoSubtitulo;
	
	private String linkBotaoSubtitulo;
	
	private String tipoTemplate;
	
	private String imagemPrincipal;

	private TemplateInstitucional01DTO template01;
	
	private TemplateInstitucional02DTO template02;
	
	private TemplateInstitucional03DTO template03;
	
	private TemplateInstitucional04DTO template04;
	
	private String nomeAutor;
	
	private boolean possuiFiltro;
	
	
	public InstitucionalDTO(Institucional obj) {
		this.id = obj.getId();
		this.link_pagina = obj.getLink_pagina();
		this.titulo = obj.getTitulo();
		this.caminhoMigalhas = obj.getCaminhoMigalhas();
		this.subtitulo = obj.getSubtitulo();
		this.txtBotaoSubtitulo = obj.getTxtBotaoSubtitulo();
		this.linkBotaoSubtitulo = obj.getLinkBotaoSubtitulo();
		this.tipoTemplate = obj.getTipoTemplate();
		this.imagemPrincipal = obj.getImagemPrincipal();
		this.template01 = obj.getTemplate01() != null ? new TemplateInstitucional01DTO(obj.getTemplate01()) : null;
		this.template02 = obj.getTemplate02() != null ? new TemplateInstitucional02DTO(obj.getTemplate02()) : null;
		this.template03 = obj.getTemplate03() != null ? new TemplateInstitucional03DTO(obj.getTemplate03()) : null;
		this.template04 = obj.getTemplate04() != null ? new TemplateInstitucional04DTO(obj.getTemplate04()) : null;
		this.possuiFiltro = obj.getPossuiFiltro() != null ? obj.getPossuiFiltro() : false;
		this.nomeAutor = obj.getNomeAutor();
	}
	
	public InstitucionalDTO(Long idInstitucional, String titulo, String subtitulo,String link_pagina) {
		this.id = idInstitucional;
		this.link_pagina = link_pagina;
		this.titulo = titulo;
		this.subtitulo = subtitulo;
	}
	
	public Institucional toEntityInsert() {
		Institucional objToInsert = new Institucional();
		
		objToInsert.setLink_pagina(this.link_pagina);
		objToInsert.setTitulo(this.titulo);
		objToInsert.setCaminhoMigalhas(this.caminhoMigalhas);
		objToInsert.setSubtitulo(this.subtitulo);
		objToInsert.setTxtBotaoSubtitulo(this.txtBotaoSubtitulo);
		objToInsert.setLinkBotaoSubtitulo(this.linkBotaoSubtitulo);
		objToInsert.setImagemPrincipal(this.imagemPrincipal);
		objToInsert.setTipoTemplate(this.tipoTemplate);
		objToInsert.setTemplate01(this.template01 != null ? this.template01.toEntityInsert() : null);
		objToInsert.setTemplate02(this.template02 != null ? this.template02.toEntityInsert() : null);
		objToInsert.setTemplate03(this.template03 != null ? this.template03.toEntityInsert() : null);
		objToInsert.setTemplate04(this.template04 != null ? this.template04.toEntityInsert() : null);
		objToInsert.setNomeAutor(this.nomeAutor);
		objToInsert.setPossuiFiltro(this.possuiFiltro);
		
		return objToInsert;
	}
	
	public Institucional toEntityUpdate(Institucional objRef) {
		 objRef.setLink_pagina(this.link_pagina);
		 objRef.setTitulo(this.titulo);
		 objRef.setCaminhoMigalhas(this.caminhoMigalhas);
		 objRef.setSubtitulo(this.subtitulo);
		 objRef.setTxtBotaoSubtitulo(this.txtBotaoSubtitulo);
		 objRef.setLinkBotaoSubtitulo(this.linkBotaoSubtitulo);
		 objRef.setTipoTemplate(this.tipoTemplate);
		 objRef.setImagemPrincipal(this.imagemPrincipal);
		 objRef.setNomeAutor(this.nomeAutor);
		 objRef.setPossuiFiltro(this.possuiFiltro);
		 
		return objRef;
	}

}
