package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.eventos.Evento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RelatorioEventosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    @NotNull
    @NotEmpty
    private LocalDate dataEvento;
    
    @NotNull
    @NotEmpty
    private String tipo;
       
    @NotNull
    @NotEmpty
    private String endereco;
    
    @NotNull
    @NotEmpty
    private String nome;
       
    @NotNull
    @NotEmpty
    private String nomePais;
    
    @NotNull
    @NotEmpty
    private String nomeProvinciaEstado;
    
    @NotNull
    @NotEmpty
    private String nomeCidade;
    
    @NotNull
    @NotEmpty
    private String descricao;
    
    @NotNull
    @NotEmpty
    private String organizador;
   
    @NotNull
    @NotEmpty
    private String temas;

    @NotNull
   	@NotEmpty
   	private String eixos;
    
    @NotNull
    @NotEmpty
    private String tituloOds;
    
    @NotNull
    @NotEmpty
    private boolean online;
    
    @NotNull
    @NotEmpty
    private String site;
    
    @NotNull
    @NotEmpty
    private Double latitude;
    
    @NotNull
    @NotEmpty
    private Double longitude;
    
    @NotNull
    @NotEmpty
    private boolean publicado;
    
    @NotNull
    @NotEmpty
    private boolean externo;
    
    @JsonProperty("link_externo")
    private String linkExterno;
    
    public RelatorioEventosDTO (Evento objRef) {
        this.id = objRef.getId();    
        this.tipo = objRef.getTipo();
        this.nome = objRef.getNome();
        this.descricao = objRef.getDescricao();
        this.dataEvento = objRef.getDataEvento();
        this.organizador = objRef.getOrganizador();
        if(objRef.getPais() != null) {
        	this.nomePais = objRef.getPais().getNome();
		}
        if(objRef.getProvinciaEstado() != null) {
        	this.nomeProvinciaEstado = objRef.getProvinciaEstado().getNome();
		}
        if(objRef.getCidade() != null) {
        	this.nomeCidade = objRef.getCidade().getNome();
		}
        if(objRef.getOds() != null) {
        	this.tituloOds = objRef.getOds().getTitulo();
        }
        this.online = objRef.isOnline();
        this.endereco = objRef.getEndereco();
        this.site = objRef.getSite();
        this.latitude = objRef.getLatitude();
        this.longitude = objRef.getLongitude();
        this.publicado = objRef.isPublicado();
        this.externo = objRef.isExterno();
        this.linkExterno = objRef.getLinkExterno();
        
    }
    
    public void listarTemas(List<AreaInteresse> listaAreaInteresse) {
    	this.temas = "";
    	for(AreaInteresse elemento: listaAreaInteresse) {
    		this.temas = this.temas + elemento.getNome() + " ";
    	}
    }

    
    public void listarEixos(List<Eixo> listaEixos) {
    	this.eixos = "";
    	for(Eixo elemento: listaEixos) {
    		this.eixos = this.eixos + elemento.getNome() + " ";
    	}
    }
    
}
