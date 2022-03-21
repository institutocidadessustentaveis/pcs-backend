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
@Table(name="HistoricoRelatorioGerado")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HistoricoRelatorioGerado implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "historico_relatorio_gerado_id_seq")
	@SequenceGenerator(name = "historico_relatorio_gerado_id_seq", sequenceName = "historico_relatorio_gerado_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="nomeusuario")
	private String nomeUsuario;
	
	@Column(name="datahora")
	private LocalDateTime dataHora;
	
	@Column(name="nomerelatorio")
	private String nomeRelatorio;	
	
	@Transient
	private String usuarioLogado;
}
