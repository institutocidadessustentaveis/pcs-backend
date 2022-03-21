package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CidadeComparativoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nome;

    private ProvinciaEstadoBuscaDTO provinciaEstado;

    public CidadeComparativoDTO(Cidade cidade) {
        this.id = cidade.getId();
        this.nome = cidade.getNome();
        this.provinciaEstado = new ProvinciaEstadoBuscaDTO(cidade.getProvinciaEstado());
    }

}
