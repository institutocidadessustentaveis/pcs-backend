package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BoundingBoxDTO {

    private Double minX;

    private Double maxX;

    private Double minY;

    private Double maxY;

    private String crs;

    public String toString() {
        return minX + "," + minY + "," + maxX + "," + maxY;
    }

}
