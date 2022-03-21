package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ODSOrdenadoDTO {

    private Long id;

    private int numero;

    private String titulo;

    private String subtitulo;

    private String descricao;

    private String icone;

    private String iconeReduzido;

    private List<MetaObjetivoDesenvolvimentoSustentavel> metas;

    public ODSOrdenadoDTO(ObjetivoDesenvolvimentoSustentavel ods) {
        super();
        this.id = ods.getId();
        this.numero = ods.getNumero();
        this.titulo = ods.getTitulo();
        this.subtitulo = ods.getSubtitulo();
        this.descricao = ods.getDescricao();
        this.icone = ods.getIcone();
        this.iconeReduzido = ods.getIconeReduzido();
    }
}
