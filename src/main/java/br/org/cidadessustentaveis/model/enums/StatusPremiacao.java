package br.org.cidadessustentaveis.model.enums;

public enum StatusPremiacao {

    INSCRICOES_ABERTAS("Inscrições Abertas"),
	EM_ANDAMENTO("Em Andamento"),
	FINALIZADA("Finalizada"),
	CANCELADA("Cancelada");
	
    private String tipo;

	StatusPremiacao(String tipo) {
    	this.tipo = tipo;
    }
	
	public String getTipo() {
		return this.tipo;
	}
	
	public static StatusPremiacao fromString(String tipo) {
		for (StatusPremiacao tipoVariavel : StatusPremiacao.values()) {
			if (tipo.equals(tipoVariavel.getTipo()))
				return tipoVariavel;
		}
		return null;
	}
}
