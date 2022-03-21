package br.org.cidadessustentaveis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ContagemCidadesCidadesParticipantesNoEstadoDTO {

	private Long idEstado;

	private String nomeEstado;

	private Long cidadesParticipantes;

	public ContagemCidadesCidadesParticipantesNoEstadoDTO(Long idEstado, String nome, 
															Long cidadesParticipantes) {
		this.idEstado = idEstado;
		this.nomeEstado = nome;
		this.cidadesParticipantes = cidadesParticipantes;
	}

}
