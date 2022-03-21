package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HistoricoOperacaoBuscaDTO {

    private Long id;

    private LocalDateTime data;

    private String usuario;

    private String tipoAcao;

    private ModuloHistoricoDTO modulo;

    public HistoricoOperacaoBuscaDTO(HistoricoOperacao historico) {
        this.id = historico.getId();
        this.data = historico.getData();

        if(historico.getUsuario() != null) {
            this.usuario = historico.getUsuario().getNome();
        }

        if(historico.getTipoAcao() != null) {
            this.tipoAcao = historico.getTipoAcao().getTipo();
        }

        if(historico.getModulo() != null) {
            this.modulo = new ModuloHistoricoDTO(historico.getModulo());
        }
    }

}
