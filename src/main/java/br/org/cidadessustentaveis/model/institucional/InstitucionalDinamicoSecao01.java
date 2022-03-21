package br.org.cidadessustentaveis.model.institucional;

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
@Table(name="institucional_dinamico_secao_01")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class InstitucionalDinamicoSecao01 implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "institucional_dinamico_secao_01_id_seq")
	@SequenceGenerator(name = "institucional_dinamico_secao_01_id_seq", sequenceName = "institucional_dinamico_secao_01_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String titulo;
	
	@Column(name="texto")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String texto;
	
	@Column(name="txt_botao_01")
	private String txtBotao01;

	@Column(name="txt_botao01_cor")
	private String txtBotao01Cor;
	
	@Column(name="link_botao_01")
	private String linkBotao01;
	
	@Column(name="txt_botao_02")
	private String txtBotao02;
	
	@Column(name="txt_botao02_cor")
	private String txtBotao02Cor;
	
	@Column(name="link_botao_02")
	private String linkBotao02;
	
	@Column(name="txt_botao_03")
	private String txtBotao03;
	
	@Column(name="txt_botao03_cor")
	private String txtBotao03Cor;
	
	@Column(name="link_botao_03")
	private String linkBotao03;
	
	@Column(name="txt_botao_04")
	private String txtBotao04;
	
	@Column(name="txt_botao04_cor")
	private String txtBotao04Cor;
	
	@Column(name="link_botao_04")
	private String linkBotao04;
	
	@Column(name="cor_fundo")
	private String corFundo;
	
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
	
	@Column(name="habilita_recurso_externo")
	private Boolean habilitaRecursoExterno;
	
}
