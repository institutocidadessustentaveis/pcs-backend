package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor @Getter @Setter @AllArgsConstructor
public class BoasPraticasFiltradasDTO implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	private List<BoaPraticaItemDTO> listBoasPraticas;
	
	private Long countTotalBoasPraticas;

	
}