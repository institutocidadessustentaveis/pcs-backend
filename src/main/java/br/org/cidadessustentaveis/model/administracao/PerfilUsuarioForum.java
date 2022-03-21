package br.org.cidadessustentaveis.model.administracao;

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
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(ListenerAuditoria.class)
@Table(name="perfil_usuario_forum") 
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor 
public class PerfilUsuarioForum  implements Serializable{

	private static final long serialVersionUID = 5548619526094321104L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;
	
	@Column(name="email_contato")
	private String email;
	
	@Column(name="nome_contato")
	private String nome;
	
	

}
