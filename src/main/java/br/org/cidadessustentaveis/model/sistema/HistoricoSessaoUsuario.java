package br.org.cidadessustentaveis.model.sistema;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="historico_sessao_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoSessaoUsuario implements Serializable {

	private static final long serialVersionUID = 341837811415356784L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private Long id;
	
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name="data_inicio")
	private LocalDateTime inicioSessao;

	@Column(name="data_fim")
	private LocalDateTime fimSessao;
	
	public HistoricoSessaoUsuario(Long id, LocalDateTime inicioSessao, LocalDateTime fimSessao, Long idUser, String userName) {
		this.setId(id);
		this.setInicioSessao(inicioSessao);
		this.setFimSessao(fimSessao);
		Usuario user = new Usuario();
		user.setId(idUser);
		user.setNome(userName);
		this.setUsuario(user);
	}
}
