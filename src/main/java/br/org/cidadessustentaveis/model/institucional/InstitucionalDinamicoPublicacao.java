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

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="institucional_dinamico_publicacao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
@ToString
public class InstitucionalDinamicoPublicacao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "institucional_dinamico_publicacao_id_seq")
	@SequenceGenerator(name = "institucional_dinamico_publicacao_id_seq", sequenceName = "institucional_dinamico_publicacao_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "imagem")
	private InstitucionalDinamicoImagem imagem;
	
	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	private String titulo;
	
	@Column(name="texto")
	private String texto;
	
	@Column(name="link")
	private String link;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="institucional_dinamico_secao03")
	@JsonBackReference
	private InstitucionalDinamicoSecao03 institucionalDinamicoSecao03;
	
	@JoinColumn(name="ordem_exibicao")
	private Integer ordemExibicao;

	@Column(name="tooltip_titulo")
	private String tooltipTitulo;
	
	@Column(name="tooltip_texto")
	private String tooltipTexto;

}
