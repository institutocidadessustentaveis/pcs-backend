package br.org.cidadessustentaveis.dto;

import java.util.Date;

import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class AprovacaoPrefeituraDTO {

	private Long id;
	
	private PrefeituraDTO prefeitura;
	
	private Date data;
	
	private Date dataAprovacao;
	
	private String status;
	
	private String justificativa;
	
	public AprovacaoPrefeituraDTO(Long id) {
		super();
		this.id = id;
	}
	
	public AprovacaoPrefeituraDTO(AprovacaoPrefeitura aprovacao) {
		super();
		this.id = aprovacao.getId();
		this.status = aprovacao.getStatus();
		this.data = aprovacao.getData();
		this.dataAprovacao = aprovacao.getDataAprovacao();
		this.prefeitura = new PrefeituraDTO(aprovacao.getPrefeitura());
		this.justificativa = aprovacao.getJustificativa();
	}

	@Setter
	@Getter
	public static class JustificativaDTO {
		private String justificativa;
	}
}
