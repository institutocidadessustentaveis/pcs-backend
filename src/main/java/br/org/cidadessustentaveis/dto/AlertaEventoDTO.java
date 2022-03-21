package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.eventos.AlertaEvento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertaEventoDTO {
	
	private Long id;
	private Long idEvento;
	private String titulo;
	private String descricao;
	private Integer qtdDias;
	private String imagem;
	private EventoDTO evento;
	private Boolean enviado;
	private LocalDate dataEnviar;
	private LocalDate dataEnvio;
	private Boolean apenasPrefeitura;
	private List<Long> perfis;

	public AlertaEventoDTO(AlertaEvento alerta) {
		this.id = alerta.getId();
		this.idEvento = alerta.getEvento().getId();
		this.titulo = alerta.getTitulo();
		this.descricao = alerta.getDescricao();
		this.qtdDias = alerta.getQtdDias();
		this.imagem = null;
		this.evento = alerta.getEvento() != null ? new EventoDTO(alerta.getEvento()) : null;
		this.apenasPrefeitura = alerta.getApenasPrefeitura();
		if( alerta.getPerfis() != null ) {
			this.perfis = new ArrayList<>();
			for(Perfil p : alerta.getPerfis()) {
				this.perfis.add(p.getId());
			}
		}
	}
}
