package br.org.cidadessustentaveis.model.capacitacao;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="certificado") 
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Certificado implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="texto1")
	private String texto1;
	
	@Column(name="texto2")
	private String texto2;
	
	@Column(name="texto3")
	private String texto3;
	
	@ManyToOne(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name="imagem", nullable = false)
	private Arquivo imagem;

	@Column(name="orientacao_paisagem")
	private Boolean orientacaoPaisagem;
}
