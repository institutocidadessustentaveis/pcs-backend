package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.Eixo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EixoParaComboDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String nome;
	
	private List<OdsParaComboDTO> listaODS;
	
	public EixoParaComboDTO(Eixo eixo) {
		this.id = eixo.getId();
		this.nome = eixo.getNome();
		this.listaODS = new ArrayList<>(eixo.getListaODS()).stream().map(ods -> new OdsParaComboDTO(ods)).collect(Collectors.toList());
	}
	public EixoParaComboDTO(Eixo eixo, Boolean comListaIndicadores) {
		this.id = eixo.getId();
		this.nome = eixo.getNome();
		this.listaODS = new ArrayList<>(eixo.getListaODS()).stream().map(ods -> new OdsParaComboDTO(ods, comListaIndicadores)).collect(Collectors.toList());
	}
}
