package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name="template_institucional_01")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class TemplateInstitucional01 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "template_institucional_01_id_seq")
	@SequenceGenerator(name = "template_institucional_01_id_seq", sequenceName = "template_institucional_01_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="titulo_primeira_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloPrimeiraSecao;
	
	@Column(name="texto_primeira_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String textoPrimeiraSecao;
	
	@Column(name="blockquote_primeira_secao")
	private String blockquotePrimeiraSecao;
	
	@Column(name="titulo_segunda_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloSegundaSecao;
	
	@Column(name="txt_segunda_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String txtSegundaSecao;
	
	@Column(name="txt_botao_01")
	private String txtBotao01;
	
	@Column(name="link_botao_01")
	private String linkBotao01;
	
	@Column(name="txt_botao_02")
	private String txtBotao02;
	
	@Column(name="link_botao_02")
	private String linkBotao02;
	
	@Column(name="txt_botao_03")
	private String txtBotao03;
	
	@Column(name="link_botao_03")
	private String linkBotao03;
	
	@Column(name="txt_botao_04")
	private String txtBotao04;
	
	@Column(name="link_botao_04")
	private String linkBotao04;
	
	@ContainedIn
	@OneToOne(mappedBy = "template01" )
	@JsonBackReference
	private Institucional institucional;

}
