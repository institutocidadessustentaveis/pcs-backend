package br.org.cidadessustentaveis.model.sistema;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.Modulo;
import br.org.cidadessustentaveis.model.enums.TipoAcaoAuditoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "historico_operacao")
@Data
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class HistoricoOperacao {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario", nullable=false)
	private Usuario usuario;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_acao")
	private TipoAcaoAuditoria tipoAcao;
	
	@Column(name="data_hora")
	private LocalDateTime data;
	

	@Enumerated(EnumType.STRING)
	@Column(name="modulo")
	private Modulo modulo;

	private String acao;
	
	private String ip;
	
	
}
