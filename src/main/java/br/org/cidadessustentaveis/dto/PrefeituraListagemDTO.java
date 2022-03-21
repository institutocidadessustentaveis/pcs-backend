package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.CartaCompromisso;
import br.org.cidadessustentaveis.model.administracao.PartidoPolitico;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PrefeituraListagemDTO {

    private Long id;

    private String nomePrefeito;

    private String nomeCidade;

    private String nomeEstado;

    private String email;
    
    private AprovacaoPrefeitura aprovacaoPrefeitura;

    private String telefone;

    private String partido;

    private LocalDate inicioMandato;

    private LocalDate fimMandato;

    public PrefeituraListagemDTO(Prefeitura prefeitura) {
    	this.aprovacaoPrefeitura = prefeitura.getAprovacaoPrefeitura();
        this.id = prefeitura.getId();

        this.nomePrefeito = prefeitura.getNome();

        if(prefeitura.getCidade() != null) {
            this.nomeCidade = prefeitura.getCidade().getNome();

            if(prefeitura.getCidade().getProvinciaEstado() != null) {
                this.nomeEstado = prefeitura.getCidade().getProvinciaEstado().getNome();
            }
        }

        this.email = prefeitura.getEmail();
        this.telefone = prefeitura.getTelefone();

        if(prefeitura.getPartidoPolitico() != null) {
            this.partido = prefeitura.getPartidoPolitico().getSiglaPartido()
                            + " - " +
                            prefeitura.getPartidoPolitico().getNome();
        }

        this.inicioMandato = prefeitura.getInicioMandato();
        this.fimMandato = prefeitura.getFimMandato();
    }
}
