package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class OdsParaComboDTO {

	private Long id;

	private int numero;

	private String titulo;
	
	private List<MetaOdsParaComboDTO> metas;
	
	public OdsParaComboDTO (ObjetivoDesenvolvimentoSustentavel ods) {
		this.id = ods.getId();
		this.numero = ods.getNumero();
		this.titulo = ods.getTitulo();
		this.metas = new ArrayList<>(ods.getMetas()).stream().map(metaOds -> new MetaOdsParaComboDTO(metaOds)).collect(Collectors.toList());
	}
	public OdsParaComboDTO (ObjetivoDesenvolvimentoSustentavel ods, Boolean comListaIndicadores) {
		this.id = ods.getId();
		this.numero = ods.getNumero();
		this.titulo = ods.getTitulo();
		this.metas = new ArrayList<>(ods.getMetas()).stream().map(metaOds -> new MetaOdsParaComboDTO(metaOds, comListaIndicadores)).collect(Collectors.toList());
	}
}
