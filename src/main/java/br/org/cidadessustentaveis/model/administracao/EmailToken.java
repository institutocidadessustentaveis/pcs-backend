package br.org.cidadessustentaveis.model.administracao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.enums.FuncionalidadeToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="email_token")
@EntityListeners(ListenerAuditoria.class)
public class EmailToken {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Boolean ativo;
	private String hash;
	
	@Enumerated(EnumType.STRING)
	@Column(name="funcionalidade")
	private FuncionalidadeToken funcionalidadeToken;
	
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="aprovacao_prefeitura", nullable = false)
	private AprovacaoPrefeitura aprovacaoPrefeitura;
	
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
}
