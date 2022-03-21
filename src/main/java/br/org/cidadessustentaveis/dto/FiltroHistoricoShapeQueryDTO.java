package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FiltroHistoricoShapeQueryDTO {

    private List<HistoricoShape> historico;

    private Long totalCount;

}
