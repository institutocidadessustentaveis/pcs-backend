package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@NoArgsConstructor
public class HistoricoOperacaoPaginacaoDTO {

    private List<HistoricoOperacaoBuscaDTO> historico;

    private Long total;

}
