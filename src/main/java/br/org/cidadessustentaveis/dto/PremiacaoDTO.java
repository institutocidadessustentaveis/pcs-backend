package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import br.org.cidadessustentaveis.model.administracao.Premiacao;
import br.org.cidadessustentaveis.model.enums.StatusPremiacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder		
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PremiacaoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private Timestamp inicio;

	private Timestamp fim;	

	private String descricao;	
	
	private StatusPremiacao status;
	
	private ArquivoDTO bannerPremiacao;
	
	public PremiacaoDTO(Premiacao premiacao){
		this.id =  premiacao.getId();
		this.inicio = premiacao.getInicio();
		this.fim = premiacao.getFim();
		this.descricao = premiacao.getDescricao();
		this.status = premiacao.getStatus();
		this.bannerPremiacao = new ArquivoDTO (premiacao.getBannerPremiacao().getId(), premiacao.getBannerPremiacao().getNomeArquivo(),
				premiacao.getBannerPremiacao().getExtensao(),premiacao.getBannerPremiacao().getConteudo());
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
