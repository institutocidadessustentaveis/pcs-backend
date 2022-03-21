package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.enums.StatusPremiacao;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Builder	
@Table(name = "premiacao")
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Premiacao implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "premiacao_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "premiacao_id_seq", sequenceName = "premiacao_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;	

	@Column(name="inicio")
	private Timestamp inicio;
	
	@Column(name="fim")
	private Timestamp fim;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private StatusPremiacao status;

	@OneToOne(fetch= FetchType.LAZY,cascade=CascadeType.ALL)	
	@JoinColumn(name="banner_premiacao", nullable = false)
	private Arquivo bannerPremiacao;
}
