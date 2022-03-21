package br.org.cidadessustentaveis.model.sistema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="relatorio_interacao_ferramentas") 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RelatorioInteracaoFerramentas implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "relatorio_interacao_ferramentas_id_seq")
	@SequenceGenerator(name = "relatorio_interacao_ferramentas_id_seq", sequenceName = "relatorio_interacao_ferramentas_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="usuario")
	private String usuario;
	
	@Column(name="data_hora")
	private LocalDateTime dataHora;
	
	@Column(name="ferramenta")
	private String ferramenta;
	
	@Column(name="tipo_interacao")
	private String tipoInteracao;
	
}
