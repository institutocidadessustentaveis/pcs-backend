package br.org.cidadessustentaveis.model.indicadores;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="valores_referencia")
@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class ValorReferencia implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "valores_referencia_id_seq")
	@SequenceGenerator(name = "valores_referencia_id_seq", sequenceName = "valores_referencia_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="valorde")
	private Double valorde;
	@Column(name="valorate")
	private Double valorate;
	private String Label;
	@Column(name="fontereferencia")
	private String fonteReferencia;
	private String cor;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinTable(name = "variaveis_valor_referencia",
	           joinColumns = @JoinColumn(name = "valor_id", referencedColumnName = "id"),
	           inverseJoinColumns = @JoinColumn(name = "variavel_id", referencedColumnName = "id"))
	@JsonBackReference
	private Variavel variavel;
	
}
