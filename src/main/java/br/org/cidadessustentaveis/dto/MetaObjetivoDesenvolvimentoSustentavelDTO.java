package br.org.cidadessustentaveis.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class MetaObjetivoDesenvolvimentoSustentavelDTO {
	
	private Long id;
	@NotNull(message = "Obrigatório preenchimento do número da meta.")
	private String numero;
	@NotNull(message = "Obrigatório preenchimento do número da meta.")
	private String descricao;
	
	@JsonIgnore
	private ObjetivoDesenvolvimentoSustentavel ods;

	public MetaObjetivoDesenvolvimentoSustentavelDTO(MetaObjetivoDesenvolvimentoSustentavel meta) {
		this.id = meta.getId();
		this.numero = meta.getNumero();
		this.descricao = meta.getDescricao();
		this.ods = meta.getOds();
	}
	
	public MetaObjetivoDesenvolvimentoSustentavel toEntityInsert() {
		return MetaObjetivoDesenvolvimentoSustentavel.builder()
					.numero(this.numero)
					.descricao(this.descricao)
					.ods(this.ods)
					.build();
	}
	
	public MetaObjetivoDesenvolvimentoSustentavel toEntityUpdate(MetaObjetivoDesenvolvimentoSustentavel meta) {
	  meta.setNumero(this.numero);
	  meta.setDescricao(this.descricao);
	  meta.setOds(this.ods);
	  return meta;
	}

}
