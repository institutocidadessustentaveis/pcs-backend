package br.org.cidadessustentaveis.model.home;

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
import org.hibernate.search.annotations.IndexedEmbedded;
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
@Table(name="home")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class Home implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "home_id_seq")
	@SequenceGenerator(name = "home_id_seq", sequenceName = "home_id_seq", allocationSize = 1)
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
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="home", cascade=CascadeType.ALL)
	@JsonManagedReference
	@OrderBy("id")
	private List<HomeImagem> imagens;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "menu_pagina")
	private MenuPagina menuPagina;
	
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "home_barra")
	private HomeBarra homeBarra;
	

//	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true )
//	@JoinColumn(name="primeira_secao", nullable=false)
//	@IndexedEmbedded(includePaths = {"primeiroTitulo","primeiroTexto"})
//	@JsonManagedReference
//	private PrimeiraSecao primeiraSecao;
	
//	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true )
//	@JoinColumn(name="segunda_secao", nullable=false)
//	@IndexedEmbedded(includePaths = {"primeiroTitulo","primeiroTexto","segundoTitulo","segundoTexto"})
//	@JsonManagedReference
//	private SegundaSecao segundaSecao;
	
//	@OneToMany(fetch=FetchType.LAZY, mappedBy="home", cascade=CascadeType.ALL)
//	@JsonManagedReference
//	@OrderBy("id")
//	private List<SegundaSecao> listaSegundaSecao;

	
	
//	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true )
//	@JoinColumn(name="terceira_secao", nullable=false)
//	@IndexedEmbedded(includePaths = {"primeiroTitulo","primeiroTexto","segundoTitulo","segundoTexto"})
//	@JsonManagedReference
//	private TerceiraSecao terceiraSecao;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true )
	@JoinColumn(name="quarta_secao", nullable=false)
	@IndexedEmbedded(includePaths = {"tituloPrincipal","primeiroTitulo"})
	@JsonManagedReference
	private QuartaSecao quartaSecao;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true )
	@JoinColumn(name="quinta_secao", nullable=false)
	@IndexedEmbedded(includePaths = {"primeiroTitulo","primeiroSubTitulo","primeiroTexto","segundoTitulo","segundoSubTitulo","segundoTexto"})
	@JsonManagedReference
	private QuintaSecao quintaSecao;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval=true )
	@JoinColumn(name="secao_lateral", nullable=false)
	@JsonManagedReference
	private SecaoLateral secaoLateral;
	
	@Column(name="exibir")
	private Boolean exibir;
}
