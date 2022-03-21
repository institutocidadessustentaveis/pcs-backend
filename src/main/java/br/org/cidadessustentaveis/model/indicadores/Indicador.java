package br.org.cidadessustentaveis.model.indicadores;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="indicador") 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class Indicador implements Serializable {

	private static final long serialVersionUID = 6469182408375674304L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "indicador_id_seq")
	@SequenceGenerator(name = "indicador_id_seq", sequenceName = "indicador_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name = "nome")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String nome;
	
	@Column(name = "descricao")
	@Field(index=Index.YES, termVector = TermVector.YES,store=Store.YES)
	@Analyzer(definition = "analyzers")
	private String descricao;
	
	@OneToOne
	@JoinColumn(name = "id_eixo", referencedColumnName = "id")
	private Eixo eixo;

	@OneToOne(targetEntity = ObjetivoDesenvolvimentoSustentavel.class)
	@JoinColumn(name = "id_ods", referencedColumnName = "id")
	private ObjetivoDesenvolvimentoSustentavel ods;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_meta_ods")
	private MetaObjetivoDesenvolvimentoSustentavel metaODS;
	
	@Column(name = "ordem_classificacao")
	private String ordemClassificacao; // 1-Maior valor, melhor, 2-Menor valor, melhor
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "indicador_variavel",
				joinColumns = @JoinColumn(name = "id_indicador", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "id_variavel", referencedColumnName = "id"))
	private List<Variavel> variaveis;

	@Column(name = "formula_resultado")
	private String formulaResultado;
	
	@Column(name = "formula_referencia", nullable = false)
	private String formulaReferencia;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "indicador_valores_referencia",
	           joinColumns = @JoinColumn(name = "indicador_id", referencedColumnName = "id"),
	           inverseJoinColumns = @JoinColumn(name = "valor_id", referencedColumnName = "id"))
	private List<ValorReferencia> valoresReferencia;
	
	@OneToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="id_prefeitura", referencedColumnName = "id")
	private Prefeitura prefeitura; 
	
	@Column(name = "data_cadastro")
	private Date dataCadastro;
	
	@ManyToMany(mappedBy="indicadores", fetch= FetchType.LAZY)
	private List<BoaPratica> boasPraticasRelacionadas;
	
	@Column(name = "tipo_conteudo", nullable = false)
	private String tipoConteudo;
	
	@Column(name = "complementar")
	private Boolean complementar;
	
	public boolean isNumerico() {
		boolean numerico = true;
		if(variaveis != null) {
			for(Variavel v : variaveis) {
				if(!v.isNumerico()) {
					numerico = false ;
					break;
				}
			}
		}
		return numerico;
	}
	
	public boolean isMultiplo() {
		boolean isMultiplo = false;
		if(this.formulaResultado == null ||  this.formulaResultado.isEmpty()) {
			return false;
		}
		if(this.formulaResultado.contains("concat")) {
			isMultiplo = true;
		}
		return isMultiplo;
	}
	
	public Indicador(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Indicador(Long id, String nome, String descricao, Eixo eixo, ObjetivoDesenvolvimentoSustentavel ods,
			MetaObjetivoDesenvolvimentoSustentavel metaODS, String ordemClassificacao, List<Variavel> variaveis,
			String formulaResultado, String formulaReferencia, List<ValorReferencia> valoresReferencia,
			Prefeitura prefeitura, Date dataCadastro, List<BoaPratica> boasPraticasRelacionadas, String tipoConteudo) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.eixo = eixo;
		this.ods = ods;
		this.metaODS = metaODS;
		this.ordemClassificacao = ordemClassificacao;
		this.variaveis = variaveis;
		this.formulaResultado = formulaResultado;
		this.formulaReferencia = formulaReferencia;
		this.valoresReferencia = valoresReferencia;
		this.prefeitura = prefeitura;
		this.dataCadastro = dataCadastro;
		this.boasPraticasRelacionadas = boasPraticasRelacionadas;
		this.tipoConteudo = tipoConteudo;
	}
	
	
}
