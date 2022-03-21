package br.org.cidadessustentaveis.model.planjementoIntegrado;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.SerializationUtils;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import br.org.cidadessustentaveis.dto.ClasseMapaTematicoDTO;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mapa_tematico")	
@EntityListeners(ListenerAuditoria.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MapaTematico implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "mapa_tematico_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "mapa_tematico_id_seq", sequenceName = "mapa_tematico_id_seq", allocationSize = 1)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="id_shapefile")
	private Long idShapeFile;
	
	@Column(name="layer_name")
	private String layerName;
	
	@Column(name="attribute_name")
	private String attributeName;
	
	@Column(name="type_name")
	private String type;
	
	
	@Column(name="classes")
	private byte[] classes;
	
    @SuppressWarnings("unchecked")
	@Transient
    public List<ClasseMapaTematicoDTO> getClasses() {
        return (List<ClasseMapaTematicoDTO>) SerializationUtils.deserialize(classes);
    }
    
    @Transient
    public void setClasses(List<ClasseMapaTematicoDTO> lista) {
    	classes = SerializationUtils.serialize((Serializable) lista);
    }
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="id_usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name="cor_minina")
	private String corMinima;
	
	@Column(name="cor_maxima")
	private String corMaxima;
	
	@Column(name="numero_classes")
	private Long numeroClasses;
	
	@Column(name="exibir_auto")
	private boolean exibirAuto;
	
	@Column(name="exibir_legenda")
	private boolean exibirLegenda;
	
	

}
