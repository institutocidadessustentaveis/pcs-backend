package br.org.cidadessustentaveis.model.eventos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="alerta_evento") 
@Data @AllArgsConstructor @NoArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class AlertaEvento implements Serializable {

	private static final long serialVersionUID = 7017299381797425739L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
	
	private String descricao;
	
	private Integer qtdDias;
	
	private Boolean apenasPrefeitura;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "alerta_perfil",
				joinColumns = @JoinColumn(name = "alerta", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "perfil", referencedColumnName = "id"))
	private List<Perfil> perfis;

	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "imagem")
	private Imagem imagem;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "evento")
	private Evento evento;
	
	private Boolean enviado;
	
	private LocalDate dataEnviar;
	
	private LocalDate dataEnvio;
}
