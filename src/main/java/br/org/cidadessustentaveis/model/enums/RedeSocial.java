package br.org.cidadessustentaveis.model.enums;

public enum RedeSocial {
	FACEBOOK("FACEBOOK"),
	TWITTER("TWITTER"),
	LINKEDIN("LINKEDIN"),
	INSTAGRAM("INSTAGRAM"),
	YOUTUBE("YOUTUBE"),
	GITHUB("GITHUB");

    private String nome;

    RedeSocial(String nome) {
       	this.nome = nome;
       }
   	
   	public String getNome() {
   		return this.nome;
   	}

}
