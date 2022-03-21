package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteSource;

import br.org.cidadessustentaveis.dto.PlanoDeMetasDTO;
import br.org.cidadessustentaveis.dto.PlanoDeMetasDetalhadoDTO;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.PlanoDeMetas;
import br.org.cidadessustentaveis.services.DownloadsExportacoesService;
import br.org.cidadessustentaveis.services.PlanoDeMetasService;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/planodemetas")
public class PlanoDeMetasResource {

	@Autowired
	private PlanoDeMetasService service;
	@Autowired
	private DownloadsExportacoesService downloadsExportacoesService;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;

	@GetMapping("/buscar/{idPlanoDeMetas}")
	public ResponseEntity<PlanoDeMetasDTO> buscarPlanoDeMetasPorId(@PathVariable("idPlanoDeMetas") Long idPlanoDeMetas) {
		PlanoDeMetasDTO planoDeMetasDTO = service.buscarCarregarPlanoDeMetasPorId(idPlanoDeMetas);
//		PlanoDeMetasDTO planoDeMetasDTO = service.buscarCarregarPlanoDeMetasPorIdLazyLoad(idPlanoDeMetas);
		return ResponseEntity.ok().body(planoDeMetasDTO);
	}
	
	@GetMapping("/buscar/{idPlanoDeMetas}/{idIndicador}")
	public ResponseEntity<PlanoDeMetasDetalhadoDTO> buscarPlanoDeMetasPorId(@PathVariable("idPlanoDeMetas") Long idPlanoDeMetas, @PathVariable("idIndicador") Long idIndicador) {
		PlanoDeMetasDetalhadoDTO planoDeMetasDTO = service.buscarCarregarPlanoDeMetasDetalhadoPorIndicador(idPlanoDeMetas, idIndicador);
		return ResponseEntity.ok().body(planoDeMetasDTO);
	}
	
	@Secured({"ROLE_CADASTRAR_PLANO_METAS"})
	@PostMapping("/inserir")
	public ResponseEntity<Void> inserir(@Valid @RequestBody PlanoDeMetasDTO planoDeMetasDTO) {
		PlanoDeMetas planoDeMetas = service.inserirPlanoDeMetas(planoDeMetasDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(planoDeMetas.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@Secured({"ROLE_CADASTRAR_PLANO_METAS"})
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		service.deletarPlanoDeMetas(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_CADASTRAR_PLANO_METAS"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<Void> editar(@Valid @RequestBody PlanoDeMetasDTO planoDeMetasDTO, @PathVariable("id") Long id) {
		service.editarPlanoDeMetas(planoDeMetasDTO, id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_CADASTRAR_PLANO_METAS"})
	@GetMapping("/criarPlanoDeMetasDetalhadoParaIndicador/{idPlanoDeMetas}/{idIndicador}")
	public ResponseEntity<PlanoDeMetasDetalhadoDTO> criarPlanoDeMetasDetalhadoParaIndicador(@PathVariable("idIndicador") Long idIndicador, @PathVariable("idPlanoDeMetas") Long idPlanoDeMetas) {
		PlanoDeMetasDetalhadoDTO planoDeMetasDTO = service.criarPlanoDeMetasDetalhadoParaIndicador(idPlanoDeMetas, idIndicador);
		return ResponseEntity.ok().body(planoDeMetasDTO);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable long id) throws Exception{
		File file = service.gerarArquivo(id);

		byte[] fileContent = Files.readAllBytes(file.toPath());
		file.delete();
		
		InputStream targetStream = ByteSource.wrap(fileContent).openStream();
		InputStreamResource resource = new InputStreamResource(targetStream);
		
		Usuario usuario = new Usuario();		
		
		try {
			usuario = usuarioContextUtil.getUsuario();
		} catch(Exception e) {
			 usuario = null;
		}
		
		if(usuario != null) {
			downloadsExportacoesService.gravarLog(usuario.getEmail(), "plano_metas.xlsx");			
		}
		else {
			downloadsExportacoesService.gravarLog("", "plano_metas.xlsx");	
		}
		
		return 	ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"plano_de_metas.xlsx\"")
				.body(resource);
		
	}
	
	@Secured({"ROLE_CADASTRAR_PLANO_METAS"})
	@PutMapping("/editarPlanoDeMetasDetalhado/{id}")
	public ResponseEntity<Void> editarPlanoDeMetasDetalhado(@Valid @RequestBody PlanoDeMetasDetalhadoDTO planoDeMetasDTO, @PathVariable("id") Long id) throws Exception {
		service.editarPlanoDeMetasDetalhado(planoDeMetasDTO, id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_CADASTRAR_PLANO_METAS"})
	@DeleteMapping("/deletarPlanoDeMetasDetalhado/{id}")
	public ResponseEntity<Void> deletarPlanoDeMetasDetalhado(@PathVariable("id") Long id) {
		service.deletarPlanoDeMetasDetalhado(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarPorPrefeitura/{idPrefeitura}")
	public ResponseEntity<PlanoDeMetasDTO> buscarPlanoDeMetasPorPrefeitura(@PathVariable("idPrefeitura") Long idPrefeitura) {
		PlanoDeMetasDTO planoDeMetasDTO = service.buscarPlanoDeMetasPorPrefeitura(idPrefeitura);
		return ResponseEntity.ok().body(planoDeMetasDTO);
	}
	
	@GetMapping("/buscarPorCidade/{idCidade}")
	public ResponseEntity<PlanoDeMetasDTO> buscarPlanoDeMetasPorCidade(@PathVariable("idCidade") Long idCidade) {
		PlanoDeMetasDTO planoDeMetasDTO = service.buscarPlanoDeMetasPorCidade(idCidade);
		return ResponseEntity.ok().body(planoDeMetasDTO);
	}
}
