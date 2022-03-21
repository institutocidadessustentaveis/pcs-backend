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

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="template_institucional_03")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class TemplateInstitucional03 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "template_institucional_03_id_seq")
	@SequenceGenerator(name = "template_institucional_03_id_seq", sequenceName = "template_institucional_03_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="titulo_secao_texto")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloSecaoTexto;
	
	@Column(name="secao_texto")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String secaoTexto;
	
	@Column(name="titulo_primeira_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloPrimeiraSecao;
	
	@Column(name="texto_primeira_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String textoPrimeiraSecao;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "imagem_primeira_secao")
	private Imagem imagemPrimeiraSecao;

	@Column(name="titulo_segunda_secao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String tituloSegundaSecao;
	

	@Column(name="ver_mais_pcs")
	private Boolean verMaisPCS;

	@Column(name="ver_mais_instituicao")
	private Boolean verMaisInstituicao;
	

	@Column(name="titulo_catalogo1")
	private String tituloCatalogo1;

	@Column(name="titulo_catalogo2")
	private String tituloCatalogo2;
	
	@ContainedIn
	@OneToOne(mappedBy = "template03" )
	@JsonBackReference
	private Institucional institucional;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "template03_publicacoes_1",
				joinColumns = @JoinColumn(name = "template03", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "publicacao", referencedColumnName = "id"))
	@OrderBy("ordemExibicao ASC")
    private List<Publicacao> publicacoes;


	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "template03_publicacoes_2",
				joinColumns = @JoinColumn(name = "template03", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "publicacao", referencedColumnName = "id"))
	@OrderBy("ordemExibicao ASC")
    private List<Publicacao> publicacoes2;
	
}
