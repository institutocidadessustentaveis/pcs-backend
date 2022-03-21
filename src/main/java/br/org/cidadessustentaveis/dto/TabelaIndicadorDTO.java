package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Data;
@Data
public class TabelaIndicadorDTO {
	String mandato;
	Integer anoInicial;
	List<List<String>> valores;
}
