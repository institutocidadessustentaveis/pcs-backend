package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CidadeMandatosDTO  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long idCidade;
	
	private List<List<LocalDate>> mandatos;
	
	private LocalDate inicioMandato;
	
	private LocalDate fimMandato;
	
	private boolean isSignataria;
	
	private String nomeCidade;
	
	private String estadoSigla;
	
	public CidadeMandatosDTO(LocalDate inicioMandato, LocalDate fimMandato, boolean isSignataria, String nomeCidade, String estadoSigla, Long idCidade) {
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;
		this.isSignataria = isSignataria;
		this.nomeCidade = nomeCidade;
		this.estadoSigla = estadoSigla;
		this.idCidade = idCidade;
	}
}
