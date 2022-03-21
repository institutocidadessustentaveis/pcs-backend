package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.institucional.Institucional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "arquivo_institucional")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class ArquivoInstitucional implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "arquivo_institucional_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "arquivo_institucional_id_seq", sequenceName = "arquivo_institucional_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;

	@Column(name = "nome_arquivo")
	private String nomeArquivo;
	
	private String extensao;
	
	private String arquivo;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="institucional", nullable = false)
	@JsonBackReference
	private Institucional institucional;
  

}
