package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.org.cidadessustentaveis.model.institucional.TemplateInstitucional04;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TemplateInstitucional04DTO implements Serializable{

	private static final long serialVersionUID = 1L;
		
	private Long id;
	private List<Long> shapeFiles = new ArrayList<Long>();
	private String primeiroTexto;
	private String segundoTexto;
	
	public TemplateInstitucional04DTO(TemplateInstitucional04 templateInstitucional04Ref) {	
		this.id = templateInstitucional04Ref.getId();
		this.shapeFiles = templateInstitucional04Ref.getShapesRelacionados();
		this.primeiroTexto = templateInstitucional04Ref.getPrimeiroTexto();
		this.segundoTexto = templateInstitucional04Ref.getSegundoTexto();
	}
	
	public TemplateInstitucional04 toEntityInsert() {
		TemplateInstitucional04 objToInsert = new TemplateInstitucional04();
		return objToInsert;
	}


}
