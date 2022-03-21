package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CombosBibliotecaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ItemComboDTO> listaPaises;
	private List<AreaInteresse> listaAreasInteresse;
	private List<ItemComboDTO> listaEixos;
	private List<ItemComboDTO> listaOds;

}	