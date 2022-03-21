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
@Table(name="home_quarta_secao")	
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class QuartaSecao implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "home_quarta_secao_id_seq")
	@SequenceGenerator(name = "home_quarta_secao_id_seq", sequenceName = "home_quarta_secao_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="indice")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private Long indice;
	
	@Column(name="titulo_principal")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloPrincipal;
	
	@Column(name = "titulo_principal_cor")
	private String tituloPrincipalCor;
	
	@Column(name = "titulo_principal_link")
	private String tituloPrincipalLink;
	
	
	@Column(name="primeiro_titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String primeiroTitulo;
	
	@Column(name = "primeiro_titulo_cor")
	private String primeiroTituloCor;

	@Column(name = "primeiro_titulo_link")
	private String primeiroTituloLink;
	
	
	@Column(name="primeira_imagem")
	private String primeiraImagem;
	
	@Column(name = "primeira_imagem_link")
	private String primeiraImagemLink;
	
	@Column(name = "primeira_imagem_tooltip")
	private String primeiraImagemTooltip;
	
	
	@Column(name="primeiro_botao_texto")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String primeiroBotaoTexto;
	
	@Column(name = "primeiro_botao_texto_cor")
	private String primeiroBotaoTextoCor;
	
	@Column(name = "primeiro_botao_link")
	private String primeiroBotaoLink;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="home")
	@JsonBackReference
	private Home home;
	
	@Column(name = "exibir")
	private Boolean exibir;
	
	@Column(name = "tipo")
	private String tipo;
	
	

}
