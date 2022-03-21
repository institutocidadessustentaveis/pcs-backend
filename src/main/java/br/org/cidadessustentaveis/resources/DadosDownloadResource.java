package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.C;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.DadosDownloadDTO;
import br.org.cidadessustentaveis.dto.DadosDownloadPaginacaoDTO;
import br.org.cidadessustentaveis.dto.DownloadsExportacoesDTO;
import br.org.cidadessustentaveis.dto.EmpresaDTO;
import br.org.cidadessustentaveis.dto.FiltroGruposAcademicosDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoCardDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoComboDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDetalheDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoPainelDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.ShapeFileDTO;
import br.org.cidadessustentaveis.dto.ShapeFileVisualizarDetalheDTO;
import br.org.cidadessustentaveis.dto.ShapesPaginacaoDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.DadosDownload;
import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import br.org.cidadessustentaveis.services.DadosDownloadService;
import br.org.cidadessustentaveis.services.GrupoAcademicoService;
import br.org.cidadessustentaveis.services.HistoricoRelatorioGeradoService;
import br.org.cidadessustentaveis.services.HistoricoSessaoUsuarioService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/dadosDownload")
public class DadosDownloadResource {

	@Autowired(required= true)
	private DadosDownloadService service;
	
	@Autowired
	private HistoricoSessaoUsuarioService relatorioSessaoUsuarioService;
	
	@Autowired
	private HistoricoRelatorioGeradoService serviceHistoricoRelatorio;
	
	
	@PostMapping("/cadastrar")
	public ResponseEntity<DadosDownloadDTO> cadastrar( @RequestBody DadosDownloadDTO dadosDownloadDTO) {
		service.inserir(dadosDownloadDTO);
		return ResponseEntity.ok(dadosDownloadDTO);
	}
	
	@GetMapping("/buscarTodosDadosDownload")
	public ResponseEntity<List<DadosDownloadDTO>> buscarTodosDadosDownload(){
		List<DadosDownloadDTO> dados = service.buscarTodosDadosDownload();
		return ResponseEntity.ok().body(dados);
	}
	
	@GetMapping("/buscarComboBoxAcao")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxArquivo() {
		List<DadosDownloadDTO> listaAcao = service.buscarComboBoxAcao();
		Collection<DadosDownloadDTO> itensCollection = 
				listaAcao.stream()
				.collect(Collectors.toMap(DadosDownloadDTO::getAcao, p -> p, (p, q) -> p)).values();
				List<ItemComboDTO> itensDTO = new ArrayList<ItemComboDTO>();
				itensCollection.forEach(obj -> { 
					ItemComboDTO itemAux = new ItemComboDTO();
					itemAux.setLabel(obj.getAcao());
					itemAux.setId(obj.getId());
					
					if(itemAux.getLabel() != null && !itemAux.getLabel().isEmpty()) {
						itensDTO.add(itemAux);
					}
				});
							
		return ResponseEntity.ok().body(itensDTO);
	}
	
	@GetMapping("/buscarComboBoxPagina")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxPagina() {
		List<DadosDownloadDTO> listaPagina = service.buscarComboBoxPagina();
		Collection<DadosDownloadDTO> itensCollection = 
				listaPagina.stream()
				.collect(Collectors.toMap(DadosDownloadDTO::getPagina, p -> p, (p, q) -> p)).values();
				List<ItemComboDTO> itensDTO = new ArrayList<ItemComboDTO>();
				itensCollection.forEach(obj -> { 
					ItemComboDTO itemAux = new ItemComboDTO();
					itemAux.setLabel(obj.getPagina());
					itemAux.setId(obj.getId());
					
					if(itemAux.getLabel() != null && !itemAux.getLabel().isEmpty()) {
						itensDTO.add(itemAux);
					}
				});
							
		return ResponseEntity.ok().body(itensDTO);
	}
	
	@GetMapping("/buscarComboBoxCidade")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBoxCidade() {
		List<DadosDownloadDTO> listaCidade = service.buscarComboBoxCidade();
		Collection<DadosDownloadDTO> itensCollection = 
				listaCidade.stream()
				.collect(Collectors.toMap(DadosDownloadDTO::getNomeCidade, p -> p, (p, q) -> p)).values();
				List<ItemComboDTO> itensDTO = new ArrayList<ItemComboDTO>();
				itensCollection.forEach(obj -> { 
					ItemComboDTO itemAux = new ItemComboDTO();
					
					itemAux.setLabel(obj.getNomeCidade());
					itemAux.setId(obj.getId());
					
					if(itemAux.getLabel() != null && !itemAux.getLabel().isEmpty()) {
						itensDTO.add(itemAux);
					}

				});
							
		return ResponseEntity.ok().body(itensDTO);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarFiltro")
	public ResponseEntity<List<DadosDownloadDTO>> buscarFiltro(
			@RequestBody DadosDownloadDTO relObj) {

		List<DadosDownloadDTO> listaDados = new ArrayList<DadosDownloadDTO>();

		listaDados = service.buscarFiltro(relObj);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Downloads e Exportações");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaDados);
	}
	
	@GetMapping("/buscarComPaginacao")
	public ResponseEntity<DadosDownloadPaginacaoDTO> buscarComPaginacao(
									 @RequestParam(required = false, defaultValue = "0") Integer page,
									 @RequestParam(required = false, defaultValue = "5") Integer itemsPerPage,
									 @RequestParam(name = "orderBy", defaultValue = "titulo") String orderBy,
									 @RequestParam(name = "direction", defaultValue = "DESC") String direction) throws Exception {
	 

	return ResponseEntity.ok(service.buscarComPaginacao(page, itemsPerPage, orderBy, direction));
	}

}
