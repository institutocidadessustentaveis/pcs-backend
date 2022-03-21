package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.ByteSource;

import br.org.cidadessustentaveis.dto.AtividadeGestorMunicipalDTO;
import br.org.cidadessustentaveis.dto.AtividadeUsuarioDTO;
import br.org.cidadessustentaveis.dto.DownloadsExportacoesDTO;
import br.org.cidadessustentaveis.dto.FiltroUsuarioDTO;
import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.dto.RegistroUsuariosFiltroDTO;
import br.org.cidadessustentaveis.dto.RelatorioApiPublicaDTO;
import br.org.cidadessustentaveis.dto.RelatorioBoaPraticaCidadeSignatariaDTO;
import br.org.cidadessustentaveis.dto.RelatorioBoaPraticaPcsDTO;
import br.org.cidadessustentaveis.dto.RelatorioContagemBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.RelatorioConteudoCompartilhadoDTO;
import br.org.cidadessustentaveis.dto.RelatorioEventoDTO;
import br.org.cidadessustentaveis.dto.RelatorioEventosDTO;
import br.org.cidadessustentaveis.dto.RelatorioIndicadorCompletoDTO;
import br.org.cidadessustentaveis.dto.RelatorioIndicadoresPreenchidosDTO;
import br.org.cidadessustentaveis.dto.RelatorioInteracaoChatForumDTO;
import br.org.cidadessustentaveis.dto.RelatorioInteracaoComFerramentasDTO;
import br.org.cidadessustentaveis.dto.RelatorioPlanoDeMetasDTO;
import br.org.cidadessustentaveis.dto.RelatorioPlanoDeMetasPrestacaoDeContasDTO;
import br.org.cidadessustentaveis.dto.RelatorioQuantidadeIndicadorCadastradoDTO;
import br.org.cidadessustentaveis.dto.RelatorioQuantidadeIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.RelatorioSessaoUsuarioDTO;
import br.org.cidadessustentaveis.dto.RelatorioShapesCriadosDTO;
import br.org.cidadessustentaveis.dto.RelatorioShapesExportadosDTO;
import br.org.cidadessustentaveis.dto.VisualizacaoCartograficaDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.services.AtividadeGestorMunicipalService;
import br.org.cidadessustentaveis.services.AtividadeUsuarioService;
import br.org.cidadessustentaveis.services.CidadeService;
import br.org.cidadessustentaveis.services.DownloadsExportacoesService;
import br.org.cidadessustentaveis.services.HistoricoRelatorioGeradoService;
import br.org.cidadessustentaveis.services.HistoricoSessaoUsuarioService;
import br.org.cidadessustentaveis.services.PrefeituraService;
import br.org.cidadessustentaveis.services.RelatorioApiPublicaService;
import br.org.cidadessustentaveis.services.RelatorioBoaPraticaCidadeSignatariaService;
import br.org.cidadessustentaveis.services.RelatorioBoaPraticaPcsService;
import br.org.cidadessustentaveis.services.RelatorioContagemBoasPraticasService;
import br.org.cidadessustentaveis.services.RelatorioConteudoCompartilhadoService;
import br.org.cidadessustentaveis.services.RelatorioEventoService;
import br.org.cidadessustentaveis.services.RelatorioEventosService;
import br.org.cidadessustentaveis.services.RelatorioIndicadoresCompletoService;
import br.org.cidadessustentaveis.services.RelatorioIndicadoresPreenchidosService;
import br.org.cidadessustentaveis.services.RelatorioInteracaoChatForumService;
import br.org.cidadessustentaveis.services.RelatorioInteracaoComFerramentasService;
import br.org.cidadessustentaveis.services.RelatorioPlanoDeMetasService;
import br.org.cidadessustentaveis.services.RelatorioQtdIndicadoresCadastradosService;
import br.org.cidadessustentaveis.services.RelatorioQtdIndicadoresPreenchidosService;
import br.org.cidadessustentaveis.services.ShapeFileService;
import br.org.cidadessustentaveis.services.UsuarioService;
import br.org.cidadessustentaveis.services.VisualizacaoCartograficaService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/relatorio")
public class RelatorioResource {

	@Autowired
	private HistoricoRelatorioGeradoService serviceHistoricoRelatorio;
	@Autowired
	private AtividadeUsuarioService serviceAtividadeUsuario;
	@Autowired
	private AtividadeGestorMunicipalService serviceAtividadeGestorMunicipal;
	@Autowired
	private RelatorioConteudoCompartilhadoService serviceConteudoCompartilhado;
	@Autowired
	private DownloadsExportacoesService serviceDownloadExportacoes;
	@Autowired
	private HistoricoSessaoUsuarioService relatorioSessaoUsuarioService;
	@Autowired
	private RelatorioQtdIndicadoresCadastradosService qtdIndicadoresCadastradosService;
	@Autowired
	private RelatorioQtdIndicadoresPreenchidosService qtdIndicadoresPreenchidosService;
	@Autowired
	private RelatorioInteracaoComFerramentasService interacaoComFerramentasService;
	@Autowired
	private RelatorioInteracaoChatForumService relatorioInteracaoChatForumService;
	@Autowired
	private RelatorioEventosService relatorioEventosService;
	@Autowired
	private RelatorioPlanoDeMetasService relatorioPlanoDeMetasService;
	@Autowired
	private RelatorioIndicadoresPreenchidosService relatorioIndicadoresPreenchidosService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private VisualizacaoCartograficaService visualizacaoCartograficaService;
	@Autowired
	private ShapeFileService shapeFileService;
	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private PrefeituraService prefeituraService;
	@Autowired
	private RelatorioEventoService relatorioEventoService;
	@Autowired
	private RelatorioBoaPraticaPcsService relatorioBoaPraticaPcsService;
	@Autowired
	private RelatorioBoaPraticaCidadeSignatariaService relatorioBoaPraticaCidadeSignatariaService;
	@Autowired
	private RelatorioContagemBoasPraticasService relatorioContagemBoasPraticasService;
	
	@Autowired
	private RelatorioIndicadoresCompletoService relatorioIndicadoresCompletosService;
	
	@Autowired
	private RelatorioApiPublicaService relatorioApiPublicaService;


	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarHistoricoRelatorio")
	public ResponseEntity<List<HistoricoRelatorioGeradoDTO>> buscarHistoricoRelatorio(
			@RequestBody HistoricoRelatorioGeradoDTO filtro) {

		List<HistoricoRelatorioGeradoDTO> listaRelatorio = new ArrayList<HistoricoRelatorioGeradoDTO>();

		listaRelatorio = serviceHistoricoRelatorio.buscar(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Histórico de Relatórios Gerados");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaRelatorio);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarAtividadeUsuario")
	public ResponseEntity<List<AtividadeUsuarioDTO>> buscarAtividadeUsuario(
			@Valid @RequestBody AtividadeUsuarioDTO relObj) {

		List<AtividadeUsuarioDTO> listaRelatorio = new ArrayList<AtividadeUsuarioDTO>();

		listaRelatorio = serviceAtividadeUsuario.buscar(relObj);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Atividades do usuário");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaRelatorio);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarRegistroUsuarios")
	public ResponseEntity<List<FiltroUsuarioDTO>> buscarRegistroUsuarios(
			@RequestBody RegistroUsuariosFiltroDTO relObj) {

		List<FiltroUsuarioDTO> listaRelatorio = new ArrayList<FiltroUsuarioDTO>();

		listaRelatorio = usuarioService.buscarRegistroUsuarioFiltrado(relObj);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Registro dos usuários");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaRelatorio);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarAtividadeGestorMunicipal")
	public ResponseEntity<List<AtividadeGestorMunicipalDTO>> buscarAcaoGestorMunicipal(
			@Valid @RequestBody AtividadeGestorMunicipalDTO relObj) {

		List<AtividadeGestorMunicipalDTO> listaRelatorio = new ArrayList<AtividadeGestorMunicipalDTO>();

		listaRelatorio = serviceAtividadeGestorMunicipal.buscar(relObj);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Atividades do Gestor Municipal");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaRelatorio);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarFiltroRelatorioConteudoCompartilhado")
	public ResponseEntity<List<RelatorioConteudoCompartilhadoDTO>> buscarFiltroRelatorioConteudoCompartilhado(
			@Valid @RequestBody RelatorioConteudoCompartilhadoDTO relObj) {

		List<RelatorioConteudoCompartilhadoDTO> listaRelatorio = new ArrayList<RelatorioConteudoCompartilhadoDTO>();

		listaRelatorio = serviceConteudoCompartilhado.buscar(relObj);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Conteudo Compartilhado");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaRelatorio);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarDownloadsExportacoes")
	@Deprecated
	public ResponseEntity<List<DownloadsExportacoesDTO>> buscarDownloadsExportacoes(
			@RequestBody DownloadsExportacoesDTO relObj) {

		List<DownloadsExportacoesDTO> listaRelatorio = new ArrayList<DownloadsExportacoesDTO>();

		listaRelatorio = serviceDownloadExportacoes.buscar(relObj);

		serviceHistoricoRelatorio.gravarLog(relObj.getUsuarioLogado(), "Downloads e Exportações");

		return ResponseEntity.ok().body(listaRelatorio);
	}

	@Deprecated
	@GetMapping("/gravaLogDownloadExportacao/{usuarioLogado}/{tipoRelatorio}")
	public ResponseEntity<Void> gravaLogDownloadExportacao(@PathVariable("usuarioLogado") String usuarioLogado,
			@PathVariable("tipoRelatorio") String tipoRelatorio) {
		serviceDownloadExportacoes.gravarLog(usuarioLogado, tipoRelatorio);
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarRelatorioSessoesUsuarios")
	public ResponseEntity<List<RelatorioSessaoUsuarioDTO>> buscarRelatorioSessoesUsuarios(
			@RequestBody RelatorioSessaoUsuarioDTO relatorioSessaoUsuarioDTO) {

		List<RelatorioSessaoUsuarioDTO> listaRelatorio = new ArrayList<RelatorioSessaoUsuarioDTO>();

		listaRelatorio = relatorioSessaoUsuarioService.buscar(relatorioSessaoUsuarioDTO);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Acesso às sessões");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaRelatorio);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarIndicadoresPreenchidosComPaginacao")
	public ResponseEntity<List<RelatorioIndicadoresPreenchidosDTO>> buscarIndicadoresPreenchidosComPaginacao(
			@Valid @RequestBody RelatorioIndicadoresPreenchidosDTO relatorioIndicadoresPreenchidosDTO) {

		List<RelatorioIndicadoresPreenchidosDTO> listaDTO = new ArrayList<RelatorioIndicadoresPreenchidosDTO>();

		listaDTO = relatorioIndicadoresPreenchidosService.buscarComPaginacao(relatorioIndicadoresPreenchidosDTO);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Indicadores Preenchidos");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaDTO);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarIndicadoresPreenchidos")
	public ResponseEntity<List<RelatorioIndicadoresPreenchidosDTO>> buscarIndicadoresPreenchidos(
			@Valid @RequestBody RelatorioIndicadoresPreenchidosDTO relatorioIndicadoresPreenchidosDTO) {

		List<RelatorioIndicadoresPreenchidosDTO> listaDTO = new ArrayList<RelatorioIndicadoresPreenchidosDTO>();

		listaDTO = relatorioIndicadoresPreenchidosService.buscar(relatorioIndicadoresPreenchidosDTO);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Indicadores Preenchidos");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaDTO);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarEventos")
	public ResponseEntity<List<RelatorioEventoDTO>> buscarEventos(@RequestBody RelatorioEventoDTO relatorioEventoDTO) {
		List<RelatorioEventoDTO> lista = new ArrayList<>();
		lista = relatorioEventosService.buscar(relatorioEventoDTO);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Eventos");			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(lista);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarVisualizacaoCartografica")
	public ResponseEntity<List<VisualizacaoCartograficaDTO>> buscarVisualizacaoCartografica(
			@Valid @RequestBody VisualizacaoCartograficaDTO filtro) {

		List<VisualizacaoCartograficaDTO> listaRelatorioDTO = new ArrayList<>();

		listaRelatorioDTO = visualizacaoCartograficaService.buscar(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Visualização Cartografica");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(listaRelatorioDTO);

	}

	@GetMapping()
	public ResponseEntity<List<Cidade>> listar() {
		/*
		 * List<Cidade> listaCidades = cidadeService.listar(); List<CidadeDTO>
		 * listaCidadesDto = listaCidades.stream().map(obj -> new
		 * CidadeDTO(obj)).collect(Collectors.toList()); return
		 * ResponseEntity.ok().body(listaCidadesDto);
		 */

		Cidade cidades;
		List<Cidade> listaCidades = new ArrayList<>();
		for (Long i = 0l; i < 100l; i++) {
			String nomeCidade = i % 2 == 0 ? "A" + i
					: i % 3 == 0 ? "B" + i : i % 4 == 0 ? "C" + i : i % 5 == 0 ? "São José dos campos " + i : "Jacarei";
			cidades = new Cidade();
			cidades.setId(i);
			cidades.setNome(nomeCidade);
			cidades.setPrefeitura(null);
			cidades.setProvinciaEstado(null);
			listaCidades.add(cidades);
		}

		return ResponseEntity.ok().body(listaCidades);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarQuantidadeIndicadoresCadastrados")
	public ResponseEntity<List<RelatorioQuantidadeIndicadorCadastradoDTO>> buscarQuantidadeIndicadoresCadastrados(
			@RequestBody RelatorioQuantidadeIndicadorCadastradoDTO filtro) {

		List<RelatorioQuantidadeIndicadorCadastradoDTO> lista = new ArrayList<RelatorioQuantidadeIndicadorCadastradoDTO>();

		lista = qtdIndicadoresCadastradosService.buscar(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Quantidade de Indicadores Cadastrados");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarQuantidadeIndicadoresPreenchidos")
	public ResponseEntity<List<RelatorioQuantidadeIndicadorPreenchidoDTO>> buscarQuantidadeIndicadoresPreenchidos(
			@RequestBody RelatorioQuantidadeIndicadorPreenchidoDTO filtro) {

		List<RelatorioQuantidadeIndicadorPreenchidoDTO> lista = new ArrayList<RelatorioQuantidadeIndicadorPreenchidoDTO>();

		lista = qtdIndicadoresPreenchidosService.buscar(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Quantidade de Indicadores Preenchidos");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarInteracaoComFerramentas")
	public ResponseEntity<List<RelatorioInteracaoComFerramentasDTO>> buscarInteracaoComFerramentas(
			@RequestBody RelatorioInteracaoComFerramentasDTO filtro) {

		List<RelatorioInteracaoComFerramentasDTO> lista = new ArrayList<RelatorioInteracaoComFerramentasDTO>();

		lista = interacaoComFerramentasService.buscar(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Interação de usuários com ferramentas");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarInteracaoChatForum")
	public ResponseEntity<List<RelatorioInteracaoChatForumDTO>> buscarInteracaoChatForum(
			@RequestBody RelatorioInteracaoChatForumDTO filtro) {
		List<RelatorioInteracaoChatForumDTO> lista = relatorioInteracaoChatForumService.buscar(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Interação de usuários com as ferramentas Chat e Fórum");			
		}catch (Exception e) {
			e.printStackTrace();
		}
		

		return ResponseEntity.ok().body(lista);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarRelatorioPlanoDeMetas")
	public ResponseEntity<List<RelatorioPlanoDeMetasDTO>> buscarRelatorioPlanoDeMetas(
			@RequestBody RelatorioPlanoDeMetasDTO filtro) {

		List<RelatorioPlanoDeMetasDTO> lista = new ArrayList<RelatorioPlanoDeMetasDTO>();

		lista = relatorioPlanoDeMetasService.buscar(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de plano de metas");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioShapesCadastrados")
	public ResponseEntity<List<RelatorioShapesCriadosDTO>> buscarRelatorioShapesCadastrados(@RequestParam Long idUsuario, 
			@RequestParam String dtCadastro, @RequestParam String dtEdicao, @RequestParam String tituloShape) {
		
		List<RelatorioShapesCriadosDTO> lista = shapeFileService.gerarRelatorioShapesCadastrados(idUsuario, dtCadastro, dtEdicao, tituloShape);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de shapes cadastrados");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioShapesExportados")
	public ResponseEntity<List<RelatorioShapesExportadosDTO>> buscarRelatorioShapesExportados(@RequestParam Long idPerfil, @RequestParam Long idCidade) {
		
		List<RelatorioShapesExportadosDTO> lista = shapeFileService.gerarRelatorioShapesExportados(idPerfil, idCidade);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de shapes exportados");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioShapesCadastradosPrefeitura")
	public ResponseEntity<List<RelatorioShapesCriadosDTO>> buscarRelatorioShapesCadastradosPrefeitura(@RequestParam Long idUsuario, @RequestParam Long idCidade, 
			@RequestParam String dtCadastro, @RequestParam String dtEdicao, @RequestParam String tituloShape) {
		
		List<RelatorioShapesCriadosDTO> lista = shapeFileService.gerarRelatorioShapesCadastradosPrefeitura(idUsuario, idCidade, dtCadastro, dtEdicao, tituloShape);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de shapes cadastrados da prefeitura");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioPlanoDeMetasPrestacaoDeContas")
	public ResponseEntity<List<RelatorioPlanoDeMetasPrestacaoDeContasDTO>> buscarRelatorioPlanoDeMetasPrestacaoDeContas(@RequestParam Long idCidade) {
		
		List<RelatorioPlanoDeMetasPrestacaoDeContasDTO> lista = prefeituraService.gerarRelatorioPlanoDeMetasPrestacaoDeContas(idCidade);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de upload do plano de metas e prestação de contas");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioEventos")
	public ResponseEntity<List<RelatorioEventosDTO>> buscarRelatorioEventos(@RequestParam String tipo, @RequestParam String dataInicial, @RequestParam String dataFinal,
			@RequestParam String endereco, @RequestParam String titulo, @RequestParam Long cidade, @RequestParam Long estado, @RequestParam Long pais) {
		
		List<RelatorioEventosDTO> lista = relatorioEventoService.buscarRelatorios(tipo, dataInicial, dataFinal, endereco, titulo, cidade, estado, pais);
		
		for (RelatorioEventosDTO evento : lista) {
			List<Eixo> eixos = relatorioEventoService.findEixoById(evento.getId());
			evento.listarEixos(eixos);
			List<AreaInteresse> temas = relatorioEventoService.findAreaInteresseById(evento.getId());
			evento.listarTemas(temas);
		}
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de eventos");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}

	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioBoasPraticasPCS")
	public ResponseEntity<List<RelatorioBoaPraticaPcsDTO>> buscarRelatorioBoasPraticasPcs(@RequestParam String titulo, @RequestParam Long idPais, @RequestParam Long idEstado,
			@RequestParam Long idCidade, @RequestParam Long idEixo, @RequestParam Long idOds, @RequestParam Long idMetaOds) {
		
		List<RelatorioBoaPraticaPcsDTO> lista = relatorioBoaPraticaPcsService.buscarRelatorios(titulo, idPais, idEstado, idCidade, idEixo, idOds, idMetaOds);
		
		for (RelatorioBoaPraticaPcsDTO boaPratica : lista) {
			List<Indicador> indicadores = relatorioBoaPraticaPcsService.findIndicadorById(boaPratica.getId());
			boaPratica.listarIndicadores(indicadores);
			
			List<MetaObjetivoDesenvolvimentoSustentavel> metasOds = relatorioBoaPraticaPcsService.findMetaOdsById(boaPratica.getId());
			boaPratica.listarMetasOds(metasOds);
			
			List<ObjetivoDesenvolvimentoSustentavel> ods = relatorioBoaPraticaPcsService.findOdsById(boaPratica.getId());
			boaPratica.listarOds(ods);
		}
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de boas práticas do PCS");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioBoasPraticasCidadesSignatarias")
	public ResponseEntity<List<RelatorioBoaPraticaCidadeSignatariaDTO>> buscarRelatorioBoasPraticasCidadesSignatarias(@RequestParam String titulo, @RequestParam Long idPais, @RequestParam Long idEstado,
			@RequestParam Long idCidade, @RequestParam Long idEixo, @RequestParam Long idOds, @RequestParam Long idMetaOds) {
		
		List<RelatorioBoaPraticaCidadeSignatariaDTO> lista = relatorioBoaPraticaCidadeSignatariaService.buscarRelatorios(titulo, idPais, idEstado, idCidade, idEixo, idOds, idMetaOds);
		
		for (RelatorioBoaPraticaCidadeSignatariaDTO boaPratica : lista) {
			List<Indicador> indicadores = relatorioBoaPraticaPcsService.findIndicadorById(boaPratica.getId());
			boaPratica.listarIndicadores(indicadores);
			
			List<MetaObjetivoDesenvolvimentoSustentavel> metasOds = relatorioBoaPraticaPcsService.findMetaOdsById(boaPratica.getId());
			boaPratica.listarMetasOds(metasOds);
			
			List<ObjetivoDesenvolvimentoSustentavel> ods = relatorioBoaPraticaPcsService.findOdsById(boaPratica.getId());
			boaPratica.listarOds(ods);
		}
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de boas práticas das cidades signatárias");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarContagemRelatorioBoasPraticas")
	public ResponseEntity<List<RelatorioContagemBoasPraticasDTO>> buscarContagemRelatorioBoasPraticas(@RequestParam String tipoVariavel, @RequestParam String tipoBoaPratica) {

		List<RelatorioContagemBoasPraticasDTO> lista = new ArrayList<RelatorioContagemBoasPraticasDTO>();
		if(tipoVariavel.equals("Eixos")) {
			lista = relatorioContagemBoasPraticasService.buscarRelatoriosPorEixo(tipoBoaPratica);
		} else if(tipoVariavel.equals("Indicadores")) {
			lista = relatorioContagemBoasPraticasService.buscarRelatoriosPorIndicador(tipoBoaPratica);
		} else if(tipoVariavel.equals("Ods")) {
			lista = relatorioContagemBoasPraticasService.buscarRelatoriosPorOds(tipoBoaPratica);
		} else if(tipoVariavel.equals("MetaOds")) {
			lista = relatorioContagemBoasPraticasService.buscarRelatoriosPorMetaOds(tipoBoaPratica);
		} else if(tipoVariavel.equals("Estado")) {
			lista = relatorioContagemBoasPraticasService.buscarRelatoriosPorEstado(tipoBoaPratica);
		} else if(tipoVariavel.equals("Cidade")) {
			lista = relatorioContagemBoasPraticasService.buscarRelatoriosPorCidade(tipoBoaPratica);
		}

		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de contagem de boas práticas");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/buscarRelatorioIndicadoresCompletos")
	public ResponseEntity<List<RelatorioIndicadorCompletoDTO>> buscarRelatorioIndicadoresCompletos() {
		
		List<RelatorioIndicadorCompletoDTO> lista = relatorioIndicadoresCompletosService.gerarRelatorio();
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de Indicadores Completo");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@GetMapping("/downloadIndicadoresCompletos")
	public ResponseEntity<Resource> downloadIndicadoresCompletos(HttpServletRequest request, Principal principal) throws IOException, AuthenticationException{
		File file = relatorioIndicadoresCompletosService.gerarArquivoXlsx();
		byte[] fileContent = Files.readAllBytes(file.toPath());
		file.delete();
		InputStream targetStream = ByteSource.wrap(fileContent).openStream();
		InputStreamResource resource = new InputStreamResource(targetStream);
		return 	ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"relatorio_indicores_completo.xlsx\"")
				.body(resource);
	}
	
	@Secured({ "ROLE_RELATORIOS" })
	@PostMapping("/buscarRelatorioApiPublica")
	public ResponseEntity<List<RelatorioApiPublicaDTO>> buscarRelatorioApiPublica(@RequestBody RelatorioApiPublicaDTO filtro) {
		
		List<RelatorioApiPublicaDTO> lista = relatorioApiPublicaService.buscarRelatorioFiltrado(filtro);
		
		try {
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			serviceHistoricoRelatorio.gravarLog(user, "Relatório de API Pública");			
		}catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(lista);
	}
}
