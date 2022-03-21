package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.enums.TipoNavItem;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="nav_item")	
@Data @NoArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class NavItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "nav_item_id_seq")
	@SequenceGenerator(name = "nav_item_id_seq", sequenceName = "nav_item_id_seq", allocationSize = 1)
	@Column(nullable = false)
	private Long id;

	private String displayName;
	
	private Boolean restrito;
	
	@ManyToOne(targetEntity = NavItem.class ,fetch= FetchType.LAZY)	
	@JoinColumn(name = "id_nav_item", referencedColumnName = "id")
	private NavItem navItemPai;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="navItemPai")
	@OrderBy("posicao")
	private List<NavItem> children;
	
	@OneToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name = "id_menu_pagina")
	@JsonManagedReference
	private MenuPagina menuPagina;
	
	private Long posicao;
	
	@Column(name="nao_logado")
	private Boolean naoLogado;
	

	@ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "nav_item_perfil",
        joinColumns = { @JoinColumn(name = "id_nav_item") },
        inverseJoinColumns = { @JoinColumn(name = "id_perfil") } )
	private List<Perfil> perfis;
	
	@Column(name="url_name")
	private String url_name;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_item")
	private TipoNavItem tipoItem;
	
	public NavItem(String displayName, Boolean restrito, Long posicao, Boolean naoLogado) {
		this.displayName = displayName;
		this.restrito = restrito;
		this.posicao = posicao;
		this.naoLogado = naoLogado;
	}
	
	public NavItem(String displayName, Boolean restrito, Long posicao, Boolean naoLogado, String url_name, TipoNavItem tipoItem) {
		this.displayName = displayName;
		this.restrito = restrito;
		this.posicao = posicao;
		this.naoLogado = naoLogado;
		this.url_name = url_name;
		this.tipoItem = tipoItem;
	}

	public NavItem(Long id, String displayName, Boolean restrito, NavItem navItemPai, List<NavItem> children,
			MenuPagina menuPagina, Long posicao, Boolean naoLogado, List<Perfil> perfis) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.restrito = restrito;
		this.navItemPai = navItemPai;
		this.children = children;
		this.menuPagina = menuPagina;
		this.posicao = posicao;
		this.naoLogado = naoLogado;
		this.perfis = perfis;
	}
	
	
	
}