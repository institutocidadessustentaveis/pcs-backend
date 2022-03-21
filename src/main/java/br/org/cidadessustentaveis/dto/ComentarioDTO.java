package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Comentario;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ComentarioDTO {

	private Long id;
	
    @NotNull
	private Long usuario;
	
    @NotNull
    @NotEmpty
	private String comentario;
	
    @NotNull
    @NotEmpty
	private String titulo;
	
    @NotNull
    @NotEmpty
	private String email;
	
    @NotNull
    @NotEmpty
	private String telefone;
    
	private Long idCidade;
	
	private String nomeCidade;
	
	private Long idPrefeitura;
	
	private String nomePrefeitura;

	@NotNull
    @NotEmpty
	private String nomeUsuario;
	
    @NotNull
	private LocalDate dataPublicacao;
    
    @NotNull
    private LocalDateTime dataHorario;
	
    @NotNull
	private LocalTime horarioPublicacao;
    
    public Comentario toEntityInsert(ComentarioDTO comentarioDTO) {
    	return new Comentario(null, null, comentario, titulo, null, null, email, telefone, nomeUsuario, null, null);
    }
    
    public Comentario toEntityUpdate(Comentario comentario) {
    	comentario.setComentario(this.comentario);
    	comentario.setTitulo(this.titulo);
    	comentario.setEmail(this.email);
    	comentario.setTelefone(this.telefone);
    	return comentario;
    }
    
    public ComentarioDTO(Comentario comentario) {
    	this.id = comentario.getId();
    	this.usuario = comentario.getUsuario().getId();
    	this.comentario = comentario.getComentario();
    	this.titulo = comentario.getTitulo();
    	this.email = comentario.getEmail();
    	this.telefone = comentario.getTelefone();
		this.idCidade = comentario.getCidade() != null ? comentario.getCidade().getId() : null;
		this.idPrefeitura = comentario.getPrefeitura() != null ? comentario.getPrefeitura().getId() : null;
    	this.nomeUsuario = comentario.getNomeUsuario();
    	this.dataPublicacao = comentario.getDataPublicacao();
    	this.horarioPublicacao = comentario.getHorarioPublicacao();
    }
    
    //Query buscarComentariosToList e buscarComentariosToListAdmin
    public ComentarioDTO(Long id, String titulo, String nomeUsuario, LocalDate dataPublicacao, LocalTime horarioPublicacao,  Cidade cidade, Prefeitura prefeitura) {
    	this.id = id;
    	this.titulo = titulo;
    	this.nomeUsuario = nomeUsuario;
    	this.dataPublicacao = dataPublicacao;
    	this.horarioPublicacao = horarioPublicacao;
    	if (this.dataHorario != null && this.horarioPublicacao != null) {
			this.dataHorario = LocalDateTime.of(dataPublicacao, horarioPublicacao);
    	}
    	if (cidade != null) {
    		this.nomeCidade = cidade.getNome();
    		this.idCidade = cidade.getId();
    	}
    	if (prefeitura != null) {
    		this.idPrefeitura = prefeitura.getId();
    		this.nomePrefeitura = prefeitura.getNome();
    		
    	}
    }
    
    public ComentarioDTO(Long id, String titulo, String nomeUsuario, LocalDate dataPublicacao, LocalTime horarioPublicacao, String comentario, String email, String telefone,  Cidade cidade, Prefeitura prefeitura) {
    	this.id = id;
    	this.titulo = titulo;
    	this.nomeUsuario = nomeUsuario;
    	this.dataPublicacao = dataPublicacao;
    	this.horarioPublicacao = horarioPublicacao;
    	if (this.dataHorario != null && this.horarioPublicacao != null) {
			this.dataHorario = LocalDateTime.of(dataPublicacao, horarioPublicacao);
    	}
    	this.comentario = comentario;
    	this.email = email;
    	this.telefone = telefone;
    	if (cidade != null) {
    		this.nomeCidade = cidade.getNome();
    		this.idCidade = cidade.getId();
    	}
    	if (prefeitura != null) {
    		this.idPrefeitura = prefeitura.getId();
    		this.nomePrefeitura = prefeitura.getNome();
    	}
    }

	public ComentarioDTO(Long id, Long usuario, String comentario, String titulo, String email, String telefone,
			Long idCidade, String nomeCidade, String nomeUsuario, LocalDate dataPublicacao, LocalTime horarioPublicacao) {
		this.id = id;
		this.usuario = usuario;
		this.comentario = comentario;
		this.titulo = titulo;
		this.email = email;
		this.telefone = telefone;
		this.idCidade = idCidade;
		this.nomeCidade = nomeCidade;
		this.nomeUsuario = nomeUsuario;
		this.dataPublicacao = dataPublicacao;
		this.horarioPublicacao = horarioPublicacao;
		this.dataHorario = LocalDateTime.of(this.dataPublicacao, this.horarioPublicacao);
	}
    
    
}
