package br.org.cidadessustentaveis.model.administracao;

import java.time.LocalDateTime;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Perfil.PerfilBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import scala.Serializable;

@Entity
@Table(name="resposta_atendimento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@EntityListeners(ListenerAuditoria.class)
public class RespostaAtendimento implements Serializable {
	
	private static final long serialVersionUID = -1635819021965912891L;
	
	@Id
	@GeneratedValue(generator = "resposta_atendimento_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "resposta_atendimento_id_seq", sequenceName = "resposta_atendimento_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name = "resposta")
    private String resposta;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="formulario_atendimento")
	private FormularioAtendimento formularioAtendimento;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name = "data_hora")
    private LocalDateTime dataHora;

}
