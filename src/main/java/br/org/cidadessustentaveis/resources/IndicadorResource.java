package br.org.cidadessustentaveis.resources;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
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

import br.org.cidadessustentaveis.dto.EixoIndicadoresDTO;
import br.org.cidadessustentaveis.dto.FiltroIndicadoresDTO;
import br.org.cidadessustentaveis.dto.IndicadorDTO;
import br.org.cidadessustentaveis.dto.IndicadorListagemDTO;
import br.org.cidadessustentaveis.dto.IndicadorParaComboDTO;
import br.org.cidadessustentaveis.dto.IndicadorSimplesDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.services.IndicadorPreenchidoService;
import br.org.cidadessustentaveis.services.IndicadorService;
import br.org.cidadessustentaveis.services.PrefeituraService;
import br.org.cidadessustentaveis.services.UsuarioService;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/indicador")
public class IndicadorResource {

	@Autowired
	private IndicadorService service;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	
	@Autowired
	private CalculadoraFormulaUtil calculadoraFormulaUtil;

	@GetMapping("/{id}")
	public ResponseEntity<IndicadorDTO> buscar(Principal principal, @PathVariable Long id) {
		Prefeitura prefeitura = getPrefeitura(principal);
		Indicador indicador;
		if (prefeitura != null)
			indicador = service.listarById(prefeitura, id);
		else
			indicador = service.listarById(id);

		return ResponseEntity.ok().body(new IndicadorDTO(indicador));
	}
	
	@GetMapping("/simples/{id}")
	public ResponseEntity<IndicadorSimplesDTO> buscarSimples(Principal principal, @PathVariable Long id) {
		Indicador indicador;		
		indicador = service.listarById(id);
		calculadoraFormulaUtil.formatarFormula(indicador);
		return ResponseEntity.ok().body(new IndicadorSimplesDTO(indicador));
	}

	@GetMapping("/buscarTodos")
	public ResponseEntity<IndicadorListagemDTO> buscarTodos(
									@RequestParam(required = false, defaultValue = "0") Integer page,
									@RequestParam(required = false, defaultValue = "10") Integer itemsPerPage,
									@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
									@RequestParam(name = "direction", defaultValue = "ASC") String direction,
																							Principal principal) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	
		Usuario usuario = usuarioService.buscarPorEmail(username);

		Prefeitura prefeitura = prefeituraService.buscarPrefeituraPorIdUsuario(usuario.getId());
		
		List<Indicador> indicador = new LinkedList<>();

		if(prefeitura != null) {
			indicador = service.listar(prefeitura, page, itemsPerPage, orderBy, direction);
		} else {
			indicador = service.listar(page, itemsPerPage, orderBy, direction);
		}

		List<IndicadorDTO> indicadorDto = indicador.stream()
														.map(obj -> new IndicadorDTO(obj))
													.collect(Collectors.toList());

		IndicadorListagemDTO dto = new IndicadorListagemDTO(indicadorDto, service.count());

		return ResponseEntity.ok().body(dto);
	}
	
	
	@PostMapping("/buscarTodosFiltrados")
	public ResponseEntity<List<IndicadorDTO>> buscarTodosFiltrados(@RequestBody FiltroIndicadoresDTO filtro) {
		
		List<IndicadorPreenchido> indicadoresPreenchidos = indicadorPreenchidoService.filtrarIndicadores(filtro);
		List<IndicadorDTO> indicadorDto = indicadoresPreenchidos.stream()
																	.map(obj -> new IndicadorDTO(obj))
																	.filter(distinctByKey(IndicadorDTO::getId))
																.collect(Collectors.toList());
		return ResponseEntity.ok().body(indicadorDto);
		
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

	@ApiIgnore
	@Secured({ "ROLE_CADASTRAR_INDICADOR" })
	@PostMapping
	public ResponseEntity<IndicadorDTO> inserir(Principal principal, @Valid @RequestBody IndicadorDTO indicador) {
		Prefeitura prefeitura = getPrefeitura(principal);
		Indicador entity;
		if (prefeitura != null)
			entity = service.inserir(prefeitura, indicador);
		else
			entity = service.inserir(indicador);

		if (entity != null) {
			indicador.setId(entity.getId());
			return ResponseEntity.ok(indicador);
		}

		return ResponseEntity.badRequest().build();
	}
	
	@ApiIgnore
	@Secured({ "ROLE_EDITAR_INDICADOR" })
	@PutMapping("/{id}")
	public ResponseEntity<IndicadorDTO> editar(Principal principal, @Valid @RequestBody IndicadorDTO indicador,
			@PathVariable Long id) {
		indicador.setId(id);
		try {
			Prefeitura prefeitura = getPrefeitura(principal);
			Indicador entity;
			if (prefeitura != null)
				entity = service.alterar(prefeitura, id, indicador);
			else
				entity = service.alterar(id, indicador);
			return ResponseEntity.ok().body(new IndicadorDTO(entity));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiIgnore
	@Secured({ "ROLE_EXCLUIR_INDICADOR" })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> apagar(Principal principal, @PathVariable Long id) throws Exception {
		Prefeitura prefeitura = getPrefeitura(principal);
		if (prefeitura != null)
			service.deletar(prefeitura, id);
		else
			service.deletar(id);
		return ResponseEntity.ok().build();
	}

	private Prefeitura getPrefeitura(Principal principal) {
		try {
			Usuario usuarioLogado = usuarioService.buscarPorEmail(principal.getName());
			Long prefeituraId  = prefeituraService.buscarPrefeituraPorIdUsuario(usuarioLogado.getId()).getId();
			Prefeitura prefeitura = prefeituraService.buscarPorId(prefeituraId);
			return prefeitura;
			
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/buscarIndicadoresPcs")
	public ResponseEntity<List<IndicadorDTO>> buscarIndicadoresPcs() {
		List<IndicadorDTO> indicadorDto = service.buscarIndicadoresPcs();
		return ResponseEntity.ok().body(indicadorDto);
	}
	
	@GetMapping("/buscarIndicadoresPcsParaCombo")
	public ResponseEntity<List<IndicadorParaComboDTO>> buscarIndicadoresPcsParaCombo() {
		List<IndicadorParaComboDTO> indicadores = service.buscarIndicadoresPcsParaCombo();
		return ResponseEntity.ok().body(indicadores);
	}
	
	
	@GetMapping("/buscarIndicadoresPcs/{idOds}")
	public ResponseEntity<List<IndicadorDTO>> buscarIndicadoresPcsSemPremissao(@PathVariable Long idOds) {
		List<Indicador> indicador = service.listar(idOds);
		List<IndicadorDTO> indicadorDto = indicador.stream().map(obj -> new IndicadorDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(indicadorDto);
	}
	
	@GetMapping("/buscarItemCombo")
	public List<ItemComboDTO> buscarItemCombo() {
		List<ItemComboDTO> indicadores = service.buscarItemCombo();
		return indicadores;
	}
	
	@GetMapping("/buscarIndicadoresPorIdEixoItemCombo")
	public List<ItemComboDTO> buscarIndicadoresPorIdEixoItemCombo(@RequestParam List<Long> idEixo) {
		List<ItemComboDTO> indicadores = service.buscarIndicadoresPorIdEixoItemCombo(idEixo);
		return indicadores;
	}	
	
	@GetMapping("/filtrarIndicadoresInicial")
	public List<ItemComboDTO> filtrarIndicadoresInicial(@RequestParam Long idEixo, @RequestParam Long idOds){
		return service.filtrarIndicadoresInicial(idEixo, idOds);
	}

	@GetMapping("/buscarIndicadoresPcsParaComboPorVariavel/{id}")
	public ResponseEntity<List<IndicadorParaComboDTO>> buscarIndicadoresPcsParaComboPorVariavel(@PathVariable Long id) {
		List<IndicadorParaComboDTO> indicadores = service.buscarIndicadoresPcsParaComboPorVariavel(id);
		return ResponseEntity.ok().body(indicadores);
	}

	@ApiIgnore
	@GetMapping("/corrigirFormula")
	@Deprecated
	public void corrigirFormula(@RequestParam Long idMinimo){
		service.corrigirFormula(idMinimo);
	}

	@GetMapping("/excluirVariavelIndicadorDuplicada")
	public void excluirVariaveisDuplicadas() {
		service.excluirVariaveisDuplicadas();
	}
	
	@GetMapping("/buscarIndicadoresParaComboPorPreenchidos")
	public ResponseEntity<List<IndicadorParaComboDTO>> buscarIndicadoresParaComboPorPreenchidos() {
		List<IndicadorParaComboDTO> indicadores = service.buscarIndicadoresParaComboPorPreenchidos();
		return ResponseEntity.ok().body(indicadores);
	}

	@GetMapping("/carregarComboAnosPreenchidosPorIndicador")
	public ResponseEntity<List<Short>> carregarComboAnosPreenchidosPorIndicador(@RequestParam ("idIndicador") Long idIndicador){
		return ResponseEntity.ok().body(service.carregarComboAnosPreenchidosPorIdIndicador(idIndicador));
	}
	@GetMapping("/setTipoConteudo")
	public void setaTipoConteudo() throws Exception {
		this.service.setaTipoConteudo();
	}
	
	@GetMapping("/cidade/{idCidade}")
	public ResponseEntity<List<EixoIndicadoresDTO>> buscarIndicadoresPorEixoCidade(@PathVariable Long idCidade) {
		List<EixoIndicadoresDTO> dtos = this.service.buscarComboEixosIndicadores(idCidade);
		return ResponseEntity.ok().body(dtos);
	}

}
