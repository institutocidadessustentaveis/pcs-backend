package br.org.cidadessustentaveis.model.sistema;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="visualizacao_cartografica")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VisualizacaoCartografica implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",nullable=false)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="indicador")
	private Indicador indicador;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade")
	private Cidade cidade;
	
	/*@Column(name="qtde_Visualizacao")
	private Long qtdeVisualizacao;	
	
	@Column(name="qtde_Indicador_Exportacao")
	private Long qtdeIndicadorExportacao;*/
	
	@Column(name="data")
	private LocalDateTime data;
	
	@Column(name="estado")
	private String estado;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario")
	private Usuario usuario;
	
	@Column(name="acao")
	private String acao;
}