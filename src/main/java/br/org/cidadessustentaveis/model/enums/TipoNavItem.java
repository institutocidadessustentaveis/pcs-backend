package br.org.cidadessustentaveis.model.enums;

public enum TipoNavItem {
	ITEM("ITEM"),
	SUBITEM("SUBITEM"),
	ELEMENTO_SUBITEM("ELEMENTO_SUBITEM");
	
	private String tipoItem;
	
	TipoNavItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}
	
	public String getTipoItem() {
		return this.tipoItem;
	}
	
	public static TipoNavItem fromString(String tipoItem) {
		for (TipoNavItem tipoNavItem : TipoNavItem.values()) {
			if (tipoItem.equals(tipoNavItem.getTipoItem()))
				return tipoNavItem;
		}
		return null;
	}
}
