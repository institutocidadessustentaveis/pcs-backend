package br.org.cidadessustentaveis.model.administracao;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="alerta_visualizado")
@EntityListeners(ListenerAuditoria.class)
public class AlertaVisualizado {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_alerta", nullable = false)
	private Alerta alerta;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_usuario", nullable = false)
	private Usuario usuario; 

	private LocalDateTime data;

}
