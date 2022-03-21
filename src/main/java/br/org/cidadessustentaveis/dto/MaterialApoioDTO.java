package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.planjementoIntegrado.MaterialApoio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class MaterialApoioDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	@NotNull(message="Campo título não pode ser nulo")
	private String titulo;
	
	private String subtitulo;
	
	@NotNull(message="Campo autor não pode ser nulo")
	private String autor;
	
	@NotNull(message="Campo instituição não pode ser nulo")
	private String instituicao;
	
	@NotNull(message="Campo data de publicação não pode ser nulo")
	private LocalDate dataPublicacao;
	
	private String idioma;
	
	private String continente;
	
	private Long pais;
	
	private String regiao;
	
	private Long provinciaEstado;
	
	private Long cidade;
	
	private List<Long> areasInteresse;
	
	private List<Long> eixo;
	
	private List<Long> indicador;
	
	private List<Long> ods;
	
	private List<Long> metaOds;
	
	private String palavraChave;
	
	private String tag;
	
	private String publicoAlvo;
	
	private String tipoArquivo;
	
	private String tipoDocumento;
	
	private String tipoMaterial;
	
	private Long tipologiaCgee;
	
	private String localExibicao;
	
	private String resumo;
	
	private ArquivoDTO imagemCapa;
	
	@NotNull(message="Campo arquivo publicação não pode ser nulo")
	private ArquivoDTO arquivoPublicacao;
	
	public MaterialApoioDTO(MaterialApoio materialApoioRef) {
		this.id = materialApoioRef.getId();
		this.titulo = materialApoioRef.getTitulo();
		this.subtitulo = materialApoioRef.getSubtitulo();
		this.autor = materialApoioRef.getAutor();
		this.instituicao = materialApoioRef.getInstituicao();
		this.dataPublicacao = materialApoioRef.getDataPublicacao();
		this.idioma = materialApoioRef.getIdioma();
		this.continente = materialApoioRef.getContinente();
		this.pais = materialApoioRef.getPais() != null ? materialApoioRef.getPais().getId() : null;
		this.regiao = materialApoioRef.getRegiao();
		this.provinciaEstado = materialApoioRef.getProvinciaEstado() != null ? materialApoioRef.getProvinciaEstado().getId() : null;
		this.cidade = materialApoioRef.getCidade() != null ? materialApoioRef.getCidade().getId() : null;
		this.areasInteresse = materialApoioRef.getAreasInteresse() != null ? materialApoioRef.getAreasInteresse().stream().map(AreaInteresse:: getId).collect(Collectors.toList()): null;
		this.eixo = materialApoioRef.getEixo() != null ? materialApoioRef.getEixo().stream().map(Eixo:: getId).collect(Collectors.toList()): null;
		this.indicador = materialApoioRef.getIndicador() != null ? materialApoioRef.getIndicador().stream().map(Indicador:: getId).collect(Collectors.toList()): null;
		this.ods = materialApoioRef.getOds() != null ? materialApoioRef.getOds().stream().map(ObjetivoDesenvolvimentoSustentavel:: getId).collect(Collectors.toList()): null;
		this.metaOds = materialApoioRef.getMetaOds() != null ? materialApoioRef.getMetaOds().stream().map(MetaObjetivoDesenvolvimentoSustentavel:: getId).collect(Collectors.toList()): null;
		this.palavraChave = materialApoioRef.getPalavraChave();
		this.tag = materialApoioRef.getTag();
		this.publicoAlvo = materialApoioRef.getPublicoAlvo();
		this.tipoArquivo = materialApoioRef.getTipoArquivo();
		this.tipoDocumento = materialApoioRef.getTipoDocumento();
		this.tipoMaterial = materialApoioRef.getTipoMaterial();
		this.tipologiaCgee = null;
		this.localExibicao = materialApoioRef.getLocalExibicao();
		this.resumo = materialApoioRef.getResumo();
		this.imagemCapa = materialApoioRef.getImagemCapa() != null ? new ArquivoDTO(materialApoioRef.getImagemCapa()) : null;
		this.arquivoPublicacao = new ArquivoDTO(materialApoioRef.getArquivoPublicacao());
	} 

	public MaterialApoio toEntityInsert(MaterialApoioDTO materialApoioDto) {
		return new MaterialApoio(null, titulo, subtitulo, autor, instituicao, dataPublicacao, idioma, continente, null, regiao, null, null, null, null, 
				null, null, null, palavraChave, tag, publicoAlvo, tipoArquivo, tipoDocumento, tipoMaterial, localExibicao, resumo, imagemCapa != null ? imagemCapa.toEntityInsert() : null, arquivoPublicacao.toEntityInsert(), null);
	}

	// Utilizado na query buscarMateriaisDeApoioToList()
	public MaterialApoioDTO(Long id, String titulo, LocalDate dataPublicacao, String tipoDocumento, String localExibicao) {
		this.id = id;
		this.titulo = titulo;
		this.dataPublicacao = dataPublicacao;
		this.tipoDocumento = tipoDocumento;
		this.localExibicao = localExibicao;
	}
	
	public MaterialApoio toEntityUpdate(MaterialApoio materialApoioRef) {
		materialApoioRef.setTitulo(this.titulo);
		materialApoioRef.setSubtitulo(this.subtitulo);
		materialApoioRef.setAutor(this.autor);
		materialApoioRef.setInstituicao(this.instituicao);
		materialApoioRef.setDataPublicacao(this.dataPublicacao);
		materialApoioRef.setIdioma(this.idioma);
		materialApoioRef.setContinente(this.continente);
		materialApoioRef.setRegiao(this.regiao);
		materialApoioRef.setPalavraChave(this.palavraChave);
		materialApoioRef.setTag(this.tag);
		materialApoioRef.setPublicoAlvo(this.publicoAlvo);
		materialApoioRef.setTipoArquivo(this.tipoArquivo);
		materialApoioRef.setTipoDocumento(this.tipoDocumento);
		materialApoioRef.setTipoMaterial(this.tipoMaterial);
		materialApoioRef.setLocalExibicao(this.localExibicao);
		materialApoioRef.setResumo(this.resumo);
		
		return materialApoioRef;
	}
}
