package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.participacaoCidada.DiscussaoPerfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DiscussaoPerfilDTO {

	private Long id;
	
	private PerfilDTO perfil;
	
	private ForumDiscussaoDTO forumDiscussao;
	
	private String autorizacao;
	
	
	public DiscussaoPerfilDTO(DiscussaoPerfil perfil) {
		this.id = perfil.getId();
		this.perfil = perfil.getPerfil() != null ? new PerfilDTO(perfil.getPerfil()) : null;
//		this.forumDiscussao = perfil.getForumDiscussao() != null ? new ForumDiscussaoDTO(perfil.getForumDiscussao()) : null;
		this.autorizacao = perfil.getAutorizacao();
	}
	
	public DiscussaoPerfil toEntityInsert() throws Exception {
		DiscussaoPerfil discussaoPerfil = new DiscussaoPerfil();
		
		discussaoPerfil.setId(this.id);
		discussaoPerfil.setPerfil(this.perfil.toEntityInsert());
		discussaoPerfil.setForumDiscussao(this.forumDiscussao != null ? this.forumDiscussao.toEntityInsert() : null);
		discussaoPerfil.setAutorizacao(this.autorizacao);	
		
		return discussaoPerfil;
	}
}
