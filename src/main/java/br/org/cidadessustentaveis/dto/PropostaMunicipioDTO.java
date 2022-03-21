package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class PropostaMunicipioDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long prefeitura;
	private Long cidade;
	private String descricao;
	private Long usuario;
	private LocalDateTime dataEnvio;
	
}
