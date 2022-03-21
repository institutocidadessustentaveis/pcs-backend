package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AprovacaoPrefeituraFiltroDTO {

	private String nomePrefeitura;
	private String dataInicioMandato;
	private String dataFimMandato;
	private String status;
	private String dataPedidoCadastramento;
}
