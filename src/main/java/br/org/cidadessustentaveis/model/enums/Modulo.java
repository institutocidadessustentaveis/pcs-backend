package br.org.cidadessustentaveis.model.enums;

public enum Modulo {

    ADMINISTRACAO("Administração"),
    BOAS_PRATICAS("Boas Práticas"),
    INDICADORES("Indicadores"),
    INSTITUCIONAL("Institucional"),
	NOTICIAS("Notícias"),
	PLANEJAMENTO_INTEGRADO("Planejamento Integrado"),
	EVENTOS("Eventos"),
	PARTICIPACAO_CIDADA("Participação Cidadã");
	
    private String descricao;

	Modulo(String tipo) {
    	this.descricao = tipo;
    }
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static Modulo fromString(String descricao) {
		for (Modulo tipoVariavel : Modulo.values()) {
			if (descricao.equals(tipoVariavel.getDescricao()))
				return tipoVariavel;
		}
		return null;
	}
}
