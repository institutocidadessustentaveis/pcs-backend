package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="provincia_estado")
@Data @NoArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class ProvinciaEstado implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "provincia_estado_id_seq")
	@SequenceGenerator(name = "provincia_estado_id_seq", sequenceName = "provincia_estado_id_seq", allocationSize = 1)
	@Column(nullable = false)
	private Long id;

	private String nome;
	
	private String sigla;
	
	private String populacao;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="provinciaEstado", targetEntity=Cidade.class)
	@JsonManagedReference(value="provincias-cidades")
	private List<Cidade> cidades;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="pais", nullable = false)
	@JsonBackReference(value="pais-provinciaEstado")
	private Pais pais;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "estado")
	private ProvinciaEstadoShape shape;

	public ProvinciaEstado(Long idEstadoProvincia, String nomeEstadoProvincia, String sigla, Pais pais, String população, List<Cidade> cidades) {
		super();
		this.id = idEstadoProvincia;
		this.nome = nomeEstadoProvincia;
		this.sigla = sigla;
		this.pais = pais;
		this.populacao = população;
		this.cidades = cidades;
	}
	
	public ProvinciaEstado(Long idEstadoProvincia, String nomeEstadoProvincia, String sigla, String população) {
		this(idEstadoProvincia, nomeEstadoProvincia, sigla, null, população, Collections.emptyList());
	}
	
	public ProvinciaEstado(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

}
