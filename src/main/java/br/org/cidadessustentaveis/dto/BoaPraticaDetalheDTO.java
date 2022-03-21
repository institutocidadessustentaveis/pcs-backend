package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BoaPraticaDetalheDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nomeCidade;
	private Long idCidade;
	private String nomeEstado;
	private String nomePais;
	private String endereco;
	private String site;
	private LocalDate dtInicio;
	private LocalDate dataPublicacao;
	private ImagemBoaPraticaDTO imagemPrincipal;
	private List<ImagemBoaPraticaDTO> galeriaDeImagens;
	private List<String> galeriaDeVideos;
	private String titulo;
	private String subtitulo;
	private String objetivoGeral;
	private String objetivoEspecifico;
	private String principaisResultados;
	private String aprendizadoFundamental;
	private String parceirosEnvolvidos;
	private String resultadosQuantitativos;
	private String resultadosQualitativos;
	private String parametrosContemplados;
	private String publicoAtingido;
//	private List<FonteReferenciaBoaPraticaDTO> fontesReferencia;
	private String fontesReferencia;
	private String informacoesComplementares;
	private String tipo;
	private String nomeEixo;
	private Long idEixo;
	private String linkEixo;
	private List<SolucaoBoaPraticaDTO> solucoes;
	private Boolean possuiFiltro;
	
	public BoaPraticaDetalheDTO(BoaPratica objRef) {
		this.id = objRef.getId();
		
		if(objRef.getMunicipio() != null) {
			this.nomeCidade = objRef.getMunicipio().getNome();
			this.idCidade = objRef.getMunicipio().getId();
		}
		
		if(objRef.getEstado() != null) {
			this.nomeEstado = objRef.getEstado().getNome();	
			this.nomePais = objRef.getEstado().getPais().getNome();
		}

		this.endereco = objRef.getEndereco();
		this.site = objRef.getSite();
		this.dtInicio = objRef.getDtInicio();
		this.galeriaDeVideos = objRef.getGaleriaDeVideos()!= null ? Arrays.asList(objRef.getGaleriaDeVideos().split("!&")) : null;
		this.titulo = objRef.getTitulo();
		this.subtitulo = objRef.getSubtitulo();
		this.dataPublicacao = objRef.getDataPublicacao();
		this.objetivoGeral = objRef.getObjetivoGeral();
		this.objetivoEspecifico = objRef.getObjetivoEspecifico();
		this.principaisResultados = objRef.getPrincipaisResultados();
		this.aprendizadoFundamental = objRef.getAprendizadoFundamental();
		this.parceirosEnvolvidos = objRef.getParceirosEnvolvidos();
		this.resultadosQuantitativos = objRef.getResultadosQuantitativos();
		this.resultadosQualitativos = objRef.getResultadosQualitativos();
		this.parametrosContemplados = objRef.getParametrosContemplados();
		this.publicoAtingido = objRef.getPublicoAtingido();
//		this.fontesReferencia = objRef.getFontesReferencia().stream().map(obj -> new FonteReferenciaBoaPraticaDTO(obj)).collect(Collectors.toList());
		this.fontesReferencia = objRef.getFontesReferencia();
		this.informacoesComplementares = objRef.getInformacoesComplementares();
		this.tipo = objRef.getTipo();
		if(objRef.getEixo()!= null) {
			this.nomeEixo = objRef.getEixo().getNome();
			this.idEixo = objRef.getEixo().getId();
			this.linkEixo = objRef.getEixo().getLink();
		}
		
		this.solucoes = objRef.getSolucoes().stream().map(obj -> new SolucaoBoaPraticaDTO(obj)).collect(Collectors.toList());
		this.possuiFiltro = objRef.getPossuiFiltro();
	}	
}