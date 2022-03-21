package br.org.cidadessustentaveis.model.planjementoIntegrado;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name="tema_geoespacial")
@Data @AllArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class TemaGeoespacial {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tema_geoespacial_area_interesse", joinColumns = @JoinColumn(name = "tema_geoespacial"), inverseJoinColumns = @JoinColumn(name = "area_interesse"))
	private List<AreaInteresse> areasInteresse;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tema_geoespacial_eixo", joinColumns = @JoinColumn(name = "tema_geoespacial"), inverseJoinColumns = @JoinColumn(name = "eixo"))
	private List<Eixo> eixos ;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tema_geoespacial_ods", joinColumns = @JoinColumn(name = "tema_geoespacial"), inverseJoinColumns = @JoinColumn(name = "ods"))
	private List<ObjetivoDesenvolvimentoSustentavel> ods;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tema_geoespacial_meta", joinColumns = @JoinColumn(name = "tema_geoespacial"), inverseJoinColumns = @JoinColumn(name = "meta"))
	private List<MetaObjetivoDesenvolvimentoSustentavel> metas;

	public TemaGeoespacial() {
		areasInteresse = new ArrayList<>();
		eixos = new ArrayList<>();
		ods = new ArrayList<>();
		metas = new ArrayList<>();
		
	}
	
	
	
}
