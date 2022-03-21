package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="comentario")
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor @Builder
public class Comentario implements Serializable{

	private static final long serialVersionUID = -7031636804513314296L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name="comentario")
	private String comentario;
	
	@Column(name="titulo")
	private String titulo;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	private Cidade cidade;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="prefeitura", nullable = false)
	private Prefeitura prefeitura;
	
	@Column(name="email")
	private String email;
	
	@Column(name="telefone")
	private String telefone;
	
	@Column(name="nome_usuario")
	private String nomeUsuario;
	
	@Column(name="data_publicacao")
	private LocalDate dataPublicacao;
	
	@Column(name="horario_publicacao")
	private LocalTime horarioPublicacao;
	
}
