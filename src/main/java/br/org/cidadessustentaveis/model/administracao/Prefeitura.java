package br.org.cidadessustentaveis.model.administracao;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prefeitura")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class Prefeitura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "prefeitura_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "prefeitura_id_seq", sequenceName = "prefeitura_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String telefone;
	
	private String cargo;

	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;

	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="prefeitura", targetEntity=CartaCompromisso.class)
	@JsonManagedReference
	private List<CartaCompromisso> cartaCompromisso;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="partido_politico", nullable=false)
	private PartidoPolitico partidoPolitico;

	private Boolean signataria;
		
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "prefeitura")
	@JsonManagedReference
	private AprovacaoPrefeitura aprovacaoPrefeitura;

    @Column(name="inicio_mandato")
    private LocalDate inicioMandato;
    
    @Column(name="fim_mandato")
    private LocalDate fimMandato;
    
	@OneToMany(fetch=FetchType.LAZY, mappedBy="prefeitura", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<BoaPratica> boasPraticas;
	
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "prefeitura")
	@JsonManagedReference
	private HistoricoPlanoMetasPrestacaoContas historicoPlanoMetasPrestacaoContas;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="prefeitura")
	@JsonIgnore
    private List<TipoSubdivisao> tiposSubdivisoes;
	
	public Prefeitura(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public Prefeitura(Long id, String nome, String cargo, String telefone, String email,
					  Long idCidade, String cidadeNome, Double latitude, Double longitude, Long codigoIbge,
					  Long idPartido, Long populacao, String nomePartido, String siglaPartido,
					  Long idProvincia, String nomeProvincia, String sigla, boolean signataria, LocalDate inicioMandato, LocalDate fimMandato) {
		this.id = id;
		this.nome = nome;
		this.signataria = signataria;
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;
		this.cargo = cargo;
		this.email = email;
		this.telefone = telefone;
		
		PartidoPolitico partido = new PartidoPolitico();
		partido.setId(idPartido);
		partido.setNome(nomePartido);
		partido.setSiglaPartido(siglaPartido);
		this.setPartidoPolitico(partido);
		
		Cidade cidade = new Cidade();
		cidade.setId(idCidade);
		cidade.setNome(cidadeNome);
		cidade.setLatitude(latitude);
		cidade.setLongitude(longitude);
		cidade.setCodigoIbge(codigoIbge);
		cidade.setPopulacao(populacao);
		this.cidade = cidade;
		
		ProvinciaEstado provincia = new ProvinciaEstado();
		provincia.setId(idProvincia);
		provincia.setNome(nomeProvincia);	
		provincia.setSigla(sigla);
		this.cidade.setProvinciaEstado(provincia);
	}
	
	public Prefeitura(Long id, Long codigoIbge) {
		setId(id);
		Cidade cidade = new Cidade();
		cidade.setCodigoIbge(codigoIbge);
		setCidade(cidade);
	}

	public Prefeitura(Long id, String nome, String email, String telefone, String cargo, Cidade cidade,
			List<CartaCompromisso> cartaCompromisso, PartidoPolitico partidoPolitico, Boolean signataria,
			AprovacaoPrefeitura aprovacaoPrefeitura, LocalDate inicioMandato, LocalDate fimMandato,
			List<BoaPratica> boasPraticas) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.cargo = cargo;
		this.cidade = cidade;
		this.cartaCompromisso = cartaCompromisso;
		this.partidoPolitico = partidoPolitico;
		this.signataria = signataria;
		this.aprovacaoPrefeitura = aprovacaoPrefeitura;
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;
		this.boasPraticas = boasPraticas;
	}
	
	public Prefeitura(Long id, Long codigoIbge, LocalDate mandatoInicio, LocalDate mandatoFim) {
		setId(id);
		setInicioMandato(mandatoInicio);
		setFimMandato(mandatoFim);
		Cidade cidade = new Cidade();
		cidade.setCodigoIbge(codigoIbge);
		setCidade(cidade);
	}
	
	public Prefeitura(Long id, Long idCidade, Long codigoIbge, LocalDate mandatoInicio, LocalDate mandatoFim) {
		setId(id);
		setInicioMandato(mandatoInicio);
		setFimMandato(mandatoFim);
		Cidade cidade = new Cidade();
		cidade.setId(idCidade);
		cidade.setCodigoIbge(codigoIbge);
		setCidade(cidade);
	}
	
	public Prefeitura(Long id, String nome, String cargo, String telefone, String email, Cidade cidade, LocalDate inicioMandato, LocalDate fimMandato, PartidoPolitico partido) {
		this.id = id;
		this.nome = nome;
		this.cargo = cargo;
		this.telefone = telefone;
		this.email = email;
		this.cidade = cidade;
		this.inicioMandato = inicioMandato;
		this.fimMandato = fimMandato;
		this.partidoPolitico = partido;
	}
    	
}