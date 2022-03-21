package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name="institucional_dinamico_secao_02")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class InstitucionalDinamicoSecao02 implements Serializable{	

	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "institucional_dinamico_secao_01_id_seq")
	@SequenceGenerator(name = "institucional_dinamico_secao_01_id_seq", sequenceName = "institucional_dinamico_secao_01_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String titulo;
	
	@Column(name="texto_primeira_coluna")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String textoPrimeiraColuna;
	
	@Column(name="texto_segunda_coluna")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String textoSegundaColuna;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "imagem_principal")
	private InstitucionalDinamicoImagem imagemPrincipal;
	
	@Column(name="autor_imagem")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String autorImagem;

	@Column(name="indice")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private Long indice;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="institucional_dinamico")
	@JsonBackReference
	private InstitucionalDinamico institucionalDinamico;
	
	@Column(name = "exibir")
	private Boolean exibir;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name="cor_fundo")
	private String corFundo;
	
	@Column(name="link_imagem")
	private String imagemLink;
	
	@Column(name="habilita_recurso_externo_col01")
	private Boolean habilitaRecursoExternoCol01;
	
	@Column(name="habilita_recurso_externo_col02")
	private Boolean habilitaRecursoExternoCol02;
	
}
