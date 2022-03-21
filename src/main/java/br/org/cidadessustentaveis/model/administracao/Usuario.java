package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.model.noticias.NoticiaHistorico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuario")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "usuario_id_seq")
	@SequenceGenerator(name = "usuario_id_seq", sequenceName = "usuario_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String telefone;
	
	private String organizacao;
	
	private String cargo;
	
	private String telefone_fixo;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="prefeitura", nullable = false)
	private Prefeitura prefeitura;
	
	@Column(name = "tipo_instituicao")
	private String tipoInstituicao;
	
	@Column(name = "cidade_interesse")
	private String cidadeInteresse;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="credencial", nullable=false)
	@JsonManagedReference
	private Credencial credencial;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "usuario_area_interesse", 
        joinColumns = { @JoinColumn(name = "id_usuario") }, 
        inverseJoinColumns = { @JoinColumn(name = "id_area_interesse") } )
	private List<AreaInteresse> areasInteresse;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "usuario_area_atuacao", 
        joinColumns = { @JoinColumn(name = "id_usuario") }, 
        inverseJoinColumns = { @JoinColumn(name = "id_area_atuacao") } )
	private List<AreaAtuacao> areasAtuacao;
	
	@Column(name = "recebe_email")
	private Boolean recebeEmail;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="usuario", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<NoticiaHistorico> noticiaHistorico;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="usuario", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Noticia> noticia;

	@Column(name = "bloqueado_forum")
	private boolean bloqueadoForum;
	
	public Usuario(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.email = usuario.getEmail();
		this.telefone = usuario.getTelefone();
		this.cidadeInteresse = usuario.getCidadeInteresse();
		this.areasInteresse = usuario.getAreasInteresse();
		this.credencial = usuario.getCredencial();
		this.areasAtuacao = usuario.getAreasAtuacao();
		this.cargo = usuario.getCargo();
		this.organizacao = usuario.getOrganizacao();
		this.tipoInstituicao = usuario.getTipoInstituicao();
		this.telefone_fixo = usuario.getTelefone_fixo();
		this.recebeEmail = usuario.getRecebeEmail();
		this.bloqueadoForum = usuario.isBloqueadoForum();
	}
	
	public Usuario(Long id, Credencial credencial, String nome, String email, String cargo, String telefone, String telefoneFixo, List<AreaInteresse> areasAtuacao, Boolean recebeEmail) {
		super();
		this.id = id;
		this.credencial = credencial;
		this.nome = nome;
		this.email = email;
		this.cargo = cargo;
		this.telefone = telefone;
		this.telefone_fixo = telefoneFixo;
		this.areasInteresse = areasAtuacao;
		this.recebeEmail = recebeEmail;
	}	
	
	public Usuario(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	
}
