package br.org.cidadessustentaveis.model.planjementoIntegrado;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.util.CustomLocalDateTimeBridge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
	

@Entity
@Table(name = "shape_files")
@EntityListeners(ListenerAuditoria.class)
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ShapeFile implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "shape_files_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "shape_files_id_seq", sequenceName = "shape_files_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;

	@Column(name="ano")
	private Integer ano;

	@Column(name="titulo")
	private String titulo;
	
	@Column(name="instituicao")
	private String instituicao;
	
	@Column(name="fonte")
	private String fonte;
	
	@Column(name="sistema_referencia")
	private String sistemaDeReferencia;
	
	@Column(name="tipo_arquivo")
	private String tipoArquivo;
	
	@Column(name="nivel_territorial")
	private String nivelTerritorial;

	@Column(name="regiao")
	private String regiao;
	
	@Column(name="publicar")
	private Boolean publicar;
	
	@Column(name="exibir_auto")
	private Boolean exibirAuto;

	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "shapefile_area_interesse", 
        joinColumns = { @JoinColumn(name = "id_shapefile") }, 
        inverseJoinColumns = { @JoinColumn(name = "id_area_interesse") } )
	private List<AreaInteresse> areasInteresse;

	@ManyToMany(fetch = FetchType.LAZY,cascade = { CascadeType.MERGE })
	@JoinTable(name = "shapefile_indicador", joinColumns = @JoinColumn(name = "shapefile"), inverseJoinColumns = @JoinColumn(name = "indicador"))
	private List<Indicador> indicadores;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "shapefile_eixo", joinColumns = @JoinColumn(name = "id_shapefile"), inverseJoinColumns = @JoinColumn(name = "id_eixo"))
	private List<Eixo> eixos ;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "shapefile_ods", joinColumns = @JoinColumn(name = "id_shapefile"), inverseJoinColumns = @JoinColumn(name = "id_ods"))
	private List<ObjetivoDesenvolvimentoSustentavel> ods;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "shapefile_meta", joinColumns = @JoinColumn(name = "id_shapefile"), inverseJoinColumns = @JoinColumn(name = "id_meta"))
	private List<MetaObjetivoDesenvolvimentoSustentavel> metas;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="tema_geoespacial", nullable = false)
	private TemaGeoespacial temaGeoespacial;

	//bi-directional many-to-one association to Shape
	@OneToMany(mappedBy="shapeFile", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.ALL})
	private List<ShapeItem> shapes;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference("raster_itens-shapeFile")
	@JoinColumn(name = "id_raster_item")
	private RasterItem rasterItem;
	
	@Column(name="dt_cadastro")
	@Field(index=Index.YES)
	@FieldBridge(impl = CustomLocalDateTimeBridge.class)
	@Nullable
	private LocalDateTime dataHoraCadastro;
	
	@Column(name="dt_alteracao")
	@Field(index=Index.YES)
	@FieldBridge(impl = CustomLocalDateTimeBridge.class)
	@Nullable
	private LocalDateTime dataHoraAlteracao;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefeitura")
	private Prefeitura prefeitura;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cidade")
	private Cidade cidade;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="shape", cascade={CascadeType.REMOVE})
	private List<HistoricoUsoShape> historicosUsoShape;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="shape", cascade={CascadeType.REMOVE})
	private List<HistoricoShape> historicosShape;
	
	@Column(name="file_name")
	private String fileName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indicador")
	private Indicador indicador;
	
	@Column(name="palavra")
	private String palavra;
	
	@Column(name="mosaico")
	private String mosaico;
	
	@Column(name="codigofolha")
	private String codigoFolha;
	
	@Column(name="integridade")
	private String integridade;
	
	@Column(name="codigomapa")
	private String codigoMapa;
	
	@Column(name="resumoconteudo")
	private String resumoConteudo;
	
	@Column(name="dadosmapeados")
	private String dadosMapeados;
	
	@Column(name="escala")
	private String escala;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "shapefile_cidade", joinColumns = @JoinColumn(name = "id_shapefile"), inverseJoinColumns = @JoinColumn(name = "id_cidade"))
	private List<Cidade> cidades ;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "shapefile_estado", joinColumns = @JoinColumn(name = "id_shapefile"), inverseJoinColumns = @JoinColumn(name = "id_estado"))
	private List<ProvinciaEstado> estados ;
	
	@Column(name="cartografia")
	private String cartografia;
	
	@Column(name="lat")
	private Double latitude;
	
	@Column(name="long")
	private Double longitude;
	
	@Column(name="atualizacao")
	private String atualizacao;

	@Column(name="ambiente")
	private String ambiente;
	
	@Column(name="codificacao")
	private String codificacao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="pais")
	private Pais pais;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_subdivisao_cidade")
	private SubdivisaoCidade subdivisaoCidade;
	
	public ShapeFile(Integer ano, String titulo, String instituicao,String fonte, String sistemaDeReferencia, 
			String tipoArquivo, String nivelTerritorial, Boolean publicar, String palavra,
			String mosaico, String codigofolha, String integridade, String codigomapa, String resumoconteudo,
			String dadosmapeados, String escala, String cartografia, Double latitude, Double longitude,
			String atualizacao, String ambiente, String codificacao, Boolean exibirAuto) {
		this.ano = ano;
		this.titulo = titulo;
		this.instituicao = instituicao;
		this.fonte = fonte;
		this.sistemaDeReferencia = sistemaDeReferencia;
		this.tipoArquivo = tipoArquivo;
		this.nivelTerritorial = nivelTerritorial;
		this.publicar = publicar;
		this.palavra = palavra;
		this.mosaico = mosaico;
		this.codigoFolha = codigofolha;
		this.integridade = integridade;
		this.codigoMapa = codigomapa;
		this.resumoConteudo = resumoconteudo;
		this.dadosMapeados = dadosmapeados;
		this.escala = escala;
		this.cartografia = cartografia;
		this.latitude = latitude;
		this.longitude = longitude;
		this.atualizacao = atualizacao;
		this.ambiente = ambiente;
		this.codificacao = codificacao;
		this.exibirAuto = exibirAuto;
	}


	public ShapeFile(Long id, Integer ano, String titulo, String instituicao, String fonte, String sistemaDeReferencia,
			String tipoArquivo, String nivelTerritorial, Boolean publicar, List<AreaInteresse> areaInteresse,
			List<ShapeItem> shapes, RasterItem rasterItem, LocalDateTime dataHoraCadastro,
			LocalDateTime dataHoraAlteracao, Prefeitura prefeitura, Usuario usuario, Cidade cidade) {
		super();
		this.id = id;
		this.ano = ano;
		this.titulo = titulo;
		this.instituicao = instituicao;
		this.fonte = fonte;
		this.sistemaDeReferencia = sistemaDeReferencia;
		this.tipoArquivo = tipoArquivo;
		this.nivelTerritorial = nivelTerritorial;
		this.publicar = publicar;
		this.areasInteresse = areaInteresse;
		this.shapes = shapes;
		this.rasterItem = rasterItem;
		this.dataHoraCadastro = dataHoraCadastro;
		this.dataHoraAlteracao = dataHoraAlteracao;
		this.prefeitura = prefeitura;
		this.usuario = usuario;
		this.cidade = cidade;
	}
	
	
}
