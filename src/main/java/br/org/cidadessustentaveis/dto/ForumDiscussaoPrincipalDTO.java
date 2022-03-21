package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ForumDiscussaoPrincipalDTO {

	private Long id;
	
	private Boolean ativo;
	
	private LocalDateTime dataHoraCriacao;
	
	private String titulo;
	
	private Long numeroDeRespostas;
	
	private Long numeroDeVisualizacao;
	
	private UsuarioDTO usuarioUltimaPostagem;

	private Boolean publico;
	
	public ForumDiscussaoPrincipalDTO(Long id, Boolean ativo, LocalDateTime dataHoraCriacao, String titulo,
			Long numeroDeRespostas, Long numeroDeVisualizacao, Usuario usuarioUltimaPostagem, Boolean publico) {
		this.id = id;
		this.ativo = ativo;
		this.dataHoraCriacao = dataHoraCriacao;
		this.titulo = titulo;
		this.numeroDeRespostas = numeroDeRespostas;
		this.numeroDeVisualizacao = numeroDeVisualizacao;
		this.usuarioUltimaPostagem = usuarioUltimaPostagem != null ? new UsuarioDTO(usuarioUltimaPostagem)  : null;
		this.publico = publico;
	}
	
	
	public ForumDiscussaoPrincipalDTO(ForumDiscussao forumDiscussao) {
		super();
		this.id = forumDiscussao.getId();
		this.ativo = forumDiscussao.getAtivo();
		this.dataHoraCriacao = forumDiscussao.getDataHoraCriacao();
		this.titulo = forumDiscussao.getTitulo();
		this.numeroDeRespostas = forumDiscussao.getNumeroDeRespostas();
		this.numeroDeVisualizacao = forumDiscussao.getNumeroDeVisualizacao();
		this.usuarioUltimaPostagem = forumDiscussao.getUsuarioUltimaPostagem() != null ? new UsuarioDTO(forumDiscussao.getUsuarioUltimaPostagem()) : null;
		this.publico = forumDiscussao.getPublico();
	}
	
	

}
