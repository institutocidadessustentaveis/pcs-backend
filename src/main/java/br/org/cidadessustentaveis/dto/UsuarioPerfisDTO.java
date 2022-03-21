package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter @Setter @NoArgsConstructor
public class UsuarioPerfisDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String telefone;	
	
	private String perfis;
	
	public UsuarioPerfisDTO(Usuario obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.email = obj.getEmail();
		this.telefone = obj.getTelefone();
		for(Perfil perfil : obj.getCredencial().getListaPerfil()) {
			if(perfil != null && perfil.getNome() != null) {
				this.perfis += perfil.getNome() + " ";
			}
			
		}
		
	}
}
