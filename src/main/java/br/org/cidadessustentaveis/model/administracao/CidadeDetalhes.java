package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity	
@Builder
@Table(name = "cidade_detalhes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class CidadeDetalhes implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "cidade_detalhes_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "cidade_detalhes_id_seq", sequenceName = "cidade_detalhes_id_seq", allocationSize = 1)
	@Column(name="codigo_ibge",nullable=false)
	private Long codigoIbge;

	@Column(name="nome")
	private String nome;

	@Column(name="nome_estado")
	private String nomeEstado;
	
	@Column(name="long")
	private Double longitude;
	
	@Column(name="lat")
	private Double latitude;
	
	@Column(name="alt")
	private Double altitude;
	
}
