package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="aprovacao_prefeitura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(ListenerAuditoria.class)
public class AprovacaoPrefeitura implements Serializable {

	private static final long serialVersionUID = 4308277033044831477L;

	@Id @GeneratedValue(generator = "aprovacao_prefeitura_id_seq")
	@SequenceGenerator(name = "aprovacao_prefeitura_id_seq", sequenceName = "aprovacao_prefeitura_id_seq", allocationSize = 1)
	@Column(name="id", nullable = false)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_prefeitura", nullable = false)
	@JsonBackReference
	private Prefeitura prefeitura;
	
	@Column(name = "data")
	private Date data;
	
	//APROVADA, REPROVADA, PENDENTE
	@Column(name = "status")
	private String status;
	
	@Column(name = "justificativa")
	private String justificativa;
	
	@Column(name = "data_aprovacao")
	private Date dataAprovacao;
}
