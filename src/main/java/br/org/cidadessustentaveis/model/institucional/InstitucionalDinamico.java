package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.MenuPagina;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="institucional_dinamico")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed		
public class InstitucionalDinamico implements Serializable{

	private static final long serialVersionUID = 1L;	
	
	@Id @GeneratedValue(generator = "institucional_dinamico_id_seq")
	@SequenceGenerator(name = "institucional_dinamico_id_seq", sequenceName = "institucional_dinamico_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;

	@Column(name="link_pagina")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String link_pagina;
	
	@Column(name="caminho_migalhas")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String caminhoMigalhas;	

	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String titulo;	
	
	@Column(name="txt_titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String txtTitulo;
			
	@Column(name="txt_subtitulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String txtSubtitulo;	
	
	@Column(name="txt_botao_subtitulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String txtBotaoSubtitulo;	
	
	@Column(name="link_botao_subtitulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String linkBotaoSubtitulo;	
	
	@Column(name="nome_autor")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String nomeAutor;	

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "menu_pagina")
	private MenuPagina menuPagina;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="institucional", cascade=CascadeType.ALL)
	@JsonManagedReference
	@OrderBy("id")
	private List<InstitucionalDinamicoImagem> imagens;
	
	@Column(name="exibir")
	private Boolean exibir;
	
	@Column(name="cor_fundo_subtitulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	private String corFundoSubtitulo;
	
}
