package br.org.cidadessustentaveis.model.administracao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "formulario_atendimento")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FormularioAtendimento {
	
	@Id
	@GeneratedValue(generator = "formulario_atendimento_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "formulario_atendimento_id_seq", sequenceName = "formulario_atendimento_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name = "nome_contato")
    private String nomeContato;
	
	@Column(name = "email_contato")
    private String emailContato;

	@Column(name = "tel_contato")
    private String telContato;
	
	@Column(name = "solicitacao")
    private String solicitacao;
	
	@Column(name = "data_hora")
    private LocalDateTime dataHora;
	
	@Column(name = "respondido")
    private Boolean respondido;
}
