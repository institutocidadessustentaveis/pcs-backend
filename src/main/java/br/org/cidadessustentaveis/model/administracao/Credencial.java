package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name="credencial")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"usuario"})
@EntityListeners(ListenerAuditoria.class)
public class Credencial implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator = "credencial_id_seq")
	@SequenceGenerator(name = "credencial_id_seq", sequenceName = "credencial_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(unique=true) 
	private String login;

	private String senha;
	
	@Column(name="sn_bloqueado")
	@Default
	private Boolean snBloqueado = Boolean.FALSE;
	
	@Column(name="sn_online")
	@Default
	private Boolean snOnline = Boolean.FALSE;
	
	@Column(name="sn_excluido")
	@Default
	private Boolean snExcluido = Boolean.FALSE;

	
	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "credencial_perfil", 
        joinColumns = { @JoinColumn(name = "id_credencial") }, 
        inverseJoinColumns = { @JoinColumn(name = "id_perfil") } )
	private List<Perfil> listaPerfil;	
	
	@OneToOne(mappedBy="credencial")
	@JsonBackReference
	private Usuario usuario;

	public Credencial(Long id, String login, String senha, Boolean snBloqueado, Boolean snOnline, Boolean snExcluido) {
		super();
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.snBloqueado = snBloqueado;
		this.snOnline = snOnline;
		this.snExcluido = snExcluido;
	}

	public Set<String> getFuncionalidades(){
		Set<String> funcionalidades = new HashSet<String>();
		if(this.listaPerfil != null) {
			this.listaPerfil.forEach(perfil -> {
				if(perfil.getPermissoes() != null){
					perfil.getPermissoes().forEach(permissao ->{
						if(permissao.getHabilitada()){
							funcionalidades.add(permissao.getFuncionalidade().getRegra());
						}
					});
				}
			});
		}
		return funcionalidades;
	}

}
