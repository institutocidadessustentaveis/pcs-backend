package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.org.cidadessustentaveis.model.administracao.AreaAtuacao;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class UsuarioDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	@NotBlank(message="Nome é obrigtório")
	private String nome;
	
	@Email
	@NotBlank(message="Email é obrigtório")
	private String email;
	
	@NotBlank(message="Telefone é obrigtório")
	private String telefone;

	private CredencialDTO credencial;
	
	private List<AreaInteresse> areasInteresse;
	
	private List<AreaAtuacao> areasAtuacoes;
	
	private String cidadeInteresse;
	
	private String organizacao;
	
	private String cargo;	
	
	private String tipoInstituicao;

	private String telefone_fixo;
	
	private PrefeituraDTO prefeitura;
	
	private Boolean recebeEmail;
	
	private String nomePerfil;

	private boolean online;
	
	private boolean bloqueadoForum;

	public UsuarioDTO(Usuario obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.email = obj.getEmail();
		this.telefone = obj.getTelefone();
		this.credencial = new CredencialDTO(obj.getCredencial());
		this.areasInteresse = obj.getAreasInteresse();
		this.organizacao = obj.getOrganizacao();
		this.tipoInstituicao = obj.getTipoInstituicao();
		this.prefeitura = new PrefeituraDTO(obj.getPrefeitura());
		this.telefone_fixo = obj.getTelefone_fixo();
		this.cargo = obj.getCargo();
		this.cidadeInteresse = obj.getCidadeInteresse();
		this.areasAtuacoes = obj.getAreasAtuacao();
		this.recebeEmail = obj.getRecebeEmail();
		this.bloqueadoForum = obj.isBloqueadoForum();
		if(obj.getCredencial()!= null) {
			Iterator<Perfil> it = obj.getCredencial().getListaPerfil().iterator();
			StringBuilder sb = new StringBuilder();
			while(it.hasNext()) {
				sb.append(it.next().getNome());
				if(it.hasNext()) {
					sb.append(" | ");
				}
			}
			this.nomePerfil = sb.toString();
		}
	}
	
	public UsuarioDTO(Long id, String email, String nome) {
		this.id = id;
		this.email = email;
		this.nome = nome;
	}

	
	public Usuario toEntityInsert() {
		return new Usuario(null, this.getCredencial().toEntityInsert(), this.nome, this.email, this.cargo, this.telefone, this.telefone_fixo, this.areasInteresse, this.recebeEmail);
	}
	
	
	public Usuario toEntityInsertResponsavel() {
		Usuario usuario = new Usuario();
		
		usuario.setId(null);
		usuario.setNome(this.nome);
		usuario.setEmail(this.email);
		usuario.setTelefone(this.telefone);
		usuario.setTelefone_fixo(this.telefone_fixo);
		usuario.setCargo(this.cargo);
		usuario.setCredencial(this.getCredencial().toEntityInsert());
		usuario.setRecebeEmail(this.recebeEmail);
		
		return new Usuario(usuario);
	}
	
	public Usuario toEntityInsertGestorPlataforma() {
		Usuario usuario = new Usuario();
		
		usuario.setId(null);
		usuario.setNome(this.nome);
		usuario.setTelefone(this.telefone);
		usuario.setEmail(this.email);
		usuario.setCredencial(this.getCredencial().toEntityInsert());
		usuario.setAreasInteresse(this.areasInteresse);
		usuario.setCidadeInteresse(this.cidadeInteresse);
		usuario.setOrganizacao(this.organizacao);
		usuario.setTipoInstituicao(this.tipoInstituicao);
		usuario.setTelefone_fixo(this.telefone_fixo);
		usuario.setRecebeEmail(this.recebeEmail);
		return new Usuario(usuario);
	}

	
	public Usuario toEntityInsertCidadao() {
		Usuario usuario = new Usuario();
		
		usuario.setId(null);
		usuario.setCredencial(this.getCredencial().toEntityInsert());
		usuario.setNome(this.nome);
		usuario.setTelefone(this.telefone);
		usuario.setEmail(this.email);
		usuario.setAreasInteresse(this.areasInteresse);
		usuario.setCidadeInteresse(this.cidadeInteresse);
		usuario.setAreasAtuacao(this.areasAtuacoes);
		usuario.setCargo(this.cargo);
		usuario.setOrganizacao(this.organizacao);
		usuario.setTipoInstituicao(this.tipoInstituicao);
		usuario.setCidadeInteresse(this.cidadeInteresse);
		usuario.setRecebeEmail(this.recebeEmail != null ? this.recebeEmail : true);
		
		return new Usuario(usuario);
	}
	
	public Usuario toEntityUpdate(Usuario userRef) {
		userRef.setCredencial(this.getCredencial().toEntityUpdate(userRef.getCredencial()));
		userRef.setNome(this.nome);
		userRef.setEmail(this.email);
		userRef.setTelefone(this.telefone);
		userRef.setAreasInteresse(areasInteresse);
		userRef.setOrganizacao(organizacao);
		userRef.setTipoInstituicao(tipoInstituicao);
		userRef.setRecebeEmail(this.recebeEmail);
		return userRef;
	}
	
	public Usuario toEntityUpdateBloqueadoForum(Usuario userRef) {
		userRef.setBloqueadoForum(this.bloqueadoForum);
		return userRef;
	}
	
	public Usuario toEntityUpdateUltimoComentario() {
		return new Usuario(this.id, this.getCredencial().toEntityInsert(), this.nome, this.email, this.cargo, this.telefone, this.telefone_fixo, this.areasInteresse, this.recebeEmail);
	}
	
	public UsuarioDTO(Long id) {
		this.id = id;
	}
}
