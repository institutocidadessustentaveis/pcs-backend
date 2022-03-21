package br.org.cidadessustentaveis.dto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class OdsPagInstitucionalDTO {

	private Long id;
	
	private int numero;
	
	private String titulo;
	
	private String subtitulo;
	
	private String descricao;

	private String icone;
	
	private List<MetaObjetivoDesenvolvimentoSustentavelDTO> metas;
	
	public OdsPagInstitucionalDTO(ObjetivoDesenvolvimentoSustentavel ods) {
		super();
		this.id = ods.getId();
		this.numero = ods.getNumero();
		this.titulo = ods.getTitulo();
		this.subtitulo = ods.getSubtitulo();
		this.descricao = ods.getDescricao();
		this.icone = ods.getIcone();
		this.metas = ods.getMetas().stream()
										.map(obj -> new MetaObjetivoDesenvolvimentoSustentavelDTO(obj))
										.sorted(Comparator.comparing(MetaObjetivoDesenvolvimentoSustentavelDTO::getNumero))
									.collect(Collectors.toList());
	}	
}
