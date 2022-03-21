package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ObservacaoVariavelDTO implements Serializable {

	private static final long serialVersionUID = -3580190392391870623L;
	
	private String nomeVariavel;
	private Short ano;
	private String observacao;
}