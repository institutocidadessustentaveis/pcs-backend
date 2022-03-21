package br.org.cidadessustentaveis.model.administracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.institucional.Institucional;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="menu_pagina")
@Data @NoArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class MenuPagina implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator = "menu_pagina_id_seq")
	@SequenceGenerator(name = "menu_pagina_id_seq", sequenceName = "menu_pagina_id_seq", allocationSize = 1)
	@Column(nullable = false)
	private Long id;

	private String nome;
	
	private String rota;
	
	private String modulo;
	
	@OneToOne(mappedBy = "menuPagina")
	@JsonBackReference
	private Institucional institucional;

	public MenuPagina(String nome, String rota, String modulo) {
		this.nome = nome;
		this.rota = rota;
		this.modulo = modulo;
	}
	
}