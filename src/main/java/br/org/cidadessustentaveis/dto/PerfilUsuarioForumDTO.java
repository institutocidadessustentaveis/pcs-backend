package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.PerfilUsuarioForum;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PerfilUsuarioForumDTO {
	
	private Long id;
	
    @NotNull
	private Long idUsuario;
	
    @NotNull
	private Long idCidade;
    
    @NotNull
	private String nomeCidade;
    
    @NotNull
    @NotEmpty
    private String email;
    
    @NotNull
    @NotEmpty
    private String nome;
    
    public PerfilUsuarioForum toEntityInsert(PerfilUsuarioForumDTO perfilUsuarioForumDTO ) {
    	PerfilUsuarioForum perfilUsuarioForum =  new PerfilUsuarioForum(null, null, null, email, nome);
    	  	
    	return perfilUsuarioForum;
    }
    
    public PerfilUsuarioForum toEntityUpdate(PerfilUsuarioForum perfilUsuarioForum) {
    	perfilUsuarioForum.setEmail(this.email);
    	perfilUsuarioForum.setNome(this.nome);
    	

    	return perfilUsuarioForum;
    }
    
    public PerfilUsuarioForumDTO(PerfilUsuarioForum perfilUsuarioForum) {
    	this.id = perfilUsuarioForum.getId();
    	this.idUsuario = perfilUsuarioForum.getUsuario().getId();
    	this.idCidade = perfilUsuarioForum.getCidade() != null ? perfilUsuarioForum.getCidade().getId() : null;
    	this.nomeCidade = perfilUsuarioForum.getCidade() != null ? perfilUsuarioForum.getCidade().getNome() : null;
    	this.email = perfilUsuarioForum.getEmail();
    	this.nome = perfilUsuarioForum.getNome();
    }
    
    //Query buscarPerfilPorIdUsuario()
    public PerfilUsuarioForumDTO(Long id, Usuario usuario, Cidade cidade, String email, String nome) {
    	this.id = id;
    	this.idUsuario = usuario.getId();
    	this.email = email;
    	this.nome = nome;
    	if (cidade != null) {
    		this.nomeCidade = cidade.getNome();
    		this.idCidade = cidade.getId();
    	}
    }

}
