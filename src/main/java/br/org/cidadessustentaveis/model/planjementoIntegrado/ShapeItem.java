package br.org.cidadessustentaveis.model.planjementoIntegrado;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vividsolutions.jts.geom.Geometry;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "shape_itens")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@TypeDef(
	    name = "json",
	    typeClass = JsonBinaryType.class
	)
public class ShapeItem implements Serializable {
	private static final long serialVersionUID = 1453L;
	@Id
	@GeneratedValue(generator = "shape_itens_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "shape_itens_id_seq", sequenceName = "shape_itens_id_seq", allocationSize = 1)	
	@Column(nullable=false)
	private Long id;
	
    @Type(type = "json")
    @Column(name = "atributos", columnDefinition = "json")
	private String atributos;

	@Column(name = "shape",columnDefinition="Geometry")
	private Geometry shape;

	//bi-directional many-to-one association to ShapeFile
	@ManyToOne(fetch=FetchType.EAGER, cascade= {CascadeType.ALL})
	@JoinColumn(name="id_shape_file")
	private ShapeFile shapeFile;

}
