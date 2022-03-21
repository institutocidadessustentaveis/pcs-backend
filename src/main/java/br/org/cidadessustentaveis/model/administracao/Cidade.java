package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.Collections;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Builder
@Table(name = "cidade")
@ToString(exclude = {"prefeitura"})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class Cidade implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "cidade_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "cidade_id_seq", sequenceName = "cidade_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	private String nome;

	@Column(name="codigo_ibge")
	private Long codigoIbge;

	@Column(name="populacao")
	private Long populacao;

	@Column(name="ano_populacao")
	private Long anoDaPopulacao;

	@Column(name="endereco_prefeitura")
	private String enderecoDaPrefeitura;

	@Column(name="eixo_x")
	private String eixoX;
	
	@Column(name="eixo_y")
	private String eixoY;
	
	@Column(name="lat")
	private Double latitude;
	
	@Column(name="long")
	private Double longitude;
	
	@Column(name="signataria")
	private Boolean isSignataria;
	
	@Column(name="site_prefeitura")
	private String sitePrefeitura;
	
	@Column(name="area")
	private String area;
	
	@Column(name="densidade_demografica")
	private String densidadeDemografica;
	
	@Column(name="salario_medio_mensal")
	private String salarioMedioMensal;
	
	@Column(name="populacao_ocupada")
	private String populacaoOcupada;
	
	@Column(name="pib_per_capita")
	private String pibPerCapita;
	
	@Column(name="idh_m")
	private String idhM;
	
	@Column(name="texto_cidade_default")
	private String textoCidadeDefault;
	
	@Column(name="foto_cidade_default")
	private String fotoCidadeDefault;
	
	@Column(name="texto_cidade")
	private String textoCidade;
	
	@Column(name="foto_cidade" )
	private String fotoCidade;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="cidade", targetEntity=SubDivisao.class)
	@JsonManagedReference("cidade-subdivisao")
	private List<SubDivisao> subdivisoes;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="provincia_estado")
	@JsonBackReference(value="provincias-cidades")
	private ProvinciaEstado provinciaEstado;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="cidade")
	@JsonIgnore
	private List<Prefeitura> prefeitura;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "imagem_cidade")
	private Imagem imagemCidade;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_plano_metas")
	private Arquivo planoMetas;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_relatorio_contas")
	private Arquivo relatorioContas;
	
	@Column(name="nome_contato")
	private String nomeContato;
	
	@Column(name="telefone_movel_contato")
	private String telMovelContato;
	
	@Column(name="telefone_fixo_contato")
	private String telFixoContato;
	
	@Column(name="email_contato")
	private String emailContato;
	
	@Column(name="campo_observacao")
	private String campoObservacao;

	public Cidade (final Long id, final String nome, final Long codigoIbge, final Long populacao, 
	    final Long anoDaPopulacao, final String enderecoDaPrefeitura, final boolean isSignataria, 
	    final ProvinciaEstado provinciaEstado, final List<SubDivisao> subdivisoes) {
	  super();
	  this.id = id;
	  this.nome = nome;
	  this.codigoIbge = codigoIbge;
	  this.populacao = populacao;
	  this.anoDaPopulacao = anoDaPopulacao;
	  this.enderecoDaPrefeitura = enderecoDaPrefeitura;
	  this.isSignataria = false;
	  this.provinciaEstado = provinciaEstado;
	  this.subdivisoes = subdivisoes;
	  
	}
	
	public Cidade (final Long id, final String nome, final Long codigoIbge, final Long populacao, 
		    final Long anoDaPopulacao, final String enderecoDaPrefeitura, final boolean isSignataria, 
		    final ProvinciaEstado provinciaEstado, final List<SubDivisao> subdivisoes, final String area,
		    final String densidadeDemografica, final String salarioMedioMensal, final String populacaoOcupada, final String pibPerCapita, final String idhM,
		    final String nomeContato, final String telFixoContato, final String telMovelContato, final String emailContato, final String campoObservacao) {
		  super();
		  this.id = id;
		  this.nome = nome;
		  this.codigoIbge = codigoIbge;
		  this.populacao = populacao;
		  this.anoDaPopulacao = anoDaPopulacao;
		  this.enderecoDaPrefeitura = enderecoDaPrefeitura;
		  this.isSignataria = false;
		  this.provinciaEstado = provinciaEstado;
		  this.subdivisoes = subdivisoes;
		  this.area = area;
		  this.densidadeDemografica = densidadeDemografica;
		  this.salarioMedioMensal = salarioMedioMensal;
		  this.populacaoOcupada = populacaoOcupada;
		  this.pibPerCapita = pibPerCapita;
		  this.idhM = idhM;		
		  this.nomeContato = nomeContato;
		  this.telFixoContato = telFixoContato;
		  this.telMovelContato = telMovelContato;
		  this.emailContato = emailContato;
		  this.campoObservacao = campoObservacao;
		}
	
	public Cidade (final Long id, final String nome, final Long codigoIbge, final Long populacao, 
		    final Long anoDaPopulacao, final String enderecoDaPrefeitura, final boolean isSignataria) {
		  this(id, nome, codigoIbge, populacao, anoDaPopulacao, enderecoDaPrefeitura,
				  isSignataria, null, Collections.emptyList());
		  this.isSignataria = isSignataria;
	}
	
	public Cidade (final Long id, final Long codigoIbge, final String nome) {
		this.id = id;
		this.codigoIbge = codigoIbge;
		this.nome = nome;
	}
	
	public Cidade (final Long id, final String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Cidade(final Long id, final String nome, final Long idEstado,
				  final String nomeEstado, final String siglaEstado) {
		this.id = id;
		this.nome = nome;
		this.provinciaEstado = new ProvinciaEstado();
		this.provinciaEstado.setId(idEstado);
		this.provinciaEstado.setNome(nomeEstado);
		this.provinciaEstado.setSigla(siglaEstado);
	}
	public Cidade(final Long id, final String nome, final Long idEstado,
				  final String nomeEstado, final String siglaEstado,
				  final Double latitude, final Double longitude) {
		this.id = id;
		this.nome = nome;
		this.provinciaEstado = new ProvinciaEstado();
		this.provinciaEstado.setId(idEstado);
		this.provinciaEstado.setNome(nomeEstado);
		this.provinciaEstado.setSigla(siglaEstado);
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	
	public Cidade(final Long id, final String nomeCidade, final Long populacao, final Long anoDaPopulacao, final Long idEstado,
			final String nomeEstado, final String siglaEstado, String nomePais, String nomeContinente) {
		this.id = id;
		this.nome = nomeCidade;
		this.populacao = populacao;
		this.anoDaPopulacao = anoDaPopulacao;
		this.provinciaEstado = new ProvinciaEstado();
		this.provinciaEstado.setId(idEstado);
		this.provinciaEstado.setNome(nomeEstado);
		this.provinciaEstado.setSigla(siglaEstado);
		Pais pais = new Pais();
		pais.setNome(nomePais);
		pais.setContinente(nomeContinente);
		this.provinciaEstado.setPais(pais);
	}

}
