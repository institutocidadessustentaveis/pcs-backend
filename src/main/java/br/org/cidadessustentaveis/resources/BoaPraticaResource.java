package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.BoaPraticaCardDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaDetalheDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaResumidoToListDTO;
import br.org.cidadessustentaveis.dto.BoaPraticaToListDTO;
import br.org.cidadessustentaveis.dto.BoasPraticasFiltradasDTO;
import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CombosCidadesComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.FiltroCidadesComBoasPraticas;
import br.org.cidadessustentaveis.dto.SolucaoBoaPraticaDTO;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.boaspraticas.FonteReferenciaBoaPratica;
import br.org.cidadessustentaveis.model.boaspraticas.ImagemBoaPratica;
import br.org.cidadessustentaveis.services.BoaPraticaService;
import br.org.cidadessustentaveis.services.HistoricoAcessoBoaPraticaService;
import br.org.cidadessustentaveis.services.SolucaoBoaPraticaService;
import br.org.cidadessustentaveis.util.ImageUtils;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/boapratica")
public class BoaPraticaResource {
	
	@Autowired
	private UsuarioContextUtil usuarioContext;
	
	@Autowired
	private BoaPraticaService service;
	
	@Autowired
	private SolucaoBoaPraticaService solucaoBoaPraticaService;

	@Autowired
	private HistoricoAcessoBoaPraticaService historicoAcessoBoaPraticaService;

	@GetMapping("/buscar")
	public ResponseEntity<List<BoaPraticaToListDTO>> buscar(){
		List<BoaPratica> boasPraticas = service.buscar();
		List<BoaPraticaToListDTO> boasPraticasDto = boasPraticas.stream().map(obj -> new BoaPraticaToListDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(boasPraticasDto);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<BoaPraticaDTO> buscarPorId(@PathVariable("id") Long id) {
		BoaPraticaDTO boaPraticaDto = service.buscarBoaPrticaParaEditar(id);
		return ResponseEntity.ok().body(boaPraticaDto);
	}
	
	@GetMapping("/buscarBoasPraticasPCS")
	public ResponseEntity<List<BoaPraticaToListDTO>> buscarBoasPraticasPCS(){
		List<BoaPratica> boasPraticas = service.buscarBoasPraticasPCS();
		List<BoaPraticaToListDTO> boasPraticasDto = boasPraticas.stream().map(obj -> new BoaPraticaToListDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(boasPraticasDto);
	}
	
	@GetMapping("/buscarBoasPraticasGeral")
	public ResponseEntity<List<BoaPraticaResumidoToListDTO>> buscarBoasPraticasGeral(){
		try {
			Usuario usuario = usuarioContext.getUsuario();
			if(usuario != null && usuario.getPrefeitura() != null) {
				List<BoaPraticaResumidoToListDTO> boasPraticas = service.buscarBoasPraticasPorPrefeitura(usuario.getPrefeitura().getId());
				return ResponseEntity.ok().body(boasPraticas);
			} else {
				List<BoaPraticaResumidoToListDTO> boasPraticas = service.buscarBoasPraticasGeral();
				return ResponseEntity.ok().body(boasPraticas);
			}
			
		} catch (Exception e) {
			List<BoaPraticaResumidoToListDTO> boasPraticas = service.buscarBoasPraticasGeral();
			return ResponseEntity.ok().body(boasPraticas);
		}
	}

	
	@GetMapping("/buscarBoasPraticasPorPrefeitura/{idPrefeitura}")
	public ResponseEntity<List<BoaPraticaResumidoToListDTO>> buscarBoasPraticasPorPrefeitura(@PathVariable("idPrefeitura") Long idPrefeitura){
		List<BoaPraticaResumidoToListDTO> boasPraticas = service.buscarBoasPraticasPorPrefeitura(idPrefeitura);
		return ResponseEntity.ok().body(boasPraticas);
	}
	
	@GetMapping("/buscarBoasPraticasRelacionadasAoIndicador/{idIndicador}")
	public ResponseEntity<List<BoaPraticaToListDTO>> buscarBoasPraticasRelacionadasAoIndicador(@PathVariable("idIndicador") Long idIndicador){
		List<BoaPratica> boasPraticas = service.buscarBoasPraticasRelacionadasAoIndicador(idIndicador);
		List<BoaPraticaToListDTO> boasPraticasDto = boasPraticas.stream().map(obj -> new BoaPraticaToListDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(boasPraticasDto);
	}
	
	@GetMapping("/buscarFonteReferencia/{idArquivo}")
	public ResponseEntity<FonteReferenciaBoaPratica> buscarFonteReferencia(@PathVariable("idArquivo") Long idArquivo) {
		FonteReferenciaBoaPratica fonteReferenciaBoaPratica = service.buscarFonteReferenciaBoaPraticaPorId(idArquivo);
		return ResponseEntity.ok().body(new FonteReferenciaBoaPratica(fonteReferenciaBoaPratica.getId(), fonteReferenciaBoaPratica.getNomeArquivo(), fonteReferenciaBoaPratica.getExtensao(), fonteReferenciaBoaPratica.getConteudo(), null));
	}
	
	@GetMapping("/buscarBoaPrticaDetalhe/{id}")
	public ResponseEntity<BoaPraticaDetalheDTO> buscarBoaPrticaDetalhe(
																@PathVariable("id") Long id,
																@RequestHeader(value = "Referer",
																				required = false) String referer) {
		BoaPraticaDetalheDTO boaPraticaDto = service.buscarBoaPrticaDetalhe(id);
		historicoAcessoBoaPraticaService.registarHistorico(boaPraticaDto.getId(), referer);
		return ResponseEntity.ok().body(boaPraticaDto);
	}
	
	@ApiIgnore
	@Deprecated
	@GetMapping("/removerEstilo")
	public ResponseEntity<String> removerEstilo() {
		try {
			service.removerEstiloBoasPraticas();
			return ResponseEntity.ok().body("");			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@ApiIgnore
	@Secured({"ROLE_CADASTRAR_BOA_PRATICA"})
	@PostMapping("/inserir")
	public ResponseEntity<Void> inserir(@RequestBody BoaPraticaDTO boaPraticaDto) throws IOException {
		BoaPratica boaPratica = service.inserir(boaPraticaDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(boaPratica.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@ApiIgnore
	@Secured({"ROLE_DELETAR_BOA_PRATICA"})
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<Void> excluirBoaPratica(@PathVariable("id") Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@ApiIgnore
	@Secured({"ROLE_EDITAR_BOA_PRATICA"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<BoaPraticaDTO> alterar(final @PathVariable("id") Long id, @RequestBody BoaPraticaDTO boaPraticaDTO) throws Exception {
		BoaPratica boaPratica = service.alterar(id, boaPraticaDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(boaPratica.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@ApiIgnore
	@Secured({"ROLE_EDITAR_BOA_PRATICA"})
	@DeleteMapping("/excluirFonteReferencia/{id}")
	public ResponseEntity<Void> excluirFonteReferencia(@PathVariable("id") Long id) {
		service.excluirFonteReferencia(id);
		return ResponseEntity.noContent().build();
	}
	
	@ApiIgnore
	@Secured({"ROLE_EDITAR_BOA_PRATICA"})
	@DeleteMapping("/excluirImagemBoaPratica/{idImagem}")
	public ResponseEntity<Void> excluirImagemBoaPratica(@PathVariable("idImagem") Long idImagem) {
		service.excluirImagemBoaPratica(idImagem);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarCidadesComBoasPraticas")
	public ResponseEntity<List<CidadeComBoasPraticasDTO>> buscarCidadesComBoasPraticas(){
		List<CidadeComBoasPraticasDTO> listaCidadescomBoasPraticas = service.buscarCidadescomBoasPraticas();
		return ResponseEntity.ok().body(listaCidadescomBoasPraticas);	
	}
	
	@GetMapping("/buscarCombosCidadesComBoasPraticas")
	public ResponseEntity<CombosCidadesComBoasPraticasDTO> buscarCombosCidadesComBoasPraticas(){
		CombosCidadesComBoasPraticasDTO combosCidadesDTO = service.buscarCombosCidadesComBoasPraticas();
		return ResponseEntity.ok().body(combosCidadesDTO);
	}
	
	@PostMapping("/buscarBoasPraticasFiltradas")	
	public ResponseEntity<BoasPraticasFiltradasDTO> buscarBoasPraticasFiltradas(
			@RequestBody FiltroCidadesComBoasPraticas filtro){
		BoasPraticasFiltradasDTO lista = service.buscarBoasPraticasFiltradas(filtro);
		return ResponseEntity.ok().body(lista);		
	}
	
	@PostMapping("/buscarBoasPraticasFiltradasPaginaInicial")	
	public ResponseEntity<BoasPraticasFiltradasDTO> buscarBoasPraticasFiltradasPaginaInicial(
			@RequestBody FiltroCidadesComBoasPraticas filtro){
		BoasPraticasFiltradasDTO lista = service.buscarBoasPraticasFiltradasPaginaInicial(filtro);
		return ResponseEntity.ok().body(lista);		
	}

	@PostMapping("/buscarCidadesComBoasPraticasFiltradas")	
	public ResponseEntity<List<CidadeComBoasPraticasDTO>> buscarCidadesComBoasPraticasFiltradas(
			@RequestBody FiltroCidadesComBoasPraticas filtro){
		List<CidadeComBoasPraticasDTO> lista = service.buscarCidadesComBoasPraticasFiltradas(filtro);
		return ResponseEntity.ok().body(lista);		
	}
	
	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagemBoaPratica(@RequestHeader(value = "User-Agent") String userAgent, final @PathVariable("id") Long id) throws Exception {
		
		BoaPratica boaPratica = service.buscarPorId(id);
		String base64Imagem = "";
		if(boaPratica == null) {
			return ResponseEntity.notFound().build();
		}
		for (ImagemBoaPratica imagem : boaPratica.getImagens()) {
			if(imagem.getTipo().equals("principal")) {
				base64Imagem = ImageUtils.isRobot(userAgent) ? 
						ImageUtils.compressImageSocialMediaB64(imagem.getConteudo()) : imagem.getConteudo();
			}
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("public");
		headers.set("ETag", ImageUtils.generateBase64ImageETag(base64Imagem));
		

		return new ResponseEntity<byte[]>(Base64.decode(base64Imagem.getBytes()), headers, HttpStatus.OK);
	}
	
	@GetMapping("/buscarBoasPraticasDaCidade")	
	public ResponseEntity<BoasPraticasFiltradasDTO> buscarBoasPraticasDaCidade(@RequestParam Integer page, @RequestParam Integer linesPerPage, @RequestParam Long idCidade){
		BoasPraticasFiltradasDTO lista = service.buscarBoasPraticasDaCidade(page, linesPerPage, idCidade);
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping("/buscarOdsDaBoaPratica/{idBoaPratica}")
	public ResponseEntity<List<ObjetivoDesenvolvimentoSustentavel>> buscarOdsDaBoaPratica(@PathVariable("idBoaPratica") Long idBoaPratica){
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = service.buscarOdsDaBoaPratica(idBoaPratica);
		return ResponseEntity.ok().body(listaOds);	
	}
	
	@GetMapping("/buscarSolucaoImagem/{id}")
	public ResponseEntity<byte[]> buscarSolucaoImagem(@PathVariable("id") Long id) throws NoSuchAlgorithmException, IOException {
		
		String srcData = solucaoBoaPraticaService.buscarSolucaoImagem(id);
		
		String base64 = srcData.split(",")[1];
		
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("public");
		headers.set("ETag", ImageUtils.generateBase64ImageETag(base64));
		

		return new ResponseEntity<byte[]>(Base64.decode(base64.getBytes()), headers, HttpStatus.OK);
	}
		
	@GetMapping("/buscarBoasPraticasRelacionadasAoIndicadorCidade/{idIndicador}")
	public ResponseEntity<List<BoaPraticaCardDTO>> buscarBoasPraticasRelacionadasAoIndicadorCidade(@PathVariable("idIndicador") Long idIndicador){
		List<BoaPratica> boasPraticas = service.buscarBoasPraticasRelacionadasAoIndicador(idIndicador);
		List<BoaPraticaCardDTO> boasPraticasDto = boasPraticas.stream().map(obj -> new BoaPraticaCardDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(boasPraticasDto);
	}

}
