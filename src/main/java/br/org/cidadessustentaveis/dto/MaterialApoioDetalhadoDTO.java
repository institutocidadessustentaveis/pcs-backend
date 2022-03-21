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
public class MaterialApoioDetalhadoDTO implements Serializable{

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
	
	private String paisNome;
	
	private String regiao;
	
	private String cidadeNome;
	
	private String provinciaEstadoNome;
	
	private List<Long> areasInteresse;
	
	private List<String> eixoNome;
	
	private List<String> indicadorNome;
	
	private List<String> odsNome;
	
	private List<String> metaOdsNome;
	
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
	
	public MaterialApoioDetalhadoDTO(MaterialApoio materialApoioRef) {
		this.id = materialApoioRef.getId();
		this.titulo = materialApoioRef.getTitulo();
		this.subtitulo = materialApoioRef.getSubtitulo();
		this.autor = materialApoioRef.getAutor();
		this.instituicao = materialApoioRef.getInstituicao();
		this.dataPublicacao = materialApoioRef.getDataPublicacao();
		this.idioma = materialApoioRef.getIdioma();
		this.continente = materialApoioRef.getContinente();
		this.paisNome = materialApoioRef.getPais() != null ? materialApoioRef.getPais().getNome() : null;
		this.provinciaEstadoNome = materialApoioRef.getProvinciaEstado() != null ? materialApoioRef.getProvinciaEstado().getNome() : null;
		this.regiao = materialApoioRef.getRegiao();
		this.cidadeNome = materialApoioRef.getCidade() != null ? materialApoioRef.getCidade().getNome() : null;
		this.areasInteresse = materialApoioRef.getAreasInteresse() != null ? materialApoioRef.getAreasInteresse().stream().map(AreaInteresse:: getId).collect(Collectors.toList()): null;
		this.eixoNome = materialApoioRef.getEixo() != null ? materialApoioRef.getEixo().stream().map(Eixo:: getNome).collect(Collectors.toList()): null;
		this.indicadorNome = materialApoioRef.getIndicador() != null ? materialApoioRef.getIndicador().stream().map(Indicador:: getNome).collect(Collectors.toList()): null;
		this.odsNome = materialApoioRef.getOds() != null ? materialApoioRef.getOds().stream().map(ObjetivoDesenvolvimentoSustentavel:: getTitulo).collect(Collectors.toList()): null;
		this.metaOdsNome = materialApoioRef.getMetaOds() != null ? materialApoioRef.getMetaOds().stream().map(MetaObjetivoDesenvolvimentoSustentavel:: getDescricao).collect(Collectors.toList()): null;
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
	
}
