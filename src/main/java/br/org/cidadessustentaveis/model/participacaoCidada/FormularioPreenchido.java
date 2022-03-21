package br.org.cidadessustentaveis.model.participacaoCidada;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class FormularioPreenchido implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	private LocalDate dataPreenchimento;
	private LocalTime horario;
	private Boolean estavaLogado;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "formulario")
	private Formulario formulario;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	@OneToMany( mappedBy = "formularioPreenchido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FormularioPreenchidoResposta> respostas = new ArrayList<FormularioPreenchidoResposta>();
	
}
