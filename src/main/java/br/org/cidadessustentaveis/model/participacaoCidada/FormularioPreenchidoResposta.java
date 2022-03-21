package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormularioPreenchidoResposta implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "formulario_preenchido")
	private FormularioPreenchido formularioPreenchido; 
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pergunta")
	private Pergunta pergunta;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resposta")
	private Resposta resposta;
	private String outro;
	private Boolean simNao;
	private String textoLivre;
	
	
}
