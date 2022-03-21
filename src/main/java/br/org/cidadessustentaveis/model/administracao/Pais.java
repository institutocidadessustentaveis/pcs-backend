package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(ListenerAuditoria.class)
@Table(name="pais")
@Data @NoArgsConstructor @Builder
public class Pais implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "pais_id_seq")
	@SequenceGenerator(name = "pais_id_seq", sequenceName = "pais_id_seq", allocationSize = 1)
	@Column(nullable = false)
	private Long id;

	private String nome;

	private String continente;

	private String populacao;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="pais", targetEntity=ProvinciaEstado.class)
	@JsonManagedReference(value="pais-provinciaEstado")
	private List<ProvinciaEstado> estados;

	
	public Pais(Long idPais, String nomePais, String continente, String população, List<ProvinciaEstado> estados) {
		super();
		this.id = idPais;
		this.nome = nomePais;
		this.continente = continente;
		this.populacao = população;
		this.estados = estados;
	}
	
}
