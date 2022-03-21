package br.org.cidadessustentaveis.dto;
import java.io.IOException;
import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.geojson.GeoJsonObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cidadessustentaveis.model.administracao.Shape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ShapeDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message="Campo chave não pode ser nulo")
	private String chave;
	
	@NotNull(message="Campo type não pode ser nulo")
	private String type;
	
	@NotNull(message="Campo nome do shape não pode ser nulo")
	private String name;
	
	private GeoJsonObject coordinates;

	public ShapeDTO(Shape shape) {
		super();
		this.id = shape.getId();
		this.chave = shape.getChave();
		this.type = shape.getType();
		this.name = shape.getName();
		
		try {
			this.coordinates = new ObjectMapper().readValue(shape.getCoordinates(), GeoJsonObject.class);
		} catch (IOException e) {
			
		}
		
	}
	
	
	public Shape toEntityInsert() {
		Shape shape = new Shape();
		shape.setId(this.id);
		shape.setChave(this.chave);
		shape.setType(this.type);
		shape.setName(this.name);
		
		try {
			shape.setCoordinates(new ObjectMapper().writeValueAsString(this.coordinates));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return shape;
	}
	
	
	/*
	public Shape toEntityInsert() {
		return new Shape(null,this.chave, this.type, this.name, this.coordinates);
	}
	
	public Shape toEntityUpdate(Shape objRef) {
		objRef.setChave(chave);
		objRef.setType(type);
		objRef.setName(name);
		objRef.setCoordinates(coordinates);
		return objRef;
	}
	*/
	
}
