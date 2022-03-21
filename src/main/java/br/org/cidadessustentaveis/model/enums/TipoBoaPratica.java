package br.org.cidadessustentaveis.model.enums;

public enum TipoBoaPratica {

    PCS("PCS"),
	PREFEITURA("Prefeitura");
	
    private String tipo;

	TipoBoaPratica(String tipo) {
    	this.tipo = tipo;
    }
	
	public String getTipo() {
		return this.tipo;
	}
	
	public static TipoBoaPratica fromString(String tipo) {
		for (TipoBoaPratica tipoVariavel : TipoBoaPratica.values()) {
			if (tipo.equals(tipoVariavel.getTipo()))
				return tipoVariavel;
		}
		return null;
	}
}
