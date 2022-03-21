package br.org.cidadessustentaveis.model.enums;

public enum TipoExportacaoHitoricoExportacaoCatalogoShape {
	PNG("png"),
	GEO_TIFF("GeoTIFF"),
	GEO_JSON("GeoJSON"),
	SHAPE_FILE("Shapefile");
    
    private String tipo;

    TipoExportacaoHitoricoExportacaoCatalogoShape(String tipo) {
       	this.tipo = tipo;
       }
   	
   	public String getTipo() {
   		return this.tipo;
   	}
   	
   	public static TipoExportacaoHitoricoExportacaoCatalogoShape fromString(String tipo) {
   		for (TipoExportacaoHitoricoExportacaoCatalogoShape te : TipoExportacaoHitoricoExportacaoCatalogoShape.values()) {
   			if (tipo.equals(te.getTipo()))
   				return te;
   		}
   		return null;
   	}
}
