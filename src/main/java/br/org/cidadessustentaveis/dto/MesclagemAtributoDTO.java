package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;

@Data
public class MesclagemAtributoDTO {
	private Long idShape;
	private List<List<String>> dados;
	private String atributo;
	private List<String> colunas;
}
