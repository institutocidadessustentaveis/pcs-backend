package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.home.HomeImagem;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamico;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoImagem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InstitucionalDinamicoDTO implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo Link da Página não pode ser nulo")
	private String link_pagina;
	
	@NotNull(message="Campo titulo da Página não pode ser nulo")
	private String titulo;
		
	
	private String txtTitulo;
	
	private String caminhoMigalhas;
	
	private String txtSubtitulo;

	private String txtBotaoSubtitulo;
	
	private String linkBotaoSubtitulo;
	
	private String nomeAutor;

	private List<InstitucionalDinamicoImagemDTO> imagens;
	
	private Boolean exibir;
	
	private String corFundoSubtitulo;

	
	public InstitucionalDinamicoDTO(InstitucionalDinamico obj) {
		this.id = obj.getId();
		this.link_pagina = obj.getLink_pagina();
		this.caminhoMigalhas = obj.getCaminhoMigalhas();
		this.titulo = obj.getTitulo();
		this.txtTitulo = obj.getTxtTitulo();
		this.txtSubtitulo = obj.getTxtSubtitulo();
		this.txtBotaoSubtitulo = obj.getTxtBotaoSubtitulo();
		this.linkBotaoSubtitulo = obj.getLinkBotaoSubtitulo();
		this.nomeAutor = obj.getNomeAutor();
		this.exibir = obj.getExibir();
		
		List<InstitucionalDinamicoImagemDTO> imagensGaleria = new ArrayList<>();
		if( obj.getImagens() != null) {
			for (InstitucionalDinamicoImagem imagem : obj.getImagens()) {
				imagensGaleria.add(new InstitucionalDinamicoImagemDTO(imagem));
			}
			imagensGaleria.sort(Comparator.comparing(InstitucionalDinamicoImagemDTO::getIndice));
		}
		this.setImagens(imagensGaleria.isEmpty()?null:imagensGaleria);
		this.corFundoSubtitulo = obj.getCorFundoSubtitulo();
	}
	
	public InstitucionalDinamicoDTO(Long id, String titulo,String link_pagina, Boolean exibir) {
		this.id = id;
		this.link_pagina = link_pagina;
		this.titulo = titulo;
		this.exibir = exibir;
	}
	
	public InstitucionalDinamico toEntityInsert() {
		InstitucionalDinamico objToInsert = new InstitucionalDinamico();
		objToInsert.setLink_pagina(this.link_pagina);
		objToInsert.setTitulo(this.titulo);
		objToInsert.setTxtTitulo(this.txtTitulo);
		objToInsert.setCaminhoMigalhas(this.caminhoMigalhas);
		objToInsert.setTxtSubtitulo(this.txtSubtitulo);
		objToInsert.setTxtBotaoSubtitulo(this.txtBotaoSubtitulo);
		objToInsert.setLinkBotaoSubtitulo(this.linkBotaoSubtitulo);
		objToInsert.setNomeAutor(this.nomeAutor);
		objToInsert.setExibir(this.exibir);
		objToInsert.setCorFundoSubtitulo(this.corFundoSubtitulo);
		
		return objToInsert;
	}
	
	public InstitucionalDinamico toEntityUpdate(InstitucionalDinamico objRef) {
		 objRef.setLink_pagina(this.link_pagina);
		 objRef.setTitulo(this.titulo);
		 objRef.setTxtTitulo(this.txtTitulo);
		 objRef.setCaminhoMigalhas(this.caminhoMigalhas);
		 objRef.setTxtSubtitulo(this.txtSubtitulo);
		 objRef.setTxtBotaoSubtitulo(this.txtBotaoSubtitulo);
		 objRef.setLinkBotaoSubtitulo(this.linkBotaoSubtitulo);
		 objRef.setNomeAutor(this.nomeAutor);
		 objRef.setExibir(this.exibir);
		 objRef.setCorFundoSubtitulo(this.corFundoSubtitulo);
		return objRef;
	}
	




}
