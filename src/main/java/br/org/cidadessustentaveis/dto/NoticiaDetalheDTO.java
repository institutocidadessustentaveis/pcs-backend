package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticiaDetalheDTO {

    private Long id;

    private String titulo;

    private String subtitulo;

    private String autor;

    private LocalDateTime dataHoraPublicacao;

    private String corpoTexto;

    private String url;
    
    private boolean possuiFiltro;

    public NoticiaDetalheDTO(Noticia noticia) {
        this.id = noticia.getId();
        this.titulo = noticia.getTitulo();
        this.subtitulo = noticia.getSubtitulo();
        this.autor = noticia.getAutor();
        this.dataHoraPublicacao = noticia.getDataHoraPublicacao();
        this.corpoTexto = noticia.getCorpoTexto();
        this.url = noticia.getUrl();
        this.possuiFiltro = noticia.getPossuiFiltro();
    }

}
