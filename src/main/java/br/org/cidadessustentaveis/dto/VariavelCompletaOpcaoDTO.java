package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class VariavelCompletaOpcaoDTO implements Serializable{
	private static final long serialVersionUID = -2035975968352620271L;
	private Long id;
	private String descricao;
	private Double valor;
	private String tipo;

	public VariavelCompletaOpcaoDTO(VariaveisOpcoes variavelOpcoes) {
		super();
		this.id = variavelOpcoes.getId();
		this.descricao = variavelOpcoes.getDescricao();
		this.valor = variavelOpcoes.getValor();
		this.tipo = variavelOpcoes.getTipo();
	}
}
