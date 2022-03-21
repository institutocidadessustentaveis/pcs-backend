package br.org.cidadessustentaveis.model.indicadores;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="plano_de_metas") 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class PlanoDeMetas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "plano_de_metas_id_seq")
	@SequenceGenerator(name = "plano_de_metas_id_seq", sequenceName = "plano_de_metas_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="status")
	private String statusPlanoDeMetas;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefeitura", nullable = false)
	@JsonBackReference
	private Prefeitura prefeitura;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="planoDeMetas", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<PlanoDeMetasDetalhado> planosDeMetasDetalhados;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name="data_criacao")
	private Date dataCriacao;
	
	@Column(name="apresentacao")
	private String apresentacao;
	
	@Column(name="descricao")
	private String descricao;
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="arquivo", nullable = false)
	private Arquivo arquivo;
	
}
