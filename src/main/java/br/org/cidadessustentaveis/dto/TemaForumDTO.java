package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.biblioteca.TemaForum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TemaForumDTO {

	private Long id;
	
	private String nome;

	public TemaForumDTO(TemaForum temaForum) {
		this.id = temaForum.getId();
		this.nome = temaForum.getNome();
	}
	
	public TemaForum toEntityInsert() {
		TemaForum temaForum = new TemaForum();
		
		temaForum.setId(this.id);
		temaForum.setNome(this.nome);
		
		return temaForum;
	}
	
	public TemaForum toEntityUpdate(TemaForum temaForum) {
		
		temaForum.setId(this.id);
		temaForum.setNome(this.nome);
		
		return temaForum;
	}
	
}
