package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ConfirmacaoCriacaoShapeFileDTO {

    private ShapeFile shapeFile;

    private Boolean shapePertenceAPrefeitura;

    private Boolean temIntersecacoNaAreaDaPrefeitura;

}
