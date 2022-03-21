package br.org.cidadessustentaveis.model.enums;

public enum SimNao {

    SIM("S"),
    NAO("N");
	
    private String descricao;

	SimNao(String tipo) {
    	this.descricao = tipo;
    }
	
	public String getDescricao() {
		return this.descricao;
	}
}
