package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;

import br.org.cidadessustentaveis.model.capacitacao.HistoricoCertificado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class HistoricoCertificadoDTO {

	private Long id;
	
	private String nomeUsuario;
	
	private String certificado;
	
	private LocalDate data;
	
	public HistoricoCertificado toEntityInsert(HistoricoCertificadoDTO certificadoDTO) {
		return new HistoricoCertificado(
				null, nomeUsuario, certificado, data);
	}
	
	public HistoricoCertificadoDTO(HistoricoCertificado historicoCertificado) {
		this.id = historicoCertificado.getId();
		this.nomeUsuario = historicoCertificado.getNomeUsuario();
		this.certificado = historicoCertificado.getTemplate();
		this.data = historicoCertificado.getData();
	}
}
