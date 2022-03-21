package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.participacaoCidada.Pergunta;
import br.org.cidadessustentaveis.model.participacaoCidada.Resposta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerguntaDTO {
	private Long id;
	private Integer ordem;
	private String pergunta;
	private String tipo; //Sim ou Não, Multiplas Opções, Texto Live, Multiplas Opções com campo "outro";
	private Boolean multiplaSelecao;
	private List<RespostaDTO> respostas;
	
	
	
	public Pergunta toEntity(){
		if(this.id != null && this.id <=0 ) {
			this.id = null;
		}
		List<Resposta> listaRespostas = new ArrayList<>();
		if (this.respostas != null ) {
			this.respostas.forEach(r -> {
				listaRespostas.add(r.toEntity());
			});
		}
		Pergunta pergunta = new Pergunta(null, this.ordem, this.pergunta, this.tipo, this.multiplaSelecao, null,null);
		listaRespostas.forEach(r -> r.setPergunta(pergunta));
		pergunta.setRespostas(listaRespostas);
		return pergunta;
	}



	public PerguntaDTO(Pergunta pergunta) {
		super();
		this.id = pergunta.getId();
		this.ordem = pergunta.getOrdem();
		this.pergunta = pergunta.getPergunta();
		this.tipo = pergunta.getTipo();
		this.multiplaSelecao = pergunta.getMultiplaSelecao();
		this.respostas = new ArrayList<>();
		if(pergunta.getRespostas() != null) {
			pergunta.getRespostas().forEach(r -> this.respostas.add(new RespostaDTO(r)));
		}
	}
	
	
}
