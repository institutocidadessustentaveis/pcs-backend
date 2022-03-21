package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class VariavelDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo nome variavel não pode ser nulo")
	private String nome;
	
	private String descricao;
	
	private String tipo;
	
	private String unidade;
	
	private boolean variavelBasica;

	private boolean permiteImportacao;
	
	private boolean multiplaSelecao;
	
	private VariavelResposta variavelResposta;
	
	private Set<ValorReferencia> variavelReferencia;
	
	private String nomePrefeitura;

	public VariavelDTO(Variavel variavel) {
		super();
		this.id = variavel.getId();
		this.nome = variavel.getNome();
		this.descricao = variavel.getDescricao();
		this.tipo = variavel.getTipo();
		this.unidade = variavel.getUnidade();
		this.variavelBasica = variavel.isVariavelBasica();
		this.permiteImportacao = variavel.isPermiteImportacao();
		this.variavelResposta = variavel.getVariavelResposta();
		this.variavelReferencia = variavel.getVariavelReferencia();
		this.multiplaSelecao = variavel.isMultiplaSelecao();
		if(variavel.getPrefeitura() == null) {
			this.nomePrefeitura = "PCS";
		}
		else {
			this.nomePrefeitura = "Prefeitura - " + variavel.getPrefeitura().getNome();
		}
	}
	
	public Variavel toEntityInsert(Prefeitura prefeitura) {
		return new Variavel(null,this.nome, this.descricao, this.tipo, this.unidade, this.variavelBasica, this.permiteImportacao, this.multiplaSelecao , this.variavelResposta, this.variavelReferencia, prefeitura);	
	}
	
	public Variavel toEntityUpdate(Variavel objRef) {
		objRef.setNome(nome);
		objRef.setDescricao(descricao);
		objRef.setTipo(tipo);
		objRef.setUnidade(unidade);
		objRef.setVariavelBasica(variavelBasica);
		objRef.setPermiteImportacao(permiteImportacao);
		objRef.setMultiplaSelecao(multiplaSelecao);
		objRef.setVariavelResposta(variavelResposta);
		objRef.setVariavelReferencia(variavelReferencia);
		objRef.getVariavelResposta().setVariavel(objRef);
		if(!objRef.getVariavelReferencia().isEmpty()) {
			objRef.getVariavelReferencia().forEach(x -> x.setVariavel(objRef));
		}
		return objRef;
	}

	public Variavel toEntityInsert() {
		return toEntityInsert(null);
	}
	
	public VariavelDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
}
