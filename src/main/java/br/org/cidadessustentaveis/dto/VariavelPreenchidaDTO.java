package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VariavelPreenchidaDTO {

	private Long id;
	@NotNull
	private Long idVariavel;
	
	private Short ano;
	
	private Double valor;
	
	private Boolean respostaSimples;
	
	private Long idOpcao;

	private Long fonte;
	
	private String observacao;
	
	private String nomeVariavel;
	
	private Date dataPreenchimento;
	
	private Date dataAvaliacao;
	
	private String status;
	
	private String valorTexto;	

	private Boolean multiplaSelecao;
	
	private List<Long> idOpcoes;

	
	private String fonteTexto;
	
	private Long orgao;

	private Long subdivisao;
	
	public VariavelPreenchidaDTO(SubdivisaoVariavelPreenchida preenchida) {
		this.id = preenchida.getId();
		this.idVariavel = preenchida.getVariavel().getId();
		this.ano = preenchida.getAno();
		this.valor = preenchida.getValor();
		this.nomeVariavel = preenchida.getVariavel().getNome();
		this.respostaSimples = preenchida.getRespostaSimples();
		this.idOpcao = preenchida.getOpcao() != null ? preenchida.getOpcao().getId() : null;
		this.observacao = preenchida.getObservacao();
		this.dataPreenchimento = preenchida.getDataPreenchimento();
		this.dataAvaliacao = preenchida.getDataAvaliacao();
		this.status = preenchida.getStatus();
		this.valorTexto = preenchida.getValorTexto();
		if(null != preenchida.getInstituicaoFonte()) {
			this.fonte = preenchida.getInstituicaoFonte().getId();
			this.fonteTexto = preenchida.getInstituicaoFonte().getNome();
			this.orgao = preenchida.getInstituicaoFonte().getOrgao() != null ? preenchida.getInstituicaoFonte().getOrgao().getId() : null;
		}
		this.multiplaSelecao = preenchida.getVariavel().isMultiplaSelecao();
		if(this.multiplaSelecao) {
			this.idOpcoes = new ArrayList<>();
			if(preenchida.getOpcoes()!= null){
				preenchida.getOpcoes().forEach(op -> this.idOpcoes.add(op.getId()));
			}
		}
		this.subdivisao = preenchida.getSubdivisao().getId();
	}

	public VariavelPreenchidaDTO(VariavelPreenchida preenchida) {
		this.id = preenchida.getId();
		this.idVariavel = preenchida.getVariavel().getId();
		this.ano = preenchida.getAno();
		this.valor = preenchida.getValor();
		this.nomeVariavel = preenchida.getVariavel().getNome();
		this.respostaSimples = preenchida.getRespostaSimples();
		this.idOpcao = preenchida.getOpcao() != null ? preenchida.getOpcao().getId() : null;
		this.observacao = preenchida.getObservacao();
		this.dataPreenchimento = preenchida.getDataPreenchimento();
		this.dataAvaliacao = preenchida.getDataAvaliacao();
		this.status = preenchida.getStatus();
		this.valorTexto = preenchida.getValorTexto();
		if(null != preenchida.getInstituicaoFonte()) {
			this.fonte = preenchida.getInstituicaoFonte().getId();
			this.fonteTexto = preenchida.getInstituicaoFonte().getNome();
			this.orgao = preenchida.getInstituicaoFonte().getOrgao() != null ? preenchida.getInstituicaoFonte().getOrgao().getId() : null;
		}
		this.multiplaSelecao = preenchida.getVariavel().isMultiplaSelecao();
		if(this.multiplaSelecao) {
			this.idOpcoes = new ArrayList<>();
			if(preenchida.getOpcoes()!= null){
				preenchida.getOpcoes().forEach(op -> this.idOpcoes.add(op.getId()));
			}
		}
		
		
	}
	
	

	public VariavelPreenchidaDTO(Long ano, Long idCidade) {
		
	}
	
	public VariavelPreenchidaDTO(Double valor) {
		this.valor = valor;
	}


	
	public VariavelPreenchida toEntityInsert() {
		return new VariavelPreenchida();
	}
	
	public VariavelPreenchida toEntityUpdate(VariavelPreenchida variavelPreenchida) {
		variavelPreenchida.setValor(valor);
		variavelPreenchida.setDataAvaliacao(dataAvaliacao);
		variavelPreenchida.setStatus(status);
		return variavelPreenchida;
	}

}
