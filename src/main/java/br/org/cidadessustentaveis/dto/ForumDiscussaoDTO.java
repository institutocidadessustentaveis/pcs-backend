package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.biblioteca.TemaForum;
import br.org.cidadessustentaveis.model.participacaoCidada.DiscussaoPerfil;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ForumDiscussaoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String titulo;
	
	private String descricao;
	
	private LocalDateTime dataHoraCriacao;
	
	private LocalDate dataAtivacao;
	
	private LocalTime horaAtivacao;
	
	private LocalDate dataDesativacao;
	
	private LocalTime horaDesativacao;
	
	private Boolean publico;
	
	private List<DiscussaoPerfilDTO> discussaoPerfis = new ArrayList<DiscussaoPerfilDTO>();
	
	private UsuarioDTO usuarioUltimaPostagem;
	
	private Long numeroDeRespostas;
	
	private Long numeroDeVisualizacao;
	
	private Boolean ativo;
	
	private Prefeitura prefeitura;
	
	private Long prefeituraId;
	
	private List<TemaForumDTO> temasForum;
	
	private Long usuarioCadastro;	
	
	public ForumDiscussaoDTO(ForumDiscussao forum) {
		this.id = forum.getId();
		this.titulo = forum.getTitulo();
		this.descricao = forum.getDescricao();
		this.dataHoraCriacao = forum.getDataHoraCriacao();
		this.dataAtivacao = forum.getDataAtivacao();
		this.horaAtivacao = forum.getHoraAtivacao();
		this.dataDesativacao = forum.getDataDesativacao();
		this.horaDesativacao = forum.getHoraDesativacao();
		this.publico = forum.getPublico();
		this.discussaoPerfis = forum.getDiscussaoPerfis() != null ? forum.getDiscussaoPerfis().stream().map(discussaoPerfil -> new DiscussaoPerfilDTO(discussaoPerfil)).collect(Collectors.toList()) : null;
		this.usuarioUltimaPostagem = forum.getUsuarioUltimaPostagem() != null ? new UsuarioDTO(forum.getUsuarioUltimaPostagem()) : null;
		this.numeroDeRespostas = forum.getNumeroDeRespostas();
		this.numeroDeVisualizacao = forum.getNumeroDeVisualizacao();
		this.ativo = forum.getAtivo();
		this.prefeitura = forum.getPrefeitura() != null ? forum.getPrefeitura() : null;
		this.temasForum = forum.getTemasForum() != null ? forum.getTemasForum().stream().map(temaForum -> new TemaForumDTO(temaForum)).collect(Collectors.toList()) : null;;
		}
	
	public ForumDiscussao toEntityInsert() throws Exception{
		List<DiscussaoPerfil> perfis = new ArrayList<DiscussaoPerfil>();
		if(this.getDiscussaoPerfis() != null) {
			this.getDiscussaoPerfis().stream().forEach(discussaoPerfil -> {
				try {
					perfis.add(discussaoPerfil.toEntityInsert());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		
		ForumDiscussao forum = new ForumDiscussao();
		
		forum.setUsuarioCadastrouDiscussao(this.usuarioCadastro);
		forum.setId(this.id);
		forum.setTitulo(this.titulo);
		forum.setDescricao(this.descricao);
		if (this.ativo == true) {
			forum.setDataAtivacao(LocalDate.now());
			forum.setHoraAtivacao(LocalTime.now());
		}
		else {
			forum.setDataDesativacao(LocalDate.now());
			forum.setHoraDesativacao(LocalTime.now());	
		}
		forum.setDataHoraCriacao(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
		forum.setPublico(this.publico);
		forum.setDiscussaoPerfis(perfis);
		forum.setUsuarioUltimaPostagem(this.usuarioUltimaPostagem != null ? this.usuarioUltimaPostagem.toEntityInsert() : null);
		forum.setNumeroDeRespostas((long) 0);
		forum.setNumeroDeVisualizacao((long) 0);
		forum.setAtivo(this.ativo);
		forum.setPrefeitura(this.prefeitura != null ? this.prefeitura : null);
		
		forum.getDiscussaoPerfis().forEach(x -> {
			x.setForumDiscussao(forum);
		});
		
		List<TemaForum> temas = new ArrayList<TemaForum>();
		if(this.getTemasForum() != null) {
			this.getTemasForum().stream().forEach(temaForum -> temas.add(temaForum.toEntityInsert()));
		}
		forum.setTemasForum(temas);	
		
		return forum;
	}

	public ForumDiscussao toEntityUpdate(ForumDiscussao forum) {
		List<DiscussaoPerfil> perfis = new ArrayList<DiscussaoPerfil>();
		if(this.getDiscussaoPerfis() != null) {
			this.getDiscussaoPerfis().stream().forEach(discussaoPerfil -> {
				try {
					perfis.add(discussaoPerfil.toEntityInsert());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		
		forum.setId(this.id);
		forum.setTitulo(this.titulo);
		forum.setDescricao(this.descricao);
		if (this.ativo == true) {
			forum.setDataAtivacao(LocalDate.now());
			forum.setHoraAtivacao(LocalTime.now());
		}
		else {
			forum.setDataDesativacao(LocalDate.now());
			forum.setHoraDesativacao(LocalTime.now());	
		}
		forum.setPublico(this.publico);
		forum.getDiscussaoPerfis().forEach(x -> {
			x.setForumDiscussao(null);
		});
		forum.setDiscussaoPerfis(null);
		
		forum.setDiscussaoPerfis(perfis);
		forum.getDiscussaoPerfis().forEach(x -> {
			x.setForumDiscussao(forum);
		});
		forum.setUsuarioUltimaPostagem(this.usuarioUltimaPostagem != null ? this.usuarioUltimaPostagem.toEntityInsert() : forum.getUsuarioUltimaPostagem() != null ? forum.getUsuarioUltimaPostagem(): null);
		forum.setAtivo(this.ativo);
		
		List<TemaForum> temas = new ArrayList<TemaForum>();
		if(this.getTemasForum() != null) {
			this.getTemasForum().stream().forEach(temaForum -> temas.add(temaForum.toEntityInsert()));
		}
		forum.setTemasForum(temas);	
		
		return forum;
	}
}
