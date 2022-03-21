package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UltimasNoticiaDTO {

    private Long id;

    private String titulo;

    private String subtitulo;

    private String autor;

    private LocalDateTime dataHoraPublicacao;

    private List<AreaInteresseDTO> areasDeInteresse;

    private String url;

    public UltimasNoticiaDTO(Noticia noticia) {
        this.id = noticia.getId();
        this.titulo = noticia.getTitulo();
        this.subtitulo = noticia.getSubtitulo();
        this.autor = noticia.getAutor();
        this.dataHoraPublicacao = noticia.getDataHoraPublicacao();
        this.areasDeInteresse = new ArrayList<>();
        this.url = noticia.getUrl();
    }

}
