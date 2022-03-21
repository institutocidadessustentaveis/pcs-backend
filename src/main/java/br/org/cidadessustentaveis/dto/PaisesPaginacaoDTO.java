package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;

@Data
public class PaisesPaginacaoDTO {

	private List<PaisBuscaDTO> paises;

	private Long quantidadeEncontrada;

	private Long quantidadeTotal;

	public PaisesPaginacaoDTO() {
		
	}

	public PaisesPaginacaoDTO(List<PaisBuscaDTO> listaPaises, Long total) {
		this.paises = listaPaises;
		this.quantidadeEncontrada = new Long(listaPaises.size());
		this.quantidadeTotal = total;
	}
}
