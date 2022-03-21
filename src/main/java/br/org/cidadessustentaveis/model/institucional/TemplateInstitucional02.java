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
@Table(name="template_institucional_02")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class TemplateInstitucional02 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "template_institucional_02_id_seq")
	@SequenceGenerator(name = "template_institucional_02_id_seq", sequenceName = "template_institucional_02_id_seq", allocationSize = 1)
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
	
	@Column(name="imagem_primeira_secao")
	private String imagemPrimeiraSecao;
	
	@Column(name="titulo_segunda_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloSegundaSecao;
	
	@Column(name="txt_segunda_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String txtSegundaSecao;
	
	@Column(name="cor_fundo_segunda_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String corFundoSegundaSecao;
	
	@Column(name="titulo_terceira_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloTerceiraSecao;
	
	@Column(name="txt_terceira_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String txtTerceiraSecao;
	
	@Column(name="imagem_terceira_secao")
	private String imagemTerceiraSecao;
	
	@Column(name="titulo_quarta_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloQuartaSecao;
	
	@Column(name="txt_quarta_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String txtQuartaSecao;
	
	@Column(name="txt_botao_01")
	private String txtBotao01;
	
	@Column(name="link_botao_01")
	private String linkBotao01;
	
	@Column(name="autor_imagem_primeira_seção")
	private String nomeAutorImagemPrimeiraSecao;
	
	@Column(name="autor_imagem_terceira_seção")
	private String nomeAutorImagemTerceiraSecao;
	
	@ContainedIn
	@OneToOne(mappedBy = "template02" )
	@JsonBackReference
	private Institucional institucional;

}
