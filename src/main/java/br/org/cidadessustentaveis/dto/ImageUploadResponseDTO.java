package br.org.cidadessustentaveis.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ImageUploadResponseDTO {

    private Long id;

    private String path;

}
