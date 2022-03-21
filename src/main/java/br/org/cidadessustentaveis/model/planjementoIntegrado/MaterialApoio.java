package br.org.cidadessustentaveis.model.planjementoIntegrado;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "material_apoio")
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialApoio implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "material_apoio_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "material_apoio_id_seq", sequenceName = "material_apoio_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="subtitulo")
	private String subtitulo;
	
	@Column(name="autor")
	private String autor;
	
	@Column(name="instituicao")
	private String instituicao;
	
	@Column(name="data_publicacao")
	private LocalDate dataPublicacao;
	
	@Column(name="idioma")
	private String idioma;
	
	@Column(name="continente")
	private String continente;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="pais", nullable = false)
	private Pais pais;
	
	@Column(name="regiao")
	private String regiao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="provincia_estado", nullable = false)
	private ProvinciaEstado provinciaEstado;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "material_apoio_area_interesse",
        joinColumns = { @JoinColumn(name = "id_material_apoio") },
        inverseJoinColumns = { @JoinColumn(name = "id_area_interesse") } )
	private List<AreaInteresse> areasInteresse;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "material_apoio_eixo",
        joinColumns = { @JoinColumn(name = "id_material_apoio") },
        inverseJoinColumns = { @JoinColumn(name = "id_eixo") } )
	private List<Eixo> eixo;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "material_apoio_indicador",
        joinColumns = { @JoinColumn(name = "id_material_apoio") },
        inverseJoinColumns = { @JoinColumn(name = "id_indicador") } )
	private List<Indicador> indicador;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "material_apoio_objetivo_desenvolvimento_sustentavel",
        joinColumns = { @JoinColumn(name = "id_material_apoio") },
        inverseJoinColumns = { @JoinColumn(name = "id_objetivo_desenvolvimento_sustentavel") } )
	private List<ObjetivoDesenvolvimentoSustentavel> ods;
	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "material_apoio_meta_objetivo_desenvolvimento_sustentavel",
        joinColumns = { @JoinColumn(name = "id_material_apoio") },
        inverseJoinColumns = { @JoinColumn(name = "id_meta_objetivo_desenvolvimento_sustentavel") } )
	private List<MetaObjetivoDesenvolvimentoSustentavel> metaOds;
	
	@Column(name="palavra_chave")
	private String palavraChave;
	
	@Column(name="tag")
	private String tag;
	
	@Column(name="publico_alvo")
	private String publicoAlvo;
	
	@Column(name="tipo_arquivo")
	private String tipoArquivo;
	
	@Column(name="tipo_documento")
	private String tipoDocumento;
	
	@Column(name="tipo_material")
	private String tipoMaterial;
	
//	@Column(name="tipologia_cgee")
//	private Long tipologiaCgee;
	
	@Column(name="local_exibicao")
	private String localExibicao;
	
	@Column(name="resumo")
	private String resumo;
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="imagem_capa", nullable = false)
	private Arquivo imagemCapa;
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="arquivo_publicacao", nullable = false)
	private Arquivo arquivoPublicacao;

	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="prefeitura", nullable = false)
	private Prefeitura prefeitura;
}
