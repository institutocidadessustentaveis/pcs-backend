package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.administracao.RespostaAtendimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespostaAtendimentoDTO {
	
	private Long id;
	
	private String resposta;
	
	private Long idFormularioAtendimento;
	
	private Long idUsuario;
	
	private LocalDateTime dataHora;
	
	private String nomeUsuario;
	
	public RespostaAtendimentoDTO(RespostaAtendimento objRef) {
		this.id = objRef.getId();
		this.resposta = objRef.getResposta();
		this.idFormularioAtendimento = objRef.getFormularioAtendimento().getId();
		this.idUsuario = objRef.getUsuario().getId();
		this.dataHora = objRef.getDataHora();
		this.nomeUsuario = objRef.getUsuario().getNome();
	}
	
}
