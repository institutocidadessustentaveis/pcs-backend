package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CidadeMapaDTO {

    private String nomeCidade;
    
    private Long id;

    private double latitude;

    private double longitude;
    
    private String siglaEstado;
    
    private Long idIndicador;
    
    private String nomeIndicador;

    public String resultado;

    public Integer ordemClassificacao;

    public CidadeMapaDTO(String nomeCidade, Long id, double latitude, double longitude) {
    	this.nomeCidade = nomeCidade;
    	this.id = id;
    	this.latitude = latitude;
    	this.longitude = longitude;
    }

    public CidadeMapaDTO(String nomeCidade, Long id, double latitude, double longitude, String resultado) {
        this.nomeCidade = nomeCidade;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.resultado = resultado;
    }

    public CidadeMapaDTO(String nomeCidade, Long id, double latitude,
                         double longitude, String resultado, String ordemClassificacao) {
        this.nomeCidade = nomeCidade;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.resultado = resultado;
        this.ordemClassificacao = Integer.parseInt(ordemClassificacao);
    }

}
