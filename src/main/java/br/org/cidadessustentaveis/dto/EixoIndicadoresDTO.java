package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.Eixo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EixoIndicadoresDTO implements Serializable{

	private Long id;
	private String nome;
	private List<IndicadorParaComboDTO> indicadores = new ArrayList<IndicadorParaComboDTO>();
	public EixoIndicadoresDTO(Eixo eixo) {
		this.id = eixo.getId();
		this.nome = eixo.getNome();
	}	
}
