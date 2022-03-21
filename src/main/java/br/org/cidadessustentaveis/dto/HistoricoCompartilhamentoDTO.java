package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;

import br.org.cidadessustentaveis.model.enums.RedeSocial;
import br.org.cidadessustentaveis.model.sistema.RelatorioConteudoCompartilhado;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter @Setter
public class HistoricoCompartilhamentoDTO {

    private RedeSocial redeSocial;

    private String tipoConteudo;

    public RelatorioConteudoCompartilhadoDTO toEntity() {
        RelatorioConteudoCompartilhado historico = new RelatorioConteudoCompartilhado();
        historico.setNomeUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
        historico.setDataHora(LocalDateTime.now());
        historico.setRedeSocial(this.redeSocial.name());
        historico.setConteudoCompartilhado(this.tipoConteudo);

        return new RelatorioConteudoCompartilhadoDTO(historico);
    }

}
