package br.org.cidadessustentaveis.model.home;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="home_primeira_secao")	
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class PrimeiraSecao implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "home_primeira_secao_id_seq")
	@SequenceGenerator(name = "home_primeira_secao_id_seq", sequenceName = "home_primeira_secao_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="indice")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private Long indice;
	
	@Column(name="primeiro_titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String primeiroTitulo;
	
	@Column(name = "primeiro_titulo_cor")
	private String primeiroTituloCor;
	
	@Column(name = "primeiro_titulo_link")
	private String primeiroTituloLink;

	@Column(name="primeiro_texto")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String primeiroTexto;
	
	@Column(name = "primeiro_texto_cor")
	private String primeiroTextoCor;
	
	@Column(name = "primeiro_texto_link")
	private String primeiroTextoLink;
	
	@Column(name = "linhas_cor")
	private String linhasCor;
	
	@Column(name="primeira_imagem")
	private String primeiraImagem;
	
	@Column(name="autor_primeira_imagem")
	private String nomeAutorPrimeiraImagem;
	
//	@ContainedIn
//	@OneToOne(mappedBy = "primeiraSecao" )
//	@JsonBackReference
//	private Home home;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="home")
	@JsonBackReference
	private Home home;
	
	@Column(name = "exibir")
	private Boolean exibir;
	
	@Column(name = "tipo")
	private String tipo;

}
