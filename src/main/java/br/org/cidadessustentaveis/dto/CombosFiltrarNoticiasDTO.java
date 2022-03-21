package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CombosFiltrarNoticiasDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private List<ItemComboDTO> listaEixos;
	
	private List<ItemComboDTO> listaOds;
	
	private List<ItemComboDTO> listaAreaInteresses;

}	