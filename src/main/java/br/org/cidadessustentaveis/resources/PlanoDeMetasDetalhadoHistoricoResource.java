package br.org.cidadessustentaveis.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.PlanoDeMetaHistoricoAnosDTO;
import br.org.cidadessustentaveis.repository.PlanoDeMetasDetalhadoHistoricoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/planoDeMetasDetalhadoHistorico")
public class PlanoDeMetasDetalhadoHistoricoResource {
	
	@Autowired
	PlanoDeMetasDetalhadoHistoricoService service;
	
	@GetMapping("/buscarHistoricoAnos/{idPlano}/{idIndicador}")
	public ResponseEntity<List<PlanoDeMetaHistoricoAnosDTO>> buscarHistoricoAnos(@PathVariable("idPlano") Long idPlano, @PathVariable("idIndicador") Long idIndicador){
		List<PlanoDeMetaHistoricoAnosDTO> planoDeMetaHistoricoAnos = service.buscarHistoricoAnos(idPlano, idIndicador);
		return ResponseEntity.ok().body(planoDeMetaHistoricoAnos);
	}
}
