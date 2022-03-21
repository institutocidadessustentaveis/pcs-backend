package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(ListenerAuditoria.class)
@Table(name="comentario_discussao", schema = "public")
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor
public class ComentarioDiscussao  implements Serializable{

	private static final long serialVersionUID = -1635819021965912891L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="forum_discussao", nullable = false)
	private ForumDiscussao forumDiscussao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name="nome_usuario")
	private String nomeUsuario;
	
	@Column(name="comentario")
	private String comentario;
	
	@Column(name="data_publicacao")
	private LocalDate dataPublicacao;
	
	@Column(name="horario_publicacao")
	private LocalTime horarioPublicacao;
	
	@Column(name="editado")
	private Boolean editado;
	
	@Column(name="data_edicao")
	private LocalDate dataEdicao;
	
	@Column(name="horario_edicao")
	private LocalTime horarioEdicao;
	
}
