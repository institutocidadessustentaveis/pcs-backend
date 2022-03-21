package br.org.cidadessustentaveis.model.administracao;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="fonte_de_dados")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class InstituicaoFonte {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String nome;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_orgao", nullable = false)
	@JsonBackReference(value="orgao-fontes")
	private Orgao orgao;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cidade", nullable = false)
	@JsonBackReference(value="cidade")
	private Cidade cidade;
	
	public InstituicaoFonte(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
}
