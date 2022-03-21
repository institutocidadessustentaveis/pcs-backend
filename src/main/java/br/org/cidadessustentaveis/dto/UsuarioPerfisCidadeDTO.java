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
public class UsuarioPerfisCidadeDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String telefone;
	
	private String celular;
	
	private String perfis;
	
	private String estado;
	
	private String cidade;
	
	public UsuarioPerfisCidadeDTO(Usuario obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.email = obj.getEmail();
		this.telefone = obj.getTelefone_fixo();
		this.celular = obj.getTelefone();
		this.perfis = "";
		for(Perfil perfil : obj.getCredencial().getListaPerfil()) {
			if(perfil != null && perfil.getNome() != null) {
				this.perfis += perfil.getNome() + " ";
			}
			
		}
		this.estado = obj.getPrefeitura() != null && obj.getPrefeitura().getId() != null ? obj.getPrefeitura().getCidade().getProvinciaEstado().getNome() : null;
		this.cidade = obj.getPrefeitura() != null && obj.getPrefeitura().getId() != null ? obj.getPrefeitura().getCidade().getNome() : null;
	}
}
