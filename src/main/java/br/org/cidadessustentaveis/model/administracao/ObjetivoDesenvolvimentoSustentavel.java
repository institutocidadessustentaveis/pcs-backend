package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "objetivo_desenvolvimento_sustentavel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
public class ObjetivoDesenvolvimentoSustentavel implements Serializable {

	private static final long serialVersionUID = -7886897685458474468L;

	@Id
	@GeneratedValue(generator = "objetivo_desenvolvimento_sustentavel_id_seq")
	@SequenceGenerator(name = "objetivo_desenvolvimento_sustentavel_id_seq", sequenceName = "objetivo_desenvolvimento_sustentavel_id_seq", allocationSize = 1)
	@Column(name="id", nullable=false)
	private Long id;
	
	private Integer numero;
	private String titulo;
	private String subtitulo;
	private String descricao;
	
	private String icone;
	
	@Column(name="icone_reduzido")
	private String iconeReduzido;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy="ods", targetEntity=MetaObjetivoDesenvolvimentoSustentavel.class)
	@JsonManagedReference
	Set<MetaObjetivoDesenvolvimentoSustentavel> metas;

}
