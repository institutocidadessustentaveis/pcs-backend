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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variavel_preenchida")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class VariavelPreenchida {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "variavel_preenchida_id_seq")
	@SequenceGenerator(name = "variavel_preenchida_id_seq", sequenceName = "variavel_preenchida_id_seq", allocationSize = 1)
	@Column(nullable=false)
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
        name = "opcao_variavel_preenchida", 
        joinColumns = { @JoinColumn(name = "variavel_preenchida") }, 
        inverseJoinColumns = { @JoinColumn(name = "variavel_opcao") } )
	private List<VariaveisOpcoes> opcoes;

	@ManyToMany(mappedBy="variaveisPreenchidas", fetch= FetchType.LAZY)
	private List<IndicadorPreenchido> indicadoresPreenchidos;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VariavelPreenchida other = (VariavelPreenchida) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	
  
}
