package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
@Table(name="institucional_dinamico_secao_04")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class InstitucionalDinamicoSecao04 implements Serializable{	

	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "institucional_dinamico_secao_04_id_seq")
	@SequenceGenerator(name = "institucional_dinamico_secao_04_id_seq", sequenceName = "institucional_dinamico_secao_04_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="texto")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String texto;

	@Column(name="indice")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private Long indice;
	
	@Column(name = "exibir")
	private Boolean exibir;
	
	@Column(name = "exibir_mapa")
	private Boolean exibirMapa;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name="cor_fundo")
	private String corFundo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="institucional_dinamico")
	@JsonBackReference
	private InstitucionalDinamico institucionalDinamico;
	
	@ElementCollection
	@CollectionTable(name="institucional_dinamico_secao_04_shapes", joinColumns=@JoinColumn(name="institucionaldinamicosecao04"))
	@Column(name="idshapefile")
	private List<Long> shapesRelacionados;
	
	@Column(name="habilita_recurso_externo")
	private Boolean habilitaRecursoExterno;
}
