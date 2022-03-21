package br.org.cidadessustentaveis.dto;


import br.org.cidadessustentaveis.model.administracao.Cidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CidadeExibicaoDTO {

    private Long id;

    private String nome;

    private String nomeEstado;

    private String siglaEstado;

    private String nomePais;

    private Boolean signataria;

    private Double latitude;
    
    private Double longitude;

    public CidadeExibicaoDTO(Cidade cidade) {
        this.id = cidade.getId();
        this.nome = cidade.getNome();

        if(cidade.getProvinciaEstado() != null) {
            this.nomeEstado = cidade.getProvinciaEstado().getNome();
            this.siglaEstado = cidade.getProvinciaEstado().getSigla();

            if(cidade.getProvinciaEstado().getPais() != null) {
                this.nomePais = cidade.getProvinciaEstado().getPais().getNome();
            }
        }
        this.latitude = cidade.getLatitude();
        this.longitude = cidade.getLongitude();

        this.signataria = cidade.getIsSignataria();
    }

}
