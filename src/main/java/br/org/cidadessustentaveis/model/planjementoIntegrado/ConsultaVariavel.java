package br.org.cidadessustentaveis.model.planjementoIntegrado;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consulta_variaveis")
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConsultaVariavel implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "consulta_variaveis_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "consulta_variaveis_id_seq", sequenceName = "consulta_variaveis_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="variavel")
	private Long idVariavel;
	
	@Column(name="valor_preenchido")
	private String valorPreenchido;
	
	@Column(name="ano_selecionado")
	private Long anoSelecionado;
	
	@Column(name="visualizar")
	private Boolean visualizarComoPontos;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_usuario", nullable = false)
	private Usuario usuario;
}
