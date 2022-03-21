package br.org.cidadessustentaveis.model.noticias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@Entity
@Table(name="informacao_livre") 
@Indexed
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(ListenerAuditoria.class)
public class InformacaoLivre implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String tituloNoticia;
	
	private String url;
	

	@ManyToMany(mappedBy = "informacaoLivre")
	List<BoletimInformativo> boletim;
	
}
