package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ForumDiscussaoListDTO {

	private Long id;
	
	private Boolean ativo;
	
	private String descricao;
	
	private LocalDateTime dataHoraCriacao;
	
	private String titulo;
	
	private Long numeroDeRespostas;
	
	private Long numeroDeVisualizacao;
	
	private UsuarioDTO usuarioUltimaPostagem;

	private Boolean publico;
	
	private List<DiscussaoPerfilDTO> discussaoPerfis;
	
	private Long prefeituraId;
	
	public ForumDiscussaoListDTO(ForumDiscussao forumDiscussao, Usuario usuarioUltimaPostagem, Prefeitura prefeitura) {
		super();
		this.id = forumDiscussao.getId();
		this.ativo = forumDiscussao.getAtivo();
		this.descricao = forumDiscussao.getDescricao();
		this.dataHoraCriacao = forumDiscussao.getDataHoraCriacao();
		this.titulo = forumDiscussao.getTitulo();
		this.numeroDeRespostas = forumDiscussao.getNumeroDeRespostas();
		this.numeroDeVisualizacao = forumDiscussao.getNumeroDeVisualizacao();
		this.usuarioUltimaPostagem = usuarioUltimaPostagem != null ? new UsuarioDTO(usuarioUltimaPostagem)  : null;
		this.publico = forumDiscussao.getPublico();
		this.dataHoraCriacao = forumDiscussao.getDataHoraCriacao();
		this.prefeituraId = prefeitura != null ? prefeitura.getId(): null;
		this.discussaoPerfis = forumDiscussao.getDiscussaoPerfis() != null ? forumDiscussao.getDiscussaoPerfis().stream().map(discussaoPerfil -> new DiscussaoPerfilDTO(discussaoPerfil)).collect(Collectors.toList()) : null;;
				
		forumDiscussao.getDiscussaoPerfis().forEach(x -> {
			x.setForumDiscussao(forumDiscussao);
		});
		
	}
	
	

}
