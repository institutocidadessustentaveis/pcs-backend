package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.dto.RelatorioShapesCriadosDTO.RelatorioShapesCriadosDTOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RelatorioShapesExportadosDTO {
	
	private Long idShape;
	
	private String usuario;
	
	private String cidade;
	
	private String tituloShape;
	
	private LocalDateTime dataExportacao;
	
	public RelatorioShapesExportadosDTO(String usuario, String cidade, String tituloShape, LocalDateTime dataExportacao) {
		setUsuario(usuario);
		setCidade(cidade);
		setTituloShape(tituloShape);
		setDataExportacao(dataExportacao);
	}

}