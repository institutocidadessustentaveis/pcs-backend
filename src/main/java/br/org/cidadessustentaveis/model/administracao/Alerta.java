package br.org.cidadessustentaveis.model.administracao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class Alerta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String mensagem;

	private String link;

	@Enumerated(EnumType.STRING)
	@Column(name="tipo_alerta")
	private TipoAlerta tipoAlerta;

	private LocalDateTime data;
	
	@ManyToOne()
	@JoinColumn(name="cidade")
	private Cidade cidade;

	@ManyToMany(mappedBy="alerta", fetch=FetchType.LAZY)
	private List<AlertaVisualizado> alertasVisualizados;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "alerta_area_interesse",
        joinColumns = { @JoinColumn(name = "alerta") },
        inverseJoinColumns = { @JoinColumn(name = "area_interesse") } )
	private List<AreaInteresse> areasInteresse;	

	public Alerta(String mensagem, TipoAlerta tipo, Cidade cidade) {
		this.mensagem = mensagem;
		this.tipoAlerta = tipo;
		this.data = LocalDateTime.now();
		this.cidade = cidade;
	}
}
