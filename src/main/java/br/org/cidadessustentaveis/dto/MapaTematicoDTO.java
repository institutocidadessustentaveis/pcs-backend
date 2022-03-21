package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;

import br.org.cidadessustentaveis.model.planjementoIntegrado.MapaTematico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MapaTematicoDTO implements Serializable{

private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String layerName;	
	private String attributeName;
	private String type;
	private List<ClasseMapaTematicoDTO> classes;
	private Long idShapeFile;
	private String corMinima;
	private String corMaxima;
	private Long numeroClasses;
	private boolean exibirAuto;
	private boolean exibirLegenda;

	public MapaTematicoDTO(MapaTematico obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.layerName = obj.getLayerName();
		this.attributeName = obj.getAttributeName();
		this.type = obj.getType();
		this.classes = obj.getClasses();
		this.idShapeFile = obj.getIdShapeFile();
		this.corMinima = obj.getCorMinima();
		this.corMaxima = obj.getCorMaxima();
		this.numeroClasses = obj.getNumeroClasses();
		this.exibirAuto = obj.isExibirAuto();
		this.exibirLegenda = obj.isExibirLegenda();
	}	

}
