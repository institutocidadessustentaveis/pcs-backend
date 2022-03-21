package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import javax.persistence.Column;

import org.modelmapper.ModelMapper;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao02;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InstitucionalDinamicoSecao02DTO implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long id;
	private String titulo;
	private String textoPrimeiraColuna;
	private String textoSegundaColuna;
	private Long indice;
	private Boolean exibir;
	private String tipo;
	private String imagem;
	private String autorImagem;
	private Long idImagem;
	private String corFundo;
	private String imagemLink;
	private Boolean habilitaRecursoExternoCol01;
	private Boolean habilitaRecursoExternoCol02;


	public InstitucionalDinamicoSecao02DTO(InstitucionalDinamicoSecao02 obj) {
		this.id = obj.getId();
		this.titulo = obj.getTitulo();
		this.textoPrimeiraColuna = obj.getTextoPrimeiraColuna();
		this.textoSegundaColuna = obj.getTextoSegundaColuna();
		this.indice = obj.getIndice();
		this.exibir = obj.getExibir();
		this.tipo = obj.getTipo();
		this.autorImagem = obj.getAutorImagem();
		this.corFundo = obj.getCorFundo();
		if(obj.getImagemPrincipal() != null) {
			this.idImagem = obj.getImagemPrincipal().getId();
		}
		this.imagemLink = obj.getImagemLink();
		this.habilitaRecursoExternoCol01 = obj.getHabilitaRecursoExternoCol01();
		this.habilitaRecursoExternoCol02 = obj.getHabilitaRecursoExternoCol02();
	}	
	
	public InstitucionalDinamicoSecao02DTO(Long id, Long indice, String primeiroTitulo, Boolean exibir, String tipo, String corFundo) {
		this.id = id;
		this.indice = indice;
		this.titulo = primeiroTitulo;
		this.exibir = exibir;
		this.tipo = tipo;
		this.corFundo = corFundo;
	}	
	
	public InstitucionalDinamicoSecao02 toEntityInsert() {
		InstitucionalDinamicoSecao02 objToInsert = new InstitucionalDinamicoSecao02();
		objToInsert.setIndice(this.indice);
		objToInsert.setTitulo(this.titulo);
		objToInsert.setTextoPrimeiraColuna(this.textoPrimeiraColuna);
		objToInsert.setTextoSegundaColuna(this.textoSegundaColuna);
		objToInsert.setAutorImagem(this.autorImagem);
		objToInsert.setExibir(this.exibir);
		objToInsert.setCorFundo(this.corFundo);
		objToInsert.setImagemLink(this.imagemLink);
		objToInsert.setHabilitaRecursoExternoCol01(this.habilitaRecursoExternoCol01);
		objToInsert.setHabilitaRecursoExternoCol02(this.habilitaRecursoExternoCol02);
		return objToInsert;
	}
	
	public InstitucionalDinamicoSecao02 toEntityUpdate(InstitucionalDinamicoSecao02 objRef) {
		objRef.setIndice(this.indice);
		objRef.setTitulo(this.titulo);
		objRef.setTextoPrimeiraColuna(this.textoPrimeiraColuna);
		objRef.setTextoSegundaColuna(this.textoSegundaColuna);
		objRef.setAutorImagem(this.autorImagem);
		objRef.setExibir(this.exibir);
		objRef.setCorFundo(this.corFundo);
		objRef.setImagemLink(this.imagemLink);
		objRef.setHabilitaRecursoExternoCol01(this.habilitaRecursoExternoCol01);
		objRef.setHabilitaRecursoExternoCol02(this.habilitaRecursoExternoCol02);
		
		return objRef;
	}
	
    public static InstitucionalDinamicoSecao02DTO create(InstitucionalDinamicoSecao02 institucionalDinamicoSecao02) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(institucionalDinamicoSecao02, InstitucionalDinamicoSecao02DTO.class);
    }

}
