package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class TemaGeoespacialExibicaoDTO {

	private Long id;

	private String nome;

	public TemaGeoespacialExibicaoDTO(TemaGeoespacial tg) {
		if(tg == null) throw new IllegalArgumentException("Tema geoespacial não pode ser nulo");

		this.id = tg.getId();
		this.nome = tg.getNome();
	}
}
