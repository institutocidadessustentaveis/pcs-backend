package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioInteracaoComFerramentasDTO {

	private String nomeUsuario;
	
	private LocalDateTime dataHora;
	
	private String ferramenta;
	
	private String tipoInteracao;
	
	private String usuarioLogado;
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	
	public RelatorioInteracaoComFerramentasDTO(String nomeUsuario, LocalDateTime dataHora, String ferramenta, String tipoInteracao){
		this.nomeUsuario = nomeUsuario;
		this.dataHora = dataHora;
		this.ferramenta = ferramenta;
		this.tipoInteracao = tipoInteracao;
	}
}
