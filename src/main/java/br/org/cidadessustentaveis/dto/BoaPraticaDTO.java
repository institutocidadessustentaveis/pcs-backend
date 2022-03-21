package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.boaspraticas.SolucaoBoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BoaPraticaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nomeInstituicao;
	private Long idPais;
	private Long idEstado;
	private Long idMunicipio;
	private String endereco;
	private String site;
	private String nomeResponsavel;
	private String contato;
	private String email;
	private String telefone;
	private String celular;
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
	private Long idEixo;
	private List<Long> idsOds;
	private List<Long> idsMetasOds;
	private List<Long> idsIndicadores;
	private String informacoesComplementares;
	private Long idPrefeituraCadastro;
	private Boolean paginaInicial;
	private String tipo;
	private List<SolucaoBoaPraticaDTO> solucoes;
	private Boolean possuiFiltro;
	private String autorImagemPrincipal;
	
	public BoaPraticaDTO(BoaPratica objRef) {
		this.id = objRef.getId();
		this.nomeInstituicao = objRef.getNomeInstituicao();
		if(objRef.getEstado() != null) {
			this.idEstado = objRef.getEstado().getId();
		}
		if(objRef.getEstado() != null && objRef.getEstado().getPais() != null) {
			this.idPais = objRef.getEstado().getPais().getId();
		}
		if(objRef.getMunicipio() != null) {
			this.idMunicipio = objRef.getMunicipio().getId();
		}
		this.endereco = objRef.getEndereco();
		this.site = objRef.getSite();
		this.nomeResponsavel = objRef.getNomeResponsavel();
		this.contato = objRef.getContato();
		this.email = objRef.getEmail();
		this.telefone = objRef.getTelefone();
		this.celular = objRef.getCelular();
		this.dataPublicacao = objRef.getDataPublicacao();
		this.dtInicio = objRef.getDtInicio();
		this.galeriaDeVideos = objRef.getGaleriaDeVideos()!= null ? Arrays.asList(objRef.getGaleriaDeVideos().split("!&")) : null;
		this.titulo = objRef.getTitulo();
		this.subtitulo = objRef.getSubtitulo();
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
		this.idEixo = objRef.getEixo().getId();
		this.idsOds =  objRef.getOds().stream().map(ObjetivoDesenvolvimentoSustentavel::getId).collect(Collectors.toList());
		this.idsMetasOds = objRef.getMetasOds().stream().map(MetaObjetivoDesenvolvimentoSustentavel::getId).collect(Collectors.toList());
		this.idsIndicadores = objRef.getIndicadores().stream().map(Indicador::getId).collect(Collectors.toList());
		this.informacoesComplementares = objRef.getInformacoesComplementares();
		this.idPrefeituraCadastro = objRef.getPrefeitura() != null ? objRef.getPrefeitura().getId(): null;
		this.paginaInicial = objRef.getPaginaInicial();
		this.tipo = objRef.getTipo();
		this.solucoes = objRef.getSolucoes() != null ? objRef.getSolucoes().stream().map(solucao -> new SolucaoBoaPraticaDTO(solucao)).collect(Collectors.toList()) : null;
		this.possuiFiltro = objRef.getPossuiFiltro();
		this.autorImagemPrincipal = objRef.getAutorImagemPrincipal();
	}
	
	public BoaPratica toEntityInsert() {
//		return new BoaPratica(null, this.nomeInstituicao, null, null, this.endereco, this.site, this.nomeResponsavel, this.contato, this.email, this.telefone, 
//				this.celular, this.dtInicio, this.galeriaDeVideos.stream().map( item -> item).collect( Collectors.joining( "!&" )), this.titulo, this.subtitulo, this.objetivoGeral, this.objetivoEspecifico, this.principaisResultados, this.aprendizadoFundamental,
//				this.parceirosEnvolvidos, this.resultadosQuantitativos, this.resultadosQualitativos, this.parametrosContemplados, this.publicoAtingido, null,
//				false,null, null, null, null, this.informacoesComplementares, this.fontesReferencia.stream().map(obj -> obj.toEntityInsert()).collect(Collectors.toList()), null, null);
		return new BoaPratica(null, this.nomeInstituicao, null, null, this.endereco, this.site, this.nomeResponsavel, this.contato, this.email, this.telefone, 
				this.celular, this.dtInicio, this.dataPublicacao, this.galeriaDeVideos.stream().map( item -> item).collect( Collectors.joining( "!&" )), this.titulo, this.subtitulo, this.objetivoGeral, this.objetivoEspecifico, this.principaisResultados, this.aprendizadoFundamental,
				this.parceirosEnvolvidos, this.resultadosQuantitativos, this.resultadosQualitativos, this.parametrosContemplados, this.publicoAtingido, this.fontesReferencia, null,
				false,null, null, null, null, this.informacoesComplementares, null, null, null, null,this.possuiFiltro, this.autorImagemPrincipal);
	}
	
	public BoaPratica toEntityUpdate(BoaPratica boaPraticaRef) {
		boaPraticaRef.setNomeInstituicao(this.nomeInstituicao);
		boaPraticaRef.setEndereco(this.endereco);
		boaPraticaRef.setSite(this.site);
		boaPraticaRef.setNomeResponsavel(this.nomeResponsavel);
		boaPraticaRef.setContato(this.contato);
		boaPraticaRef.setEmail(this.email);
		boaPraticaRef.setTelefone(this.telefone);
		boaPraticaRef.setCelular(this.celular);
		boaPraticaRef.setDtInicio(this.dtInicio);
		boaPraticaRef.setDataPublicacao(this.dataPublicacao);
		boaPraticaRef.setTitulo(this.titulo);
		boaPraticaRef.setSubtitulo(this.subtitulo);
		boaPraticaRef.setObjetivoGeral(this.objetivoGeral);
		boaPraticaRef.setObjetivoEspecifico(this.objetivoEspecifico);
		boaPraticaRef.setPrincipaisResultados(this.principaisResultados);
		boaPraticaRef.setAprendizadoFundamental(this.aprendizadoFundamental);
		boaPraticaRef.setParceirosEnvolvidos(this.parceirosEnvolvidos);
		boaPraticaRef.setResultadosQuantitativos(this.resultadosQuantitativos);
		boaPraticaRef.setResultadosQualitativos(this.resultadosQualitativos);
		boaPraticaRef.setParametrosContemplados(this.parametrosContemplados);
		boaPraticaRef.setPublicoAtingido(this.publicoAtingido);
		boaPraticaRef.setFontesReferencia(this.fontesReferencia);
		boaPraticaRef.setInformacoesComplementares(informacoesComplementares);
		boaPraticaRef.setGaleriaDeVideos(this.galeriaDeVideos.stream().map( item -> item).collect( Collectors.joining( "!&" )));
		boaPraticaRef.setPossuiFiltro(possuiFiltro);
		boaPraticaRef.setAutorImagemPrincipal(this.autorImagemPrincipal);
		
		return boaPraticaRef;
	}
	
	
}