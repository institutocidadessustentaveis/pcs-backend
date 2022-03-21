package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RelatorioBoaPraticaPcsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    private String nomeInstituicao;
    private String titulo;
    private String provinciaEstado;
    private String cidade;
    
    private String endereco;
    private String site;
    private String nomeResponsavel;
    private String contato;
    private String email;
    private String telefone;
    private String celular;
    
    private LocalDate dtInicio;
    
    private String objetivoGeral;
    private String objetivoEspecifico;
    private String principaisResultados;
    private String aprendizadoFundamental;
    private String parceirosEnvolvidos;
    private String resultadosQuantitativos;
    private String resultadosQualitativos;
    
    private String parametrosContemplados;
    private String publicoAtingido;
    private String eixo;
    private String informacoesComplementares;
    
    private String prefeitura;
    private String subtitulo;
    private String galeriaDeVideos;
    private String fontesReferencia;
    private LocalDate dataPublicacao;
    
    private String indicadores;
    private String metasOds; 
    private String ods; 
    
    private String tipo;
    private Boolean paginaInicial;
    private Boolean possuiFiltro;
    private String autorImagemPrincipal;

	public RelatorioBoaPraticaPcsDTO(BoaPratica objRef) {
		super();
		this.id = objRef.getId();
		this.nomeInstituicao = objRef.getNomeInstituicao();
		this.titulo = objRef.getTitulo();
        if(objRef.getEstado() != null) {
        	this.provinciaEstado = objRef.getEstado().getNome();
		}
        if(objRef.getMunicipio() != null) {
        	this.cidade = objRef.getMunicipio().getNome();
		}
        this.endereco = objRef.getEndereco();
        this.site = objRef.getSite();
        this.nomeResponsavel = objRef.getNomeResponsavel();
        this.contato = objRef.getContato();
        this.email = objRef.getEmail();
        this.telefone = objRef.getTelefone();
        this.celular = objRef.getCelular();
        this.dtInicio = objRef.getDtInicio();
        this.objetivoGeral = objRef.getObjetivoGeral();
        this.objetivoEspecifico = objRef.getObjetivoEspecifico();
        this.principaisResultados = objRef.getPrincipaisResultados();
        this.aprendizadoFundamental = objRef.getAprendizadoFundamental();
        this.parceirosEnvolvidos = objRef.getParceirosEnvolvidos();
        this.resultadosQuantitativos = objRef.getResultadosQuantitativos();
        this.resultadosQualitativos = objRef.getResultadosQualitativos();
        this.parametrosContemplados = objRef.getParametrosContemplados();
        this.publicoAtingido = objRef.getPublicoAtingido();
        if(objRef.getEixo() != null) {
        	this.eixo = objRef.getEixo().getNome();
        }
        this.informacoesComplementares = objRef.getInformacoesComplementares();
        if(objRef.getPrefeitura() != null) {
        	this.prefeitura = objRef.getPrefeitura().getNome();
        }
        this.subtitulo = objRef.getSubtitulo();
        this.galeriaDeVideos = objRef.getGaleriaDeVideos();
        this.fontesReferencia = objRef.getFontesReferencia();
        this.dataPublicacao = objRef.getDataPublicacao();
        this.tipo = objRef.getTipo();
        this.paginaInicial = objRef.getPaginaInicial();
        this.possuiFiltro = objRef.getPossuiFiltro();
        this.autorImagemPrincipal = objRef.getAutorImagemPrincipal();
	}
	
	
    
    public void listarIndicadores(List<Indicador> listaIndicador) {
    	this.indicadores = "";
    	for(Indicador elemento: listaIndicador) {
    		this.indicadores = this.indicadores + elemento.getNome() + " ";
    	}
    }

    public void listarMetasOds(List<MetaObjetivoDesenvolvimentoSustentavel> listaMetasOds) {
    	this.metasOds = "";
    	for(MetaObjetivoDesenvolvimentoSustentavel elemento: listaMetasOds) {
    		this.metasOds = this.metasOds + elemento.getDescricao() + " ";
    	}
    }
  
    public void listarOds(List<ObjetivoDesenvolvimentoSustentavel> listaOds) {
    	this.ods = "";
    	for(ObjetivoDesenvolvimentoSustentavel elemento: listaOds) {
    			this.ods = this.ods + elemento.getTitulo() + "; ";
    	}
    }

}
