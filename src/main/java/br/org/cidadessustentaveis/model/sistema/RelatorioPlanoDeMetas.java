package br.org.cidadessustentaveis.model.sistema;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="relatorio_plano_de_metas") 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RelatorioPlanoDeMetas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "relatorio_plano_de_metas_id_seq")
	@SequenceGenerator(name = "relatorio_plano_de_metas_id_seq", sequenceName = "relatorio_plano_de_metas_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="data_hora")
	private LocalDateTime dataHora;
	
	@Column(name="cidade")
	private String cidade;
	
	@Column(name="nome_usuario_logado")
	private String nomeUsuario;
	
	@Column(name="id_plano_de_metas")
	private Long idPlanoDeMetas;
	
	@Column(name="estado")
	private String estado;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_cidade")
	private Cidade idCidade;
	
}
