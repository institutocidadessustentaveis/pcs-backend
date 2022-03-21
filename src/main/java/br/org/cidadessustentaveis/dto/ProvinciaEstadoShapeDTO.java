package br.org.cidadessustentaveis.dto;

import java.io.IOException;

import org.geojson.GeoJsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cidadessustentaveis.model.administracao.ProvinciaEstadoShape;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProvinciaEstadoShapeDTO {

	private Long id;

	private ProvinciaEstadoBuscaDTO estado;

	private GeoJsonObject geometria;

	public ProvinciaEstadoShapeDTO(ProvinciaEstadoShape shape) {
		this.id = shape.getId();
		this.estado = new ProvinciaEstadoBuscaDTO(shape.getEstado());
		
		try {
			this.geometria = new ObjectMapper().readValue(shape.getGeometry(), GeoJsonObject.class);
		} catch (IOException e) {
			
		}
	}

}
