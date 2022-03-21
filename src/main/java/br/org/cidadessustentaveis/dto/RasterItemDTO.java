package br.org.cidadessustentaveis.dto;


import br.org.cidadessustentaveis.model.planjementoIntegrado.RasterItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RasterItemDTO {

	private Long id;
	private String workspaceName;
	private String storeName;
	private String coverageName;
	private String policyName;
	private String defaultStyle;
	
	public RasterItemDTO(RasterItem rasterItem) {
		this.id = rasterItem.getId();
		this.workspaceName = rasterItem.getWorkspaceName();
		this.storeName = rasterItem.getStoreName();
		this.coverageName = rasterItem.getCoverageName();
		this.policyName = rasterItem.getCoverageName();
		this.defaultStyle = rasterItem.getDefaultStyle();
	}
	
}