package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="faq")
@Indexed
@AnalyzerDefs(
		@AnalyzerDef(name = "analyzersFaq",
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
@Getter @Setter  @AllArgsConstructor @Builder @NoArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class Faq implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="pergunta")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String pergunta;
	
	@Column(name="resposta")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String resposta;
		
}





