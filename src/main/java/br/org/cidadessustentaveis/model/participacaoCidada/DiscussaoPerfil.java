package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="discussao_perfil")
@EntityListeners(ListenerAuditoria.class)
public class DiscussaoPerfil implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_perfil")
	@JsonBackReference()
	private Perfil perfil;
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name="id_forum_discussao")
	@JsonBackReference()
	private ForumDiscussao forumDiscussao;
	
	@Column(name="autorizacao")
	private String autorizacao;
	
}
