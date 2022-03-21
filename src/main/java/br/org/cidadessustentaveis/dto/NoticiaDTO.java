package br.org.cidadessustentaveis.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticiaDTO {

	private Long id;
	private boolean publicada;
	private String titulo;
	private String subtitulo;
	private String autor;
	private String usuario;
	private LocalDateTime dataHoraCriacao;
	private LocalDateTime dataHoraPublicacao;
	private String imagemPrincipal;
	private String corpoTexto;
	private String palavraChave;
	private Boolean exibirEventoTelaInicial;
	private List<String> linksRelacionados;
	private List<OdsParaComboDTO> odss;
	private List<EixoParaComboDTO> eixos;
	private List<AreaInteresseDTO> areasDeInteresse;
	private List<NoticiaHistoricoDTO> noticiaHistorico;
	private String url;
	private boolean noticiaEvento;
	private boolean possuiFiltro;
	private boolean imagemEditada;
	private Boolean habilitaEstilo;

	public NoticiaDTO(Noticia noticia) {
		this.id = noticia.getId();
		this.publicada = noticia.getIsPublicada();
		this.titulo = noticia.getTitulo();
		this.subtitulo = noticia.getSubtitulo();
		this.autor = noticia.getAutor();
		this.usuario = noticia.getUsuario();
		this.dataHoraCriacao = noticia.getDataHoraCriacao();
		this.dataHoraPublicacao = noticia.getDataHoraPublicacao();
		this.imagemPrincipal = noticia.getImagemPrincipal();
		this.corpoTexto = noticia.getCorpoTexto();
		this.palavraChave = noticia.getPalavraChave();
		this.exibirEventoTelaInicial = noticia.getExibirEventoTelaInicial();
		this.linksRelacionados = noticia.getLinksRelacionados();
		this.odss = new ArrayList<>();
		this.eixos = new ArrayList<>();
		this.areasDeInteresse = new ArrayList<>();
		this.url = noticia.getUrl();
		this.noticiaHistorico = new ArrayList<>();
		this.noticiaEvento = noticia.getIsNoticiaEvento();
		this.possuiFiltro = noticia.getPossuiFiltro();
		this.habilitaEstilo = noticia.getHabilitaEstilo();

		if (noticia.getNoticiaHistorico() != null) {
			noticia.getNoticiaHistorico()
					.forEach(noticias -> this.noticiaHistorico.add(new NoticiaHistoricoDTO(noticias)));
		}

		if (noticia.getOdss() != null) {
			noticia.getOdss().forEach(ods -> this.odss.add(new OdsParaComboDTO(ods)));
		}
		if (noticia.getEixos() != null) {
			noticia.getEixos().forEach(eixo -> this.eixos.add(new EixoParaComboDTO(eixo)));
		}
		if (noticia.getAreasDeInteresse() != null) {
			noticia.getAreasDeInteresse().forEach(area -> this.areasDeInteresse.add(new AreaInteresseDTO(area)));
		}
	}

	public NoticiaDTO(Long idNoticia, String titulo, String subtitulo, String url, String a) {
		this.id = idNoticia;
		this.titulo = titulo;
		this.url = url;
		this.subtitulo = subtitulo;
	}

	public NoticiaDTO(Long idNoticia, String titulo, String url, String corpoTexto) {
		this.id = idNoticia;
		this.titulo = titulo;
		this.url = url;
		this.corpoTexto = corpoTexto;
	}

	public NoticiaDTO(Long idNoticia, String titulo, String subtitulo, String url, Boolean exibirEventoTelaInicial) {
		this.id = idNoticia;
		this.titulo = titulo;
		this.url = url;
		this.subtitulo = subtitulo;
		this.exibirEventoTelaInicial = exibirEventoTelaInicial;
	}

	public Noticia toEntity() {
		Noticia noticia = Noticia.builder().id(this.id)
				.isPublicada(this.publicada)
				.titulo(this.titulo)
				.subtitulo(this.subtitulo).autor(this.autor)
				.usuario(usuario)
				.dataHoraCriacao(this.dataHoraCriacao)
				.dataHoraPublicacao(this.dataHoraPublicacao)
				.corpoTexto(this.corpoTexto)
				.imagemPrincipal(this.imagemPrincipal)
				.palavraChave(this.palavraChave)
				.exibirEventoTelaInicial(this.exibirEventoTelaInicial)
				.isNoticiaEvento(this.noticiaEvento)
				.linksRelacionados(this.linksRelacionados)
				.possuiFiltro(this.possuiFiltro)
				.habilitaEstilo(this.habilitaEstilo).build();
		return noticia;
	}

}
