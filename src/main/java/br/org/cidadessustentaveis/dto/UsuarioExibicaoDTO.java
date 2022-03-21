package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioExibicaoDTO {

    private Long id;

    private String nome;

    private String email;

    public UsuarioExibicaoDTO(Usuario usuario) {
        if(usuario == null) throw new IllegalArgumentException("Usuário não pode ser nulo");

        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

}
