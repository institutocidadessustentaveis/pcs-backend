package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrefeituraPlanoMetasDTO {

    private Long idCidade;
    
    private Long idPrefeitura;

    private String cidade;

    private String estado;

    private String responsavel;

    private LocalDate inicioMandato;

    private LocalDate fimMandato;
    
    private Long idPrefeituraLogada;

	public PrefeituraPlanoMetasDTO(Long idCidade, Long idPrefeitura, String cidade, String estado, String responsavel,
			LocalDate inicioMandato, LocalDate fimMandato) {
		super();
		this.idCidade = idCidade;
		this.idPrefeitura = idPrefeitura;
		this.cidade = cidade;
		this.estado = estado;
		this.responsavel = responsavel;
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;
	}
    
    
}
