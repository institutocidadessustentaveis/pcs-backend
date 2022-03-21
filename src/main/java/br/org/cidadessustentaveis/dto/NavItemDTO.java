package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.NavItem;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NavItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String displayName;
	private Boolean disabled;
	private Boolean restrito;
	private String route;	
	private List<NavItemDTO> children;
	private List<Long> idsPerfil;
	private Boolean naoLogado;
	private String url_name;
	private Boolean itemPrincipal;
	private String tipoItem;

	
	public NavItemDTO(NavItem objRef) {
		this.id = objRef.getId();
		this.displayName = objRef.getDisplayName();
		this.url_name = objRef.getUrl_name();
		this.restrito = objRef.getRestrito();
		this.naoLogado = objRef.getNaoLogado();
		this.tipoItem = objRef.getTipoItem() != null ? objRef.getTipoItem().toString() : null;
		if(objRef.getMenuPagina() != null) {
			this.route = objRef.getMenuPagina().getRota();
		}
		if(objRef.getChildren() != null) {
			this.children = objRef.getChildren().stream().map(obj -> new NavItemDTO(obj)).collect(Collectors.toList());
		}
		if(objRef.getPerfis() != null) {
			this.idsPerfil = objRef.getPerfis().stream().map(Perfil::getId).collect(Collectors.toList());
		}
		
	}
	
}