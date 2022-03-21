package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class CidadeComboDTO {

    private Long id;
    
    private String nomeCidade;

	public CidadeComboDTO(Long id, String nomeCidade, String siglaEstado) {
		this.id = id;
		this.nomeCidade = nomeCidade + " - " + siglaEstado;
	}

}
