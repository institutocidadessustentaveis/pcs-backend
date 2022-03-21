package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="atividade_usuario")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AtividadeUsuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "atividadeUsuario_id_seq")
	@SequenceGenerator(name = "atividadeUsuario_id_seq", sequenceName = "atividadeUsuario_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="nomeusuario")
	private String nomeUsuario;
	
	@Column(name="datahora")
	private LocalDateTime dataHora;
	
	@Column(name="acao")
	private String acao;	
	
	@Column(name="modulo")
	private String modulo;	
	
	@Transient
	private String usuarioLogado;
}
