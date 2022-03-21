package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import br.org.cidadessustentaveis.model.administracao.Credencial;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class CredencialDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank
	private String login;

	private String senha;
	
	private Boolean snBloqueado;
	
	private Boolean snOnline;
	
	private Boolean snExcluido;
	
	private PerfilDTO perfil;
	
	private List<PerfilDTO> listaPerfil;
//
	public CredencialDTO(Credencial credencial) {
		this.id = credencial.getId();
		this.login = credencial.getLogin();
		this.snBloqueado = credencial.getSnBloqueado();
		this.snOnline = credencial.getSnOnline();
		this.snExcluido = credencial.getSnExcluido();
		this.listaPerfil = new ArrayList<PerfilDTO>();

		if(credencial.getListaPerfil() != null) {
			credencial.getListaPerfil().forEach(p -> this.listaPerfil.add(new PerfilDTO(p)));
		}
	}

	public Credencial toEntityInsert() {
		return new Credencial(null, this.login, this.senha, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);	
	}
	
	public Credencial toEntityUpdate(Credencial credencialRef) {
		credencialRef.setLogin(this.login);
		return credencialRef;
	}
}
