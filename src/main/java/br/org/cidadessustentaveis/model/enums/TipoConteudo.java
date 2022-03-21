package br.org.cidadessustentaveis.model.enums;

public enum TipoConteudo {
	BOA_PRATICA("Boa prática");

    private String nome;

    TipoConteudo(String nome) {
       	this.nome = nome;
       }
   	
   	public String getNome() {
   		return this.nome;
   	}

}
