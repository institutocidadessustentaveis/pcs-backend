package br.org.cidadessustentaveis.model.enums;

public enum TipoAcaoAuditoria {
	LOGIN("Login"),
	LOGOUT("Logout"),
	CRIACAO("Criação"),
	ALTERACAO("Alteração"),
	REMOCAO("Remoção");
    
    private String tipo;

    TipoAcaoAuditoria(String tipo) {
       	this.tipo = tipo;
       }
   	
   	public String getTipo() {
   		return this.tipo;
   	}
   	
   	public static TipoAcaoAuditoria fromString(String tipo) {
   		for (TipoAcaoAuditoria tipoAlerta : TipoAcaoAuditoria.values()) {
   			if (tipo.equals(tipoAlerta.getTipo()))
   				return tipoAlerta;
   		}
   		return null;
   	}
}
