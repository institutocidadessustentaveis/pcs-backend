package br.org.cidadessustentaveis.model.indicadores;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="variavel_opcoes")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class VariaveisOpcoes implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "variavel_opcoes_id_seq")
	@SequenceGenerator(name = "variavel_opcoes_id_seq", sequenceName = "variavel_opcoes_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	private String descricao;
	
	private Double valor;
	
	private String tipo;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_variavel_resposta")
	@JsonBackReference(value="variavelResposta-variaveisOpcoes")
	private VariavelResposta variavelResposta;

	
}
