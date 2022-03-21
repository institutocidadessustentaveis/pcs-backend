package br.org.cidadessustentaveis.dto;

import java.io.Serializable;

import br.org.cidadessustentaveis.model.home.HomeBarra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HomeBarraDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long indice;
	
    private String div1Texto;
    private String div1Link;
    private String div1CorFundo;
    private String div1CorTexto;
    
    private String div2Texto;
    private String div2Link;
    private String div2CorFundo;
    private String div2CorTexto;
    
    private String div3Texto;
    private String div3Link;
    private String div3CorFundo;
    private String div3CorTexto;
    
    private String div4Texto;
    private String div4Link;
    private String div4CorFundo;
    private String div4CorTexto;
 
    private String div5Texto;
    private String div5Link;
    private String div5CorFundo;
    private String div5CorTexto;
    
    private Boolean exibir;

    
    public HomeBarraDTO(HomeBarra homeBarraRef) {
    	this.id = homeBarraRef.getId();
    	this.indice = homeBarraRef.getIndice();	
    	this.div1Texto = homeBarraRef.getDiv1Texto();
    	this.div1Link = homeBarraRef.getDiv1Link();
    	this.div1CorFundo = homeBarraRef.getDiv1CorFundo();
    	this.div1CorTexto = homeBarraRef.getDiv1CorTexto();
    	this.div2Texto = homeBarraRef.getDiv2Texto();
    	this.div2Link = homeBarraRef.getDiv2Link();
    	this.div2CorFundo = homeBarraRef.getDiv2CorFundo();
    	this.div2CorTexto = homeBarraRef.getDiv2CorTexto();
    	this.div3Texto = homeBarraRef.getDiv3Texto();
    	this.div3Link = homeBarraRef.getDiv3Link();
    	this.div3CorFundo = homeBarraRef.getDiv3CorFundo();
    	this.div3CorTexto = homeBarraRef.getDiv3CorTexto();
    	this.div4Texto = homeBarraRef.getDiv4Texto();
    	this.div4Link = homeBarraRef.getDiv4Link();
    	this.div4CorFundo = homeBarraRef.getDiv4CorFundo();
    	this.div4CorTexto = homeBarraRef.getDiv4CorTexto();
    	this.div5Texto = homeBarraRef.getDiv5Texto();
    	this.div5Link = homeBarraRef.getDiv5Link();
    	this.div5CorFundo = homeBarraRef.getDiv5CorFundo();
    	this.div5CorTexto = homeBarraRef.getDiv5CorTexto();
    	this.exibir = homeBarraRef.getExibir();
    }
    
	public HomeBarra toEntityInsert() {
		return new HomeBarra(null,this.indice,
				this.div1Texto, this.div1Link, this.div1CorFundo,this.div1CorTexto, 
				this.div2Texto, this.div2Link, this.div2CorFundo,this.div2CorTexto,  
				this.div3Texto, this.div3Link, this.div3CorFundo,this.div3CorTexto, 
				this.div4Texto, this.div4Link, this.div4CorFundo,this.div4CorTexto, 
				this.div5Texto, this.div5Link, this.div5CorFundo,this.div5CorTexto, 
				this.exibir);
	}
	
	public HomeBarra toEntityUpdate(HomeBarra objRef) {
		 objRef.setIndice(this.indice);
		 objRef.setDiv1Texto(this.div1Texto);
		 objRef.setDiv1Link(this.div1Link);
		 objRef.setDiv1CorTexto(this.div1CorTexto);
		 objRef.setDiv1CorFundo(this.div1CorFundo);
		 
		 objRef.setDiv2Texto(this.div2Texto);
		 objRef.setDiv2Link(this.div2Link);
		 objRef.setDiv2CorTexto(this.div2CorTexto);
		 objRef.setDiv2CorFundo(this.div2CorFundo);
		 
		 objRef.setDiv3Texto(this.div3Texto);
		 objRef.setDiv3Link(this.div3Link);
		 objRef.setDiv3CorTexto(this.div3CorTexto);
		 objRef.setDiv3CorFundo(this.div3CorFundo);
		 
		 objRef.setDiv4Texto(this.div4Texto);
		 objRef.setDiv4Link(this.div4Link);
		 objRef.setDiv4CorTexto(this.div4CorTexto);
		 objRef.setDiv4CorFundo(this.div4CorFundo);
		 
		 objRef.setDiv5Texto(this.div5Texto);
		 objRef.setDiv5Link(this.div5Link);
		 objRef.setDiv5CorTexto(this.div5CorTexto);
		 objRef.setDiv5CorFundo(this.div5CorFundo);
		 
		 objRef.setExibir(this.exibir);

		return objRef;
	}

	

	
}