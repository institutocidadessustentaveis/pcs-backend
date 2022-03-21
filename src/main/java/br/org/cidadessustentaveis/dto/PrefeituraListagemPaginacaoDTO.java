package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PrefeituraListagemPaginacaoDTO {

    private List<PrefeituraListagemDTO> prefeituras;

    private Long total;

}
