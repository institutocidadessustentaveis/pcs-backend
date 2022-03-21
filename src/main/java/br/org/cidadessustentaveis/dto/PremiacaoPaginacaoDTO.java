package br.org.cidadessustentaveis.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiacaoPaginacaoDTO {

    private List<PremiacaoExibicaoDTO> premiacoes;

    private Long total;

}
