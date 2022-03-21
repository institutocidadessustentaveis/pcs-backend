package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.participacaoCidada.FormularioPreenchidoResposta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormularioPreenchidoRespostaDTO {
	
	private Long id;
	private String pergunta;
	private String secao;
	private String resposta;
	private String outro;
	private String simNao;
	private String textoLivre;
	
	public FormularioPreenchidoRespostaDTO(FormularioPreenchidoResposta formulario) {
		this.id = formulario.getId();
		this.pergunta = formulario.getPergunta() != null ? formulario.getPergunta().getPergunta() : null;
		this.secao = formulario.getPergunta() != null ? (formulario.getPergunta().getSecao() != null ? formulario.getPergunta().getSecao().getNome() : null) : null;
		this.resposta = formulario.getResposta() != null ? formulario.getResposta().getResposta() : null;
		this.outro = formulario.getOutro();
		this.simNao = formulario.getSimNao() != null ? (formulario.getSimNao() == true ? "Sim" : "Não") : null;
		this.textoLivre = formulario.getTextoLivre();
	}
	
	
}


