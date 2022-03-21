package br.org.cidadessustentaveis.model.boaspraticas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.sistema.HistoricoAcessoBoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boa_pratica")
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Indexed
public class BoaPratica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "boa_pratica_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "boa_pratica_id_seq", sequenceName = "boa_pratica_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;

	@Column(name="nome_instituicao")
	private String nomeInstituicao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="provincia_estado", nullable = false)
	private ProvinciaEstado estado;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="municipio", nullable = false)
	private Cidade municipio;
	
	@Column(name="endereco")
	private String endereco;
	
	@Column(name="site")
	private String site;
	
	@Column(name="nome_responsavel")
	private String nomeResponsavel;
	
	@Column(name="contato")
	private String contato;
	
	@Column(name="email")
	private String email;
	
	@Column(name="telefone")
	private String telefone;
	
	@Column(name="celular")
	private String celular;
	
	@Column(name="dt_inicio")
	private LocalDate dtInicio;
	
	@Column(name="dt_publicacao")
	private LocalDate dataPublicacao;
	
	@Column(name="galeria_videos")
	private String galeriaDeVideos;
	
	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String titulo;
	
	@Column(name="subtitulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String subtitulo;
	
	@Column(name="objetivo_geral")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String objetivoGeral;
	
	@Column(name="objetivo_especifico")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String objetivoEspecifico;
	
	@Column(name="principais_resultados")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String principaisResultados;
	
	@Column(name="aprendizado_fundamental")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String aprendizadoFundamental;
	
	@Column(name="parceiros_envolvidos")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String parceirosEnvolvidos;
	
	@Column(name="resultados_quantitativos")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String resultadosQuantitativos;
	
	@Column(name="resultados_qualitativos")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String resultadosQualitativos;
	
	@Column(name="parametros_contemplados")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String parametrosContemplados;
	
	@Column(name="publico_atingido")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String publicoAtingido;
	
	@Column(name="fontes_referencia")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String fontesReferencia;
	
	@Column(name="tipo")
	private String tipo;
	
	/* Flag para exibir ou não o conteúdo na página inicial*/
	@Column(name="pagina_inicial")
	private Boolean paginaInicial;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="eixo", nullable = false)
	private Eixo eixo;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "boa_pratica_ods",
        joinColumns = { @JoinColumn(name = "id_boa_pratica") },
        inverseJoinColumns = { @JoinColumn(name = "id_ods") } )
	private List<ObjetivoDesenvolvimentoSustentavel> ods;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "boa_pratica_meta_ods",
        joinColumns = { @JoinColumn(name = "id_boa_pratica") },
        inverseJoinColumns = { @JoinColumn(name = "id_meta_ods") } )
	private List<MetaObjetivoDesenvolvimentoSustentavel> metasOds;

	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "boa_pratica_indicador",
        joinColumns = { @JoinColumn(name = "id_boa_pratica") },
        inverseJoinColumns = { @JoinColumn(name = "id_indicador") } )
	private List<Indicador> indicadores;
	
	@Column(name="informacoes_complementares")
	private String informacoesComplementares;	

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="prefeitura", nullable = false)
	private Prefeitura prefeitura;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="boaPratica", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<ImagemBoaPratica> imagens;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="boaPratica", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<HistoricoAcessoBoaPratica> historicoAcessoBoaPratica;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="boaPratica", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<SolucaoBoaPratica> solucoes;
	
	@Column(name="possui_filtro")
	private Boolean possuiFiltro;
	
	@Column(name="autor_imagem_principal")
	private String autorImagemPrincipal;
}