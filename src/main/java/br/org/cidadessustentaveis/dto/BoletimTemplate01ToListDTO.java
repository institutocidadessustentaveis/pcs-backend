package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BoletimTemplate01ToListDTO {
	
	private Long id;
	
	private String titulo;
	
	private LocalDateTime dataHoraEnvio;
	
	private String nomeUsuario;
	
}
