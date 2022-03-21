package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NavItemCombosDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> modulos;	
	private List<MenuPaginaDTO> paginas;
	private List<ItemComboDTO> perfis;

	
}