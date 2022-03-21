package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="solicitacoes_boas_praticas")
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor
public class SolicitacoesBoasPraticas implements Serializable{

	private static final long serialVersionUID = -1601224439419033812L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="solicitacao")
	private String solicitacao;
	
	@Column(name="nome_usuario")
	private String nomeUsuario;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;
	
	@Column(name="data_publicacao")
	private LocalDate dataPublicacao;
	
	@Column(name="horario_publicacao")
	private LocalTime horarioPublicacao;
}
