package br.org.cidadessustentaveis.model.indicadores;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="variavel_resposta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class VariavelResposta implements Serializable{

	private static final long serialVersionUID = 1L; 
	
	@Id @GeneratedValue(generator = "variavel_resposta_id_seq")
	@SequenceGenerator(name = "variavel_resposta_id_seq", sequenceName = "variavel_resposta_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="respostasim")
	private Double respostaSim;
	
	@Column(name="respostanao")
	private Double respostaNao;
	
	@Column(name="exibiropcaosim")
	private boolean exibirOpcaoSim;
	
	@Column(name="exibiropcaonao")
	private boolean exibirOpcaoNao;
	
	@Column(name="exibiropcao")
	private boolean exibirOpcao;
	
	@Builder.Default
	@OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy="variavelResposta", targetEntity=VariaveisOpcoes.class)
	@JsonManagedReference(value="variavelResposta-variaveisOpcoes")
	private Set<VariaveisOpcoes> listaOpcoes = new HashSet<VariaveisOpcoes>(); 

	@OneToOne
	@JoinColumn(name="id_variavel", nullable=false)
	@JsonBackReference(value = "variavel-variavelResposta")
	private Variavel variavel;

}
