package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.AreaAtuacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CidadeEstadoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String nomeCidade;
	
	private String siglaEstado;
	
}
