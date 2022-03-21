package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.administracao.FormularioAtendimento;
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
public class FormularioAtendimentoDTO {
	
	private Long id;
	
	private String nomeContato;
	
	private String emailContato;
	
	private String telContato;
	
	private String solicitacao;
	
	private LocalDateTime dataHora;
	
	private Boolean respondido;
	
	public FormularioAtendimentoDTO(FormularioAtendimento objRef) {
		this.id = objRef.getId();
		this.nomeContato = objRef.getNomeContato();
		this.emailContato = objRef.getEmailContato();
		this.telContato = objRef.getTelContato();
		this.solicitacao = objRef.getSolicitacao();
		this.dataHora = objRef.getDataHora();
		this.respondido = objRef.getRespondido();
	}
	
	public FormularioAtendimento toEntityInsert() {
		FormularioAtendimento objToInsert = new FormularioAtendimento();
		
		objToInsert.setNomeContato(this.nomeContato);
		objToInsert.setEmailContato(this.emailContato);
		objToInsert.setTelContato(this.telContato);
		objToInsert.setSolicitacao(this.solicitacao);
		objToInsert.setDataHora(LocalDateTime.now());
		objToInsert.setRespondido(false);
		
		return objToInsert;
	}
}
