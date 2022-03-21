package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Premiacao;
import br.org.cidadessustentaveis.model.administracao.PremiacaoPrefeitura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder		
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PremiacaoPrefeituraDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String prefeitura;
	
	private String cidade;

	private String premiacao;
	
	public PremiacaoPrefeituraDTO(PremiacaoPrefeitura premiacaoPrefeitura){
		this.id =  premiacaoPrefeitura.getId();
		this.prefeitura = premiacaoPrefeitura.getPrefeitura().getNome();
		this.cidade = premiacaoPrefeitura.getPrefeitura().getCidade().getNome();
		this.premiacao = premiacaoPrefeitura.getPremiacao().getDescricao();
	}

	
	public PremiacaoPrefeituraDTO toEntityInsert() {
		return new PremiacaoPrefeituraDTO(null, this.getPrefeitura(), this.getCidade(), this.getPremiacao());
	}
	
	public PremiacaoPrefeituraDTO toEntityUpdate(PremiacaoPrefeituraDTO premiacaoPrefeituraRef) {
		premiacaoPrefeituraRef.setPrefeitura(this.prefeitura);
		premiacaoPrefeituraRef.setPremiacao(this.premiacao);
		premiacaoPrefeituraRef.setCidade(this.cidade);

		return premiacaoPrefeituraRef;
	}
	

}
