package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class IndicadorDTO {

	private Long id;
	
    @NotNull
    @NotEmpty
	private String nome;
	
    @NotNull
    @NotEmpty
	private String descricao;
	
	@JsonIgnoreProperties({"icone", "iconeReduzido", "listaODS"})
	@NotNull
	private EixoDTO eixo;
	
	@JsonIgnoreProperties({"icone", "iconeReduzido", "metas"})
	@NotNull
	private ObjetivoDesenvolvimentoSustentavelDTO ods;
	
	@JsonProperty("metaOds")
	@NotNull
	private MetaObjetivoDesenvolvimentoSustentavelDTO metaODS;
	
	@JsonProperty("ordem_classificacao")
	@NotNull
	@NotEmpty
	private String ordemClassificacao;
	
	@NotEmpty
	private List<VariavelDTO> variaveis;
	
	@JsonProperty("formula_resultado")
	private String formulaResultado;
	
	@JsonProperty("formula_referencia")
	private String formulaReferencia;
	
	@JsonProperty("referencia")
	private List<ValorReferenciaDTO> valoresReferencia;
	
	@JsonIgnore
	private PrefeituraDTO prefeitura;
	
	private Date dataCadastro;
	
	private Boolean numerico;
	
	private String nomePrefeitura;
	
	private Boolean complementar;
	
	public IndicadorDTO(Indicador indicador) {
		super();
		this.id = indicador.getId();
		this.nome = indicador.getNome();
		this.descricao = indicador.getDescricao();
		this.eixo = new EixoDTO(indicador.getEixo());

		if(indicador.getOds() != null) {
			this.ods = new ObjetivoDesenvolvimentoSustentavelDTO(indicador.getOds());
		}

		if(indicador.getMetaODS() != null) {
			this.metaODS = new MetaObjetivoDesenvolvimentoSustentavelDTO(indicador.getMetaODS());
		}

		this.ordemClassificacao = indicador.getOrdemClassificacao();
		this.dataCadastro = indicador.getDataCadastro();
		if(indicador.getPrefeitura() == null) {
			this.nomePrefeitura = "PCS";
		}
		else {
			this.nomePrefeitura = "Prefeitura - " + indicador.getPrefeitura().getNome();
		}
		
		this.variaveis = new ArrayList<VariavelDTO>();
		this.numerico = indicador.isNumerico();
		indicador.getVariaveis().stream()
								.forEach(variavel -> this.variaveis.add(new VariavelDTO(variavel)));
		
		this.formulaResultado = indicador.getFormulaResultado();
		this.formulaReferencia = indicador.getFormulaReferencia();
		
		this.valoresReferencia = new ArrayList<ValorReferenciaDTO>();
		indicador.getValoresReferencia().stream()
										.forEach(referencia -> this.valoresReferencia.add(new ValorReferenciaDTO(referencia)));
		this.prefeitura = new PrefeituraDTO(indicador.getPrefeitura());
		this.complementar = indicador.getComplementar();
	}
	public Indicador toEntityInsert(Eixo eixo, ObjetivoDesenvolvimentoSustentavel ods, MetaObjetivoDesenvolvimentoSustentavel meta, Prefeitura prefeitura) {
		List<Variavel> variaveis = new ArrayList<>();
		
		// Remover esse relacionamento de variavel com a tabela de valores de referencia
		this.getVariaveis().stream()
						   .forEach(variavel -> variaveis.add(Variavel.builder().id(variavel.getId()).nome(variavel.getNome()).build()));
		
		List<ValorReferencia> valoresReferencia = new ArrayList<>();
		if (this.getValoresReferencia() != null) {
			this.getValoresReferencia().stream().forEach(referencia -> valoresReferencia.add(referencia.toEntityInsert()));
		}
		
		return Indicador.builder()
					.nome(this.getNome())
					.descricao(this.getDescricao())
					.eixo(eixo)
					.ods(ods)
					.metaODS(meta)
					.ordemClassificacao(this.getOrdemClassificacao())
					.variaveis(variaveis)
					.formulaResultado(this.getFormulaResultado())
					.formulaReferencia(this.getFormulaReferencia())
					.valoresReferencia(valoresReferencia.isEmpty() ? null : valoresReferencia)
					.prefeitura(prefeitura)
					.dataCadastro(new Date())
					.complementar(this.getComplementar())
					.build();
	}
	
	public Indicador toEntityUpdate(Indicador indicadorRef) {
		indicadorRef.setNome(this.nome);
		indicadorRef.setDescricao(this.descricao);
		indicadorRef.setOrdemClassificacao(ordemClassificacao);
		indicadorRef.setFormulaReferencia(formulaReferencia);
		indicadorRef.setFormulaResultado(formulaResultado);
		indicadorRef.setComplementar(this.complementar);

		return indicadorRef;
	}
	
	public IndicadorDTO(IndicadorPreenchido indicadorPreenchido) {
		super();
		this.id = indicadorPreenchido.getIndicador().getId();
		this.nome = indicadorPreenchido.getIndicador().getNome();
		this.descricao = indicadorPreenchido.getIndicador().getDescricao();
		
		if(indicadorPreenchido.getIndicador().getEixo() != null) {
			this.eixo = new EixoDTO(indicadorPreenchido.getIndicador().getEixo());
		}
		
		if(indicadorPreenchido.getIndicador().getOds() != null) {
			this.ods = new ObjetivoDesenvolvimentoSustentavelDTO(indicadorPreenchido.getIndicador().getOds());
		}
		
		if(indicadorPreenchido.getIndicador().getMetaODS() != null) {
			this.metaODS = new MetaObjetivoDesenvolvimentoSustentavelDTO(indicadorPreenchido.getIndicador().getMetaODS());
		}
		
		this.ordemClassificacao = indicadorPreenchido.getIndicador().getOrdemClassificacao();
		this.dataCadastro = indicadorPreenchido.getIndicador().getDataCadastro();
		this.variaveis = new ArrayList<VariavelDTO>();
		this.numerico = indicadorPreenchido.getIndicador().isNumerico();
		indicadorPreenchido.getIndicador().getVariaveis().stream()
								.forEach(variavel -> this.variaveis.add(new VariavelDTO(variavel)));
		
		this.formulaResultado = indicadorPreenchido.getIndicador().getFormulaResultado();
		this.formulaReferencia = indicadorPreenchido.getIndicador().getFormulaReferencia();
		
		this.valoresReferencia = new ArrayList<ValorReferenciaDTO>();
		indicadorPreenchido.getIndicador().getValoresReferencia().stream()
										.forEach(referencia -> this.valoresReferencia.add(new ValorReferenciaDTO(referencia)));
		this.prefeitura = new PrefeituraDTO(indicadorPreenchido.getIndicador().getPrefeitura());
		
		this.nomePrefeitura = indicadorPreenchido.getIndicador().getPrefeitura() != null ? "Prefeitura - " + indicadorPreenchido.getIndicador().getPrefeitura().getNome() : "PCS";

	}
}
