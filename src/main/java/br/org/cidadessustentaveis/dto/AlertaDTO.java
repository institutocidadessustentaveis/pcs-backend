package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class AlertaDTO {
	private Long id;
	private String mensagem;
	private String link;
	private Boolean visualizado;
	private String tipo;
	private LocalDateTime data;
	private CidadeDTO cidade;
	
	public Alerta createEntity(){
		return Alerta.builder().id(id).mensagem(mensagem).link(link).tipoAlerta(TipoAlerta.fromString(tipo)).data(data).cidade(null).build();
	}

}
