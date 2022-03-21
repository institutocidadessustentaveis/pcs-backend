package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.Premiacao;
import br.org.cidadessustentaveis.model.enums.StatusPremiacao;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Builder		
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PremiacaoExibicaoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private Timestamp inicio;

	private Timestamp fim;

	private String descricao;

	private StatusPremiacao status;

	private ArquivoDTO bannerPremiacao;

	public PremiacaoExibicaoDTO(Premiacao premiacao){
		this.id =  premiacao.getId();
		this.inicio = premiacao.getInicio();
		this.fim = premiacao.getFim();
		this.descricao = premiacao.getDescricao();
		this.status = premiacao.getStatus();
	}

	public Premiacao toEntityInsert() {
		return new Premiacao(null, this.getInicio(), this.getFim(), this.getDescricao(),this.getStatus(), this.bannerPremiacao.toEntityInsert());
	}
	
	public Premiacao toEntityUpdate(Premiacao premiacaoRef) {
		premiacaoRef.setInicio(this.inicio);
		premiacaoRef.setFim(this.fim);
		premiacaoRef.setDescricao(this.descricao);
		premiacaoRef.setStatus(this.status);
		return premiacaoRef;
	}
	

}
