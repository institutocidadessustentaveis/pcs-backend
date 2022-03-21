package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;

@Data
public class RespostaFormularioDTO {
	private String key;
	private List<String> valores;
}
