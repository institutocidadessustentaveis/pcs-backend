package br.org.cidadessustentaveis.model.sistema;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="historico_conteudos_compartilhados")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RelatorioConteudoCompartilhado implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "historico_conteudos_compartilhados_id_seq")
	@SequenceGenerator(name = "historico_conteudos_compartilhados_id_seq", sequenceName = "historico_relatorio_gerado_id_seq", allocationSize = 1)
	@Column(name="id",nullable=false)
	private Long id;
	
	@Column(name="nome_usuario")
	private String nomeUsuario;
	
	@Column(name="data_hora")
	private LocalDateTime dataHora;
	
	@Column(name="rede_social")
	private String redeSocial;
	
	@Column(name="conteudo_compartilhado")
	private String conteudoCompartilhado;
}
