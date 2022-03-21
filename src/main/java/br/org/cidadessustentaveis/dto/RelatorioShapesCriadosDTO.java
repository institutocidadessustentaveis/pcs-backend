package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioShapesCriadosDTO {
	
	private Long idShape;
	
	private LocalDateTime dataCriacao;
	
	private LocalDateTime dataEdicao;
	
	private String usuario;
	
	private String cidade;
	
	private String tituloShape;

	public RelatorioShapesCriadosDTO(Long idShape, LocalDateTime dataCriacao, LocalDateTime dataEdicao, String usuario,
			String tituloShape) {
		super();
		this.idShape = idShape;
		this.dataCriacao = dataCriacao;
		this.dataEdicao = dataEdicao;
		this.usuario = usuario;
		this.tituloShape = tituloShape;
	}
	
	
}