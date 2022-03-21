package br.org.cidadessustentaveis.model.boaspraticas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="fonte_referencia_boa_pratica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FonteReferenciaBoaPratica implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "fonte_referencia_boa_pratica_id_seq")
	@SequenceGenerator(name = "fonte_referencia_boa_pratica_id_seq", sequenceName = "fonte_referencia_boa_pratica_id_seq", allocationSize = 1)
	@Column(name="id", nullable = false)
	private Long id;

	@Column(name = "nome_arquivo")
	private String nomeArquivo;
	
	@Column(name = "extensao")
	private String extensao;
	
	@Column(name = "conteudo")
	private String conteudo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="boa_pratica")
	@JsonBackReference
	private BoaPratica boaPratica;
}
