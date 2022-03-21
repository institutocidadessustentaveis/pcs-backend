package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.TemaForum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners(ListenerAuditoria.class)
@Table(name="forum_discussao")
@Indexed
@AnalyzerDefs(
		@AnalyzerDef(name = "analyzersForum",
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
public class ForumDiscussao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="usuario_cadastro")
	private Long usuarioCadastrouDiscussao;
	
	@Column(name="titulo")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzersForum")
	private String titulo;
	
	@Column(name="descricao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzersForum")
	private String descricao;
	
	@Column(name="data_hora_criacao")
	private LocalDateTime dataHoraCriacao;
	
	@Column(name="data_ativacao")
	private LocalDate dataAtivacao;
	
	@Column(name="hora_ativacao")
	private LocalTime horaAtivacao;
	
	@Column(name="data_desativacao")
	private LocalDate dataDesativacao;
	
	@Column(name="hora_desativacao")
	private LocalTime horaDesativacao;
	
	@Column(name="publico")
	private Boolean publico;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="forumDiscussao", cascade=CascadeType.ALL)
	@JsonManagedReference	
	private List<DiscussaoPerfil> discussaoPerfis = new ArrayList<DiscussaoPerfil>();
	
	@ManyToOne
	@JoinColumn(name="usuario_ultima_postagem")
	private Usuario usuarioUltimaPostagem;
	
	@Column(name="numero_de_respostas")
	private Long numeroDeRespostas;
	
	@Column(name="numero_de_visualizacao")
	private Long numeroDeVisualizacao;
	
	@Column(name="ativo")
	private Boolean ativo;
	
	@ManyToOne
	@JoinColumn(name="prefeitura")
	private Prefeitura prefeitura;
	

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "discussao_tema_forum", 
        joinColumns = { @JoinColumn(name = "forum_discussao") }, 
        inverseJoinColumns = { @JoinColumn(name = "tema_forum") } )
	private List<TemaForum> temasForum;
	
}
