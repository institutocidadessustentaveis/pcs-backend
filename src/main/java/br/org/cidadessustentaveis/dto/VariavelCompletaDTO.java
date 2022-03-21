package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelResposta;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class VariavelCompletaDTO implements Serializable{

	private static final long serialVersionUID = -4591529054547499023L;

	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private String tipo;
	
	private String unidade;
	
	private boolean variavelBasica;

	private boolean permiteImportacao;
	
	private boolean multiplaSelecao;
	
	private String nomePrefeitura;

	private VariavelCompletaRespostaDTO resposta;

	public VariavelCompletaDTO(Variavel variavel) {
		super();
		this.id = variavel.getId();
		this.nome = variavel.getNome();
		this.descricao = variavel.getDescricao();
		this.tipo = variavel.getTipo();
		this.unidade = variavel.getUnidade();
		this.variavelBasica = variavel.isVariavelBasica();
		this.permiteImportacao = variavel.isPermiteImportacao();
		this.multiplaSelecao = variavel.isMultiplaSelecao();
		if(variavel.getPrefeitura() == null) {
			this.nomePrefeitura = "PCS";
		}
		else {
			this.nomePrefeitura = "Prefeitura - " + variavel.getPrefeitura().getNome();
		}
		if(variavel.getVariavelResposta() != null) {
			resposta = new VariavelCompletaRespostaDTO(variavel.getVariavelResposta());
		}
	}
	
}
