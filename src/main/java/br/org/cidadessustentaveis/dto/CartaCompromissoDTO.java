package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.CartaCompromisso;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CartaCompromissoDTO {

	private Long id;

	private String nomeArquivo;
	
	private String extensao;
	
	private String arquivo;
	

}