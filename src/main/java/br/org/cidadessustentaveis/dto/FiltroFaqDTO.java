package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.participacaoCidada.Faq;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter @NoArgsConstructor
public class FiltroFaqDTO {

private Long idFaq;
	
	public FiltroFaqDTO(Long idFaq) {
		this.idFaq = idFaq;
	}
	
	public FiltroFaqDTO(Faq faq) {
		this.idFaq = faq.getId();
	}

}
