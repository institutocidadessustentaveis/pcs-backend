package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class MetaOdsParaComboDTO {
	
	private Long id;
	private String numero;
	private String descricao;
	private List<IndicadorParaComboDTO> indicadores;
	
	public MetaOdsParaComboDTO(MetaObjetivoDesenvolvimentoSustentavel metaOds) {
		this.id = metaOds.getId();
		this.numero = metaOds.getNumero();
		this.descricao = metaOds.getDescricao();
		this.indicadores = new ArrayList<>(metaOds.getIndicadores()).stream().map(indicador -> new IndicadorParaComboDTO(indicador)).collect(Collectors.toList());
	}
	public MetaOdsParaComboDTO(MetaObjetivoDesenvolvimentoSustentavel metaOds,Boolean comListaIndicadores) {
		this.id = metaOds.getId();
		this.numero = metaOds.getNumero();
		this.descricao = metaOds.getDescricao();
		if(comListaIndicadores) {
			this.indicadores = new ArrayList<>(metaOds.getIndicadores()).stream().filter(indicador -> indicador.getPrefeitura() == null).map(indicador -> new IndicadorParaComboDTO(indicador)).collect(Collectors.toList());
		}
	}
}
