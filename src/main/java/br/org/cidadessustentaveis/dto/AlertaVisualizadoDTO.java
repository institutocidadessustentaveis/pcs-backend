package br.org.cidadessustentaveis.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class AlertaVisualizadoDTO {
	private Long id;
	private AlertaDTO alerta;
	private UsuarioDTO usuario; 

}
