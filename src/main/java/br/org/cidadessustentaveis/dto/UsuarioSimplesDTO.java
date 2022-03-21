package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class UsuarioSimplesDTO {
	private Long id;
	private String nome;
	private String email;
	private String cargo;
	private String telefone;
	private String celular;
	private List<Long> perfis;
	private Long idCidade;	
	

	public UsuarioSimplesDTO(Usuario usuario) {
		id = usuario.getId();
		nome = usuario.getNome();
		email = usuario.getEmail();
		cargo = usuario.getCargo();
		telefone = usuario.getTelefone_fixo();
		celular = usuario.getTelefone();
		perfis = new ArrayList<>();
		if(usuario.getCredencial() != null && usuario.getCredencial().getListaPerfil() != null ) {
			for(Perfil p : usuario.getCredencial().getListaPerfil()) {
				perfis.add(p.getId());
			}
		}
		if(usuario.getPrefeitura() != null && usuario.getPrefeitura().getCidade() != null) {
			idCidade = usuario.getPrefeitura().getCidade().getId();
		}
	}
}
