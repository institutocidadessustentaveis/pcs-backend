package br.org.cidadessustentaveis.model.planjementoIntegrado;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import br.org.cidadessustentaveis.config.auditoria.ListenerAuditoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "raster_itens")
@EntityListeners(ListenerAuditoria.class)
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class RasterItem implements Serializable {
	private static final long serialVersionUID = 1453L;
	@Id
	@GeneratedValue(generator = "shape_itens_id_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "shape_itens_id_seq", sequenceName = "shape_itens_id_seq", allocationSize = 1)	
	@Column(nullable=false)
	private Long id;

	@Column(name = "workspace_name")
	private String workspaceName;
	
	@Column(name = "store_name")
	private String storeName;
	
	@Column(name = "coverage_name")
	private String coverageName;
	
	@Column(name = "policy_name")
	private String policyName;
	
	@Column(name = "default_style")
	private String defaultStyle;

	

}
