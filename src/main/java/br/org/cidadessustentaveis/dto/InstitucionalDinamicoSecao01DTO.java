package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoSecao01;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InstitucionalDinamicoSecao01DTO implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Long id;
	private String titulo;
	private String texto;
	private String txtBotao01;
	private String txtBotao01Cor;
	private String linkBotao01;
	private String txtBotao02;
	private String txtBotao02Cor;
	private String linkBotao02;
	private String txtBotao03;
	private String txtBotao03Cor;
	private String linkBotao03;
	private String txtBotao04;
	private String txtBotao04Cor;
	private String linkBotao04;
	private String corFundo;
	private Long indice;
	private Boolean exibir;
	private String tipo;
	private Boolean habilitaRecursoExterno;


	public InstitucionalDinamicoSecao01DTO(InstitucionalDinamicoSecao01 obj) {
		this.id = obj.getId();
		this.titulo = obj.getTitulo();
		this.texto  = obj.getTexto();
		this.txtBotao01  = obj.getTxtBotao01();
		this.txtBotao01Cor = obj.getTxtBotao01Cor();
		this.linkBotao01 = obj.getLinkBotao01();
		this.txtBotao02 = obj.getTxtBotao02();
		this.txtBotao02Cor = obj.getTxtBotao02Cor();
		this.linkBotao02 = obj.getLinkBotao02();
		this.txtBotao03 = obj.getTxtBotao03();
		this.txtBotao03Cor = obj.getTxtBotao03Cor();
		this.linkBotao03 = obj.getLinkBotao03();
		this.txtBotao04 = obj.getTxtBotao04();
		this.txtBotao04Cor = obj.getTxtBotao04Cor();
		this.linkBotao04 = obj.getLinkBotao04();
		this.corFundo = obj.getCorFundo();
		this.indice = obj.getIndice();
		this.exibir = obj.getExibir();
		this.tipo = obj.getTipo();
		this.habilitaRecursoExterno = obj.getHabilitaRecursoExterno();
	}	
	
	public InstitucionalDinamicoSecao01DTO(Long id, Long indice, String primeiroTitulo, Boolean exibir, String tipo) {
		this.id = id;
		this.indice = indice;
		this.titulo = primeiroTitulo;
		this.exibir = exibir;
		this.tipo = tipo;
	}	
	
	public InstitucionalDinamicoSecao01 toEntityInsert() {
		InstitucionalDinamicoSecao01 objToInsert = new InstitucionalDinamicoSecao01();
		objToInsert.setIndice(this.indice);
		objToInsert.setTitulo(this.titulo);
		objToInsert.setTexto(this.texto);
		objToInsert.setTxtBotao01(this.txtBotao01);
		objToInsert.setTxtBotao01Cor(this.txtBotao01Cor);
		objToInsert.setLinkBotao01(this.linkBotao01);
		objToInsert.setTxtBotao02(this.txtBotao02);
		objToInsert.setTxtBotao02Cor(this.txtBotao02Cor);
		objToInsert.setLinkBotao02(this.linkBotao02);
		objToInsert.setTxtBotao03(this.txtBotao03);
		objToInsert.setTxtBotao03Cor(this.txtBotao03Cor);
		objToInsert.setLinkBotao03(this.linkBotao03);
		objToInsert.setTxtBotao04(this.txtBotao04);
		objToInsert.setTxtBotao04Cor(this.txtBotao04Cor);
		objToInsert.setLinkBotao04(this.linkBotao04);
		objToInsert.setCorFundo(this.corFundo);
		objToInsert.setExibir(this.exibir);
		objToInsert.setHabilitaRecursoExterno(this.habilitaRecursoExterno);
		return objToInsert;
	}
	
	public InstitucionalDinamicoSecao01 toEntityUpdate(InstitucionalDinamicoSecao01 objRef) {
		objRef.setIndice(this.indice);
		objRef.setTitulo(this.titulo);
		objRef.setTexto(this.texto);
		objRef.setTxtBotao01(this.txtBotao01);
		objRef.setTxtBotao01Cor(this.txtBotao01Cor);
		objRef.setLinkBotao01(this.linkBotao01);
		objRef.setTxtBotao02(this.txtBotao02);
		objRef.setTxtBotao02Cor(this.txtBotao02Cor);
		objRef.setLinkBotao02(this.linkBotao02);
		objRef.setTxtBotao03(this.txtBotao03);
		objRef.setTxtBotao03Cor(this.txtBotao03Cor);
		objRef.setLinkBotao03(this.linkBotao03);
		objRef.setTxtBotao04(this.txtBotao04);
		objRef.setTxtBotao04Cor(this.txtBotao04Cor);
		objRef.setLinkBotao04(this.linkBotao04);
		objRef.setCorFundo(this.corFundo);
		objRef.setExibir(this.exibir);
		objRef.setHabilitaRecursoExterno(this.habilitaRecursoExterno);
		
		return objRef;
	}
	
    public static InstitucionalDinamicoSecao01DTO create(InstitucionalDinamicoSecao01 institucionalDinamicoSecao01) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(institucionalDinamicoSecao01, InstitucionalDinamicoSecao01DTO.class);
    }

}
