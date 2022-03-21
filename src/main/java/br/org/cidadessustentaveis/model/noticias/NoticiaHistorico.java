package br.org.cidadessustentaveis.model.noticias;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@Entity
@Table(name="noticia_historico")

@NoArgsConstructor @AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class NoticiaHistorico {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="datahora")
	private LocalDateTime dataHora;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="noticia", nullable=false)
	@JsonBackReference()
	private Noticia noticia;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="usuario", nullable=false)
	@JsonBackReference()
	private Usuario usuario;
	
	

}
