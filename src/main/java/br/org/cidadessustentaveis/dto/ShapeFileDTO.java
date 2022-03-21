package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShapeFileDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer ano;
	private String titulo;
	private List<Long> areasInteresse;
	private Long temaGeoespacial;
	private String instituicao;
	private String fonte;
	private String sistemaDeReferencia;
	private String tipoArquivo;
	private String nivelTerritorial;
	private Boolean publicar;
	private String origemCadastro;
	private Long idCidade;
	private String fileName;
	private List<Long> eixos;
	private List<Long> ods;
	private List<Long> metas;
	private List<Long> indicadores;
	private Long indicador;
	private String palavra;
	private String mosaico;
	private String codigoFolha;
	private String integridade;
	private String codigoMapa;
	private String resumoConteudo;
	private String dadosMapeados;
	private String escala;
	private List<Long> cidades;
	private List<Long> estados;
	private String cartografia;
	private Double latitude;
	private Double longitude;
	private String atualizacao;
	private String ambiente;
	private String codificacao;
	private LocalDateTime dataHoraAlteracao;
	private LocalDateTime dataHoraCadastro;
	private Long pais;
	private String regiao;
	private Boolean exibirAuto;

	
	public ShapeFileDTO(ShapeFile shapefile) {
		this.id = shapefile.getId();
		this.ano = shapefile.getAno();
		this.titulo = shapefile.getTitulo();
		this.instituicao = shapefile.getInstituicao();
		this.fonte = shapefile.getFonte();
		this.sistemaDeReferencia = shapefile.getSistemaDeReferencia();
		this.tipoArquivo = shapefile.getTipoArquivo();
		this.nivelTerritorial = shapefile.getNivelTerritorial();
		this.regiao = shapefile.getRegiao();
		this.publicar = shapefile.getPublicar();
		this.origemCadastro = shapefile.getPrefeitura() != null ? shapefile.getPrefeitura().getCidade().getNome() +" - "+ shapefile.getPrefeitura().getCidade().getProvinciaEstado().getSigla(): "PCS";
		this.idCidade = shapefile.getPrefeitura() != null ? shapefile.getPrefeitura().getCidade().getId() : null;
		this.fileName = shapefile.getFileName();
		this.dataHoraAlteracao = shapefile.getDataHoraAlteracao();
		this.dataHoraCadastro = shapefile.getDataHoraCadastro();
		this.exibirAuto = shapefile.getExibirAuto();
	}
	
	public ShapeFileDTO(Long id, Integer ano, String titulo, String instituicao, String fonte,
			String sistemaDeReferencia, String tipoArquivo, String nivelTerritorial, Boolean publicar) {
		this.id = id;
		this.ano = ano;
		this.titulo = titulo;
		this.instituicao = instituicao;
		this.fonte = fonte;
		this.sistemaDeReferencia = sistemaDeReferencia;
		this.tipoArquivo = tipoArquivo;
		this.nivelTerritorial = nivelTerritorial;
		this.publicar = publicar;
	}
	
	public ShapeFile toEntityInsert() {
		return new ShapeFile(this.ano, this.titulo, this.instituicao, this.fonte, this.sistemaDeReferencia, 
				this.tipoArquivo, this.nivelTerritorial, this.publicar, this.palavra,
				this.mosaico, this.codigoFolha, this.integridade, this.codigoMapa, this.resumoConteudo, 
				this.dadosMapeados, this.escala, this.cartografia, this.latitude, this.longitude,
				this.atualizacao, this.ambiente, this.codificacao, this.exibirAuto);
	}	
	
	public ShapeFile toEntityUpdate(ShapeFile shapeFileRef) {
		shapeFileRef.setId(this.id);
		shapeFileRef.setAno(this.ano);
		shapeFileRef.setTitulo(this.titulo);
		shapeFileRef.setInstituicao(this.instituicao);
		shapeFileRef.setFonte(this.fonte);
		shapeFileRef.setSistemaDeReferencia(this.sistemaDeReferencia);
		shapeFileRef.setTipoArquivo(this.tipoArquivo);
		shapeFileRef.setNivelTerritorial(this.nivelTerritorial);
		shapeFileRef.setRegiao(this.regiao);
		shapeFileRef.setPublicar(this.publicar);
		shapeFileRef.setPalavra(this.palavra);
		shapeFileRef.setMosaico(this.mosaico);
		shapeFileRef.setCodigoFolha(this.codigoFolha);
		shapeFileRef.setIntegridade(this.integridade);
		shapeFileRef.setCodigoMapa(this.codigoMapa);
		shapeFileRef.setResumoConteudo(this.resumoConteudo);
		shapeFileRef.setDadosMapeados(this.dadosMapeados);
		shapeFileRef.setEscala(this.escala);
		shapeFileRef.setCartografia(this.cartografia);
		shapeFileRef.setLatitude(this.latitude);
		shapeFileRef.setLongitude(this.longitude);
		shapeFileRef.setAtualizacao(this.atualizacao);
		shapeFileRef.setAmbiente(this.ambiente);
		shapeFileRef.setCodificacao(this.codificacao);
		shapeFileRef.setExibirAuto(this.exibirAuto);
		return shapeFileRef;
	}
}
