package br.org.cidadessustentaveis.model.sistema;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="atividade_gestor_municipal")
@Data @NoArgsConstructor
public class AtividadeGestorMunicipal implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "atividade_gestor_municipal_id_seq")
	@SequenceGenerator(name = "atividade_gestor_municipal_id_seq", sequenceName = "atividade_gestor_municipal_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="nomegestormunicipal")
	private String nomeUsuario;
	
	@Column(name="datahora")
	private LocalDateTime dataHora;
	
	@Column(name="acao")
	private String acao;	
	
	@Column(name="cidade")
	private String cidade;
	
	@Column(name="estado")
	private String estado;
	
	@Transient
	private String usuarioLogado;
	
	public AtividadeGestorMunicipal(Long id, String acao) {
		this.id = id;
		this.acao = acao;
	}
	
	public AtividadeGestorMunicipal(String nomeUsuario, LocalDateTime dataHora, String acao, String cidade, String estado, String usuarioLogado) {
		this.nomeUsuario = nomeUsuario;
		this.dataHora = dataHora;
		this.acao = acao;
		this.cidade = cidade;
		this.estado = estado;
		this.usuarioLogado = usuarioLogado;
	}

}
