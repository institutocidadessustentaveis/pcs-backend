package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.dto.ArquivoDTO;
import br.org.cidadessustentaveis.dto.CidadeComboDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CidadeDetalheDTO;
import br.org.cidadessustentaveis.dto.CidadeEdicaoDTO;
import br.org.cidadessustentaveis.dto.CidadeExibicaoDTO;
import br.org.cidadessustentaveis.dto.CidadeIbgeDTO;
import br.org.cidadessustentaveis.dto.CidadesPaginacaoDTO;
import br.org.cidadessustentaveis.dto.ContagemCidadesCidadesParticipantesNoEstadoDTO;
import br.org.cidadessustentaveis.dto.ExibirCidadeProvinciaEstadoDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PorcentagemCidadesSignatariasDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.services.AlertaService;
import br.org.cidadessustentaveis.services.ArquivoService;
import br.org.cidadessustentaveis.services.CidadeService;
import br.org.cidadessustentaveis.services.DownloadsExportacoesService;
import br.org.cidadessustentaveis.services.HistoricoPlanoMetasPrestacaoContasService;
import br.org.cidadessustentaveis.services.RelatorioPlanoDeMetasService;
import br.org.cidadessustentaveis.services.ShapeFileService;
import br.org.cidadessustentaveis.services.UsuarioService;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/cidade")
public class CidadeResource {

	@Autowired
	private CidadeService cidadeService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DownloadsExportacoesService downloadsExportacoesService;
	
	@Autowired
	private ArquivoService arquivoService;
	
	@Autowired
	private HistoricoPlanoMetasPrestacaoContasService historicoPlanoMetasPrestacaoContasService;
	
	@Autowired
	private ShapeFileService shapeFileService;
	
	@Autowired
	private RelatorioPlanoDeMetasService relatorioPlanoDeMetasService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private AlertaService alertaService;

	@GetMapping()
	public ResponseEntity<CidadesPaginacaoDTO> listar(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "itemsPerPage", defaultValue = "10") Integer itemsPerPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {

		List<Cidade> listaCidades = new ArrayList<>();
		listaCidades = cidadeService.listar(page, itemsPerPage ,orderBy, direction);

		List<ExibirCidadeProvinciaEstadoDTO> listaCidadesDto = 
									listaCidades.stream()
												.map(obj -> new ExibirCidadeProvinciaEstadoDTO(obj))
												.collect(Collectors.toList());

		CidadesPaginacaoDTO dto = new CidadesPaginacaoDTO(listaCidadesDto, cidadeService.count());

	    
		logger.info("/cidade - "+dto.toString());

		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/signatarias/buscarParaCombo")
	public ResponseEntity<List<ItemComboDTO>> listar() {
		List<ItemComboDTO> listaCidades = cidadeService.buscarTodasCidadesSignatariasParaCombo();
		return ResponseEntity.ok().body(listaCidades);
	}

	@GetMapping("/buscar/{nome}")
	public ResponseEntity<CidadesPaginacaoDTO> 
								buscarPorNome(@PathVariable String nome,
											@RequestParam(required = false) Integer page, 
											@RequestParam(required = false) Integer itemsPerPage) {
		List<Cidade> listaCidades = new LinkedList<>();

		if ((page != null && itemsPerPage != null) && (page >= 0 && itemsPerPage > 0)) {
			listaCidades = cidadeService.buscarPorNome(nome, page, itemsPerPage).getContent();
		} else {
			listaCidades = cidadeService.buscarPorNome(nome);
		}

		List<ExibirCidadeProvinciaEstadoDTO> listaCidadesDto = 
				listaCidades.stream()
							.map(obj -> new ExibirCidadeProvinciaEstadoDTO(obj))
							.collect(Collectors.toList());

		CidadesPaginacaoDTO dto = new CidadesPaginacaoDTO(listaCidadesDto, cidadeService.count());

		return ResponseEntity.ok().body(dto);
	}
	
	
	@GetMapping("/porEstado/{id}")
	public ResponseEntity<List<ExibirCidadeProvinciaEstadoDTO>> listarPorEstado(@PathVariable("id") Long id) {
		List<Cidade> listaCidades = cidadeService.listarPorEstado(id);
		List<ExibirCidadeProvinciaEstadoDTO> listaCidadesDto = listaCidades.stream()
		        .map(obj -> new ExibirCidadeProvinciaEstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaCidadesDto);
	}
	
	@GetMapping("/porEstadoNome/{nome}")
	public ResponseEntity<List<ExibirCidadeProvinciaEstadoDTO>> listarPorEstado(@PathVariable("nome") String nome) {
		List<Cidade> listaCidades = cidadeService.listarPorEstadoNome(nome);
		List<ExibirCidadeProvinciaEstadoDTO> listaCidadesDto = listaCidades.stream()
		        .map(obj -> new ExibirCidadeProvinciaEstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaCidadesDto);
	}
	
	
	@GetMapping("/porEstadoItemCombo/{id}")
	public ResponseEntity<List<ItemComboDTO>> buscarComboBox(@PathVariable("id") Long id) {
		List<Cidade> listaCidades = cidadeService.buscarComboBox(id);
		List<ItemComboDTO> cidadeDTO = listaCidades.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(cidadeDTO);
	}
	
	@GetMapping("/porEstadoPCSCombo/{id}")
	public ResponseEntity<List<ItemComboDTO>> buscarPCSComboBox(@PathVariable("id") Long id) {
		List<Cidade> listaCidades = cidadeService.buscarEstadoPCSComboBox(id);
		List<ItemComboDTO> cidadeDTO = listaCidades.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome()))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(cidadeDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExibirCidadeProvinciaEstadoDTO> listarPorId(final @PathVariable("id") Long id) {
		ExibirCidadeProvinciaEstadoDTO cidadeDTO = new ExibirCidadeProvinciaEstadoDTO(cidadeService.buscarPorId(id));
		return ResponseEntity.ok().body(cidadeDTO);
	}

	@GetMapping("/buscarCidadeEdicao/{id}")
	public ResponseEntity<CidadeEdicaoDTO> buscarCidadeEdicao(@PathVariable("id") Long id) {
		CidadeEdicaoDTO dto = new CidadeEdicaoDTO(cidadeService.buscarPorId(id));
		List<Feature> shapeZoneamento = shapeFileService.buscarShapeZoneamento(id);
		dto.setShapeZoneamento(shapeZoneamento);
		return ResponseEntity.ok(dto);
	}

	@Secured({ "ROLE_CADASTRAR_CIDADE" })
	@PostMapping()
	public ResponseEntity<CidadeDTO> inserir(@Valid @RequestBody CidadeDTO cidadeDTO) throws IOException {
		if(cidadeDTO.getFotoCidade() != null &&
				ImageUtils.guessImageFormat(cidadeDTO.getFotoCidade()).equalsIgnoreCase("GIF")) {
			throw new DataIntegrityException("Formato de imagem GIF não suportado");
		}

		Cidade cidade = cidadeService.inserir(cidadeDTO);
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorEmailCredencial(user);
		
		if(cidade.getPlanoMetas() != null || cidade.getRelatorioContas() != null) {
			historicoPlanoMetasPrestacaoContasService.gerarHistoricoPlanoMetasPrestacaoContas(cidade, usuario);
			relatorioPlanoDeMetasService.inserirRelatorioPlanoDeMetas(usuario.getNome(), cidade.getNome(), cidade.getProvinciaEstado().getNome(), cidade.getPlanoMetas().getId(), cidade);
			
			alertaService.salvar(Alerta.builder()
					.mensagem("A prefeitura de " + cidade.getNome() + " cadastrou um Plano de Metas.")
						.link("/painel-cidade/detalhes/" + cidade.getId()) 
						.tipoAlerta(TipoAlerta.CADASTRO_PLANO_DE_METAS)
						.data(LocalDateTime.now())
						.build());
		}
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cidade.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@Secured({ "ROLE_EDITAR_CIDADE", "ROLE_EDITAR_DADOS_CIDADE" })
	@PutMapping(value = "/{id}")
	public ResponseEntity<CidadeDTO> alterar(final @PathVariable("id") Long id,
											 		@RequestBody CidadeDTO cidadeDTO) throws Exception {
		Cidade cidade = cidadeService.alterar(id, cidadeDTO);
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorEmailCredencial(user);
		
		if(cidade.getPlanoMetas() != null || cidade.getRelatorioContas() != null) {
			historicoPlanoMetasPrestacaoContasService.gerarHistoricoPlanoMetasPrestacaoContas(cidade, usuario);
			relatorioPlanoDeMetasService.inserirRelatorioPlanoDeMetas(usuario.getNome(), cidade.getNome(), cidade.getProvinciaEstado().getNome(), cidade.getPlanoMetas().getId(), cidade);
			
			alertaService.salvar(Alerta.builder()
					.mensagem("A prefeitura de " + cidade.getNome() + " cadastrou um Plano de Metas.")
						.link("/painel-cidade/detalhes/" + cidade.getId()) 
						.tipoAlerta(TipoAlerta.CADASTRO_PLANO_DE_METAS)
						.data(LocalDateTime.now())
						.build());
		}
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cidade.getId()).toUri();
		return ResponseEntity.ok().location(uri).build();
	}

	@Secured({ "ROLE_DELETAR_CIDADE" })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		cidadeService.deletar(id);
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_CADASTRAR_CIDADE" })
	@PutMapping("/atualizarCidadesIBGE")
	public ResponseEntity<Void> atualizarCidadesIBGE(@RequestBody List<CidadeIbgeDTO> cidadesDoIbge) {
		cidadeService.atualizarCidadesIbge(cidadesDoIbge);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/signatarias/quantidadePorEstado")
	public ResponseEntity<List<ContagemCidadesCidadesParticipantesNoEstadoDTO>> 
																countCidadesSignatariasPorEstado() {
		List<ContagemCidadesCidadesParticipantesNoEstadoDTO> contagem = cidadeService.countCidadeSignatariaPorEstado();
		return ResponseEntity.ok(contagem);
	}

	@GetMapping("/signatarias/porcentagensPorEstado")
	public ResponseEntity<List<PorcentagemCidadesSignatariasDTO>> calcularPorcentagensSignatarias() {
		List<PorcentagemCidadesSignatariasDTO> porcentagens = 
									cidadeService.calcularPorcentagemCidadesSignatariasPorEstado();
		return ResponseEntity.ok(porcentagens);
	}
	
	@PutMapping(value = "/editarCordenadas")
	public ResponseEntity<?> alterarCoordenadas(@RequestBody Cidade cidadeDTO)
	        throws Exception {
		cidadeService.alterarCoordenadas(Cidade.builder().id(cidadeDTO.getId()).eixoX(cidadeDTO.getEixoX()).eixoY(cidadeDTO.getEixoY()).build());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/buscarCidadesSignatarias")
	public ResponseEntity<List<String>> buscarCidadesSignatarias(){
		List<String> listaCidade = new ArrayList<String>();
		List<Cidade> listaCidadeSignataria = cidadeService.buscarSignatarias();
		for(Cidade cid : listaCidadeSignataria) {
			listaCidade.add(cid.getNome());
		}
		return ResponseEntity.ok().body(listaCidade);
	}
	
	@GetMapping("/buscarCidadesSignatariasComparacaoIndicadores")
	public ResponseEntity<List<String>> buscarCidadesSignatariasComparacaoIndicadores(){
		List<String> listaNomeCidades= cidadeService.buscarNomeCidadesSignatarias();
		return ResponseEntity.ok().body(listaNomeCidades);
	}

	@GetMapping("/buscarPorIdEstado/{idEstado}")
	public ResponseEntity<List<CidadeDTO>> buscarPorIdEstado(@PathVariable("idEstado") Long idEstado) {
		List<Cidade> listaCidades = cidadeService.listarPorEstado(idEstado);
		List<CidadeDTO> listaCidadesDto = listaCidades.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		
		listaCidadesDto.stream().forEach(x -> x.setSubdivisoes(null));
		listaCidadesDto.stream().forEach(x -> x.setProvinciaEstado(null));
		return ResponseEntity.ok().body(listaCidadesDto);
	}
	
	@GetMapping("/cidadesSignatarias")
	public ResponseEntity<List<ExibirCidadeProvinciaEstadoDTO>> cidadesSignatarias(){
		List<Cidade> listaCidadeSignataria = cidadeService.buscarSignatarias();
		
		List<ExibirCidadeProvinciaEstadoDTO> dtos = listaCidadeSignataria.stream().map(signataria -> new ExibirCidadeProvinciaEstadoDTO(signataria))
				.collect(Collectors.toList());
				
		return ResponseEntity.ok().body(dtos);
		
	}
	
	
	@GetMapping("/cidadesSignatariasFiltroIndicador")
	public ResponseEntity<List<ExibirCidadeProvinciaEstadoDTO>> cidadesSignatariasFiltroIndicador(){
		List<Cidade> listaCidadeSignataria = cidadeService.buscarSignatarias();
		
		List<ExibirCidadeProvinciaEstadoDTO> dtos = listaCidadeSignataria.stream().map(signataria -> new ExibirCidadeProvinciaEstadoDTO(signataria.getId(), signataria.getNome()))
				.collect(Collectors.toList());
				
		return ResponseEntity.ok().body(dtos);
		
	}

	@GetMapping("buscarParaEditarViaPrefeitura/{idCidade}")
	public ResponseEntity<ExibirCidadeProvinciaEstadoDTO> buscarParaEditarViaPrefeitura(final @PathVariable("idCidade") Long idCidade) {
		ExibirCidadeProvinciaEstadoDTO cidadeDTO = cidadeService.buscarParaEditarViaPrefeitura(idCidade);
		return ResponseEntity.ok().body(cidadeDTO);
	}
	
	@GetMapping("/listarComboPorEstado/{id}")
	public ResponseEntity<List<ItemComboDTO>> listarComboPorEstado(@PathVariable("id") Long id) {
		List<ItemComboDTO> listaCidades = cidadeService.listarComboPorEstado(id);
		return ResponseEntity.ok().body(listaCidades);
	}
	
	@GetMapping("/buscarCidadeComboPorId/{id}")
	public ResponseEntity<CidadeComboDTO> buscarCidadeComboPorId(final @PathVariable("id") Long idCidade) {
		CidadeComboDTO cidadeDTO = cidadeService.buscarCidadeComboPorId(idCidade);
		return ResponseEntity.ok().body(cidadeDTO);
	}
	
	@GetMapping("/buscarCidadeComboBox")
	public ResponseEntity<List<ItemComboDTO>> buscarCidadeComboBox() {
		List<ItemComboDTO> listaComboCidade= cidadeService.buscarCidadeComboBox();
		return ResponseEntity.ok().body(listaComboCidade);
	}
	
	@GetMapping("/buscarCidadeEstadoComboBox")
	public ResponseEntity<List<ItemComboDTO>> buscarCidadeEstadoComboBox() {
		List<ItemComboDTO> listaComboCidade= cidadeService.buscarCidadeEstadoComboBox();
		return ResponseEntity.ok().body(listaComboCidade);
	}
	
	@GetMapping("/buscarCidadePorId/{idCidade}")
	public ResponseEntity<CidadeDetalheDTO> findByIdCidade(final @PathVariable("idCidade") Long idCidade) {
		CidadeDetalheDTO cidade = cidadeService.findByIdCidade(idCidade);
		return ResponseEntity.ok().body(cidade);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<ArquivoDTO> download(@PathVariable long id, Principal principal) throws IOException{
		
		Arquivo arq = arquivoService.buscarPorId(id);

		ArquivoDTO arquivoDto = new ArquivoDTO(arq.getId(), arq.getNomeArquivo(), arq.getExtensao(), arq.getConteudo());

		if(principal != null) {
			downloadsExportacoesService.gravarLog(principal.getName(), arq.getNomeArquivo());
		} else {
			downloadsExportacoesService.gravarLog("Usuário não autenticado", arq.getNomeArquivo());
		}

		return 	ResponseEntity.ok().body(arquivoDto);
		
	}

	@GetMapping("/atualizarPlanoDeMetas")
	public void atualizarPlanoDeMetas(@RequestParam String path) {
		List<List<String>> registros = cidadeService.geraListaPlanosMeta(path+"plano_metas.csv");
		cidadeService.criarPlanosDeMeta(registros, path);
	}
	@GetMapping("/atualizarRelatorioPrestacao")
	public void atualizarRelatorioPrestacao(@RequestParam String path) {
		List<List<String>> registros = cidadeService.geraListaPlanosMeta(path+"relatorio-prestacao.csv");
		cidadeService.criarRelatorioPrestacao(registros, path);
	}
	
	@GetMapping("/buscarEstadoECidadesPorNome")
	public ResponseEntity<HashMap<String, List<CidadeDTO>>> buscarEstadoECidadesPorNome(
				@RequestParam String nomeCidade) {
		
		HashMap<String, List<CidadeDTO>> chaveValor = cidadeService.buscarPorNomeESignataria(nomeCidade);

		return ResponseEntity.ok().body(chaveValor);
	}
	
	@GetMapping("/buscarCidadeParaComboPorListaIdsEstados")
	public ResponseEntity<List<ItemComboDTO>> buscarCidadeParaComboPorListaIdsEstados(@RequestParam List<Long> idsEstados) {
		List<ItemComboDTO> listaCidades = cidadeService.buscarCidadeParaComboPorListaIdsEstados(idsEstados);
		return ResponseEntity.ok().body(listaCidades);
	}

	@GetMapping("/buscarPorSiglaCidade")
	public ResponseEntity<CidadeExibicaoDTO> buscarPorSiglaCidade(@RequestParam String sigla, @RequestParam String nome) {
		Cidade cidade = cidadeService.findByNomeAndProvinciaEstadoSigla(nome, sigla);
		CidadeExibicaoDTO dto = new CidadeExibicaoDTO(cidade);
		return ResponseEntity.ok().body(dto);
	}
	
	
	@GetMapping("/buscarSignatariasComboPorIdEstado/{idEstado}")
	public ResponseEntity<List<ItemComboDTO>> buscarSignatariasComboPorIdEstado(@PathVariable("idEstado") Long idEstado) {
		List<ItemComboDTO> listaCidades = cidadeService.buscarSignatariasComboPorIdEstado(idEstado);
		return ResponseEntity.ok().body(listaCidades);
	}


}
