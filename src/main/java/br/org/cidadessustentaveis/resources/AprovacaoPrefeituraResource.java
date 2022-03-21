package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraDTO.JustificativaDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraFiltroDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraPendenteDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraSimplesDTO;
import br.org.cidadessustentaveis.services.AprovacaoPrefeituraService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/aprovacaoPrefeitura")
public class AprovacaoPrefeituraResource {

	@Autowired
	private AprovacaoPrefeituraService service;

	@Secured({ "ROLE_AP" })
	@GetMapping
	public ResponseEntity<List<AprovacaoPrefeituraDTO>> buscarPedidosAprovacaoPrefeitura() {
		List<AprovacaoPrefeituraDTO> pedidos = new ArrayList<>();
		
		service.getAprovacoesPrefeituras().forEach(aprovacao -> pedidos.add(new AprovacaoPrefeituraDTO(aprovacao)));
		
		return ResponseEntity.ok().body(pedidos);
	}
	
	@Secured({ "ROLE_AP" })
	@PostMapping("/filtrarAprovacaoPrefeitura")
	public ResponseEntity<List<AprovacaoPrefeituraDTO>> filtrarAprovacaoPrefeitura(@RequestBody AprovacaoPrefeituraFiltroDTO aprovacaoPrefeituraFiltroDTO) {
		List<AprovacaoPrefeituraDTO> listAprovacaoPrefeituraDTO = service.filtrarAprovacaoPrefeitura(aprovacaoPrefeituraFiltroDTO);
		
		return ResponseEntity.ok().body(listAprovacaoPrefeituraDTO);
	}

	@Secured({ "ROLE_APROVAR_AP" })
	@PostMapping("/aprovar")
	public ResponseEntity<AprovacaoPrefeituraDTO> aprovarPrefeitura(@RequestBody AprovacaoPrefeituraPendenteDTO dto) {		
		AprovacaoPrefeituraDTO aprovacao = new AprovacaoPrefeituraDTO(service.aprovarAlterarDados(dto));
		return ResponseEntity.ok().body(aprovacao);
	}

	@Secured({ "ROLE_REPROVAR_AP" })
	@PostMapping("/reprovar/{id}")
	public ResponseEntity<AprovacaoPrefeituraDTO> reprovarPrefeitura(@PathVariable("id") final Long id, @RequestBody JustificativaDTO justificativa) {		
		AprovacaoPrefeituraDTO aprovacao = new AprovacaoPrefeituraDTO(service.reprovar(id, justificativa.getJustificativa()));
		
		return ResponseEntity.ok().body(aprovacao);
	}
	
	/* @Secured({ "ROLE_AP" }) */
	@PostMapping("/reenviaremail/{id}")
	public boolean reenviarEmailPrefeitura(@PathVariable("id") Long id, @RequestBody String listaEmail) {	
		return service.reenviarEmailPrefeitura(id, listaEmail);
	}
	
}
