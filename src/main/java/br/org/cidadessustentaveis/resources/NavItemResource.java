package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.NavItemCombosDTO;
import br.org.cidadessustentaveis.dto.NavItemDTO;
import br.org.cidadessustentaveis.dto.NavItemDetalheDTO;
import br.org.cidadessustentaveis.model.administracao.NavItem;
import br.org.cidadessustentaveis.services.NavItemService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/menuitem")
public class NavItemResource {
	
	@Autowired
	private NavItemService service;	
			
	@GetMapping("/buscar")	
	public ResponseEntity<List<NavItemDTO>> buscar(){
		List<NavItemDTO> menuExibicaoDTO = service.buscar();
		return ResponseEntity.ok().body(menuExibicaoDTO);	
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<NavItemDetalheDTO> buscarPorId(@PathVariable("id") Long id) {
		NavItem navItem = service.buscarPorId(id);
		return ResponseEntity.ok().body(new NavItemDetalheDTO(navItem));
	}
	
	@GetMapping("/buscar/Combos")
	public ResponseEntity<NavItemCombosDTO> buscarCombos() {
		NavItemCombosDTO navItemCombosDTO = service.buscarCombos();
		return ResponseEntity.ok().body(navItemCombosDTO);
	}
	
	@GetMapping("/buscar/Todos")
	public ResponseEntity<List<ItemComboDTO>> buscarTodosParaCombos() {
		List<ItemComboDTO> navItemCombosDTO = service.buscarTodosParaCombo();
		return ResponseEntity.ok().body(navItemCombosDTO);
	}
	
	@Secured({"ROLE_EDITAR_MENU"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<NavItemDetalheDTO> alterar(final @PathVariable("id") Long id, @RequestBody NavItemDetalheDTO navItemDetalheDTO) throws Exception {
		NavItem navItem = service.alterar(id, navItemDetalheDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/buscar/{id}").buildAndExpand(navItem.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({"ROLE_CADASTRAR_MENU"})
	@PostMapping("/inserir")
	public ResponseEntity<Void> inserir(@RequestBody NavItemDetalheDTO navItemDetalheDTO) throws IOException {
		NavItem navItem = service.inserir(navItemDetalheDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/buscar/{id}").buildAndExpand(navItem.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@Secured({"ROLE_APAGAR_MENU"})
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<Void> excluirBoaPratica(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarNavItemPorTipoParaCombo")
	public ResponseEntity<List<ItemComboDTO>> buscarNavItemPorTipoParaCombo(@RequestParam("tipoItem") String tipoItem) {
		List<ItemComboDTO> navItemCombosDTO = service.buscarNavItemPorTipoParaCombo(tipoItem);
		return ResponseEntity.ok().body(navItemCombosDTO);
	}
}
