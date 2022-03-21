package br.org.cidadessustentaveis.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class BibliotecaDTO {

	private Long id;
	
	private List<Long> areasInteresse;
	
	private String autor;
	
	private List<String> tipoAutor;
	
	private Long cidade;
	
	private LocalDate dataPublicacao;
	
	private List<Long> eixos;
	
	private String idioma;
	
	private ArquivoDTO imagemCapa;
	
	private List<Long> indicadores;
	
	private String instituicao;
	
	private List<Long> meta;
	
	private List<Long> ods;
	
	private Long paisPublicacao;
	
	private String palavraChave;
	
	private List<String> publicoAlvo;
	
	private String descricao;
	
	private String subtitulo;
	
	private String tipoMaterial;
	
	private String tituloPublicacao;
	
	private Long usuario;
	
	private String localExibicao;
	
	private List<String> modulo;
	
	private Long estado;
	
	private List<String> galeriaDeVideos;
	
	private List<ArquivoDTO> galeriaDeAudios;
	
	private List<ArquivoDTO> arquivoMultimidia;
	
	private Long grupoAcademico;
	
	private String grupoAcademicoNome;
	private List<Long> shapeFiles = new ArrayList<Long>();
	
	public BibliotecaDTO(Biblioteca biblioteca) {
		this.id = biblioteca.getId();
		this.areasInteresse = biblioteca.getAreasInteresse().stream().map(AreaInteresse:: getId).collect(Collectors.toList());
		this.autor = biblioteca.getAutor();
		this.tipoAutor = biblioteca.getTipoAutor();
		this.cidade = biblioteca.getCidade() != null ? biblioteca.getCidade().getId() : null;
		this.dataPublicacao = biblioteca.getDataPublicacao();
		this.eixos = biblioteca.getEixos().stream().map(Eixo:: getId).collect(Collectors.toList());
		this.idioma = biblioteca.getIdioma();
		this.imagemCapa = biblioteca.getImagemCapa() != null ? new ArquivoDTO(biblioteca.getImagemCapa()) : null;
		this.indicadores = biblioteca.getIndicadores().stream().map(Indicador:: getId).collect(Collectors.toList());
		this.instituicao = biblioteca.getInstituicao();
		this.meta = biblioteca.getMeta().stream().map(MetaObjetivoDesenvolvimentoSustentavel:: getId).collect(Collectors.toList());
		this.ods = biblioteca.getOds().stream().map(ObjetivoDesenvolvimentoSustentavel:: getId).collect(Collectors.toList());
		this.paisPublicacao = biblioteca.getPaisPublicacao() != null ? biblioteca.getPaisPublicacao().getId() : null;
		this.localExibicao = biblioteca.getLocalExibicao();
		this.palavraChave = biblioteca.getPalavraChave();
		this.publicoAlvo = biblioteca.getPublicoAlvo();
		this.descricao = biblioteca.getDescricao();
		this.subtitulo = biblioteca.getSubtitulo();
		this.tipoMaterial = biblioteca.getTipoMaterial();
		this.tituloPublicacao = biblioteca.getTituloPublicacao();
		this.usuario = biblioteca.getUsuario().getId();
		this.modulo = biblioteca.getModulo();
		this.estado = biblioteca.getEstado() != null ?  biblioteca.getEstado().getId(): null;
		this.galeriaDeVideos = biblioteca.getGaleriaDeVideos()!= null ? Arrays.asList(biblioteca.getGaleriaDeVideos().split("!&")) : null;
		this.galeriaDeAudios = biblioteca.getArquivos()!= null ? biblioteca.getArquivos().stream().map(arquivo -> new ArquivoDTO(arquivo)).collect(Collectors.toList()): null;
		this.arquivoMultimidia = biblioteca.getArquivoMultimidia().stream().map(arquivo -> new ArquivoDTO(arquivo)).collect(Collectors.toList());
		this.grupoAcademico = biblioteca.getGrupoAcademico() != null ? biblioteca.getGrupoAcademico().getId() : null;
		this.grupoAcademicoNome = biblioteca.getGrupoAcademicoNome();
		this.shapeFiles = biblioteca.getShapesRelacionados();
	}

	public Biblioteca toEntityInsert(BibliotecaDTO bibliotecaDTO) {
		return new Biblioteca(
				null, null, autor, tipoAutor, null, null, null, idioma, imagemCapa != null ? imagemCapa.toEntityInsert() : null,
				null, instituicao, null, null, null, palavraChave, publicoAlvo, descricao, subtitulo, tipoMaterial, tituloPublicacao, null, localExibicao, modulo, null,
				this.galeriaDeVideos.stream().map( item -> item).collect( Collectors.joining( "!&" )),
				this.galeriaDeAudios != null? this.galeriaDeAudios.stream().map(arquivo -> arquivo.toEntityInsert()).collect(Collectors.toList()):null,
				this.arquivoMultimidia.stream().map(arquivo -> arquivo.toEntityInsert()).collect(Collectors.toList()), null,
				grupoAcademicoNome, this.shapeFiles);
	}
	
	public Biblioteca toEntityUpdate(Biblioteca biblioteca) {
		biblioteca.setAutor(this.autor);
		biblioteca.setModulo(this.modulo);
		biblioteca.setIdioma(this.idioma);
		biblioteca.setDescricao(this.descricao);
		biblioteca.setSubtitulo(this.subtitulo);
		biblioteca.setPublicoAlvo(this.publicoAlvo);
		biblioteca.setInstituicao(this.instituicao);
		biblioteca.setPalavraChave(this.palavraChave);
		biblioteca.setTipoMaterial(this.tipoMaterial);
		biblioteca.setLocalExibicao(this.localExibicao);
		biblioteca.setTituloPublicacao(this.tituloPublicacao);
		biblioteca.setGrupoAcademicoNome(this.grupoAcademicoNome);
		biblioteca.setGaleriaDeVideos(this.galeriaDeVideos.stream().map( item -> item).collect( Collectors.joining( "!&" )));
		biblioteca.setArquivos(this.galeriaDeAudios.stream().map(audio -> audio.toEntityInsert()).collect(Collectors.toList()));
		biblioteca.setArquivoMultimidia(this.arquivoMultimidia.stream().map(arquivo -> arquivo.toEntityInsert()).collect(Collectors.toList()));
		biblioteca.setShapesRelacionados(this.shapeFiles);
		return biblioteca;
	}
	
	//query buscarBibliotecasToList()
	public BibliotecaDTO(Long id, String tituloPublicacao, String subtitulo, LocalDate dataPublicacao, String autor) {
		this.id = id;
		this.tituloPublicacao = tituloPublicacao;
		this.subtitulo = subtitulo;
		this.dataPublicacao = dataPublicacao;
		this.autor = autor;
	}
	
	//query buscarBibliotecasParaComboBox()
	public BibliotecaDTO(Long id, String tituloPublicacao) {
		this.id = id;
		this.tituloPublicacao = tituloPublicacao;
	}
}
