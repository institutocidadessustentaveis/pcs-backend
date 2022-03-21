package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteSource;

import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CidadeMandatosDTO;
import br.org.cidadessustentaveis.dto.IdPaisIdEstadoIdCidadeDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PrefeituraDTO;
import br.org.cidadessustentaveis.dto.PrefeituraEdicaoDTO;
import br.org.cidadessustentaveis.dto.PrefeituraImportacaoDTO;
import br.org.cidadessustentaveis.dto.PrefeituraListagemDTO;
import br.org.cidadessustentaveis.dto.PrefeituraListagemPaginacaoDTO;
import br.org.cidadessustentaveis.dto.PrefeituraMapaDTO;
import br.org.cidadessustentaveis.dto.PrefeituraPlanoMetasDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.services.CidadeService;
import br.org.cidadessustentaveis.services.DownloadsExportacoesService;
import br.org.cidadessustentaveis.services.PlanoDeMetasService;
import br.org.cidadessustentaveis.services.PrefeituraService;
import br.org.cidadessustentaveis.util.PDFUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/prefeitura")
public class PrefeituraResource {

	@Autowired
	private PrefeituraService prefeituraService;
	@Autowired
	private DownloadsExportacoesService downloadsExportacoesService;
	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private PlanoDeMetasService planoDeMetasService;
	
	@GetMapping()
	public ResponseEntity<List<PrefeituraDTO>> listar() {
		List<Prefeitura> listaCadastrosPrefeituras = prefeituraService.listar();
		List<PrefeituraDTO> listaPrefeiturasDto = listaCadastrosPrefeituras.stream().map(obj -> new PrefeituraDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaPrefeiturasDto);
	}

	@PutMapping
	@Secured({"ROLE_EDITAR_PREFEITURA"})
	public ResponseEntity<Void> editarPrefeitura(@RequestBody @Valid PrefeituraEdicaoDTO dto) {
		prefeituraService.editar(dto);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/listarComPaginacao")
	public ResponseEntity<PrefeituraListagemPaginacaoDTO> listar(
											@RequestParam(required = false) Integer page,
											 @RequestParam(required = false) Integer itemsPerPage,
											 @RequestParam(name = "orderBy", defaultValue = "cidade") String orderBy,
											 @RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		List<Prefeitura> prefeituras = prefeituraService.listar(page, itemsPerPage, orderBy, direction);
		List<PrefeituraListagemDTO> prefeiturasDTOs = prefeituras.stream()
														.map(p -> new PrefeituraListagemDTO(p))
													.collect(Collectors.toList());

		PrefeituraListagemPaginacaoDTO dto = new PrefeituraListagemPaginacaoDTO(prefeiturasDTOs,
																				prefeituraService.count());

		return ResponseEntity.ok(dto);
	}

	@PostMapping()
	public ResponseEntity<PrefeituraDTO> inserir(@Valid @RequestBody PrefeituraDTO prefeituraDTO) {
		Prefeitura prefeitura = prefeituraService.inserir(prefeituraDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prefeitura.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/alterarCartaCompromisso")
	@Secured({"ROLE_EDITAR_PREFEITURA"})
	public ResponseEntity<PrefeituraDTO> alterarCartaCompromisso(@RequestBody PrefeituraDTO prefeituraDTO) {
		Prefeitura prefeitura = prefeituraService.alterarCartaCompromisso(prefeituraDTO);
		PrefeituraDTO prefeituraAlteradaDTO = new PrefeituraDTO(prefeitura);
		return ResponseEntity.ok().body(prefeituraAlteradaDTO);
	}
	
	@GetMapping("/buscarDetalhesParaAprovacao/{id}")
	public ResponseEntity<PrefeituraDTO> buscarDetalhesParaAprovacao(@PathVariable("id") final Long id) {
		PrefeituraDTO prefeituraDTO = prefeituraService.buscarDetalhesParaAprovacao(id);
	  return ResponseEntity.ok().body(prefeituraDTO);
	}
	
	@GetMapping("/buscarLogin/{id}")
	public ResponseEntity<PrefeituraDTO> buscarLogin(@PathVariable("id") final Long id) {
	  PrefeituraDTO prefeitura = prefeituraService.buscarLogin(id);
	  return ResponseEntity.ok().body(prefeitura);
	}
	
	@GetMapping("/buscarPaisEstadoCidadePorPrefeitura/{idPrefeitura}")
	public ResponseEntity<IdPaisIdEstadoIdCidadeDTO> buscarPaisEstadoCidadePorPrefeitura(@PathVariable("idPrefeitura") final Long idPrefeitura) {
	  return ResponseEntity.ok().body(prefeituraService.buscarPaisEstadoCidadePorPrefeitura(idPrefeitura));
	}

	@GetMapping("/buscarPrefeiturasSignatariasVigentes")
	public ResponseEntity<List<PrefeituraMapaDTO>> buscarPrefeiturasSignatariasVigentes() {
		List<Prefeitura> prefeituras = prefeituraService.listarPrefeiturasSignatariasVigentes();
		List<PrefeituraMapaDTO> dto = prefeituras.stream()
													.map(p -> new PrefeituraMapaDTO(p))
												.collect(Collectors.toList());
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/buscarCidadesSignatariasDataMandatos")
	public ResponseEntity<ArrayList<List<CidadeMandatosDTO>>> buscarCidadesSignatariasDataMandatos() {
		ArrayList<List<CidadeMandatosDTO>> prefeituras = prefeituraService.buscarCidadesSignatariasDataMandatos();
		return ResponseEntity.ok(prefeituras);
	}

	@GetMapping("/buscarPrefeiturasSignatariasVigentesPorEstado")
	public ResponseEntity<List<PrefeituraMapaDTO>> buscarPrefeiturasSignatariasVigentesPorEstado(
																						@RequestParam Long idEstado) {
		List<Prefeitura> prefeituras = prefeituraService.listarPrefeiturasSignatariasVigentesPorEstado(idEstado);
		List<PrefeituraMapaDTO> dto = prefeituras.stream()
													.map(p -> new PrefeituraMapaDTO(p))
												.collect(Collectors.toList());
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/buscarPrefeiturasSignatariasVigentesPorEstadoCidadePartidoPopulacao")
	public ResponseEntity<List<PrefeituraMapaDTO>>
												buscarPrefeiturasSignatariasVigentesPorEstadoCidadePartidoPopulacao
											(@RequestParam(required = false, defaultValue = "-1") Long idEstado,
											 @RequestParam(required = false, defaultValue = "") String cidade,
											 @RequestParam(required = false, defaultValue = "") String prefeito,
											 @RequestParam(required = false, defaultValue = "-1") Long idPartido,
											 @RequestParam(required = false, defaultValue = "") Long populacaoMin,
											 @RequestParam(required = false, defaultValue = "") Long populacaoMax,
											 @RequestParam(name = "orderBy", defaultValue = "cidade") String orderBy,
											 @RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		List<Prefeitura> prefeituras = prefeituraService
												.buscarPrefeiturasSignatariasVigentesPorEstadoCidadePartidoPopulacao
																						(idEstado, cidade,
																						prefeito, idPartido,
																						populacaoMin, populacaoMax,
																						orderBy, direction);
		List<PrefeituraMapaDTO> dto = prefeituras.stream()
													.map(p -> new PrefeituraMapaDTO(p))
												.collect(Collectors.toList());
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/filtrarPrefeituras")
	public ResponseEntity<PrefeituraListagemPaginacaoDTO> filtrarPrefeituras(
											@RequestParam(required = false, defaultValue = "-1") Long idEstado,
											@RequestParam(required = false, defaultValue = "") String cidade,
											@RequestParam(required = false, defaultValue = "") String prefeito,
											@RequestParam(required = false, defaultValue = "-1") Long idPartido,
											@RequestParam(name = "orderBy", defaultValue = "cidade") String orderBy,
											@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		List<Prefeitura> prefeituras = prefeituraService.listarPrefeituraPorEstadoCidadePartido(idEstado, cidade,
																								prefeito, idPartido,
																								orderBy, direction);

		List<PrefeituraListagemDTO> prefeiturasDTOs = prefeituras.stream()
																	.map(p -> new PrefeituraListagemDTO(p))
																.collect(Collectors.toList());

		PrefeituraListagemPaginacaoDTO dto = new PrefeituraListagemPaginacaoDTO(prefeiturasDTOs,
																				prefeituraService.count());

		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/buscarComboBoxPrefeitura")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBox() {
		List<ItemComboDTO> dto = prefeituraService.buscarComboBox();
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping("/buscarPrefeituraEdicao")
	public ResponseEntity<PrefeituraEdicaoDTO> buscarPrefeituraEdicao(@RequestParam Long id) {
		Prefeitura prefeitura = prefeituraService.buscarPorId(id);
		return ResponseEntity.ok(new PrefeituraEdicaoDTO(prefeitura));
	}
	
	@GetMapping("/atualizarCartaCompromisso")
	public void atualizarCartasCompromisso(@RequestParam String path) {
		System.out.println("Iniciando processo de Atualização de Cartas Compromisso");
		List<List<String>> registros = prefeituraService.geraListaCartas(path+"cartas.csv");
		prefeituraService.criarCartas(registros,path); 
	}

	@GetMapping("/downloadCartaCompromisso/{id}/{numero}")
	public ResponseEntity<Resource> downloadCartaCompromisso(@PathVariable long id,@PathVariable int numero, Principal principal) throws Exception{
		//numero--;
		Cidade cidade = cidadeService.buscarPorId(id);
		List<Prefeitura> prefeituras = prefeituraService.listarPorCidade(cidade);
		if( prefeituras == null || prefeituras.isEmpty()) {
			throw new Exception("Cidade não encontrada.");
		}
		Prefeitura prefeitura = prefeituras.get(prefeituras.size()-1);
		if(numero > prefeitura.getCartaCompromisso().size() - 1) {
			//throw new Exception("Arquivo não encontrado.");
			 return ResponseEntity.notFound().build();
		}
		// System.out.println(prefeitura.getCartaCompromisso().get(numero).getExtensao());
		// CartaCompromisso carta = prefeitura.getCartaCompromisso().get(numero); 
		// CartaCompromissoDTO dto = new CartaCompromissoDTO();
		
		// dto.setArquivo(carta.getArquivo());
		// dto.setExtensao(carta.getExtensao());
		// dto.setNomeArquivo(carta.getNomeArquivo());

		// return ResponseEntity.ok().body(dto);

		byte[] pdfBytes = PDFUtils.B64Decode(prefeitura.getCartaCompromisso().get(numero).getArquivo());
		
		String extensao = prefeitura.getCartaCompromisso().get(numero).getExtensao();
	    String arqExtensao = null;
		if(extensao.contains("image/jpeg")) {
			arqExtensao = ".jpg";
		}else if(extensao.contains("image/png")) {
			arqExtensao = ".png";
		}else if(extensao.contains("application/pdf")) {
			arqExtensao = ".pdf";
		}
		
		String nomeArquivoParaDownload = "carta_compromisso_+"+LocalDateTime.now().getNano()+ arqExtensao;
		String nomeRealArquivo = prefeitura.getCartaCompromisso().get(numero).getNomeArquivo();
		FileOutputStream fos = new FileOutputStream(nomeArquivoParaDownload);
	    fos.write(pdfBytes);
	    fos.flush();
	    fos.close();    
	    File arquivo = new File(nomeArquivoParaDownload);
	    byte[] fileContent = Files.readAllBytes(arquivo.toPath());
	    arquivo.delete();
		
		InputStream targetStream = ByteSource.wrap(fileContent).openStream();
		InputStreamResource resource = new InputStreamResource(targetStream);
		String userName = principal != null ? principal.getName() :"";
        downloadsExportacoesService.gravarLog(userName, nomeRealArquivo);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION , arqExtensao);
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        
		return 	ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.headers(headers)
				.body(resource);
		
	}
	
	@GetMapping("/planometasporcidade")
	public ResponseEntity<List<PrefeituraPlanoMetasDTO>> listaPrefeituraPlanoDeMetas() {
		List<PrefeituraPlanoMetasDTO> listaPrefeituraPlanoMetas = planoDeMetasService.listaPrefeituraPlanoDeMetas();
		return ResponseEntity.ok().body(listaPrefeituraPlanoMetas);
	}

	@PostMapping("/inativarPrefeituras")
	public void  atualizarPrefeituras() {
		prefeituraService.inativarPrefeituras();
		prefeituraService.liberarCredencialPrefeituras();
	}

	@PostMapping("/importarPrefeitura")
	public void  importarPrefeitura(@RequestBody PrefeituraImportacaoDTO dto) {
		prefeituraService.importarPrefeitura(dto);
	}

	
	
	@GetMapping("/buscarCidadesSignatariasDataMandatosPorIdCidade")
	public ResponseEntity<List<CidadeMandatosDTO>> buscarCidadesSignatariasDataMandatosPorIdCidade(
																						@RequestParam Long idCidade) {
		List<CidadeMandatosDTO> cidadeMandatosDTO = prefeituraService.buscarCidadesSignatariasDataMandatosPorIdCidade(idCidade);

		return ResponseEntity.ok(cidadeMandatosDTO);
	}
	
	@GetMapping("/buscarPrefeiturasSignatariasComBoasPraticas")
	public ResponseEntity<List<CidadeComBoasPraticasDTO>> buscarPrefeiturasSignatariasComBoasPraticas() {
		List<CidadeComBoasPraticasDTO> prefeituras = prefeituraService.buscarPrefeiturasSignatariasComBoasPraticas();

		return ResponseEntity.ok(prefeituras);
	}
	
}
