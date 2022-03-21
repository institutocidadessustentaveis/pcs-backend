package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.Iterator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.org.cidadessustentaveis.model.administracao.Credencial;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioCadastroDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private boolean online;
	
	@NotBlank(message="Nome é obrigtório")
	private String nome;

	private String nomePerfil;
	
	private String cidade;
	private String uf;
	
	@Email
	@NotBlank(message="Email é obrigtório")
	private String email;
	
	@NotBlank(message="Telefone é obrigtório")
	private String telefone;
	
	private Long totalElements;
	
	private boolean bloqueadoForum;
	
	private Long codigoIbge;


	public UsuarioCadastroDTO(Usuario obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.email = obj.getEmail();
		this.telefone = obj.getTelefone();
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
		if(obj.getPrefeitura() != null) {
			this.cidade = obj.getPrefeitura().getCidade().getNome();
			this.uf = obj.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
		}
	}
	
	public UsuarioCadastroDTO(Long id, String nome, String telefone, String email, String nomeCidade, String siglaUf, Credencial credencial) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.bloqueadoForum = credencial.getSnBloqueado();
		if(credencial != null) {
			Iterator<Perfil> it = credencial.getListaPerfil().iterator();
			StringBuilder sb = new StringBuilder();
			while(it.hasNext()) {
				sb.append(it.next().getNome());
				if(it.hasNext()) {
					sb.append(" | ");
				}
			}
			this.nomePerfil = sb.toString();
		}
		
		this.cidade = nomeCidade;
		this.uf = siglaUf;
		
	}
	
	public UsuarioCadastroDTO(Long id, String nome, String telefone, String email, String nomeCidade, String siglaUf, Credencial credencial, Long codigoIbge) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.bloqueadoForum = credencial.getSnBloqueado();
		if(credencial != null) {
			Iterator<Perfil> it = credencial.getListaPerfil().iterator();
			StringBuilder sb = new StringBuilder();
			while(it.hasNext()) {
				sb.append(it.next().getNome());
				if(it.hasNext()) {
					sb.append(" | ");
				}
			}
			this.nomePerfil = sb.toString();
		}
		
		this.cidade = nomeCidade;
		this.uf = siglaUf;
		this.codigoIbge = codigoIbge;
	}

}
