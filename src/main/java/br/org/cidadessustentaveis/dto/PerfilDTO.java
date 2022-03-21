package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PerfilDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo nome não pode ser nulo")
	private String nome;
	
	private Boolean padrao;
	
	private Boolean gestaoPublica;

	private List<PermissaoDTO> permissoes;

	public PerfilDTO(Perfil perfil) {
		super();
		this.id = perfil.getId();
		this.nome = perfil.getNome();
		this.padrao = perfil.getPadrao();
		this.gestaoPublica = perfil.getGestaoPublica();
		this.permissoes = perfil.getPermissoes() != null ? perfil.getPermissoes().stream().map(obj -> new PermissaoDTO(obj)).collect(Collectors.toList()) : null;
	}
	
	
	public Perfil toEntityInsert() {
		return new Perfil(
				this.id != null ? this.id : null,
				this.nome,
				this.gestaoPublica != null ? this.gestaoPublica : false,
				Boolean.FALSE,
				this.permissoes != null ? this.permissoes.stream().map(obj -> obj.toEntityInsert()).collect(Collectors.toList()) : null
				);
	}
	
	public Perfil toEntityUpdate(Perfil perfilRef) {
		perfilRef.setNome(this.nome);
		perfilRef.setGestaoPublica(this.gestaoPublica);
		return perfilRef;
	}
	
}
