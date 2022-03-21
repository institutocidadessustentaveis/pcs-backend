package br.org.cidadessustentaveis.model.indicadores;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="variavel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(ListenerAuditoria.class)
public class Variavel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "variavel_id_seq")
	@SequenceGenerator(name = "variavel_id_seq", sequenceName = "variavel_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	private String nome;
	private String descricao;
	private String tipo;
	private String unidade;
	
	@Column(name="variavel_basica")
	private boolean variavelBasica;

	@Column(name="permite_importacao")
	private boolean permiteImportacao;
	

	@Column(name="multipla_selecao")
	private boolean multiplaSelecao;
	
	@OneToOne(fetch=FetchType.LAZY, mappedBy = "variavel", targetEntity = VariavelResposta.class, cascade = CascadeType.ALL)
	@JsonManagedReference(value = "variavel-variavelResposta")
	private VariavelResposta variavelResposta;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "variaveis_valor_referencia",
	           joinColumns = @JoinColumn(name = "variavel_id", referencedColumnName = "id"),
	           inverseJoinColumns = @JoinColumn(name = "valor_id", referencedColumnName = "id"))
	@JsonManagedReference
	private Set<ValorReferencia> variavelReferencia;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_prefeitura", referencedColumnName = "id")
	private Prefeitura prefeitura;
	
	public boolean isNumerico() {
		return (tipo.equals("Numérico decimal") || tipo.equals("Numérico inteiro"));		
	}
}
