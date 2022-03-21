package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="permissao")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"perfil"}) 
@EntityListeners(ListenerAuditoria.class)
public class Permissao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "permissao_id_seq")
	@SequenceGenerator(name = "permissao_id_seq", sequenceName = "permissao_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="funcionalidade", nullable=false)
	private Funcionalidade funcionalidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="perfil", nullable=false)
	@JsonBackReference()
	private Perfil perfil;
		
	private Boolean habilitada;
	
	public Permissao(Long id, Funcionalidade funcionalidade,  Boolean habilitada) {
		super();
		this.id = id;
		this.funcionalidade = funcionalidade;
		this.habilitada = habilitada;
	}
	
	public String calculaRegraDePermissaoHabilitada() {
		StringBuilder sb = new StringBuilder();		
		if (habilitada) {
			sb.append("ROLE_");
			sb.append(funcionalidade.getRegra());
		}		
		return sb.toString();
	}	

}
