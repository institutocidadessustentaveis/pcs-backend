package br.org.cidadessustentaveis.model.institucional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "material_institucional")
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialInstitucional implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "material_institucional_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "material_institucional_id_seq", sequenceName = "material_institucional_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;

	@Column(name="titulo")
	private String titulo;
	
	@Column(name="subtitulo")
	private String subtitulo;
	
	@Column(name="autor")
	private String autor;
	
	@Column(name="links_relacionados")
	private String linksRelacionados;
	
	@Column(name="tag_palavraschave")
	private String tagPalavrasChave;
	
	@Column(name="corpo_texto")
	private String corpoTexto;
	
	@Column(name="data_publicacao")
	private Date dtPublicacao;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinTable(
        name = "material_institucional_arquivo",
        joinColumns = { @JoinColumn(name = "id_material_institucional") },
        inverseJoinColumns = { @JoinColumn(name = "id_arquivo") } )
	private List<Arquivo> arquivos;
}