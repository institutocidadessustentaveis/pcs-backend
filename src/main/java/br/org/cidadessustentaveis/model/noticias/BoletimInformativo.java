package br.org.cidadessustentaveis.model.noticias;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="boletim_informativo") 
@Getter @Setter  @AllArgsConstructor @NoArgsConstructor @Builder
@EntityListeners(ListenerAuditoria.class)
public class BoletimInformativo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime data_hora_enviado;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="usuario", nullable=false)
	@JsonBackReference()
	private Usuario usuario;
	

	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "boletim_informacao_livre", 
        joinColumns = { @JoinColumn(name = "id_boletim") }, 
        inverseJoinColumns = { @JoinColumn(name = "id_informacao_livre") } )
	private List<InformacaoLivre> informacaoLivre;
	
	@OneToOne(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "boletim_informativo_template_01", 
        joinColumns = { @JoinColumn(name = "id_boletim") }, 
        inverseJoinColumns = { @JoinColumn(name = "id_template_01") } )
	private BoletimTemplate01 boletimTemplate01;
}
