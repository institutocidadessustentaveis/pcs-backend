package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussao;
import br.org.cidadessustentaveis.model.administracao.ComentarioDiscussaoFilho;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.participacaoCidada.ForumDiscussao;
import br.org.cidadessustentaveis.services.ComentarioDiscussaoFilhoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDiscussaoFilhoDTO {
	
	private Long id;
    
	private Long discussao;
	
	private Long usuario;
    
    private String nomeUsuario;
	
	private String comentario;

	private LocalDate dataPublicacao;
    
	private LocalTime horarioPublicacao;
    
    private LocalDateTime dataHorario;
    
    private Boolean editado;  
    
    private LocalTime horarioEdicao;
    
    private LocalDate dataEdicao;
    
    private Long comentarioPai;
    
    public ComentarioDiscussaoFilho toEntityInsert(ComentarioDiscussaoFilhoDTO comentarioDiscussaoDTO) {
    	return new ComentarioDiscussaoFilho(null, null, null, null, comentario, dataPublicacao, horarioPublicacao, false, null, null, null);
    }
    
    public ComentarioDiscussaoFilho toEntityUpdate(ComentarioDiscussaoFilho comentarioDiscussao) {
    	comentarioDiscussao.setComentario(this.comentario);
    	comentarioDiscussao.setEditado(true);
    	comentarioDiscussao.setDataEdicao(LocalDate.now());
    	comentarioDiscussao.setHorarioEdicao(LocalTime.now());
    	return comentarioDiscussao;
    }

    public ComentarioDiscussaoFilhoDTO(Long id, ForumDiscussao forumDiscussao, Usuario usuario, String comentario, LocalTime horarioPublicacao, LocalDate dataPublicacao, Boolean editado, LocalTime horarioEdicao, LocalDate dataEdicao, Long comentarioPai) { 
    	this.id = id;
    	this.discussao = forumDiscussao.getId();
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
    	this.comentarioPai = comentarioPai;
    }    
}
