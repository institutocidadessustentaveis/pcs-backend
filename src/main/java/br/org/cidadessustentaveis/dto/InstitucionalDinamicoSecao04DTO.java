package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao04;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InstitucionalDinamicoSecao04DTO implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long id;
	private String texto;
	private Long indice;
	private Boolean exibir;
	private String tipo;
	private Boolean exibirMapa;
	private List<Long> shapeFiles = new ArrayList<Long>();
	private String titulo;
	private String corFundo;
	private Boolean habilitaRecursoExterno;


	public InstitucionalDinamicoSecao04DTO(InstitucionalDinamicoSecao04 obj) {	
		this.id = obj.getId();
		this.texto  = obj.getTexto();
		this.indice = obj.getIndice();
		this.exibir = obj.getExibir();
		this.tipo = obj.getTipo();
		this.exibirMapa = obj.getExibirMapa();
		this.shapeFiles = obj.getShapesRelacionados();
		this.corFundo = obj.getCorFundo();
        if(obj.getTexto() != null) {
            String titulo = Jsoup.parse(obj.getTexto()).text();
            int max = titulo.length() >= 30?  30 : titulo.length();
            this.setTitulo(titulo.substring(0, max)+ "...");
        }
       this.habilitaRecursoExterno = obj.getHabilitaRecursoExterno();
	}	
	
	
	public InstitucionalDinamicoSecao04 toEntityInsert() {
		InstitucionalDinamicoSecao04 objToInsert = new InstitucionalDinamicoSecao04();
		objToInsert.setTexto(this.texto);
		objToInsert.setIndice(this.indice);
		objToInsert.setExibir(this.exibir);
		objToInsert.setTipo(this.tipo);
		objToInsert.setExibirMapa(this.exibirMapa);
		objToInsert.setShapesRelacionados(this.shapeFiles);
		objToInsert.setCorFundo(this.corFundo);
		objToInsert.setHabilitaRecursoExterno(this.habilitaRecursoExterno);
		return objToInsert;
	}
	
	public InstitucionalDinamicoSecao04 toEntityUpdate(InstitucionalDinamicoSecao04 objRef) {
		objRef.setTexto(this.texto);
		objRef.setIndice(this.indice);
		objRef.setExibir(this.exibir);
		objRef.setTipo(this.tipo);
		objRef.setExibirMapa(this.exibirMapa);
		objRef.setShapesRelacionados(this.shapeFiles);
		objRef.setCorFundo(this.corFundo);
		objRef.setHabilitaRecursoExterno(this.habilitaRecursoExterno);
		return objRef;
	}
	
    public static InstitucionalDinamicoSecao04DTO create(InstitucionalDinamicoSecao04 institucionalDinamicoSecao04) {
    	
        ModelMapper modelMapper = new ModelMapper();
        InstitucionalDinamicoSecao04DTO institucionalDinamicoSecao04DTO = modelMapper.map(institucionalDinamicoSecao04, InstitucionalDinamicoSecao04DTO.class);
        
        institucionalDinamicoSecao04DTO.setShapeFiles(institucionalDinamicoSecao04.getShapesRelacionados());
        
        return institucionalDinamicoSecao04DTO;
    }

}
