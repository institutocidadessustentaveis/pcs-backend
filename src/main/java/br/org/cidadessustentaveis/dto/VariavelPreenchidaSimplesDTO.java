package br.org.cidadessustentaveis.dto;


import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.util.VariavelPreenchidaUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VariavelPreenchidaSimplesDTO {

	private Long id;
	@NotNull
	private Long idVariavel;
	
	private Short ano;
	
	private String valor;
	
	private String nomeVariavel;
	
	private String status;

	private String observacao;

	private String fonte;
	
	
	public VariavelPreenchidaSimplesDTO(VariavelPreenchida preenchida) {
		this.id = preenchida.getId();
		this.idVariavel = preenchida.getVariavel().getId();
		this.ano = preenchida.getAno();
		this.valor = VariavelPreenchidaUtil.valorApresentacao(preenchida);
		this.nomeVariavel = preenchida.getVariavel().getNome();
		this.status = preenchida.getStatus();
		this.observacao = preenchida.getObservacao();
		this.observacao = preenchida.getObservacao();
		if ( preenchida.getInstituicaoFonte() != null ){
			this.fonte = preenchida.getInstituicaoFonte().getNome();
		}else if(preenchida.getFonteMigracao() != null){
			this.fonte = preenchida.getFonteMigracao();
		}
	}

	
	public VariavelPreenchidaSimplesDTO(SubdivisaoVariavelPreenchida preenchida) {
		this.id = preenchida.getId();
		this.idVariavel = preenchida.getVariavel().getId();
		this.ano = preenchida.getAno();
		this.valor = VariavelPreenchidaUtil.valorApresentacao(preenchida);
		this.nomeVariavel = preenchida.getVariavel().getNome();
		this.status = preenchida.getStatus();
		this.observacao = preenchida.getObservacao();
		if ( preenchida.getInstituicaoFonte() != null ){
			this.fonte = preenchida.getInstituicaoFonte().getNome();
		}else if(preenchida.getFonteMigracao() != null){
			this.fonte = preenchida.getFonteMigracao();
		}
		
	}
	
	

}
