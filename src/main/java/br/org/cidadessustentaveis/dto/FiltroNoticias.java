package br.org.cidadessustentaveis.dto;



import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor
public class FiltroNoticias {

	private Integer page;
	
	private Integer linesPerPage;
	
	private Long idEixo;
	
	private Long idOds;
	
	private Long idAreaInteresse;
	
	private String palavraChave;
}
