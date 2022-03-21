package br.org.cidadessustentaveis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class IndicadorIntegracaoDTO {

	private Long id;
	
	private String nome;
	
	private String descricao;
	
	@JsonIgnoreProperties({"icone", "iconeReduzido", "metas"})
	private ObjetivoDesenvolvimentoSustentavel ods;
	
	private MetaObjetivoDesenvolvimentoSustentavel metaODS;
	
	private String formulaResultado;
	
	private String urlPlataforma;

	public IndicadorIntegracaoDTO(Long id, String nome, String descricao,
			ObjetivoDesenvolvimentoSustentavel ods, MetaObjetivoDesenvolvimentoSustentavel metaODS) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.ods = ods;
		this.metaODS = metaODS;
	}

	
}
