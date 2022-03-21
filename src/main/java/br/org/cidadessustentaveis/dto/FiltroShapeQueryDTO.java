package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FiltroShapeQueryDTO {

    private List<ShapeFile> shapes;

    private Long totalCount;

}
