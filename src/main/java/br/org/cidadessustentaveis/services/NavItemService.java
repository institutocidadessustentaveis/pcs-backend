package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.MenuPaginaDTO;
import br.org.cidadessustentaveis.dto.NavItemCombosDTO;
import br.org.cidadessustentaveis.dto.NavItemDTO;
import br.org.cidadessustentaveis.dto.NavItemDetalheDTO;
import br.org.cidadessustentaveis.model.administracao.MenuPagina;
import br.org.cidadessustentaveis.model.administracao.NavItem;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoNavItem;
import br.org.cidadessustentaveis.repository.MenuPaginaRepository;
import br.org.cidadessustentaveis.repository.NavItemRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class NavItemService {


	@Autowired
	private NavItemRepository navRepository;

	@Autowired
	private MenuPaginaRepository menuPaginaRepository;

	@Autowired
	private PerfilService perfilService;

	@Autowired
	private UsuarioService usuarioService;


	public List<NavItemDTO> buscar(){

		List<NavItem> listaNavItem = navRepository.buscarTodosOrdemPosicaoAsc();

		List<NavItemDTO> listaNavItemDTO = new ArrayList<>();
		for(NavItem navItem : listaNavItem) {
			NavItemDTO navItemDTO = new NavItemDTO(navItem);
			navItemDTO.setItemPrincipal(true);
			listaNavItemDTO.add(navItemDTO);
		}

		List<Long> listaIdsPerfilUsuario = this.getIdsPerfil();

		this.verificaPerfil(listaNavItemDTO,listaIdsPerfilUsuario);

		return listaNavItemDTO;
	}

	public NavItem buscarPorId(Long id) {
		Optional<NavItem> obj = navRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Item não encontrado!"));
	}
	
	public MenuPagina buscarPorIdMenuPagina(Long id) {
		Optional<MenuPagina> obj = menuPaginaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Menu Página não encontrado!"));
	}

	private void verificaPerfil(List<NavItemDTO> listNavItemDTO, List<Long> listaIdsPerfilUsuario) {
		for (NavItemDTO navItemDTO : listNavItemDTO) {
			
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			if(!"anonymousUser".equals(user) && (navItemDTO.getNaoLogado() != null && navItemDTO.getNaoLogado())) {
				navItemDTO.setDisabled(true);
			}else if (navItemDTO.getIdsPerfil() != null && !navItemDTO.getIdsPerfil().isEmpty()) {
				Boolean aux = navItemDTO.getIdsPerfil().stream().anyMatch(o -> listaIdsPerfilUsuario.contains(o));
				navItemDTO.setDisabled(!aux);
			}else {
				navItemDTO.setDisabled(false);
			}

			if(navItemDTO.getChildren() != null && !navItemDTO.getChildren().isEmpty()) {
				verificaPerfil(navItemDTO.getChildren(),listaIdsPerfilUsuario);
			}

		}
	}

	private List<Long> getIdsPerfil() {
		List<Long> idsPerfil = new ArrayList<Long>();
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!"anonymousUser".equals(user)) {
			Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
			if(usuario != null && usuario.getCredencial() != null) {
				List<Perfil> listaPerfil = usuario.getCredencial().getListaPerfil();
				if(listaPerfil != null) {
					idsPerfil = listaPerfil.stream().map(Perfil::getId).collect(Collectors.toList());
				}
			}
		}
		return idsPerfil;
	}


	private List<ItemComboDTO> getItemComboDTOPerfil() {
		List<ItemComboDTO> perfis = new ArrayList<>();

		List<Perfil> listaPerfil = perfilService.buscar();

		if(listaPerfil != null) {
			for(Perfil perfil : listaPerfil) {
				perfis.add(new ItemComboDTO(perfil.getId(), perfil.getNome()));
			}
		}
		return perfis;
	}


	public NavItemCombosDTO buscarCombos(){

		List<MenuPagina> listaMenuPagina = menuPaginaRepository.buscarTodos();

		List<ItemComboDTO> perfis = getItemComboDTOPerfil();
		List<String> modulos = new ArrayList<>();
		List<MenuPaginaDTO> paginas = new ArrayList<>();


		for(MenuPagina menuPagina : listaMenuPagina) {
			paginas.add(new MenuPaginaDTO(menuPagina.getId(), menuPagina.getNome(), menuPagina.getModulo()));
			modulos.add(menuPagina.getModulo());
		}

		List<String> distinctModulos = modulos.stream().distinct().collect(Collectors.toList());

		NavItemCombosDTO navItemCombosDTO = new NavItemCombosDTO();
		navItemCombosDTO.setModulos(distinctModulos);
		navItemCombosDTO.setPerfis(perfis);
		navItemCombosDTO.setPaginas(paginas);


		return navItemCombosDTO;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Set<Object> seen = ConcurrentHashMap.newKeySet();

		return t -> seen.add(keyExtractor.apply(t));
	}
	
	public List<ItemComboDTO> buscarTodosParaCombo(){
		
		List<ItemComboDTO> listaNavItem = navRepository.buscarTodosParaCombo();
		
		return listaNavItem;
	}
	
	public NavItem alterar(Long id, NavItemDetalheDTO navItemDetalheDTO) throws IOException {
		NavItem navItemRef = navItemDetalheDTO.toEntityUpdate(buscarPorId(id));
		
		NavItem navItemPaiRef = null;
		if(navItemDetalheDTO.getIdNavItemPai() != null) {
			navItemPaiRef = buscarPorId(navItemDetalheDTO.getIdNavItemPai());
		}
		navItemRef.setNavItemPai(navItemPaiRef);
		
		MenuPagina paginaRef = null;
		if(navItemDetalheDTO.getPagina() != null && navItemDetalheDTO.getPagina().getIdMenuPagina() != null) {
			paginaRef = buscarPorIdMenuPagina(navItemDetalheDTO.getPagina().getIdMenuPagina());
		}
		navItemRef.setMenuPagina(paginaRef);

		/*Recupera referência dos Perfil*/
		List<Perfil> listaPerfilRef = new ArrayList<>();
		for (Long idPerfil : navItemDetalheDTO.getIdsPerfil()) {
			listaPerfilRef.add(perfilService.buscarPorId(idPerfil));
		}
		navItemRef.setPerfis(listaPerfilRef);
		
		navRepository.save(navItemRef);
		
		return navItemRef;
	}
	
	public NavItem inserir(NavItemDetalheDTO navItemDetalheDTO) throws IOException {
		NavItem navItemRef = navItemDetalheDTO.toEntityInsert();
		
		NavItem navItemPaiRef = null;
		if(navItemDetalheDTO.getIdNavItemPai() != null) {
			navItemPaiRef = buscarPorId(navItemDetalheDTO.getIdNavItemPai());
		}
		navItemRef.setNavItemPai(navItemPaiRef);
		
		MenuPagina paginaRef = null;
		if(navItemDetalheDTO.getPagina() != null && navItemDetalheDTO.getPagina().getIdMenuPagina() != null) {
			paginaRef = buscarPorIdMenuPagina(navItemDetalheDTO.getPagina().getIdMenuPagina());
		}
		navItemRef.setMenuPagina(paginaRef);

		/*Recupera referência dos Perfil*/
		List<Perfil> listaPerfilRef = new ArrayList<>();
		if(navItemDetalheDTO.getIdsPerfil() == null) {
			navItemDetalheDTO.setIdsPerfil(new ArrayList<>());
		}
		for (Long idPerfil : navItemDetalheDTO.getIdsPerfil()) {
			listaPerfilRef.add(perfilService.buscarPorId(idPerfil));
		}
		navItemRef.setPerfis(listaPerfilRef);
		
		navRepository.save(navItemRef);
		
		return navItemRef;
	}
	
	public List<Long> buscarNavItemComRelacaoNavPai(Long id){
		return navRepository.buscarNavItemComRelacaoNavPai(id);
	}
	
	@Transactional
	public void deletar(Long id) {
		try {
			List<Long> idsNavsRelacionadas = buscarNavItemComRelacaoNavPai(id);
			
			if (!idsNavsRelacionadas.isEmpty()) {
				navRepository.deleteByIdIn(idsNavsRelacionadas);
			}
			
			navRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Erro ao excluir Menu");
		}
	}
	
	@Transactional
	public void deletarPorIdMenuPagina(Long id) {
		navRepository.deleteByIdMenuPagina(id);
	}
	
	public List<ItemComboDTO> buscarNavItemPorTipoParaCombo(String tipoNavItem) {
		return navRepository.buscarNavItemPorTipoParaCombo(TipoNavItem.fromString(tipoNavItem));
	}

}
