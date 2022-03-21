package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HistoricoShapeDTO {

    private Long id;

    private String nomeArquivo;

    private UsuarioExibicaoDTO usuario;

    private TemaGeoespacialExibicaoDTO tema;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataEdicao;

    public HistoricoShapeDTO(HistoricoShape shape) {
        if(shape == null) throw new IllegalArgumentException("Shape não pode ser nulo");

        this.id = shape.getId();
        this.nomeArquivo = shape.getNomeArquivo();

        if(shape.getUsuario() != null) {
            this.usuario = new UsuarioExibicaoDTO(shape.getUsuario());
        }


        if(shape.getTema() != null) {
            this.tema = new TemaGeoespacialExibicaoDTO(shape.getTema());
        }

        this.dataCriacao = shape.getDataCriacao();
        this.dataEdicao = shape.getDataEdicao();
    }

}
