package br.org.cidadessustentaveis.model.contribuicoesAcademicas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name="grupo_academico") 

public class GrupoAcademico implements Serializable{

	private static final long serialVersionUID = 3643386406286732005L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="tipo_cadastro")
	private String tipoCadastro;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name="data_cadastro")
	private LocalDate dataCadastro;
	
	@Column(name="continente")
	private String continente;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="pais", nullable = false)
	private Pais pais;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="estado", nullable = false)
	private ProvinciaEstado estado;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;
	
	@Column(name="tipo")
	private String tipo;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "grupo_academico_area_interesse",
        joinColumns = { @JoinColumn(name = "grupo_academico") },
        inverseJoinColumns = { @JoinColumn(name = "area_interesse") } )
	private List<AreaInteresse> areasInteresse;	
	
	@Column(name="nome_grupo")
	private String nomeGrupo;
	
	@Column(name="pagina_online")
	private String paginaOnline;
	
	@Column(name="nome_contato")
	private String nomeContato;
	
	@Column(name="email_contato")
	private String emailContato;
	
	@Column(name="telefone_contato")
	private String telefoneContato;

	@Column(name="email_institucional")
	private String emailInstitucional;
	
	@Column(name="telefone_institucional")
	private String telefoneInstitucional;
	
	
	@Column(name="link_base_dados")
	private String linkBaseDados;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "grupo_academico_eixo",
	        joinColumns = { @JoinColumn(name = "grupo_academico") },
	        inverseJoinColumns = { @JoinColumn(name = "eixo") } )
	private List<Eixo> eixos;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "grupo_academico_ods",
	        joinColumns = { @JoinColumn(name = "grupo_academico") },
	        inverseJoinColumns = { @JoinColumn(name = "ods") } )
	private List<ObjetivoDesenvolvimentoSustentavel> ods;
	
	@Column(name="observacoes")
	private String observacoes;
	
	@Column(name="descricao_instituicao")
	private String descricaoInstituicao;
	
	@Column(name="possui_experiencias")
	private boolean possuiExperiencias;
	
	@Column(name="experiencias_desenvolvidas")
	private String experienciasDesenvolvidas;
	
	@Column(name="logradouro")
	private String logradouro;
	
	@Column(name="numero")
	private String numero;
	
	@Column(name="complemento")
	private String complemento;
	
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="longitude")
	private Double longitude;
	
	@Column(name="quantidade_alunos")
	private Long quantidadeAlunos;
	
	@Column(name="nomeAcademia")
	private String nomeAcademia;
	
	@Column(name="participa_apl")
	private boolean participaApl;
	
	@Column(name="nome_apl")
	private String nomeApl;
	
	@ElementCollection
	@CollectionTable(name="grupo_academico_setores_apl", joinColumns=@JoinColumn(name="grupo_academico"))
	@Column(name="setor_apl")
	private List<String> setoresApl;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="pais_apl", nullable = false)
	private Pais paisApl;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="estado_apl", nullable = false)
	private ProvinciaEstado estadoApl;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "grupo_academico_cidades_apl",
	        joinColumns = { @JoinColumn(name = "grupo_academico") },
	        inverseJoinColumns = { @JoinColumn(name = "cidade") } )
	private List<Cidade> cidadesApl;
	
	@Column(name="descricao_apl")
	private String descricaoApl;
	
	@Column(name="porte_empresa")
	private String porteEmpresa;
	
	@Column(name="setor_economico")
	private String setorEconomico;
	
	@Column(name="quantidade_funcionarios")
	private Long quantidadeFuncionarios;
	
	@Column(name="receita_anual")
	private Long receitaAnual;
	
	@Column(name="associada_ethos")
	private boolean associadaEthos;
	
	@Column(name="atua_projeto_sustentabilidade")
	private boolean atuaProjetoSustentabilidade;
	
	@Column(name="vinculo")
	private String vinculo;
	
	@Column(name="tipo_fundacao")
	private String tipoFundacao;
	
	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(
	        name = "grupo_academico_biblioteca",
	        joinColumns = { @JoinColumn(name = "grupo_academico") },
	        inverseJoinColumns = { @JoinColumn(name = "biblioteca") } )
	private List<Biblioteca> bibliotecas;
	
}
