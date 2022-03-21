package br.org.cidadessustentaveis.model.enums;

public enum TipoImagemBoaPratica {

    PRINCIPAL("principal"),
	GALERIA("galeria");
	
    private String tipo;

	TipoImagemBoaPratica(String tipo) {
    	this.tipo = tipo;
    }
	
	public String getTipo() {
		return this.tipo;
	}
	
	public static TipoImagemBoaPratica fromString(String tipo) {
		for (TipoImagemBoaPratica tipoVariavel : TipoImagemBoaPratica.values()) {
			if (tipo.equals(tipoVariavel.getTipo()))
				return tipoVariavel;
		}
		return null;
	}
}
