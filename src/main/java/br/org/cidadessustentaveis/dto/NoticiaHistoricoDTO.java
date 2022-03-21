package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.model.noticias.NoticiaHistorico;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter @NoArgsConstructor
public class NoticiaHistoricoDTO {

	private Long id;

	private LocalDateTime dataHora;

	private UsuarioDTO usuario;

	private NoticiaDTO noticia;
	
	
	public NoticiaHistorico toEntity() {
		NoticiaHistorico noticiaHistorico = NoticiaHistorico.builder()
				.id(this.id)
				.dataHora(this.dataHora)
				.build();
		return noticiaHistorico;
	}

	public NoticiaHistoricoDTO(NoticiaHistorico noticia) {
		this.id = noticia.getId();
		this.dataHora = noticia.getDataHora();
		
		NoticiaDTO noticiaDTO = new NoticiaDTO(); 
		noticiaDTO.setId(noticia.getId());
		this.noticia = noticiaDTO;
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(noticia.getUsuario().getId());
		usuarioDTO.setNome(noticia.getUsuario().getNome());
		this.usuario = usuarioDTO;
	}
	
	
}
