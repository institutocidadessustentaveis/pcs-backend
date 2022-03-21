package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoUsoShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FiltroHistoricoUsoShapeQueryDTO {

    private List<HistoricoUsoShape> historico;

    private Long totalCount;

}
