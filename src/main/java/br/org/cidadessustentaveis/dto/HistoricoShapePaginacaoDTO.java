package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HistoricoShapePaginacaoDTO {

    private List<HistoricoShapeDTO> historico;

    private Long totalCount;

}
