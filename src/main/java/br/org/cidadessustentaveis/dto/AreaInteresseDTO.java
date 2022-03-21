package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class AreaInteresseDTO implements Serializable {
	
	private static final long serialVersionUID = 3774655501868304777L;

	private Long id;
	
	private String nome;
	
	
	public AreaInteresseDTO(AreaInteresse areaInteresse) { 
		this.id = areaInteresse.getId();
		this.nome = areaInteresse.getNome();
	}
	
}
