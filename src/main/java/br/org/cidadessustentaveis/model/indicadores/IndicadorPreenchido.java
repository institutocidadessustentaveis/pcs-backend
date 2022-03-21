package br.org.cidadessustentaveis.model.indicadores;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.util.NumeroUtil;
import br.org.cidadessustentaveis.util.VariavelPreenchidaUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "indicador_preenchido")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(ListenerAuditoria.class)
public class IndicadorPreenchido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "indicador_preenchido_id_seq")
	@SequenceGenerator(name = "indicador_preenchido_id_seq", sequenceName = "indicador_preenchido_id_seq", allocationSize = 1)
	@Column(nullable = false)
	private Long id;

	@OneToOne(targetEntity = Indicador.class)
	@JoinColumn(name = "id_indicador", referencedColumnName = "id")
	private Indicador indicador;

	@Column(name = "ano")
	private Short ano;

	@Column(name = "justificativa")
	private String justificativa;

	@Column(name = "resultado")
	private String resultado;
	
	@Column(name = "resultado_apresentacao")
	private String resultadoApresentacao;

	@Column(name = "resultado_referencia")
	private Double resultadoReferencia;

	@Column(name = "data_preenchimento")
	private LocalDateTime dataPreenchimento;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "indicador_variavel_preenchida", joinColumns = @JoinColumn(name = "id_indicador_preenchido", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_variavel_preenchida", referencedColumnName = "id"))
	private List<VariavelPreenchida> variaveisPreenchidas;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_prefeitura", referencedColumnName = "id")
	private Prefeitura prefeitura;
	
	@Column(name = "valor_texto", nullable = false)
	private String valorTexto;

	public String getReferencia() {
		String referencia = "";

		if (isAguardandoAvaliacao()) {
			referencia = "Aguardando Avaliação";
			return referencia;
		}

		if (indicador.isNumerico()) {

			if(indicador.isMultiplo()) {
//				if(resultado != null && !resultado.isEmpty()) {
//					Double resultadoDouble = Double.valueOf(resultado);
//					for (ValorReferencia vr : indicador.getValoresReferencia()) {
//						if (resultadoDouble >= vr.getValorde() && resultadoDouble <= vr.getValorate()) {
//							referencia = vr.getLabel();
//							break;
//						}
//					}
//				}
			} else {
				Double resultadoDouble = null;

				if(resultado != null && NumeroUtil.isANumber(resultado)) {
					resultadoDouble = Double.valueOf(resultado);
				}
				if( resultadoDouble != null ) {
					for (ValorReferencia vr : indicador.getValoresReferencia()) {
						if (vr.getValorde() != null && vr.getValorate() != null && resultadoDouble >= vr.getValorde()
								&& resultadoDouble <= vr.getValorate()) {
							referencia = vr.getLabel();
							break;
						}
					}
				}
			}

		} else {
//			Double resultadoDouble = (resultadoReferencia == null ? Double.valueOf(resultado) : resultadoReferencia);
//			for (ValorReferencia vr : indicador.getValoresReferencia()) {
//				if (vr.getValorde() != null && vr.getValorate() != null && resultadoDouble >= vr.getValorde()
//						&& resultadoDouble <= vr.getValorate()) {
//					referencia = vr.getLabel();
//					break;
//				}
//			}
		}
		return referencia;
	}

	
	public String getReferenciaDescritiva() {
		String referencia = "";

		if (isAguardandoAvaliacao()) {
			referencia = "Aguardando Avaliação";
			return referencia;
		}

		if (indicador.isNumerico()) {

			if(indicador.isMultiplo()) {
			} else {
				Double resultadoDouble = null;

				if(resultado != null && NumeroUtil.isANumber(resultado)) {
					resultadoDouble = Double.valueOf(resultado);
				}
				if( resultadoDouble != null ) {
					for (ValorReferencia vr : indicador.getValoresReferencia()) {
						if (vr.getValorde() != null && vr.getValorate() != null && resultadoDouble >= vr.getValorde()
								&& resultadoDouble <= vr.getValorate()) {
							referencia = ano + "- " + vr.getLabel()+": \nValor entre " + NumeroUtil.decimalToString(vr.getValorde())  + " e "+ NumeroUtil.decimalToString(vr.getValorate());
							break;
						}
					}
				}
			}

		}
		return referencia;
	}

	public Boolean isAguardandoAvaliacao() {
		boolean aguardando = false;
		if (variaveisPreenchidas != null) {
			for (VariavelPreenchida vp : variaveisPreenchidas) {
				if (null != vp.getStatus() && vp.getStatus().equals("Aguardando Avaliação")) {
					aguardando = true;
					break;
				}
			}
		}
		return aguardando;
	}

	public String getValorApresentacao() {
		if (null != getResultado()) {
			if (indicador.isNumerico()) {
				String resultado = getResultado();
				if (resultado.contains("[")) {
					resultado = resultado.replace("[", "");
				}
				if (resultado.contains("]")) {
					resultado = resultado.replace("]", "");
				}

				try {
					return NumeroUtil.decimalToString(Double.valueOf(resultado));
				} catch (Exception e) {
					return null;
				}

			} else {
				return formulaTextual();
			}
		} else {
			return "-";
		}
	}

	public String getValorApresentacaoTabelas() {
		if (null != getResultado()) {
			if (indicador.isMultiplo()) {
				String resultadoApresentacao = getResultado();
				if (resultadoApresentacao.contains("[")) {
					resultadoApresentacao = resultadoApresentacao.replace("[", "");
				}
				if (resultadoApresentacao.contains("]")) {
					resultadoApresentacao = resultadoApresentacao.replace("]", "");
				}

				if (indicador.isNumerico()) {
					boolean resultadoNaN = false;
					String valores[] = resultadoApresentacao.split(",");
					resultadoApresentacao = "";
					for (int i = 0; i < valores.length; i++) {
						if (NumeroUtil.isANumber(valores[i])) {
							if (i == 0) {
								if(indicador.getId().equals(3985l)) {
									resultadoApresentacao = NumeroUtil.decimal3ToString(
											NumeroUtil.arredondarTresCasasDecimais(Double.valueOf(valores[i])));									
								} else {
									resultadoApresentacao = NumeroUtil.decimalToString(
											NumeroUtil.arredondarDuasCasasDecimais(Double.valueOf(valores[i])));									
								}
							} else {
								if (i > 0 && i < valores.length) {
									if(indicador.getId().equals(3985l)) {
										resultadoApresentacao = resultadoApresentacao + " - " + NumeroUtil.decimal3ToString(
												NumeroUtil.arredondarTresCasasDecimais(Double.valueOf(valores[i])));									
									} else {
										resultadoApresentacao = resultadoApresentacao + " - " + NumeroUtil.decimalToString(
												NumeroUtil.arredondarTresCasasDecimais(Double.valueOf(valores[i])));								
									}
								}
							}
						}
						else {
							resultadoNaN = true;
							break;
						}
					}
					if (resultadoNaN) {
						return "Preenchido";
					}
					return resultadoApresentacao;
				} else {
					return "Preenchido";
				}
			}
			if (indicador.isNumerico()) {
				try {
					if (NumeroUtil.isANumber(getResultado())) {

						if(indicador.getId().equals(3985l)) {
							return NumeroUtil.decimal3ToString(NumeroUtil.arredondarTresCasasDecimais(Double.valueOf(getResultado())));
						} else {
							return NumeroUtil.decimalToString(NumeroUtil.arredondarDuasCasasDecimais(Double.valueOf(getResultado())));
						}
					} else {
						if(this.getResultado().contains("[")) {
							return this.getResultado();
						}

						return "Preenchido";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return "Preenchido";
			}
		} else {
			if (!indicador.isNumerico()) {
				return "Preenchido";				
			}
			return "";
		}
	}

	public String formulaTextual() {
		String formula = this.indicador.getFormulaResultado();
		try{
			for (VariavelPreenchida vp : variaveisPreenchidas) {
				formula = formula.replaceAll("#" + vp.getVariavel().getId() + "#",
						"\"" + VariavelPreenchidaUtil.valorApresentacao(vp) + "\"");
			}
			try{
				indicador.getVariaveis().forEach(variavel -> {
					indicador.setFormulaResultado(indicador.getFormulaResultado().replaceAll("#" + variavel.getId() + "#",
							"\"" + variavel.getNome() + "\""));
				});
			} catch (Exception f) {
				System.out.println(f.getMessage());
			}

			formula = formula.replaceAll("concat", "");
			// formula = (indicador.getFormulaResultado().replaceAll("\\.", ","));
		} catch (Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return formula;
	}
	
	
	public String getValorApresentacaoMapaPlanejamento() {
		if (indicador.isMultiplo()) {
			String resultadoApresentacao = getResultado();
			if (resultadoApresentacao.contains("[")) {
				resultadoApresentacao = resultadoApresentacao.replace("[", "");
			}
			if (resultadoApresentacao.contains("]")) {
				resultadoApresentacao = resultadoApresentacao.replace("]", "");
			}

			if (indicador.isNumerico()) {
				String valores[] = resultadoApresentacao.split(",");
				resultadoApresentacao = "";
				for (int i = 0; i < valores.length; i++) {
					if (NumeroUtil.isANumber(valores[i])) {
						if (i == 0) {
							resultadoApresentacao = NumeroUtil.decimalToString(
									NumeroUtil.arredondarDuasCasasDecimais(Double.valueOf(valores[i])));
						} else {
							if (i > 0 && i < valores.length) {
								resultadoApresentacao = resultadoApresentacao + ";" + NumeroUtil.decimalToString(
										NumeroUtil.arredondarDuasCasasDecimais(Double.valueOf(valores[i])));
							}
						}
					}
				}
				return resultadoApresentacao;
			}
		}
		if (null != getResultado()) {
			if (indicador.isNumerico()) {
				String resultado = getResultado();
				if (resultado.contains("[")) {
					resultado = resultado.replace("[", "");
				}
				if (resultado.contains("]")) {
					resultado = resultado.replace("]", "");
				}

				try {
					return NumeroUtil.decimalToString(Double.valueOf(resultado));
				} catch (Exception e) {
					return null;
				}

			} else {
				return formulaTextualMapaPlanejamento();
			}
		} else {
			return "-";
		}
	}
	
	public String formulaTextualMapaPlanejamento() {
		String formula = this.indicador.getFormulaResultado();
		for (VariavelPreenchida vp : variaveisPreenchidas) {
			formula = formula.replaceAll("#" + vp.getVariavel().getId() + "#",
					"\"" + VariavelPreenchidaUtil.valorApresentacao(vp) + "\"");
		}
		formula = formula.replaceAll("concat", "");
		formula = (formula.replaceAll("\\.", ","));
		formula = formula.replaceAll("\\(", "");

		return formula;
	}

	public String formulaTextualDadosAbertos() {
		String formula = this.indicador.getFormulaResultado();
		for (VariavelPreenchida vp : variaveisPreenchidas) {
			String va = VariavelPreenchidaUtil.valorApresentacao(vp);
			if(va == null) {
				va = "";
			}
			va = va.replaceAll("(\r\n|\n)", ".");
			try{
				formula = formula.replace("#" + vp.getVariavel().getId() + "#",
						"'" + va + "'");
			}catch (Exception e){
				System.out.println(vp.getId()+"-"+va+"-"+formula+" - "+e.getMessage());
			}
		}
		formula = formula.replace("concat(", "");
		formula = (formula.replace("\\.", ","));
		if(formula.endsWith(")")){
			formula = formula.substring(0, formula.length()-1 );
		}

		return formula;
	}

	public IndicadorPreenchido(Indicador indicador) {
		super();
		this.id = indicador.getId();
		this.indicador = indicador;
	}

	public IndicadorPreenchido(Long id, Indicador indicador, Short ano, String justificativa, String resultado,
							   Double resultadoReferencia, LocalDateTime dataPreenchimento, Prefeitura prefeitura, String nomeIndicador, String nomeCidade, String resultadoApresentacao) {
		super();
		this.id = id;
		this.indicador = indicador;
		this.ano = ano;
		this.justificativa = justificativa;
		this.resultado = resultado;
		this.resultadoReferencia = resultadoReferencia;
		this.dataPreenchimento = dataPreenchimento;
		this.prefeitura = prefeitura;
		this.resultadoApresentacao = resultadoApresentacao;

		//Utilizado no order by
		nomeIndicador = nomeIndicador;
		nomeCidade = nomeCidade;
	}
	
	
	//usado na geração de tabela da visualização de indicadores
	public IndicadorPreenchido(Long id, Short ano, String justificativa, String resultado, Double resultadoReferencia, LocalDateTime dataPreenchimento, 
			Long idPrefeitura, String nomePrefeitura, Long idProvinciaEstado, String siglaEstado, Long idCidade, String nomeCidade, Long idIndicador, String nomeIndicador, String descricao) {
		this.id = id;
		Indicador indicador = new Indicador();
		indicador.setId(idIndicador);
		indicador.setNome(nomeIndicador);
		indicador.setDescricao(descricao);
		this.indicador = indicador;
		this.ano = ano;
		this.justificativa = justificativa;
		this.resultado = resultado;
		this.resultadoReferencia = resultadoReferencia;
		this.dataPreenchimento = dataPreenchimento;
		Prefeitura prefeitura = new Prefeitura();
		prefeitura.setId(idPrefeitura);
		prefeitura.setNome(nomePrefeitura);
		Cidade cidade = new Cidade();
		cidade.setId(idCidade);
		cidade.setNome(nomeCidade);
		ProvinciaEstado pe = new ProvinciaEstado();
		pe.setId(idProvinciaEstado);
		pe.setSigla(siglaEstado);
		cidade.setProvinciaEstado(pe);
		prefeitura.setCidade(cidade);
		this.prefeitura = prefeitura;
	}
	
	//usado na geração de tabela da visualização de indicadores
		public IndicadorPreenchido(Long id, Short ano, String justificativa, String resultado, Double resultadoReferencia, LocalDateTime dataPreenchimento, 
				Long idPrefeitura, String nomePrefeitura, Long idProvinciaEstado, String siglaEstado, Long idCidade, String nomeCidade, Long idIndicador, String nomeIndicador, String descricao, Long populacao) {
			this.id = id;
			Indicador indicador = new Indicador();
			indicador.setId(idIndicador);
			indicador.setNome(nomeIndicador);
			indicador.setDescricao(descricao);
			this.indicador = indicador;
			this.ano = ano;
			this.justificativa = justificativa;
			this.resultado = resultado;
			this.resultadoReferencia = resultadoReferencia;
			this.dataPreenchimento = dataPreenchimento;
			Prefeitura prefeitura = new Prefeitura();
			prefeitura.setId(idPrefeitura);
			prefeitura.setNome(nomePrefeitura);
			Cidade cidade = new Cidade();
			cidade.setId(idCidade);
			cidade.setNome(nomeCidade);
			cidade.setPopulacao(populacao);
			ProvinciaEstado pe = new ProvinciaEstado();
			pe.setId(idProvinciaEstado);
			pe.setSigla(siglaEstado);
			cidade.setProvinciaEstado(pe);
			prefeitura.setCidade(cidade);
			this.prefeitura = prefeitura;
		}
	
	public IndicadorPreenchido(Long id, Short ano, String justificativa, String resultado, Double resultadoReferencia, LocalDateTime dataPreenchimento, 
			Long idPrefeitura, String nomePrefeitura, Long idProvinciaEstado, String siglaEstado, Long idCidade, String nomeCidade, Double latitude, Double longitude, Long idIndicador, String nomeIndicador, String descricao, String formulaResultado, String resultadoApresentacao) {
		this.id = id;
		Indicador indicador = new Indicador();
		indicador.setId(idIndicador);
		indicador.setNome(nomeIndicador);
		indicador.setDescricao(descricao);
		indicador.setFormulaResultado(formulaResultado);
		this.indicador = indicador;
		this.ano = ano;
		this.justificativa = justificativa;
		this.resultado = resultado;
		this.resultadoReferencia = resultadoReferencia;
		this.dataPreenchimento = dataPreenchimento;
		Prefeitura prefeitura = new Prefeitura();
		prefeitura.setId(idPrefeitura);
		prefeitura.setNome(nomePrefeitura);
		Cidade cidade = new Cidade();
		cidade.setId(idCidade);
		cidade.setNome(nomeCidade);
		cidade.setLatitude(latitude);
		cidade.setLongitude(longitude);
		ProvinciaEstado pe = new ProvinciaEstado();
		pe.setId(idProvinciaEstado);
		pe.setSigla(siglaEstado);
		cidade.setProvinciaEstado(pe);
		prefeitura.setCidade(cidade);
		this.prefeitura = prefeitura;
		this.resultadoApresentacao = resultadoApresentacao;
	}
	
	public IndicadorPreenchido(Long id, Short ano, String justificativa, String resultado, Double resultadoReferencia, LocalDateTime dataPreenchimento, 
			Long idPrefeitura, String nomePrefeitura, Long idProvinciaEstado, String siglaEstado, Long idCidade, String nomeCidade, Double latitude, Double longitude, Long idIndicador, String nomeIndicador, String descricao) {
		this.id = id;
		Indicador indicador = new Indicador();
		indicador.setId(idIndicador);
		indicador.setNome(nomeIndicador);
		indicador.setDescricao(descricao);
		this.indicador = indicador;
		this.ano = ano;
		this.justificativa = justificativa;
		this.resultado = resultado;
		this.resultadoReferencia = resultadoReferencia;
		this.dataPreenchimento = dataPreenchimento;
		Prefeitura prefeitura = new Prefeitura();
		prefeitura.setId(idPrefeitura);
		prefeitura.setNome(nomePrefeitura);
		Cidade cidade = new Cidade();
		cidade.setId(idCidade);
		cidade.setNome(nomeCidade);
		cidade.setLatitude(latitude);
		cidade.setLongitude(longitude);
		ProvinciaEstado pe = new ProvinciaEstado();
		pe.setId(idProvinciaEstado);
		pe.setSigla(siglaEstado);
		cidade.setProvinciaEstado(pe);
		prefeitura.setCidade(cidade);
		this.prefeitura = prefeitura;
	}
}
