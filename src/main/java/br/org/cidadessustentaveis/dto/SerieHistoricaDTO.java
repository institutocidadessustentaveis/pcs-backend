package br.org.cidadessustentaveis.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
@Data @Builder
public class SerieHistoricaDTO {
	private String mandato;
	private Integer inicioMandato;
	private Integer fimMandato;
	private List<String> cabecalho;
	private List<List<String>> valores;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerieHistoricaDTO other = (SerieHistoricaDTO) obj;
		if (mandato == null) {
			if (other.mandato != null)
				return false;
		} else if (!mandato.equals(other.mandato))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mandato == null) ? 0 : mandato.hashCode());
		return result;
	}
	
	
}
