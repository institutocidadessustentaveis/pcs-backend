package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.sistema.AtividadeGestorMunicipal;
import br.org.cidadessustentaveis.services.AtividadeGestorMunicipalService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/atividadeGestorMunicipal")
public class AtividadeGestorMunicipalResource {

	@Autowired
	private AtividadeGestorMunicipalService service;
	
	@GetMapping("/buscarComboBoxAcao")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxAcao() {
		List<AtividadeGestorMunicipal> atividadesGestorMunicipal = service.buscarComboBoxAcao();
		List<ItemComboDTO> atividadeGestorMunicipalDto = atividadesGestorMunicipal.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getAcao()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(atividadeGestorMunicipalDto);
	}

}
