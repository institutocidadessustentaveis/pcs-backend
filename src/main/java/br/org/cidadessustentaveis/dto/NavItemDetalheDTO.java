package br.org.cidadessustentaveis.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import br.org.cidadessustentaveis.model.administracao.NavItem;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.enums.TipoNavItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NavItemDetalheDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String displayName;
	private Boolean restrito;
	private Long posicao;
	private Boolean naoLogado;
	private String url_name;
	
	private NavItemPaginaDTO pagina;
	private List<Long> idsPerfil;
	private Long idNavItemPai;
	private String tipoItem;

	public NavItemDetalheDTO(NavItem objRef) {
		this.id = objRef.getId();
		this.displayName = objRef.getDisplayName();
		this.restrito = objRef.getRestrito();
		this.posicao = objRef.getPosicao();
		this.naoLogado = objRef.getNaoLogado();
		this.url_name = objRef.getUrl_name();
		this.tipoItem = objRef.getTipoItem().toString();
		if(objRef.getMenuPagina() != null) {
			this.pagina = new NavItemPaginaDTO();
			this.pagina.setIdMenuPagina(objRef.getMenuPagina().getId());
			this.pagina.setModulo(objRef.getMenuPagina().getModulo());
			this.pagina.setNome(objRef.getMenuPagina().getNome());
		}

		if(objRef.getPerfis() != null) {
			this.idsPerfil = objRef.getPerfis().stream().map(Perfil::getId).collect(Collectors.toList());
		}
		
		if(objRef.getNavItemPai() != null) {
			this.idNavItemPai = objRef.getNavItemPai().getId();
		}
		
	}
	
	public NavItem toEntityUpdate(NavItem navItemRef) {
		navItemRef.setDisplayName(this.displayName);
		navItemRef.setRestrito(this.restrito);
		navItemRef.setUrl_name(this.url_name);
		navItemRef.setPosicao(this.posicao);
		navItemRef.setNaoLogado(this.naoLogado);
		navItemRef.setTipoItem(TipoNavItem.fromString(tipoItem));
		return navItemRef;
	}
	
	public NavItem toEntityInsert() {
		return new NavItem(this.displayName,this.restrito, this.posicao, this.naoLogado, this.url_name, TipoNavItem.fromString(tipoItem));
	}
	
}