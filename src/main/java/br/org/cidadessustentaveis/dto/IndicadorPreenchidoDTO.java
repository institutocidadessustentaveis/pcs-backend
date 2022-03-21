package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class IndicadorPreenchidoDTO {
	
	private Long id;
	
	@NotNull
	private Long idIndicador;

	@NotNull
	private Short ano;
	
	private String justificativa;
	
	private LocalDateTime dataPreenchimento;

	@NotNull
	private List<VariavelPreenchidaDTO> variaveisPreenchidas;

	private Prefeitura prefeitura;

	@JsonIgnore
	private String resultado;

	@JsonIgnore
	private Double resultadoReferencia;

	
	private Long subdivisao;
	
	public IndicadorPreenchidoDTO(IndicadorPreenchido preenchido) {
		this.id = preenchido.getId();
		this.idIndicador = preenchido.getIndicador().getId();
		this.ano = preenchido.getAno();
		this.justificativa = preenchido.getJustificativa();
		this.variaveisPreenchidas = new ArrayList<>();
		this.dataPreenchimento = preenchido.getDataPreenchimento();
		preenchido.getVariaveisPreenchidas()
				.forEach(preenchida -> this.variaveisPreenchidas.add(new VariavelPreenchidaDTO(preenchida)));
		this.prefeitura = preenchido.getPrefeitura();
		this.resultado = preenchido.getResultado();
		this.resultadoReferencia = preenchido.getResultadoReferencia();
	}

	public IndicadorPreenchidoDTO(Long id, Short ano, Long idIndicador, String justificativa) {
		this.id = id;
		this.idIndicador = idIndicador;
		this.ano = ano;
		this.justificativa = justificativa;
	}	

}
