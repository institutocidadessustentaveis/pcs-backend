package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configuracao_comentario")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
@Builder
public class ConfiguracaoComentario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tamanho_comentario")
	private Long tamanhoComentario;

}
