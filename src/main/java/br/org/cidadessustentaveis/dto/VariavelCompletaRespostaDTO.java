package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.VariavelResposta;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class VariavelCompletaRespostaDTO implements Serializable{


	private Long id;
	private Double respostaSim;
	private Double respostaNao;
	private boolean exibirOpcaoSim;
	private boolean exibirOpcaoNao;
	private boolean exibirOpcao;
	private List<VariavelCompletaOpcaoDTO> listaOpcoes;
	
	public VariavelCompletaRespostaDTO(VariavelResposta variavelResposta) {
		super();
		this.id = variavelResposta.getId();
		this.respostaSim = variavelResposta.getRespostaSim();
		this.respostaNao = variavelResposta.getRespostaNao();
		this.exibirOpcaoSim = variavelResposta.isExibirOpcaoSim();
		this.exibirOpcaoNao = variavelResposta.isExibirOpcaoNao();
		this.exibirOpcao = variavelResposta.isExibirOpcao();
		if(variavelResposta.getListaOpcoes() != null){
			this.listaOpcoes = new ArrayList<>();
			for(VariaveisOpcoes opcao: variavelResposta.getListaOpcoes()) {
				VariavelCompletaOpcaoDTO opcaoDTO = new VariavelCompletaOpcaoDTO(opcao);
				this.listaOpcoes.add(opcaoDTO);
			}
		}
		// this.listaOpcoes = variavelResposta.getListaOpcoes();
	}

}
