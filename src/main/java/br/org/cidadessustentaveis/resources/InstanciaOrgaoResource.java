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
import br.org.cidadessustentaveis.model.administracao.InstanciaOrgao;
import br.org.cidadessustentaveis.services.InstanciaOrgaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/instanciaorgao")
public class InstanciaOrgaoResource {

	@Autowired
	private InstanciaOrgaoService service;
	
	@GetMapping("/carregacomboboxinstanciaorgao")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxInstanciaOrgao() {
		List<InstanciaOrgao> instanciaOrgao = service.buscarComboBoxInstanciaOrgao();
		List<ItemComboDTO> instanciaOrgaoDto = instanciaOrgao.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(instanciaOrgaoDto);
	}

}
