package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.enums.Modulo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModuloHistoricoDTO {

    private String nome;

    private String descricao;

    public ModuloHistoricoDTO(Modulo modulo) {
        if(modulo != null) {
            this.nome = modulo.name();
            this.descricao = modulo.getDescricao();
        }
    }

}
