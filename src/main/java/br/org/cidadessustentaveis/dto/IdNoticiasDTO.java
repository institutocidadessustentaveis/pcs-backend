package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class IdNoticiasDTO {

    private Long id;

    public IdNoticiasDTO(Noticia noticia) {
        this.id = noticia.getId();
    }

}
