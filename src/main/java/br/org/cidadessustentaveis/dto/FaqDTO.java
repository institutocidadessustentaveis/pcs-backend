package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.participacaoCidada.Faq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FaqDTO {
	
	private Long id;
	
	@NotNull
	@NotEmpty
	private String pergunta;
	
	@NotNull
	@NotEmpty
	private String resposta;
	
	public FaqDTO(Faq faq) {
		this.id = faq.getId();
		this.pergunta = faq.getPergunta();
		this.resposta = faq.getResposta();
	}
	
	public FaqDTO(Long id, String pergunta, String resposta) {
		this.id = id;
		this.pergunta = pergunta;
		this.resposta = resposta;
	}
	
	public Faq toEntityInsert(FaqDTO faq) {
		return new Faq(null, pergunta, resposta);
	}
	
	public Faq toEntityUpdate(Faq faq) {
		faq.setPergunta(this.pergunta);
		faq.setResposta(this.resposta);
		return faq;
	}
	
}
