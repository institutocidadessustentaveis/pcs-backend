package br.org.cidadessustentaveis.model.noticias;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.lucene.analysis.br.BrazilianStemFilterFactory;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.charfilter.MappingCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.TrimFilterFactory;
import org.apache.lucene.analysis.pt.PortugueseStemFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.tr.ApostropheFilterFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.CharFilterDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.sistema.HistoricoAcessoNoticia;
import br.org.cidadessustentaveis.util.CustomLocalDateTimeBridge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@Entity
@Table(name="noticia")
@Indexed
@AnalyzerDefs(
		@AnalyzerDef(name = "analyzers",
				tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
				filters = {
						@TokenFilterDef(factory = LowerCaseFilterFactory.class),
						@TokenFilterDef(factory = StopFilterFactory.class),
						@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
						@TokenFilterDef(factory = PortugueseStemFilterFactory.class),
						@TokenFilterDef(factory = BrazilianStemFilterFactory.class),
						@TokenFilterDef(factory = EnglishMinimalStemFilterFactory.class),
						@TokenFilterDef(factory = TrimFilterFactory.class),
						@TokenFilterDef(factory = ApostropheFilterFactory.class)
				},
				charFilters = {
						@CharFilterDef(factory = HTMLStripCharFilterFactory.class),
						@CharFilterDef(factory = MappingCharFilterFactory.class),
				}
		)
)
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class Noticia {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String titulo;
	
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String subtitulo;
	
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String autor;
	
	private String usuario;

	@Column(name="data_hora_criacao")	
	@Field(index=Index.YES)
	@FieldBridge(impl = CustomLocalDateTimeBridge.class)
	private LocalDateTime dataHoraCriacao;
	
	@Column(name="data_hora_publicacao")
	@Field(index=Index.YES)
	@FieldBridge(impl = CustomLocalDateTimeBridge.class)
	@Nullable
	private LocalDateTime dataHoraPublicacao;

	private String imagemPrincipal;

	@Field(index= Index.YES, termVector = TermVector.YES, store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String corpoTexto;

	@Field(index = Index.YES, termVector = TermVector.YES, store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String palavraChave;

	@Column(name = "exibir_evento_tela_inicial")
	private Boolean exibirEventoTelaInicial;

	@ElementCollection
	@CollectionTable(name="noticia_link", joinColumns=@JoinColumn(name="noticia"))
	@Column(name="link")
	private List<String> linksRelacionados;

	@Column(name = "url")
	private String url;
	
	@Column(name="publicada")
	private Boolean isPublicada;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "noticia_ods", 
        joinColumns = { @JoinColumn(name = "noticia") }, 
        inverseJoinColumns = { @JoinColumn(name = "ods") } )
	private List<ObjetivoDesenvolvimentoSustentavel> odss;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "noticia_eixo", 
        joinColumns = { @JoinColumn(name = "noticia") }, 
        inverseJoinColumns = { @JoinColumn(name = "eixo") } )
	private List<Eixo> eixos;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "noticia_area_interesse", 
        joinColumns = { @JoinColumn(name = "noticia") }, 
        inverseJoinColumns = { @JoinColumn(name = "area_interesse") } )
	private List<AreaInteresse> areasDeInteresse;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="noticia", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<NoticiaHistorico> noticiaHistorico;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="noticia", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<HistoricoAcessoNoticia> historicoAcessoNoticia;
	
	@Column(name="noticia_evento")
	private Boolean isNoticiaEvento;
	
	@Column(name="possui_filtro")
	private Boolean possuiFiltro;
	
	@Column(name="habilita_estilo")
	private Boolean habilitaEstilo;
}
