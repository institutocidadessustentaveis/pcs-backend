package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.org.cidadessustentaveis.dto.RespostaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resposta implements Serializable{
	private static final long serialVersionUID = -2281215853588115890L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String resposta;
	@ManyToOne
    @JoinColumn(name = "pergunta")
	private Pergunta pergunta;
	private Boolean outro;
	
	public void atualizar(RespostaDTO dto) {
		this.resposta = dto.getResposta();
		this.outro = dto.getOutro();
	}
}
