package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.VariavelResposta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VariaveisOpcoesDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String descricao;
	private Double valor;
	private String tipo;
	private VariavelResposta variavelResposta;

	public VariaveisOpcoesDTO(VariaveisOpcoes variavelOpcoes) {
		super();
		this.id = variavelOpcoes.getId();
		this.descricao = variavelOpcoes.getDescricao();
		this.valor = variavelOpcoes.getValor();
		this.tipo = variavelOpcoes.getTipo();
		this.variavelResposta = variavelOpcoes.getVariavelResposta();
	}

	public VariaveisOpcoes toEntityInsert() {
		return new VariaveisOpcoes(null, this.descricao, this.valor, this.tipo, this.variavelResposta);
	}

	public VariaveisOpcoes toEntityUpdate(VariaveisOpcoes objRef) {
		objRef.setDescricao(descricao);
		objRef.setValor(valor);
		objRef.setTipo(tipo);
		objRef.setVariavelResposta(variavelResposta);
		return objRef;
	}
}
