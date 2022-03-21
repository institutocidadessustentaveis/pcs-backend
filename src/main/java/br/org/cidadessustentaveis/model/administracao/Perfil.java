package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.participacaoCidada.DiscussaoPerfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="perfil")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class Perfil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "perfil_id_seq")
	@SequenceGenerator(name = "perfil_id_seq", sequenceName = "perfil_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	private String nome;
	
	@Column(name="gestao_publica")
	private Boolean gestaoPublica;
	
	@Column(name="padrao")
	private Boolean padrao;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="perfil", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Permissao> permissoes;
	

}
