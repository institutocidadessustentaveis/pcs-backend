package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "meta_objetivo_desenvolvimento_sustentavel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
public class MetaObjetivoDesenvolvimentoSustentavel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "meta_objetivo_desenvolvimento_sustentavel_id_seq")
	@SequenceGenerator(name = "meta_objetivo_desenvolvimento_sustentavel_id_seq", sequenceName = "meta_objetivo_desenvolvimento_sustentavel_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;	
	private String numero;
	private String descricao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ods_id", nullable = false)
	@JsonBackReference
	private ObjetivoDesenvolvimentoSustentavel ods;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="metaODS", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Indicador> indicadores;
}
