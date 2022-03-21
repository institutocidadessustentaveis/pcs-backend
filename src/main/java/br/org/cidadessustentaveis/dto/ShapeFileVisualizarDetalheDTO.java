package br.org.cidadessustentaveis.dto;

import java.util.ArrayList;
import java.util.List;

import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.model.planjementoIntegrado.RasterItem;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @AllArgsConstructor @NoArgsConstructor      
public class ShapeFileVisualizarDetalheDTO {
	private Long id;
	private Integer ano;
	private String titulo;
	private List<String> areasInteresse = new ArrayList<>();
	private String temaGeoespacial;
	private String instituicao;
	private String fonte;
	private String sistemaDeReferencia;
	private String tipoArquivo;
	private String nivelTerritorial;
	private boolean publicar;
	private List<Feature> shapes; // preenchido se tipoArquivo é 'Vetorial'
	private RasterItem raster;
	private List<String> eixos = new ArrayList<>();
	private List<Long> ods = new ArrayList<>();
	private List<String> metas = new ArrayList<>();
	private List<String> indicadores = new ArrayList<>();
	private String palavra;
	private String integridade;
	private String codigoMapa;
	private String resumoConteudo;
	private String dadosMapeados;
	private String escala;
	private List<String> cidades = new ArrayList<>();
	private List<String> estados = new ArrayList<>();
	private String cartografia;
	private Double latitude;
	private Double longitude;
	private String atualizacao;
	private String ambiente;
	private String codificacao;
	private String pais;
	private String regiao;
	private Boolean exibirAuto;
	private String mosaico;

	public ShapeFileVisualizarDetalheDTO( ShapeFile shapeFile) {
		this.id = shapeFile.getId();
		this.ano = shapeFile.getAno();
		this.titulo = shapeFile.getTitulo();
		this.instituicao = shapeFile.getInstituicao();
		this.fonte = shapeFile.getFonte();
		this.sistemaDeReferencia = shapeFile.getSistemaDeReferencia();
		this.tipoArquivo = shapeFile.getTipoArquivo();
		this.nivelTerritorial = shapeFile.getNivelTerritorial();
		this.publicar = shapeFile.getPublicar();
		this.exibirAuto = shapeFile.getExibirAuto();
		if(null != shapeFile.getTemaGeoespacial()) {
			this.temaGeoespacial = shapeFile.getTemaGeoespacial().getNome();
		}
		if(null !=  shapeFile.getEixos()) {			
			shapeFile.getEixos().forEach(it ->{
				eixos.add(it.getNome());
			});
		}
		if(null !=  shapeFile.getOds()) {			
			shapeFile.getOds().forEach(it ->{
				ods.add(it.getId());
			});
		}
		if(null !=  shapeFile.getMetas()) {			
			shapeFile.getMetas().forEach(it ->{
				metas.add(it.getDescricao());
			});
		}
		if(null !=  shapeFile.getIndicadores()) {			
			shapeFile.getIndicadores().forEach(it ->{
				indicadores.add(it.getNome());
			});
		}
		if(null !=  shapeFile.getAreasInteresse()) {			
			shapeFile.getAreasInteresse().forEach(it ->{
				areasInteresse.add(it.getNome());
			});
		}
		this.palavra = shapeFile.getPalavra();
		this.integridade= shapeFile.getIntegridade();
		this.codigoMapa= shapeFile.getCodigoMapa();
		this.resumoConteudo = shapeFile.getResumoConteudo();
		this.escala = shapeFile.getEscala();
		if(null !=  shapeFile.getCidades()) {			
			shapeFile.getCidades().forEach(it ->{
				cidades.add(it.getNome());
			});
		}
		if(null !=  shapeFile.getEstados()) {			
			shapeFile.getEstados().forEach(it ->{
				estados.add(it.getNome());
			});
		}
		this.cartografia = shapeFile.getCartografia();
		this.latitude = shapeFile.getLatitude();
		this.longitude = shapeFile.getLongitude();
		this.atualizacao = shapeFile.getAtualizacao();
		this.ambiente = shapeFile.getAmbiente();
		this.codificacao = shapeFile.getCodificacao();
		this.pais = shapeFile.getPais() != null ? shapeFile.getPais().getNome() : null;
		this.regiao = shapeFile.getRegiao();
		this.dadosMapeados = shapeFile.getDadosMapeados();
		this.mosaico = shapeFile.getMosaico();
	}
		
}