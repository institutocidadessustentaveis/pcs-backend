package br.org.cidadessustentaveis.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter @NoArgsConstructor
public class FiltroBibliotecasDTO {
	
	private Long idBiblioteca;
	
	public FiltroBibliotecasDTO(Long idBiblioteca) {
		this.idBiblioteca = idBiblioteca;
	}
	
}
