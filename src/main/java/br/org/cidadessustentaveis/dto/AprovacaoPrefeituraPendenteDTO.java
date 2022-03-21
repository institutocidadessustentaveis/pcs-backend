package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.Date;

import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AprovacaoPrefeituraPendenteDTO {

	private Long id;
	
	private Prefeitura prefeitura;
	
	private String nome;
	
	private Date data;
	
	private Date dataAprovacao;
	
	private String status;
	
	private String justificativa;
	
	private LocalDate inicioMandato;
	
	private LocalDate fimMandato;
	
}
