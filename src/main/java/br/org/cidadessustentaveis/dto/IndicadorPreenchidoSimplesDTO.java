package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class IndicadorPreenchidoSimplesDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2324410687843681447L;
	private Long id;
	private Long idIndicador;
	private String nomeIndicador;
	private String descricaoIndicador;
	private Short ano;
	private LocalDateTime dataPreenchimento;
	private String resultado;
	private Long idCidade;
	private String nomeCidade;
	private Boolean complementar;
	
	public IndicadorPreenchidoSimplesDTO(Long id, Long idIndicador, String nomeIndicador, String descricaoIndicador,
			Short ano, LocalDateTime dataPreenchimento, String resultado, Long idCidade, String nomeCidade) {
		super();
		this.id = id;
		this.idIndicador = idIndicador;
		this.nomeIndicador = nomeIndicador;
		this.descricaoIndicador = descricaoIndicador;
		this.ano = ano;
		this.dataPreenchimento = dataPreenchimento;
		this.resultado = resultado;
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
	}
	
	public IndicadorPreenchidoSimplesDTO(Long id, Long idIndicador, String nomeIndicador, String descricaoIndicador,
			Short ano, LocalDateTime dataPreenchimento, String resultado, Long idCidade, String nomeCidade, Boolean complementar) {
		this.id = id;
		this.idIndicador = idIndicador;
		this.nomeIndicador = nomeIndicador;
		this.descricaoIndicador = descricaoIndicador;
		this.ano = ano;
		this.dataPreenchimento = dataPreenchimento;
		this.resultado = resultado;
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
		this.complementar = complementar;
	}
}
