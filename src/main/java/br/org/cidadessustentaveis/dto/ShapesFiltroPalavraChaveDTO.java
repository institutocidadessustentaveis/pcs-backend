package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ShapesFiltroPalavraChaveDTO {

    private List<ShapeFileDTO> shapes;

    private Long totalCount;
    
}
