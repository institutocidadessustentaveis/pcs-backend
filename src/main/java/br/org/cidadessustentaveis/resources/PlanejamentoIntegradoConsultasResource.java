package br.org.cidadessustentaveis.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.FiltroCidadesComBoasPraticas;
import br.org.cidadessustentaveis.dto.FiltroIndicadoresPorMunicipios;
import br.org.cidadessustentaveis.dto.FiltroVariaveisPorMunicipios;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaBoaPratica;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaIndicador;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaVariavel;
import br.org.cidadessustentaveis.services.PlanejamentoIntegradoConsultaService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController	
@RequestMapping("/planejamentoIntegrado")
public class PlanejamentoIntegradoConsultasResource {
	
	@Autowired
	private PlanejamentoIntegradoConsultaService service;
	

	@Secured({"ROLE_CADASTRAR_CONSULTA_PLANEJAMENTO"})
	@PostMapping("/inserirConsultaBoaPratica")	
	public ResponseEntity<Void> inserirConsultaComBoasPraticasFiltradas(
			@RequestBody FiltroCidadesComBoasPraticas filtro){
		service.inserirConsultaBoaPratica(filtro);
		return ResponseEntity.noContent().build();	
	}

	@GetMapping("/buscarConsultasBoaPratica")
	public ResponseEntity<List<FiltroCidadesComBoasPraticas>> buscarConsultasBoaPratica(){
		List<ConsultaBoaPratica> listaConsultaBoaPratica = service.buscarConsultasBoaPratica();
		List<FiltroCidadesComBoasPraticas> listaConsultas = listaConsultaBoaPratica.stream().map(obj -> new FiltroCidadesComBoasPraticas(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaConsultas);	
	}
	
	@Secured({"ROLE_DELETAR_CONSULTA_PLANEJAMENTO"})
	@DeleteMapping("/excluirConsultaBoaPratica/{id}")
	public ResponseEntity<Void> excluirConsultaBoaPratica(@PathVariable("id") Long id) {
		service.deletarConsultaBoaPratica(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@Secured({"ROLE_CADASTRAR_CONSULTA_PLANEJAMENTO"})
	@PostMapping("/inserirConsultaVariavel")	
	public ResponseEntity<Void> inserirConsultaComVariavel(
			@RequestBody FiltroVariaveisPorMunicipios filtro){
		service.inserirConsultaVariavel(filtro);
		return ResponseEntity.noContent().build();	
	}

	@GetMapping("/buscarConsultasVariavel")
	public ResponseEntity<List<FiltroVariaveisPorMunicipios>> buscarConsultasVariavel(){
		List<ConsultaVariavel> listaConsultaVariavel = service.buscarConsultasVariavel();
		List<FiltroVariaveisPorMunicipios> listaConsultas = listaConsultaVariavel.stream().map(obj -> new FiltroVariaveisPorMunicipios(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaConsultas);	
	}
	
	@Secured({"ROLE_DELETAR_CONSULTA_PLANEJAMENTO"})
	@DeleteMapping("/excluirConsultaVariavel/{id}")
	public ResponseEntity<Void> excluirConsultaVariavel(@PathVariable("id") Long id) {
		service.deletarConsultaVariavel(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_CADASTRAR_CONSULTA_PLANEJAMENTO"})
	@PostMapping("/inserirConsultaIndicador")	
	public ResponseEntity<Void> inserirConsultaComIndicador(
			@RequestBody FiltroIndicadoresPorMunicipios filtro){
		service.inserirConsultaIndicador(filtro);
		return ResponseEntity.noContent().build();	
	}

	@GetMapping("/buscarConsultasIndicador")
	public ResponseEntity<List<FiltroIndicadoresPorMunicipios>> buscarConsultasIndicador(){
		List<ConsultaIndicador> listaConsultaIndicador = service.buscarConsultasIndicador();
		List<FiltroIndicadoresPorMunicipios> listaConsultas = listaConsultaIndicador.stream().map(obj -> new FiltroIndicadoresPorMunicipios(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaConsultas);	
	}
	
	@Secured({"ROLE_DELETAR_CONSULTA_PLANEJAMENTO"})
	@DeleteMapping("/excluirConsultaIndicador/{id}")
	public ResponseEntity<Void> excluirConsultaIndicador(@PathVariable("id") Long id) {
		service.deletarConsultaIndicador(id);
		return ResponseEntity.noContent().build();
	}
	
}
