package br.org.cidadessustentaveis.model.enums;

public enum TipoAlerta {
	PEDIDO_ADESAO_PREFEITURA("Pedido de adesão"), SHAPE_FORA_DA_PREFEITURA("Shape fora da prefeitura"),
	EVENTO("Evento"), PROPOSTA_MUNICIPIO("Proposta para município"), 
	RESPOSTA_COMENTARIO_FORUM("Resposta de Comentário no Forúm"), CADASTRO_INDICADOR("Cadastro de novo indicador"),
	CADASTRO_PLANO_DE_METAS("Cadastro de Plano de Metas");

	private String tipo;

	TipoAlerta(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public static TipoAlerta fromString(String tipo) {
		for (TipoAlerta tipoAlerta : TipoAlerta.values()) {
			if (tipo.equals(tipoAlerta.getTipo()))
				return tipoAlerta;
		}
		return null;
	}
}
