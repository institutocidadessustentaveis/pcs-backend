package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CidadesPorEstadoDTO {

	private Long idEstado;

	private String nome;

	private Long quantidadeCidades;

	public CidadesPorEstadoDTO(Long idEstado, String nome, Long quantidadeCidades) {
		this.idEstado = idEstado;
		this.nome = nome;
		this.quantidadeCidades = quantidadeCidades;
	}

}
