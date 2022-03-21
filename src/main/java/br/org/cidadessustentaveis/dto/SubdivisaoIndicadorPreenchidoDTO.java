package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SubdivisaoIndicadorPreenchidoDTO {
	
	private Long id;
	private Long idIndicador;
	private Short ano;
	private String justificativa;
	private String resultado;
	private Double resultadoReferencia;
	private Long idSubdivisao;
	private String nomeSubdivisao;
	public SubdivisaoIndicadorPreenchidoDTO(SubdivisaoIndicadorPreenchido preenchido) {
		this.id = preenchido.getId();
		this.idIndicador = preenchido.getIndicador().getId();
		this.ano = preenchido.getAno();
		this.justificativa = preenchido.getJustificativa();
		this.resultado = preenchido.getResultado();
		this.resultadoReferencia = preenchido.getResultadoReferencia();
		this.idSubdivisao = preenchido.getSubdivisao().getId();
		this.nomeSubdivisao = preenchido.getSubdivisao().getNome();
	}

}
