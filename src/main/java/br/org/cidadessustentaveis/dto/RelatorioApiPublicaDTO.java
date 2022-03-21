package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatorioApiPublicaDTO {
	
	private Long id;
	
	private String endpoint;
	
	private LocalDateTime dataHora;
	
	private String origemRequisicao;

	//campos para filtro
	
	private LocalDateTime dataInicio;
	
	private LocalDateTime dataFim;
	
	public RelatorioApiPublicaDTO(Long id, String endpoint, LocalDateTime dataHora, String origemReq) {
		this.id = id;
		this.endpoint = endpoint;
		this.dataHora = dataHora;
		this.origemRequisicao = origemReq;
	}
}


