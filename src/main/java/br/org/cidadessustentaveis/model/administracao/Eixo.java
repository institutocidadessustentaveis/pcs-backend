package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="eixo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
public class Eixo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "eixo_id_seq")
	@SequenceGenerator(name = "eixo_id_seq", sequenceName = "eixo_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="icone")
	private String icone;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="link")
	private String link;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "eixo_ods",
	joinColumns = @JoinColumn(name = "id_eixo"),
	inverseJoinColumns = @JoinColumn(name = "id_ods"))
	private Set<ObjetivoDesenvolvimentoSustentavel> listaODS = new HashSet<ObjetivoDesenvolvimentoSustentavel>();
}
