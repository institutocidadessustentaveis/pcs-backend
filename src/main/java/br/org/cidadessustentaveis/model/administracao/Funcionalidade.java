package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="funcionalidade")
@Data @NoArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class Funcionalidade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "funcionalidade_id_seq")
	@SequenceGenerator(name = "funcionalidade_id_seq", sequenceName = "funcionalidade_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	private String nome;

	private String regra;
	
	public Funcionalidade(Long id, String nome, String regra) {
		super();
		this.id = id;
		this.nome = nome;
		this.regra = regra;
	}	
}
