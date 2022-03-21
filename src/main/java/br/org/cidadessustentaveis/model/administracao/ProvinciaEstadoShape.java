package br.org.cidadessustentaveis.model.administracao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="provincia_estado_shape")
@Data @NoArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class ProvinciaEstadoShape {

	@Id @GeneratedValue(generator = "provincia_estado_shape_id_seq")
	@SequenceGenerator(name = "provincia_estado_shape_id_seq", sequenceName = "provincia_estado_shape_id_seq", allocationSize = 1)
	@Column(nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado", nullable = false)
	private ProvinciaEstado estado;

	@Column(name = "geometry")
	private String geometry;
}
