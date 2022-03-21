package br.org.cidadessustentaveis.model.eventos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="evento") 
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String tipo;
	
	private String nome;
	
	private LocalTime horario;
	
	private String descricao;
	
	private LocalDate dataEvento;
	
	private String organizador;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "evento_tema",
        joinColumns = { @JoinColumn(name = "evento") },
        inverseJoinColumns = { @JoinColumn(name = "area_interesse") } )
	private List<AreaInteresse> temas;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="pais", nullable = false)
	private Pais pais;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="provincia_estado")
	@JsonBackReference(value="provincias-cidades")
	private ProvinciaEstado provinciaEstado;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "evento_eixo",
	        joinColumns = { @JoinColumn(name = "evento") },
	        inverseJoinColumns = { @JoinColumn(name = "eixo") } )
	private List<Eixo> eixos;

	@OneToOne(targetEntity = ObjetivoDesenvolvimentoSustentavel.class)
	@JoinColumn(name = "ods", referencedColumnName = "id")
	private ObjetivoDesenvolvimentoSustentavel ods;
	
	private boolean online;
	
	private String endereco;
	
	private String site;

	private Double latitude;
	
	private Double longitude;

	private boolean publicado;
	
	private boolean externo;
	
	private String linkExterno;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "evento_noticia",
        joinColumns = { @JoinColumn(name = "evento") },
        inverseJoinColumns = { @JoinColumn(name = "noticia") } )
	private List<Noticia> noticiasRelacionadas;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario_cadastro", nullable = false)
	private Usuario usuarioCadastro;

	
	public Evento(Evento evento) {
		this.id =  evento.getId();
		this.dataEvento = evento.getDataEvento();
		this.tipo = evento.getTipo();
		this.nome = evento.getNome();
		this.descricao = evento.getDescricao();
		this.organizador = evento.getOrganizador();
		this.latitude = evento.getLatitude();
		this.longitude = evento.getLongitude();
		this.publicado = evento.isPublicado(); 
		this.horario = evento.getHorario();
		this.site = evento.getSite();
		this.endereco = evento.getEndereco(); 
		this.linkExterno = evento.getLinkExterno();
	}
}
