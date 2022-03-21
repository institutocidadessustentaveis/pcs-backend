package br.org.cidadessustentaveis.resources;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.AlertaDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.AlertaVisualizado;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.services.AlertaService;
import br.org.cidadessustentaveis.services.AlertaVisualizadoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/alerta")
public class AlertaResource {

	@Autowired
	private AlertaService alertaService;
	@Autowired
	private AlertaVisualizadoService alertaVisualizadoService;

	@Secured({ "ROLE_VISUALIZAR_ALERTA" })
	@GetMapping
	public List<AlertaDTO> get(Principal principal){
		List<Alerta> alertas = alertaService.buscarAlertas(principal.getName());
		List<AlertaDTO> lista = new ArrayList<>();
		if(alertas != null && !alertas.isEmpty()) {
			alertas.forEach(a -> lista.add(AlertaDTO.builder()
					.id(a.getId())
					.mensagem(a.getMensagem())
					.link(a.getLink())
					.data(a.getData())
					.tipo(a.getTipoAlerta().getTipo())
					.visualizado(false)
					.build()));
		}
		return lista;
	}
	
	@Secured({ "ROLE_VISUALIZAR_ALERTA" })
	@GetMapping("/visualizados")
	public List<AlertaDTO> getAntigos(Principal principal){
		List<AlertaVisualizado> alertas = alertaVisualizadoService.buscarAlertas(principal.getName());
		List<AlertaDTO> lista = new ArrayList<>();
		if(alertas != null && !alertas.isEmpty()) {
			alertas.forEach(a -> {
				if(a.getAlerta() != null) {
					lista.add(AlertaDTO.builder()
						.id(a.getAlerta().getId())
						.mensagem(a.getAlerta().getMensagem())
						.link(a.getAlerta().getLink())
						.data(a.getAlerta().getData())
						.tipo(a.getAlerta().getTipoAlerta().getTipo())
						.visualizado(true)
						.build());
				}
			});
		}
		return lista;
	}

	@Secured({ "ROLE_VISUALIZAR_ALERTA" })
	@GetMapping(path="/quantidadeNovas")
	public Long contarNovosAlertas(Principal principal){
		try{
			Long alertas = alertaService.contarNovosAlertas(principal.getName());
			return alertas;
		} 
		catch(Exception e){
			System.out.println(e.getMessage());
			return 0l;
		}
	}
	
	@Secured({ "ROLE_VISUALIZAR_ALERTA" })
	@PostMapping(path="/visualizar")
	public ResponseEntity<String> visualizar( @RequestBody AlertaDTO alerta, Principal principal){
		alertaVisualizadoService.salvar(alerta.createEntity(), principal.getName());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path="/inserir")
	public ResponseEntity<String> inserir(@RequestBody AlertaDTO alerta) {
		alertaService.salvar(Alerta.builder()
				.mensagem("Proposta para a Prefeitura")
				.link(alerta.getLink())
				.tipoAlerta(TipoAlerta.PROPOSTA_MUNICIPIO)
				.data(LocalDateTime.now())
				.build());
		return ResponseEntity.ok().build();
	}
}
