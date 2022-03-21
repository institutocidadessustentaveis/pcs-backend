package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoPublicacao;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao02;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao03;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor	
public class InstitucionalDinamicoSecao03DTO implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long id;
	private String titulo;
	private Long indice;
	private Boolean exibir;
	private String tipo;
	private String corFundo;
	private List<InstitucionalDinamicoPublicacaoDTO> publicacoes = new ArrayList<InstitucionalDinamicoPublicacaoDTO>();


	public InstitucionalDinamicoSecao03DTO(InstitucionalDinamicoSecao03 obj) {
		this.id = obj.getId();
		this.titulo = obj.getTitulo();
		this.indice = obj.getIndice();
		this.exibir = obj.getExibir();
		this.tipo = obj.getTipo();
		this.corFundo = obj.getCorFundo();
		
		for(InstitucionalDinamicoPublicacao publicacao : obj.getPublicacoes() ) {
			this.getPublicacoes().add(new InstitucionalDinamicoPublicacaoDTO(publicacao));
		}
	}	
	
	public InstitucionalDinamicoSecao03DTO(Long id, Long indice, String primeiroTitulo, Boolean exibir, String tipo, String corFundo) {
		this.id = id;
		this.indice = indice;
		this.titulo = primeiroTitulo;
		this.exibir = exibir;
		this.tipo = tipo;
		this.corFundo = corFundo;
	}	
	
	public InstitucionalDinamicoSecao03 toEntityInsert() {
		InstitucionalDinamicoSecao03 objToInsert = new InstitucionalDinamicoSecao03();
		objToInsert.setIndice(this.indice);
		objToInsert.setTitulo(this.titulo);
		objToInsert.setExibir(this.exibir);
		objToInsert.setCorFundo(this.corFundo);
		return objToInsert;
	}
	
	public InstitucionalDinamicoSecao03 toEntityUpdate(InstitucionalDinamicoSecao03 objRef) {
		objRef.setIndice(this.indice);
		objRef.setTitulo(this.titulo);
		objRef.setExibir(this.exibir);
		objRef.setCorFundo(this.corFundo);
		return objRef;
	}
	
    public static InstitucionalDinamicoSecao03DTO create(InstitucionalDinamicoSecao03 institucionalDinamicoSecao03) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(institucionalDinamicoSecao03, InstitucionalDinamicoSecao03DTO.class);
    }
    


}
