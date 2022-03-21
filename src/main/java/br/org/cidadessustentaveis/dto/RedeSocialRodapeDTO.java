package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.RedeSocialRodape;

import br.org.cidadessustentaveis.model.enums.RedeSocial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RedeSocialRodapeDTO {

    private Long id;

    private RedeSocial tipo;

    private String urlPerfil;

    private Integer ordem;

    public RedeSocialRodapeDTO(RedeSocialRodape rede) {
        this.id = rede.getId();
        this.tipo = rede.getTipo();
        this.urlPerfil = rede.getUrlPerfil();
        this.ordem = rede.getOrdem();
    }

    public RedeSocialRodape toEntityInsert() {
        RedeSocialRodape rede = new RedeSocialRodape();
        rede.setId(this.id);
        rede.setTipo(this.tipo);
        rede.setUrlPerfil(this.urlPerfil);
        rede.setOrdem(this.ordem);
        return rede;
    }

}
