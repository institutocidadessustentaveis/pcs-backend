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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
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
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.home.HomeImagem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="institucional_dinamico_secao_03")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)	
@Indexed
public class InstitucionalDinamicoSecao03 implements Serializable{	

	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "institucional_dinamico_secao_03_id_seq")
	@SequenceGenerator(name = "institucional_dinamico_secao_03_id_seq", sequenceName = "institucional_dinamico_secao_03_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String titulo;

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
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="institucionalDinamicoSecao03", cascade=CascadeType.ALL)
	@JsonManagedReference
	@OrderBy("ordemExibicao")
	private List<InstitucionalDinamicoPublicacao> publicacoes;
	
}
