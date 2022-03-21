package br.org.cidadessustentaveis.model.biblioteca;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="biblioteca") 
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Biblioteca implements Serializable{

	private static final long serialVersionUID = -221476515171440178L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "biblioteca_area_interesse",
        joinColumns = { @JoinColumn(name = "biblioteca") },
        inverseJoinColumns = { @JoinColumn(name = "area_interesse") } )
	private List<AreaInteresse> areasInteresse;	
	
	@Column(name="autor")
	private String autor;
	
	@ElementCollection
	@CollectionTable(name="biblioteca_tipo_autor", joinColumns=@JoinColumn(name="biblioteca"))
	@Column(name="tipo_autor")
	private List<String> tipoAutor;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;
	
	@Column(name="data_publicacao")
	private LocalDate dataPublicacao;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "biblioteca_eixo",
	        joinColumns = { @JoinColumn(name = "biblioteca") },
	        inverseJoinColumns = { @JoinColumn(name = "eixo") } )
	private List<Eixo> eixos;
	
	@Column(name="idioma")
	private String idioma;
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="imagem_capa", nullable = false)
	private Arquivo imagemCapa;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "biblioteca_indicador",
	        joinColumns = { @JoinColumn(name = "biblioteca") },
	        inverseJoinColumns = { @JoinColumn(name = "indicador") } )
	private List<Indicador> indicadores;
	
	@Column(name="instituicao")
	private String instituicao;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "biblioteca_meta",
	        joinColumns = { @JoinColumn(name = "biblioteca") },
	        inverseJoinColumns = { @JoinColumn(name = "meta") } )
	private List<MetaObjetivoDesenvolvimentoSustentavel> meta;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "biblioteca_ods",
	        joinColumns = { @JoinColumn(name = "biblioteca") },
	        inverseJoinColumns = { @JoinColumn(name = "ods") } )
	private List<ObjetivoDesenvolvimentoSustentavel> ods;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="pais_publicacao", nullable = false)
	private Pais paisPublicacao;
	
	@Column(name="palavra_chave")
	private String palavraChave;
	
	@ElementCollection
	@CollectionTable(name="biblioteca_publico_alvo", joinColumns=@JoinColumn(name="biblioteca"))
	@Column(name="publico_alvo")
	private List<String> publicoAlvo;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="subtitulo")
	private String subtitulo;
	
	@Column(name="tipo_material")
	private String tipoMaterial;
	
	@Column(name="titulo_publicacao")
	private String tituloPublicacao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name="local_exibicao")
	private String localExibicao;
	
	@ElementCollection
	@CollectionTable(name="biblioteca_modulo", joinColumns=@JoinColumn(name="biblioteca"))
	@Column(name="modulo")
	private List<String> modulo;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="estado", nullable = false)
	private ProvinciaEstado estado;
	
	@Column(name="galeria_videos")
	private String galeriaDeVideos;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinTable(
        name = "biblioteca_arquivo_audio",
        joinColumns = { @JoinColumn(name = "biblioteca") },
        inverseJoinColumns = { @JoinColumn(name = "arquivo") } )
	private List<Arquivo> arquivos;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinTable(
        name = "biblioteca_arquivo_multimidia",
        joinColumns = { @JoinColumn(name = "biblioteca") },
        inverseJoinColumns = { @JoinColumn(name = "arquivo") } )
	private List<Arquivo> arquivoMultimidia;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="grupo_academico")
	private GrupoAcademico grupoAcademico;
	
	@Column(name="grupo_academico_nome")
	private String grupoAcademicoNome;
	
	@ElementCollection
	@CollectionTable(name="biblioteca_shapes", joinColumns=@JoinColumn(name="biblioteca"))
	@Column(name="shape")
	private List<Long> shapesRelacionados;

}
