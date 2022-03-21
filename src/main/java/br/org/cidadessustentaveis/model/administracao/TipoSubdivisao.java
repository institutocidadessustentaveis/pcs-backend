package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(ListenerAuditoria.class)
@Table(name = "tipo_subdivisao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TipoSubdivisao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private Long nivel;
	
	private Long tipoPai;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="prefeitura", nullable = false)
	@JsonIgnore
	private Prefeitura prefeitura;

}
