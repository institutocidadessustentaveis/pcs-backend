package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BibliotecaDetalhesDTO {

	private Long id;

	private List<AreaInteresseDTO> areasInteresse;

	private String autor;

	private List<String> tipoAutor;

	private String cidadeNome;
	
	private String estadoNome;

	private LocalDate dataPublicacao;

	private List<EixoDTO> eixos;

	private String idioma;

	private ArquivoDTO imagemCapa;

	private List<IndicadorDTO> indicadores;

	private String instituicao;

	private List<MetaObjetivoDesenvolvimentoSustentavelDTO> meta;

	private List<ObjetivoDesenvolvimentoSustentavelDTO> ods;

	private String paisNome;

	private String palavraChave;

	private List<String> publicoAlvo;

	private String descricao;

	private String subtitulo;

	private String tipoMaterial;

	private String tituloPublicacao;

	private String usuarioNome;

	private String localExibicao;

	private List<String> modulo;
	
	private List<String> galeriaDeVideos;
	
	private List<ArquivoDTO> galeriaDeAudios;
	
	private Boolean possuiArquivos;
	
	private String grupoAcademico;
	
	private List<Long> shapeFiles = new ArrayList<Long>(); 

	public BibliotecaDetalhesDTO(Biblioteca biblioteca) {
		this.id = biblioteca.getId();
		this.areasInteresse = biblioteca.getAreasInteresse() != null
				? biblioteca.getAreasInteresse().stream().map(areaInteresse -> new AreaInteresseDTO(areaInteresse))
						.collect(Collectors.toList())
				: null;
		this.autor = biblioteca.getAutor();
		this.tipoAutor = biblioteca.getTipoAutor();
		this.cidadeNome = biblioteca.getCidade() != null ? biblioteca.getCidade().getNome() : null;
		this.estadoNome = biblioteca.getEstado() != null ? biblioteca.getEstado().getNome() : null;
		this.dataPublicacao = biblioteca.getDataPublicacao();
		this.eixos = biblioteca.getEixos() != null
				? biblioteca.getEixos().stream().map(eixo -> new EixoDTO(eixo)).collect(Collectors.toList())
				: null;
		this.idioma = biblioteca.getIdioma();
		this.imagemCapa = biblioteca.getImagemCapa() != null ? new ArquivoDTO(biblioteca.getImagemCapa()) : null;
		this.indicadores = biblioteca.getIndicadores() != null ? biblioteca.getIndicadores().stream()
				.map(indicador -> new IndicadorDTO(indicador)).collect(Collectors.toList()) : null;
		this.instituicao = biblioteca.getInstituicao();
		this.meta = biblioteca.getMeta() != null
				? biblioteca.getMeta().stream().map(meta -> new MetaObjetivoDesenvolvimentoSustentavelDTO(meta))
						.collect(Collectors.toList())
				: null;
		this.ods = biblioteca.getOds() != null ? biblioteca.getOds().stream()
				.map(ods -> new ObjetivoDesenvolvimentoSustentavelDTO(ods)).collect(Collectors.toList()) : null;
		this.paisNome = biblioteca.getPaisPublicacao() != null ? biblioteca.getPaisPublicacao().getNome() : null;
		this.palavraChave = biblioteca.getPalavraChave();
		this.publicoAlvo = biblioteca.getPublicoAlvo();
		this.descricao = biblioteca.getDescricao();
		this.subtitulo = biblioteca.getSubtitulo();
		this.tipoMaterial = biblioteca.getTipoMaterial();
		this.tituloPublicacao = biblioteca.getTituloPublicacao();
		this.usuarioNome = biblioteca.getUsuario() != null ? biblioteca.getUsuario().getNome() : null;
		this.localExibicao = biblioteca.getLocalExibicao();
		this.modulo = biblioteca.getModulo();
		this.galeriaDeVideos = biblioteca.getGaleriaDeVideos()!= null ? Arrays.asList(biblioteca.getGaleriaDeVideos().split("!&")) : null;
		this.galeriaDeAudios = biblioteca.getArquivos()!= null ? biblioteca.getArquivos().stream().map(arquivo -> new ArquivoDTO(arquivo)).collect(Collectors.toList()): null;
		this.shapeFiles = biblioteca.getShapesRelacionados();
	}
	
	public BibliotecaDetalhesDTO(Biblioteca biblioteca, Boolean possuiArquivos) {
		this.id = biblioteca.getId();
		this.areasInteresse = biblioteca.getAreasInteresse() != null
				? biblioteca.getAreasInteresse().stream().map(areaInteresse -> new AreaInteresseDTO(areaInteresse))
						.collect(Collectors.toList())
				: null;
		this.autor = biblioteca.getAutor();
		this.tipoAutor = biblioteca.getTipoAutor();
		this.cidadeNome = biblioteca.getCidade() != null ? biblioteca.getCidade().getNome() : null;
		this.estadoNome = biblioteca.getEstado() != null ? biblioteca.getEstado().getNome() : null;
		this.dataPublicacao = biblioteca.getDataPublicacao();
		this.eixos = biblioteca.getEixos() != null
				? biblioteca.getEixos().stream().map(eixo -> new EixoDTO(eixo)).collect(Collectors.toList())
				: null;
		this.idioma = biblioteca.getIdioma();
		this.imagemCapa = biblioteca.getImagemCapa() != null ? new ArquivoDTO(biblioteca.getImagemCapa()) : null;
		this.indicadores = biblioteca.getIndicadores() != null ? biblioteca.getIndicadores().stream()
				.map(indicador -> new IndicadorDTO(indicador)).collect(Collectors.toList()) : null;
		this.instituicao = biblioteca.getInstituicao();
		this.meta = biblioteca.getMeta() != null
				? biblioteca.getMeta().stream().map(meta -> new MetaObjetivoDesenvolvimentoSustentavelDTO(meta))
						.collect(Collectors.toList())
				: null;
		this.ods = biblioteca.getOds() != null ? biblioteca.getOds().stream()
				.map(ods -> new ObjetivoDesenvolvimentoSustentavelDTO(ods)).collect(Collectors.toList()) : null;
		this.paisNome = biblioteca.getPaisPublicacao() != null ? biblioteca.getPaisPublicacao().getNome() : null;
		this.palavraChave = biblioteca.getPalavraChave();
		this.publicoAlvo = biblioteca.getPublicoAlvo();
		this.descricao = biblioteca.getDescricao();
		this.subtitulo = biblioteca.getSubtitulo();
		this.tipoMaterial = biblioteca.getTipoMaterial();
		this.tituloPublicacao = biblioteca.getTituloPublicacao();
		this.usuarioNome = biblioteca.getUsuario() != null ? biblioteca.getUsuario().getNome() : null;
		this.localExibicao = biblioteca.getLocalExibicao();
		this.modulo = biblioteca.getModulo();
		this.galeriaDeVideos = biblioteca.getGaleriaDeVideos()!= null ? Arrays.asList(biblioteca.getGaleriaDeVideos().split("!&")) : null;
		this.galeriaDeAudios = biblioteca.getArquivos()!= null ? biblioteca.getArquivos().stream().map(arquivo -> new ArquivoDTO(arquivo)).collect(Collectors.toList()): null;
		this.possuiArquivos = possuiArquivos;
		this.grupoAcademico = biblioteca.getGrupoAcademico() != null ? biblioteca.getGrupoAcademico().getNomeGrupo() : null;
		this.shapeFiles = biblioteca.getShapesRelacionados();
	}
}
