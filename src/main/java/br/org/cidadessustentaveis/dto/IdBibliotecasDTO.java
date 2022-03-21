package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IdBibliotecasDTO {
	private Long id;
	
	public IdBibliotecasDTO(Biblioteca biblioteca) {
		this.id = biblioteca.getId();
	}
}
