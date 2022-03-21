package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.participacaoCidada.Resposta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespostaDTO {
	private Long id;
	private String resposta;
	private Boolean outro;
	
	public Resposta toEntity(){
		if(this.id != null && this.id <=0 ) {
			this.id = null;
		}
		Resposta resposta = new Resposta(null, this.resposta,null, this.outro != null ? this.outro : false);
		return resposta;
	}

	public RespostaDTO(Resposta resposta) {
		super();
		this.id = resposta.getId();
		this.resposta = resposta.getResposta();
		this.outro = resposta.getOutro();
	}
	
	
}
