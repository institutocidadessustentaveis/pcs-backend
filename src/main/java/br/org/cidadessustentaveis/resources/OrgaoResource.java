package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Orgao;
import br.org.cidadessustentaveis.services.OrgaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/orgao")
public class OrgaoResource {

	@Autowired
	private OrgaoService service;
	
	@GetMapping("/carregacomboboxorgao/{id}")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxInstanciaOrgao(@PathVariable Long id) {
		List<Orgao> orgao = service.buscarComboBoxOrgao(id);
		List<ItemComboDTO> orgaoDto = orgao.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(orgaoDto);
	}
	
	@GetMapping("/carregacomboboxorgao")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxInstanciaOrgao() {
		List<Orgao> orgao = service.buscarComboBoxOrgao();
		List<ItemComboDTO> orgaoDto = orgao.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(orgaoDto);
	}


}
