package br.org.cidadessustentaveis.model.administracao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="instancia_do_orgao")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class InstanciaOrgao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String nome;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="instanciaOrgao", targetEntity=Orgao.class)
	@JsonManagedReference(value="instancia-orgao")
	private List<Orgao> orgaos;
	
	public InstanciaOrgao(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
}
