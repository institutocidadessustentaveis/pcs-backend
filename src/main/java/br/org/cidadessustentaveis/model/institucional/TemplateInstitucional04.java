package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="template_institucional_04")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
@Indexed
public class TemplateInstitucional04 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "template_institucional_04_id_seq")
	@SequenceGenerator(name = "template_institucional_04_id_seq", sequenceName = "template_institucional_04_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@ContainedIn
	@OneToOne(mappedBy = "template04" )
	@JsonBackReference
	private Institucional institucional;
	
	@ElementCollection
	@CollectionTable(name="template04_shapes", joinColumns=@JoinColumn(name="idtemplate04"))
	@Column(name="idshapefile")
	private List<Long> shapesRelacionados;
	
	@Column(name="primeiro_texto")
	private String primeiroTexto;
	
	@Column(name="segundo_texto")
	private String segundoTexto;

}
