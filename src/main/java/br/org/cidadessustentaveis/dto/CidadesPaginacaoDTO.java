package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;

@Data
public class CidadesPaginacaoDTO {

	List<ExibirCidadeProvinciaEstadoDTO> listaCidades;

	private Long quantidadeEncontrada;

	private Long quantidadeTotal;

	public CidadesPaginacaoDTO() {

	}

	public CidadesPaginacaoDTO(List<ExibirCidadeProvinciaEstadoDTO> listaCidades, Long total) {
		this.listaCidades = listaCidades;
		this.quantidadeEncontrada = new Long(listaCidades.size());
		this.quantidadeTotal = total;
	}

}
