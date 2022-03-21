package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProvinciaEstadoPaginacaoDTO {

	List<ProviniciaEstadoBuscaDTO> estados;

	private Long quantidadeEncontrada;

	private Long quantidadeTotal;

	public ProvinciaEstadoPaginacaoDTO() {
		
	}

	public ProvinciaEstadoPaginacaoDTO(List<ProviniciaEstadoBuscaDTO> listaEstado, Long total) {
		this.estados = listaEstado;
		this.quantidadeEncontrada = new Long(listaEstado.size());
		this.quantidadeTotal = total;
	}
}
