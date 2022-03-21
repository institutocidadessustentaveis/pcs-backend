package br.org.cidadessustentaveis.model.enums;

public enum TipoVariavel {

    SIM_NAO("Tipo sim/não"),
	SIM_NAO_COM_LISTA_OPCOES("Tipo sim/não com lista de opções"),
	LISTA_OPCOES("Tipo lista de opções"),
    INTEIRO("Numérico inteiro"),
    DECIMAL("Numérico decimal"),
    TEXTO_LIVRE("Texto livre");
	
    private String tipo;

	TipoVariavel(String tipo) {
    	this.tipo = tipo;
    }
	
	public String getTipo() {
		return this.tipo;
	}
	
	public static TipoVariavel fromString(String tipo) {
		for (TipoVariavel tipoVariavel : TipoVariavel.values()) {
			if (tipo.equals(tipoVariavel.getTipo()))
				return tipoVariavel;
		}
		return null;
	}
}
