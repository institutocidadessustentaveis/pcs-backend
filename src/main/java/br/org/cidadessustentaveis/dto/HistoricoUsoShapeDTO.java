package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoUsoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TipoUsoShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HistoricoUsoShapeDTO {

    private Long id;

    private CidadeExibicaoDTO cidade;

    private UsuarioExibicaoDTO usuario;

    private ShapeFileDTO shape;

    private TipoUsoShape tipo;

    private LocalDateTime dataHoraAcesso;

    public HistoricoUsoShapeDTO(HistoricoUsoShape historicoUsoShape) {
        this.id = historicoUsoShape.getId();

        if(historicoUsoShape.getCidade() != null) {
            this.cidade = new CidadeExibicaoDTO(historicoUsoShape.getCidade());
        }

        if(historicoUsoShape.getUsuario() != null) {
            this.usuario = new UsuarioExibicaoDTO(historicoUsoShape.getUsuario());
        }

        if(historicoUsoShape.getShape() != null) {
            this.shape = new ShapeFileDTO(historicoUsoShape.getShape());
        }

        this.tipo = historicoUsoShape.getTipo();
        this.dataHoraAcesso = historicoUsoShape.getDataHoraAcesso();
    }

}
