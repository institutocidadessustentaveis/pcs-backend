package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ShapeFileDetalheDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer ano;
	private String titulo;
	private List<Long> areasInteresse = new ArrayList<>();
	private Long temaGeoespacial;
	private String instituicao;
	private String fonte;
	private String sistemaDeReferencia;
	private String tipoArquivo;
	private String nivelTerritorial;
	private boolean publicar;
	private List<Feature> shapes; // preenchido se tipoArquivo é 'Vetorial'
	private RasterItemDTO raster;
	private Long idPrefeitura;
	private Long idCidade;
	private List<Long> eixos = new ArrayList<>();
	private List<Long> ods = new ArrayList<>();
	private List<Long> metas = new ArrayList<>();
	private List<Long> indicadores = new ArrayList<>();
	private Long indicador;
	private String palavra;
	private String mosaico;
	private String codigoFolha;
	private String integridade;
	private String codigoMapa;
	private String resumoConteudo;
	private String dadosMapeados;
	private String escala;
	private List<Long> cidades = new ArrayList<>();
	private List<Long> estados = new ArrayList<>();
	private String cartografia;
	private Double latitude;
	private Double longitude;
	private String atualizacao;
	private String ambiente;
	private String codificacao;
	private Long pais;
	private Boolean exibirAuto;
	private String origemCadastro;
	
	public ShapeFileDetalheDTO(ShapeFile shapeFile) {
		this.id = shapeFile.getId();
		this.ano = shapeFile.getAno();
		this.titulo = shapeFile.getTitulo();
		this.instituicao = shapeFile.getInstituicao();
		this.fonte = shapeFile.getFonte();
		this.sistemaDeReferencia = shapeFile.getSistemaDeReferencia();
		this.tipoArquivo = shapeFile.getTipoArquivo();
		this.nivelTerritorial = shapeFile.getNivelTerritorial();
		this.publicar = shapeFile.getPublicar();
		this.origemCadastro = shapeFile.getPrefeitura() != null ? shapeFile.getPrefeitura().getCidade().getNome() +" - "+ shapeFile.getPrefeitura().getCidade().getProvinciaEstado().getSigla(): "PCS";
		this.exibirAuto = shapeFile.getExibirAuto();
		if(null != shapeFile.getPrefeitura()) {
			this.idPrefeitura = shapeFile.getPrefeitura().getId();
			this.idCidade = shapeFile.getPrefeitura().getCidade().getId();
		}
		if(null !=  shapeFile.getAreasInteresse()) {			
			shapeFile.getAreasInteresse().forEach(it ->{
				areasInteresse.add(it.getId());
			});
		}
		if(null != shapeFile.getTemaGeoespacial()) {
			this.temaGeoespacial = shapeFile.getTemaGeoespacial().getId();
		}
		if(null !=  shapeFile.getEixos()) {			
			shapeFile.getEixos().forEach(it ->{
				eixos.add(it.getId());
			});
		}
		if(null !=  shapeFile.getOds()) {			
			shapeFile.getOds().forEach(it ->{
				ods.add(it.getId());
			});
		}
		if(null !=  shapeFile.getMetas()) {			
			shapeFile.getMetas().forEach(it ->{
				metas.add(it.getId());
			});
		}
		if(null !=  shapeFile.getIndicadores()) {			
			shapeFile.getIndicadores().forEach(it ->{
				indicadores.add(it.getId());
			});
		}
		if(null != shapeFile.getIndicador()) {
			this.indicador = shapeFile.getIndicador().getId();
		}
		this.palavra = shapeFile.getPalavra();
		this.mosaico = shapeFile.getMosaico();
		this.codigoFolha= shapeFile.getCodigoFolha();
		this.integridade= shapeFile.getIntegridade();
		this.codigoMapa= shapeFile.getCodigoMapa();
		this.resumoConteudo = shapeFile.getResumoConteudo();
		this.dadosMapeados = shapeFile.getDadosMapeados();
		this.escala = shapeFile.getEscala();
		if(null !=  shapeFile.getCidades()) {			
			shapeFile.getCidades().forEach(it ->{
				cidades.add(it.getId());
			});
		}
		if(null !=  shapeFile.getEstados()) {			
			shapeFile.getEstados().forEach(it ->{
				estados.add(it.getId());
			});
		}
		this.cartografia = shapeFile.getCartografia();
		this.latitude = shapeFile.getLatitude();
		this.longitude = shapeFile.getLongitude();
		this.atualizacao = shapeFile.getAtualizacao();
		this.ambiente = shapeFile.getAmbiente();
		this.codificacao = shapeFile.getCodificacao();
		this.pais = shapeFile.getPais() != null ? shapeFile.getPais().getId() : null;
	}
}