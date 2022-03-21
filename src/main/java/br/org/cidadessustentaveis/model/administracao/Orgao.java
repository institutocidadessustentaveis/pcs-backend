package br.org.cidadessustentaveis.model.administracao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="orgao")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class Orgao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String nome;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="orgao", targetEntity=InstituicaoFonte.class)
	@JsonManagedReference(value="orgao-fontes")
	private List<InstituicaoFonte> fontes;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_instancia_do_orgao", nullable = false)
	@JsonBackReference(value="instancia-do-orgao")
	private InstanciaOrgao instanciaOrgao;
	
	public Orgao(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
}
