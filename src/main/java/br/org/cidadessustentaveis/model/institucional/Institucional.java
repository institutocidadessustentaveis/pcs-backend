package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;

import javax.persistence.*;

import br.org.cidadessustentaveis.model.administracao.MenuPagina;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="institucional")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class Institucional implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "institucional_id_seq")
	@SequenceGenerator(name = "institucional_id_seq", sequenceName = "institucional_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;

	@Column(name="link_pagina")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String link_pagina;

	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String titulo;
	
	@Column(name="caminho_migalhas")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String caminhoMigalhas;
	
	@Column(name="subtitulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String subtitulo;
	
	@Column(name="txt_botao_subtitulo")
	private String txtBotaoSubtitulo;
	
	@Column(name="link_botao_subtitulo")
	private String linkBotaoSubtitulo;
	
	@Column(name="imagem_principal")
	private String imagemPrincipal;
	
	@Column(name="tipo_template")
	private String tipoTemplate;

	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true )
	@JoinColumn(name="template01", nullable=false)
	@IndexedEmbedded(includePaths = {"tituloPrimeiraSecao","textoPrimeiraSecao","tituloSegundaSecao","txtSegundaSecao"})
	@JsonManagedReference
	private TemplateInstitucional01 template01;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true)
	@JoinColumn(name="template02", nullable=false)
	@IndexedEmbedded(includePaths = {"tituloPrimeiraSecao","textoPrimeiraSecao","tituloSegundaSecao","txtSegundaSecao",
			"tituloTerceiraSecao","txtTerceiraSecao","tituloQuartaSecao","txtQuartaSecao"})
	@JsonManagedReference
	private TemplateInstitucional02 template02;	
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true)
	@JoinColumn(name="template03", nullable=false)
	@IndexedEmbedded(includePaths = {"tituloPrimeiraSecao","textoPrimeiraSecao","tituloSegundaSecao"})
	@JsonManagedReference
	private TemplateInstitucional03 template03;	
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true)
	@JoinColumn(name="template04", nullable=false)
	@JsonManagedReference
	private TemplateInstitucional04 template04;	
	
	@Column(name="autor")
	private String nomeAutor;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true)
	@JoinColumn(name = "menu_pagina")
	@JsonManagedReference
	private MenuPagina menuPagina;
	
	@Column(name="possui_filtro")
	private Boolean possuiFiltro;
}
