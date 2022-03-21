package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PorcentagemCidadesSignatariasDTO {

	private Long idEstado;

	private String nome;

	private Double porcentagemSignatarias;

	public PorcentagemCidadesSignatariasDTO(Long idEstado, String nome, 
											Double porcentagemSignatarias) {
		this.idEstado = idEstado;
		this.nome = nome;
		this.porcentagemSignatarias = porcentagemSignatarias;
	}

}
