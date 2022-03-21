package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Set;

import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelResposta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VariavelRespostaDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Double respostaSim;
	private Double respostaNao;
	private boolean exibirOpcaoSim;
	private boolean exibirOpcaoNao;
	private boolean exibirOpcao;
	private Set<VariaveisOpcoes> listaOpcoes;
	private Variavel variavel;

	public VariavelRespostaDTO(VariavelResposta variavelResposta) {
		super();
		this.id = variavelResposta.getId();
		this.respostaSim = variavelResposta.getRespostaSim();
		this.respostaNao = variavelResposta.getRespostaNao();
		this.exibirOpcaoSim = variavelResposta.isExibirOpcaoSim();
		this.exibirOpcaoNao = variavelResposta.isExibirOpcaoNao();
		this.exibirOpcao = variavelResposta.isExibirOpcao();
		this.listaOpcoes = variavelResposta.getListaOpcoes();
		this.variavel = variavelResposta.getVariavel();
	}
	
	public VariavelResposta toEntityInsert() {
		return new VariavelResposta(null,this.respostaSim, this.respostaNao, this.exibirOpcaoSim, this.exibirOpcaoNao, this.exibirOpcao, this.listaOpcoes, this.variavel);	
	}
	
	public VariavelResposta toEntityUpdate(VariavelResposta objRef) {
		objRef.setRespostaSim(respostaSim);
		objRef.setRespostaNao(respostaNao);
		objRef.setExibirOpcaoSim(exibirOpcaoSim);
		objRef.setExibirOpcaoNao(exibirOpcaoNao);
		objRef.setExibirOpcao(exibirOpcao);
		objRef.setListaOpcoes(listaOpcoes);
		objRef.setVariavel(variavel);
		return objRef;
	}
}
