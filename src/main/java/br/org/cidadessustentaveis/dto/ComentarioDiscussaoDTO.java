package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ComentarioDiscussaoDTO {

	private Long id;
    
	@NotNull
	private Long discussao;
	
    @NotNull
	private Long usuario;
    
    private String nomeUsuario;
	
    @NotNull
    @NotEmpty
	private String comentario;

    @NotNull
	private LocalDate dataPublicacao;
    
    @NotNull
	private LocalTime horarioPublicacao;
    
    @NotNull
    private LocalDateTime dataHorario;
    
    @NotNull
    private Boolean editado;  
    
    private LocalTime horarioEdicao;
    
    private LocalDate dataEdicao;

    public ComentarioDiscussao toEntityInsert(ComentarioDiscussaoDTO comentarioDiscussaoDTO) {
    	return new ComentarioDiscussao(null, null, null, null, comentario, dataPublicacao, horarioPublicacao, false, null, null);
    }
    
    public ComentarioDiscussao toEntityUpdate(ComentarioDiscussao comentarioDiscussao) {
    	comentarioDiscussao.setComentario(this.comentario);
    	comentarioDiscussao.setEditado(true);
    	comentarioDiscussao.setDataEdicao(LocalDate.now());
    	comentarioDiscussao.setHorarioEdicao(LocalTime.now());
    	return comentarioDiscussao;
    }
    
    public ComentarioDiscussaoDTO(ComentarioDiscussao comentarioDiscussao) {
    	this.id = comentarioDiscussao.getId();
    	this.discussao = comentarioDiscussao.getForumDiscussao().getId();
    	this.usuario = comentarioDiscussao.getUsuario().getId();
    	this.comentario = comentarioDiscussao.getComentario();
    	this.nomeUsuario = comentarioDiscussao.getNomeUsuario();
    	this.horarioPublicacao = comentarioDiscussao.getHorarioPublicacao();
    	this.dataPublicacao = comentarioDiscussao.getDataPublicacao();
    	if (this.dataPublicacao != null && this.horarioPublicacao != null) {
			this.dataHorario = LocalDateTime.of(this.dataPublicacao, this.horarioPublicacao);
    	}
    }
    
    //Query buscarComentariosDiscussaoByIdDiscussao()
    public ComentarioDiscussaoDTO(Long id, ForumDiscussao discussao, Usuario usuario, String comentario, LocalTime horarioPublicacao, LocalDate dataPublicacao, Boolean editado, LocalTime horarioEdicao, LocalDate dataEdicao) { 
    	this.id = id;
    	this.discussao = discussao.getId();
    	this.usuario = usuario.getId();
    	this.nomeUsuario = usuario.getNome();
    	this.comentario = comentario;
    	this.horarioPublicacao = horarioPublicacao;
    	this.dataPublicacao = dataPublicacao;
    	if (this.dataPublicacao != null && this.horarioPublicacao != null) {
			this.dataHorario = LocalDateTime.of(this.dataPublicacao, this.horarioPublicacao);
    	}
    	this.editado = editado;
    	this.dataEdicao = dataEdicao;
    	this.horarioEdicao = horarioEdicao;
    	
    }    
}
