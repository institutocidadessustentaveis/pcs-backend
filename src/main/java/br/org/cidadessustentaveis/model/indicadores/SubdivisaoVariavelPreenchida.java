package br.org.cidadessustentaveis.model.indicadores;

import java.util.Date;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subdivisao_variavel_preenchida")
@Data @NoArgsConstructor @AllArgsConstructor 
@EntityListeners(ListenerAuditoria.class)
public class SubdivisaoVariavelPreenchida {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(targetEntity = Variavel.class)
	@JoinColumn(name = "id_variavel", referencedColumnName = "id")
	private Variavel variavel;
	
	@Column(name = "ano")
	private Short ano;
	
	@Column(name = "valor")
	private Double valor;
	
	@Column(name = "sim_nao")
	private Boolean respostaSimples;
	
	@OneToOne(targetEntity = VariaveisOpcoes.class)
	@JoinColumn(name = "id_opcao", referencedColumnName = "id")
	private VariaveisOpcoes opcao;
	
	@Column(name = "observacao")
	private String observacao;
	
	@Column(name = "data_preenchimento")
	private Date dataPreenchimento;
	
	@Column(name = "data_avaliacao")
	private Date dataAvaliacao;

	@Column(name = "status")
	private String status;

	@Column(name = "valor_texto")
	private String valorTexto;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_prefeitura", nullable = false)
	@JsonBackReference
	private Prefeitura prefeitura;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="fonte", nullable = false)
	@JsonBackReference
	private InstituicaoFonte instituicaoFonte;
	

	@Column(name = "fonte_migracao")
	private String fonteMigracao;
		
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "subdivisao_opcao_variavel_preenchida", 
        joinColumns = { @JoinColumn(name = "subdivisao_variavel_preenchida") }, 
        inverseJoinColumns = { @JoinColumn(name = "variavel_opcao") } )
	private List<VariaveisOpcoes> opcoes;

	@ManyToMany(mappedBy="variaveisPreenchidas", fetch= FetchType.LAZY)
	private List<IndicadorPreenchido> indicadoresPreenchidos;

	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="subdivisao", referencedColumnName = "id")
	private SubdivisaoCidade subdivisao;
	
  
}
