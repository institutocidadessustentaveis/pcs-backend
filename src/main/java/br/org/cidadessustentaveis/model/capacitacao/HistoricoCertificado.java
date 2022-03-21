package br.org.cidadessustentaveis.model.capacitacao;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="historico_certificado") 
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class HistoricoCertificado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="nome_usuario")
	private String nomeUsuario;
	
	@Column(name="template")
	private String template;
	
	@Column(name="data")
	private LocalDate data;
}
