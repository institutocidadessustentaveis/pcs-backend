package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.ArrayList;
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

import br.org.cidadessustentaveis.dto.EventoDTO;
import br.org.cidadessustentaveis.dto.EventoDetalheDTO;
import br.org.cidadessustentaveis.dto.EventoMapaDTO;
import br.org.cidadessustentaveis.dto.EventosFiltradosDTO;
import br.org.cidadessustentaveis.model.eventos.Evento;
import br.org.cidadessustentaveis.services.EventoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/evento")
public class EventoResource {
	
	@Autowired(required= true)
	private EventoService service;
	
	@Secured({ "ROLE_CADASTRAR_EVENTO" })
	@PostMapping("/cadastrar")
	public ResponseEntity<EventoDTO> cadastrar( @RequestBody EventoDTO evento) throws Exception {
		Evento eventoCadastrado = service.inserir(evento);
		evento.setId(eventoCadastrado.getId());
		return ResponseEntity.ok(evento);
	}
	
	@Secured({ "ROLE_EXCLUIR_EVENTO" })
	@DeleteMapping("excluir/{id}")
	public ResponseEntity<Void> apagar( @PathVariable Long id) {
			service.deletar(id);
		return ResponseEntity.ok().build();
	}
	
	@Secured({ "ROLE_EDITAR_EVENTO" })
	@PutMapping("/editar")
	public ResponseEntity<EventoDTO> alterar(@RequestBody EventoDTO eventoDTO) throws Exception {
		Evento evento = service.alterar(eventoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(evento.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}

	@GetMapping("/buscarEventoPorId/{id}")
	public ResponseEntity<EventoDTO> buscarEventoPorId(@PathVariable("id") Long id) throws Exception{
		EventoDTO evento = service.buscarEventoPorId(id);
		return ResponseEntity.ok().body(evento);
	}
	
	@GetMapping("/buscarEventoDetalhePorId/{id}")
	public ResponseEntity<EventoDetalheDTO> buscarEventoDetalhePorId(@PathVariable("id") Long id) throws Exception{
		EventoDetalheDTO evento = service.buscarEventoDetalhePorId(id);
		return ResponseEntity.ok().body(evento);
	}

	@GetMapping("/pcs")
	public ResponseEntity<List<EventoMapaDTO>> buscar(){
		List<EventoMapaDTO> dtos = service.buscarProximosEventosOficiais();
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping("/buscarEventosToList")
	public ResponseEntity<List<EventoDTO>> buscarEventosToList() throws Exception{
		List<EventoDTO> eventosDTO = service.buscarEventosToList();
		return ResponseEntity.ok().body(eventosDTO);
	}
	
	@GetMapping("/buscarEventosToListByIdCidade/{id}")
	public ResponseEntity<List<EventoDTO>> buscarEventosToListByIdCidade(@PathVariable("id") Long id) throws Exception{
		List<EventoDTO> eventosDTO = service.buscarEventosToListByIdCidade(id);
		return ResponseEntity.ok().body(eventosDTO);
	}
	
	@GetMapping("/buscarEventosFiltrados")
	public ResponseEntity<List<EventosFiltradosDTO>> buscarEventosFiltrados(@RequestParam String tipo, @RequestParam String dataInicial, @RequestParam String dataFinal,
			@RequestParam String palavraChave) throws Exception{
		List<EventosFiltradosDTO> eventosFiltradosDTO = service.buscarEventosFiltrados(tipo, dataInicial, dataFinal, palavraChave);
		return ResponseEntity.ok().body(eventosFiltradosDTO);
	}
	
	@GetMapping("/buscarEventosParticipacaoCidada")
	public ResponseEntity<List<EventoDTO>>  buscarEventosParticipacaoCidada() throws Exception{
		List<EventoDTO> eventosDTO = service.buscarEventosParticipacaoCidada();
		return ResponseEntity.ok().body(eventosDTO); 
	}
	
	@GetMapping("/buscarEventosParticipacaoCidadaFiltrados")
	public ResponseEntity<List<EventosFiltradosDTO>>  buscarEventosParticipacaoCidadaFiltrados(@RequestParam String dataInicial, @RequestParam String dataFinal) throws Exception{
		List<EventosFiltradosDTO> eventosDTO = service.buscarEventosParticipacaoCidadaFiltrados(dataInicial, dataFinal);
		return ResponseEntity.ok().body(eventosDTO); 
	}
	
	@GetMapping("/buscarEventosNaoPrefeitura")
	public ResponseEntity<List<EventosFiltradosDTO>>  buscarEventosNaoPrefeitura() throws Exception{
		List<EventosFiltradosDTO> eventos = service.buscarEventosNaoPrefeitura();
		return ResponseEntity.ok().body(eventos);
	}	
	
	@GetMapping("/buscarEventosCapacitacao")
	public ResponseEntity<List<EventoDTO>>  buscarEventosCapacitacao() throws Exception{
		List<EventoDTO> eventosDTO = service.buscarEventosCapacitacao();
		return ResponseEntity.ok().body(eventosDTO); 
	}
	
	@GetMapping("/buscarTodosEventosCapacitacao")
	public ResponseEntity<List<EventoDTO>>  buscarTodosEventosCapacitacao() throws Exception{
		List<EventoDTO> eventosDTO = service.buscarTodosEventosCapacitacao();
		return ResponseEntity.ok().body(eventosDTO); 
	}
	
	@GetMapping("/buscarEventosTipoAcademiaCalendario")
	public ResponseEntity<List<EventoDTO>>  buscarEventosTipoAcademiaCalendario() throws Exception{
		List<EventoDTO> eventosDTO = service.buscarEventosTipoAcademiaCalendario();
		return ResponseEntity.ok().body(eventosDTO); 
	}
	
	@GetMapping("/buscarEventosCapacitacaoFiltrados")
	public ResponseEntity<List<EventosFiltradosDTO>> buscarEventosCapacitacaoFiltrados(@RequestParam String palavraChave, @RequestParam String dataInicial, @RequestParam String dataFinal) throws Exception{
		List<EventosFiltradosDTO> eventosDTO = service.buscarEventosCapacitacaoFiltrados(palavraChave, dataInicial, dataFinal);
		return ResponseEntity.ok().body(eventosDTO); 
	}
	
	@PostMapping("/buscarEventosFiltradosPorNomeData")
	public ResponseEntity<List<EventoDTO>> buscarEventosFiltradosPorNomeData(@RequestBody EventoDTO relObj) throws Exception {

		List<EventoDTO> listaEvento = new ArrayList<EventoDTO>();

		listaEvento = service.buscarEventosFiltradosPorNomeData(relObj);

		return ResponseEntity.ok().body(listaEvento);
	}
	
}
