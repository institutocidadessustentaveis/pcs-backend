package br.org.cidadessustentaveis.model.boaspraticas;

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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="sugestao_boa_pratica")
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @Data @NoArgsConstructor @Builder
public class SugestaoBoasPraticas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@SequenceGenerator(name = "sugestao_boas_praticas_id_seq", sequenceName = "sugestao_boas_praticas_id_seq", allocationSize = 1)
	@Column(nullable = false)
	private Long id;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="descricao")
	private String descricao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario")
	private Usuario usuario;
	
	
	public SugestaoBoasPraticas(Long idSugestaoBoasPraticas, String titulo, String descricao, Usuario usuario) {
		super();
		this.id = idSugestaoBoasPraticas;
		this.titulo = titulo;
		this.descricao = descricao;
		this.usuario = usuario;
	}
	
	public SugestaoBoasPraticas(Long id, String titulo, String descricao, String nomeUsuario) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		Usuario user = new Usuario();
		user.setNome(nomeUsuario);
		this.usuario = user;
	}
	
}
