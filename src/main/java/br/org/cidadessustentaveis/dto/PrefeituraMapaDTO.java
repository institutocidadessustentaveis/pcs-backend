package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrefeituraMapaDTO {

    private Long id;

    private Long idCidade;

    private String cidade;

    private String provinciaEstado;

    private String candidato;

    private String partido;

    private double latitude;

    private double longitude;

    private String siglaEstado;
    
    private Long populacao;
    
    public PrefeituraMapaDTO(Prefeitura prefeitura) {
        super();
        if(prefeitura!= null) {
            this.id = prefeitura.getId();
            this.candidato = prefeitura.getNome();

            if(prefeitura.getPartidoPolitico() != null) {
                this.partido = prefeitura.getPartidoPolitico().getSiglaPartido()
                                + " - " +
                                prefeitura.getPartidoPolitico().getNome();
            }

            if(prefeitura.getCidade() != null) {
                this.idCidade = prefeitura.getCidade().getId();
                this.cidade = prefeitura.getCidade().getNome();
                this.populacao = prefeitura.getCidade().getPopulacao();
                this.latitude = prefeitura.getCidade().getLatitude();
                this.longitude = prefeitura.getCidade().getLongitude();

                if(prefeitura.getCidade().getProvinciaEstado() != null) {
                    this.provinciaEstado = prefeitura.getCidade().getProvinciaEstado().getNome();
                    this.siglaEstado = prefeitura.getCidade().getProvinciaEstado().getSigla();
                }
            }
        }
    }

}
