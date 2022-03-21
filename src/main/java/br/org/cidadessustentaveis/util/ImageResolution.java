package br.org.cidadessustentaveis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ImageResolution {

    private Integer width;

    private Integer height;

    public ImageResolution scale(double percentage) {
        width = (int) (width * percentage);
        height = (int) (height * percentage);
        return this;
    }

}
