package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AjusteGeralDTO {
	
	private Long id;
	
	private String conteudo;
	
	private String localAplicacao;
	
}
