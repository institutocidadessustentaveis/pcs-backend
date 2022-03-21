package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CriacaoShapeDTO {

    private Boolean shapePertenceAPrefeitura;

    private Boolean temIntersecacoNaAreaDaPrefeitura;
    
    private Long idShapeFile;

}
