package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BuscarDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private List<BuscaItemDTO> noticias;
	private Double porcentagemNoticiasPalavraChaveExata;

	private List<BuscaItemDTO> boasPraticas;
	private Double porcentagemBoasPraticasPalavraChaveExata;

	private List<BuscaItemDTO> institucionais;
	private Double porcentagemInstitucionaisPalavraChaveExata;

	private List<BuscaItemDTO> indicadores;
	private Double porcentagemIndicadoresPalavraChaveExata;

}
