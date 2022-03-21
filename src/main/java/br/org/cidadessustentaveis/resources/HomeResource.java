package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.HomeBarraDTO;
import br.org.cidadessustentaveis.dto.HomeDTO;
import br.org.cidadessustentaveis.dto.HomeImagemDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO;
import br.org.cidadessustentaveis.dto.PrimeiraSecaoDTO;
import br.org.cidadessustentaveis.dto.QuartaSecaoDTO;
import br.org.cidadessustentaveis.dto.QuintaSecaoDTO;
import br.org.cidadessustentaveis.dto.SecaoLateralDTO;
import br.org.cidadessustentaveis.dto.SegundaSecaoDTO;
import br.org.cidadessustentaveis.dto.SetimaSecaoDTO;
import br.org.cidadessustentaveis.dto.TerceiraSecaoDTO;
import br.org.cidadessustentaveis.dto.SextaSecaoDTO;
import br.org.cidadessustentaveis.model.home.Home;
import br.org.cidadessustentaveis.model.home.HomeImagem;
import br.org.cidadessustentaveis.services.HomeService;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/home")
public class HomeResource {
	
	@Autowired
	private HomeService homeService;

	@Secured({"ROLE_EDITAR_HOME"})
	@GetMapping("/buscarParaEdicao/{id}")
	public ResponseEntity<HomeDTO> buscarParaEdicao(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(homeService.buscarParaEdicao(id));
	}
	
	
	@Secured({"ROLE_CADASTRAR_HOME"})
	@PostMapping()
	public ResponseEntity<HomeDTO> inserir(@Valid @RequestBody HomeDTO homeDTO) throws IOException {
		Home home = homeService.inserir(homeDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
																	.buildAndExpand(home.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/{id}")
	public ResponseEntity<HomeDTO> alterar(final @PathVariable("id") Long id,
													@RequestBody HomeDTO HomeDTO) throws Exception {
		Home home = homeService.alterar(id, HomeDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
												.path("/{id}").buildAndExpand(home.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@GetMapping("/{id}")
	public ResponseEntity<Home> listarPorId(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(homeService.findById(id));
	}
	
	@GetMapping("/buscarPaginaHomePorLink/{link}")
	public ResponseEntity<HomeDTO> buscarPaginaHomePorLink(final @PathVariable("link") String link) {
		HomeDTO home = homeService.buscarPaginaHomePorLink(link);
		return ResponseEntity.ok().body(home);
	}
	
	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarHomeImagem(final @PathVariable("id") Long id)
																					throws IOException,
																							NoSuchAlgorithmException {
		HomeImagem homeImagem = homeService.buscarHomeImagemById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("public");
		headers.set("ETag", ImageUtils.generateImageETag(homeImagem.getConteudo()));

		return new ResponseEntity<byte[]>(Base64.decode(homeImagem.getConteudo().getBytes()),
											headers, HttpStatus.OK);
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirHomeImagem/{idImagem}")
	public ResponseEntity<Void> excluirHomeImagem(@PathVariable("idImagem") Long idImagem) {
		homeService.excluirHomeImagem(idImagem);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/buscarIdsPaginaHomePorLink/{link}")
	public ResponseEntity<HomeDTO> buscarIdsPaginaHomePorLink(final @PathVariable("link") String link) {
		HomeDTO home = homeService.buscarIdsPaginaHomePorLink(link);
		return ResponseEntity.ok().body(home);
	}
	
	@GetMapping("/buscarHomeBarraPorId/{id}")
	public ResponseEntity<HomeBarraDTO> buscarHomeBarraPorId(final @PathVariable("id") Long id) {
		HomeBarraDTO homeBarraDTO = homeService.buscarHomeBarraPorId(id);
		return ResponseEntity.ok().body(homeBarraDTO);
	}
	
	@GetMapping("/buscarPrimeiraSecaoPorId/{id}")
	public ResponseEntity<List<PrimeiraSecaoDTO>> buscarPrimeiraSecaoPorId(final @PathVariable("id") Long id) {
		List<PrimeiraSecaoDTO> listaPrimeiraSecaoDTO = homeService.buscarPrimeiraSecaoPorId(id);
		return ResponseEntity.ok().body(listaPrimeiraSecaoDTO);
	}
	
	@GetMapping("/buscarListaPrimeiraSecaoResumidaPorId/{id}")
	public ResponseEntity<List<PrimeiraSecaoDTO>> buscarListaPrimeiraSecaoResumidaPorId(final @PathVariable("id") Long id) {
		List<PrimeiraSecaoDTO> listaPrimeiraSecaoDTO = homeService.buscarListaPrimeiraSecaoResumidaPorId(id);
		return ResponseEntity.ok().body(listaPrimeiraSecaoDTO);
	}
	
	@GetMapping("/buscarPrimeiraSecaoDetalhe/{id}")
	public ResponseEntity<PrimeiraSecaoDTO> buscarPrimeiraSecaoDetalhe(final @PathVariable("id") Long id) {
		PrimeiraSecaoDTO primeiraSecaoDTO = homeService.buscarPrimeiraSecaoDetalhe(id);
		return ResponseEntity.ok().body(primeiraSecaoDTO);
	}
	
	@GetMapping("/buscarSegundaSecaoDetalhe/{id}")
	public ResponseEntity<SegundaSecaoDTO> buscarSegundaSecaoDetalhe(final @PathVariable("id") Long id) {
		SegundaSecaoDTO segundaSecaoDTO = homeService.buscarSegundaSecaoDetalhe(id);
		return ResponseEntity.ok().body(segundaSecaoDTO);
	}
	
	@GetMapping("/buscarTerceiraSecaoDetalhe/{id}")
	public ResponseEntity<TerceiraSecaoDTO> buscarTerceiraSecaoDetalhe(final @PathVariable("id") Long id) {
		TerceiraSecaoDTO terceiraSecaoDTO = homeService.buscarTerceiraSecaoDetalhe(id);
		return ResponseEntity.ok().body(terceiraSecaoDTO);
	}
	
	@GetMapping("/buscarQuartaSecaoDetalhe/{id}")
	public ResponseEntity<QuartaSecaoDTO> buscarQuartaSecaoDetalhe(final @PathVariable("id") Long id) {
		QuartaSecaoDTO quartaSecaoDTO = homeService.buscarQuartaSecaoDetalhe(id);
		return ResponseEntity.ok().body(quartaSecaoDTO);
	}
	
	@GetMapping("/buscarQuintaSecaoDetalhe/{id}")
	public ResponseEntity<QuintaSecaoDTO> buscarQuintaSecaoDetalhe(final @PathVariable("id") Long id) {
		QuintaSecaoDTO quintaSecaoDTO = homeService.buscarQuintaSecaoDetalhe(id);
		return ResponseEntity.ok().body(quintaSecaoDTO);
	}
	
	@GetMapping("/buscarSecaoLateralDetalhe/{id}")
	public ResponseEntity<SecaoLateralDTO> buscarSecaoLateralDetalhe(final @PathVariable("id") Long id) {
		SecaoLateralDTO secaoLateralDTO = homeService.buscarSecaoLateralDetalhe(id);
		return ResponseEntity.ok().body(secaoLateralDTO);
	}
	
	@GetMapping("/buscarSextaSecaoDetalhe/{id}")
	public ResponseEntity<SextaSecaoDTO> buscarSextaSecaoDetalhe(final @PathVariable("id") Long id) {
		SextaSecaoDTO sextaSecaoDTO = homeService.buscarSextaSecaoDetalhe(id);
		return ResponseEntity.ok().body(sextaSecaoDTO);
	}
	
	@GetMapping("/buscarSetimaSecaoDetalhe/{id}")
	public ResponseEntity<SetimaSecaoDTO> buscarSetimaSecaoDetalhe(final @PathVariable("id") Long id) {
		SetimaSecaoDTO setimaSecaoDTO = homeService.buscarSetimaSecaoDetalhe(id);
		return ResponseEntity.ok().body(setimaSecaoDTO);
	}
	
	@GetMapping("/buscarSegundaSecaoPorId/{id}")
	public ResponseEntity<List<SegundaSecaoDTO>> buscarSegundaSecaoPorId(final @PathVariable("id") Long id) {
		List<SegundaSecaoDTO> listaSegundaSecaoDTO = homeService.buscarSegundaSecaoPorId(id);
		return ResponseEntity.ok().body(listaSegundaSecaoDTO);	
	}
	
	@GetMapping("/buscarListaSegundaSecaoResumidaPorId/{id}")
	public ResponseEntity<List<SegundaSecaoDTO>> buscarListaSegundaSecaoResumidaPorId(final @PathVariable("id") Long id) {
		List<SegundaSecaoDTO> listaSegundaSecaoDTO = homeService.buscarListaSegundaSecaoResumidaPorId(id);
		return ResponseEntity.ok().body(listaSegundaSecaoDTO);
	}
	
	@GetMapping("/buscarTerceiraSecaoPorId/{id}")
	public ResponseEntity<List<TerceiraSecaoDTO>> buscarTerceiraSecaoPorId(final @PathVariable("id") Long id) {
		List<TerceiraSecaoDTO> listaTerceiraSecaoDTO = homeService.buscarTerceiraSecaoPorId(id);
		return ResponseEntity.ok().body(listaTerceiraSecaoDTO);	
	}
	
	@GetMapping("/buscarListaTerceiraSecaoResumidaPorId/{id}")
	public ResponseEntity<List<TerceiraSecaoDTO>> buscarListaTerceiraSecaoResumidaPorId(final @PathVariable("id") Long id) {
		List<TerceiraSecaoDTO> listaTerceiraSecaoDTO = homeService.buscarListaTerceiraSecaoResumidaPorId(id);
		return ResponseEntity.ok().body(listaTerceiraSecaoDTO);
	}
	
	@GetMapping("/buscarQuartaSecaoPorId/{id}")
	public ResponseEntity<List<QuartaSecaoDTO>> buscarQuartaSecaoPorId(final @PathVariable("id") Long id) {
		List<QuartaSecaoDTO> listaQuartaSecaoDTO = homeService.buscarQuartaSecaoPorId(id);
		return ResponseEntity.ok().body(listaQuartaSecaoDTO);		
	}
	
	@GetMapping("/buscarListaQuartaSecaoResumidaPorId/{id}")
	public ResponseEntity<List<QuartaSecaoDTO>> buscarListaQuartaSecaoResumidaPorId(final @PathVariable("id") Long id) {
		List<QuartaSecaoDTO> listaQuartaSecaoDTO = homeService.buscarListaQuartaSecaoResumidaPorId(id);
		return ResponseEntity.ok().body(listaQuartaSecaoDTO);
	}
	
	@GetMapping("/buscarQuintaSecaoPorId/{id}")
	public ResponseEntity<List<QuintaSecaoDTO>> buscarQuintaSecaoPorId(final @PathVariable("id") Long id) {
		List<QuintaSecaoDTO> listaQuintaSecaoDTO = homeService.buscarQuintaSecaoPorId(id);
		return ResponseEntity.ok().body(listaQuintaSecaoDTO);			
	}
	
	@GetMapping("/buscarListaQuintaSecaoResumidaPorId/{id}")
	public ResponseEntity<List<QuintaSecaoDTO>> buscarListaQuintaSecaoResumidaPorId(final @PathVariable("id") Long id) {
		List<QuintaSecaoDTO> listaQuintaSecaoDTO = homeService.buscarListaQuintaSecaoResumidaPorId(id);
		return ResponseEntity.ok().body(listaQuintaSecaoDTO);
	}
	
	@GetMapping("/buscarSecaoLateralPorId/{id}")
	public ResponseEntity<List<SecaoLateralDTO>> buscarSecaoLateralPorId(final @PathVariable("id") Long id) {
		List<SecaoLateralDTO> listaSecaoLateralDTO = homeService.buscarSecaoLateralPorId(id);
		return ResponseEntity.ok().body(listaSecaoLateralDTO);				
	}
	
	@GetMapping("/buscarListaSecaoLateralResumidaPorId/{id}")
	public ResponseEntity<List<SecaoLateralDTO>> buscarListaSecaoLateralResumidaPorId(final @PathVariable("id") Long id) {
		List<SecaoLateralDTO> listaSecaoLateralDTO = homeService.buscarListaSecaoLateralResumidaPorId(id);
		return ResponseEntity.ok().body(listaSecaoLateralDTO);
	}
	
	@GetMapping("/buscarSextaSecaoPorId/{id}")
	public ResponseEntity<List<SextaSecaoDTO>> buscarSextaSecaoPorId(final @PathVariable("id") Long id) {
		List<SextaSecaoDTO> listaSextaSecaoDTO = homeService.buscarSextaSecaoPorId(id);
		return ResponseEntity.ok().body(listaSextaSecaoDTO);	
	}
	
	@GetMapping("/buscarListaSextaSecaoResumidaPorId/{id}")
	public ResponseEntity<List<SextaSecaoDTO>> buscarListaSextaSecaoResumidaPorId(final @PathVariable("id") Long id) {
		List<SextaSecaoDTO> listaSextaSecaoDTO = homeService.buscarListaSextaSecaoResumidaPorId(id);
		return ResponseEntity.ok().body(listaSextaSecaoDTO);
	}
	
	@GetMapping("/buscarSetimaSecaoPorId/{id}")
	public ResponseEntity<List<SetimaSecaoDTO>> buscarSetimaSecaoPorId(final @PathVariable("id") Long id) {
		List<SetimaSecaoDTO> listaSetimaSecaoDTO = homeService.buscarSetimaSecaoPorId(id);
		return ResponseEntity.ok().body(listaSetimaSecaoDTO);	
	}
	
	@GetMapping("/buscarListaSetimaSecaoResumidaPorId/{id}")
	public ResponseEntity<List<SetimaSecaoDTO>> buscarListaSetimaSecaoResumidaPorId(final @PathVariable("id") Long id) {
		List<SetimaSecaoDTO> listaSetimaSecaoDTO = homeService.buscarListaSetimaSecaoResumidaPorId(id);
		return ResponseEntity.ok().body(listaSetimaSecaoDTO);
	}
	
	@GetMapping("/buscarPaginaHomePorId/{id}")
	public ResponseEntity<HomeDTO> buscarPaginaHomePorId(final @PathVariable("id") Long id) {
		HomeDTO home = homeService.buscarIdsPaginaHomePorIdHome(id);
		return ResponseEntity.ok().body(home);
	}
	
	@GetMapping("/buscarListaImagensGaleriaPorId/{id}")
	public ResponseEntity<List<HomeImagemDTO>> buscarListaImagensGaleriaPorId(final @PathVariable("id") Long id) {
		List<HomeImagemDTO> list = homeService.buscarListaImagensGaleriaPorId(id);
		
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/buscarTodasSemConteudoPorIdHome/{id}")
	public ResponseEntity<List<HomeImagemDTO>> buscarTodasSemConteudoPorIdHome(final @PathVariable("id") Long id) {
		List<HomeImagemDTO> list = homeService.buscarTodasSemConteudoPorIdHome(id);
		
		return ResponseEntity.ok().body(list);
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarHomeBarra/{id}")
	public ResponseEntity<HomeBarraDTO> editarHomeBarra(final @PathVariable("id") Long id,
													@RequestBody HomeBarraDTO homeBarraDTO) throws Exception {
		homeService.editarHomeBarra(id, homeBarraDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarHomeGaleria/{id}")
	public ResponseEntity<HomeImagemDTO> editarHomeGaleria(final @PathVariable("id") Long id,
													@RequestBody HomeImagemDTO homeImagemDTO) throws Exception {
		HomeImagemDTO homeImagem = homeService.editarHomeGaleria(id, homeImagemDTO);
		return ResponseEntity.ok().body(homeImagem);
	}

	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarPrimeiraSecao/{id}")
	public ResponseEntity<PrimeiraSecaoDTO> editarPrimeiraSecao(final @PathVariable("id") Long id,
													@RequestBody PrimeiraSecaoDTO primeiraSecaoDTO) throws Exception {
		homeService.editarPrimeiraSecao(id, primeiraSecaoDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirPrimeiraSecao/{id}")
	public ResponseEntity<Void> excluirPrimeiraSecao(@PathVariable("id") Long id) {
		homeService.excluirPrimeiraSecao(id);
		return ResponseEntity.noContent().build();
	}

	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarSegundaSecao/{id}")
	public ResponseEntity<SegundaSecaoDTO> editarSegundaSecao(final @PathVariable("id") Long id,
													@RequestBody SegundaSecaoDTO segundaSecaoDTO) throws Exception {
		homeService.editarSegundaSecao(id, segundaSecaoDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirSegundaSecao/{id}")
	public ResponseEntity<Void> excluirSegundaSecao(@PathVariable("id") Long id) {
		homeService.excluirSegundaSecao(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarTerceiraSecao/{id}")
	public ResponseEntity<TerceiraSecaoDTO> editarTerceiraSecao(final @PathVariable("id") Long id,
													@RequestBody TerceiraSecaoDTO terceiraSecaoDTO) throws Exception {
		homeService.editarTerceiraSecao(id, terceiraSecaoDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirTerceiraSecao/{id}")
	public ResponseEntity<Void> excluirTerceiraSecao(@PathVariable("id") Long id) {
		homeService.excluirTerceiraSecao(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarQuartaSecao/{id}")
	public ResponseEntity<QuartaSecaoDTO> editarQuartaSecao(final @PathVariable("id") Long id,
													@RequestBody QuartaSecaoDTO quartaSecaoDTO) throws Exception {
		homeService.editarQuartaSecao(id, quartaSecaoDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirQuartaSecao/{id}")
	public ResponseEntity<Void> excluirQuartaSecao(@PathVariable("id") Long id) {
		homeService.excluirQuartaSecao(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarQuintaSecao/{id}")
	public ResponseEntity<QuintaSecaoDTO> editarQuintaSecao(final @PathVariable("id") Long id,
													@RequestBody QuintaSecaoDTO quintaSecaoDTO) throws Exception {
		homeService.editarQuintaSecao(id, quintaSecaoDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirQuintaSecao/{id}")
	public ResponseEntity<Void> excluirQuintaSecao(@PathVariable("id") Long id) {
		homeService.excluirQuintaSecao(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarSecaoLateral/{id}")
	public ResponseEntity<SecaoLateralDTO> editarSecaoLateral(final @PathVariable("id") Long id,
													@RequestBody SecaoLateralDTO secaoLateralDTO) throws Exception {
		homeService.editarSecaoLateral(id, secaoLateralDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirSecaoLateral/{id}")
	public ResponseEntity<Void> excluirSecaoLateral(@PathVariable("id") Long id) {
		homeService.excluirSecaoLateral(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarSextaSecao/{id}")
	public ResponseEntity<SextaSecaoDTO> editarSextaSecao(final @PathVariable("id") Long id,
													@RequestBody SextaSecaoDTO sextaSecaoDTO) throws Exception {
		homeService.editarSextaSecao(id, sextaSecaoDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirSextaSecao/{id}")
	public ResponseEntity<Void> excluirSextaSecao(@PathVariable("id") Long id) {
		homeService.excluirSextaSecao(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarSetimaSecao/{id}")
	public ResponseEntity<SetimaSecaoDTO> editarSetimaSecao(final @PathVariable("id") Long id,
													@RequestBody SetimaSecaoDTO setimaSecaoDTO) throws Exception {
		homeService.editarSetimaSecao(id, setimaSecaoDTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirSetimaSecao/{id}")
	public ResponseEntity<Void> excluirSetimaSecao(@PathVariable("id") Long id) {
		homeService.excluirSetimaSecao(id);
		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@PutMapping(value = "/editarIndiceSecao/{tipo}/{id}/{indice}")
	public ResponseEntity<Long> editarIndiceSecao(final @PathVariable("id") Long id, final @PathVariable("tipo") String tipo, 
			final @PathVariable("indice") Long indice) throws Exception {
		Long idSecao = homeService.editarIndiceSecao(id, tipo, indice);

		return ResponseEntity.ok().body(idSecao);
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@GetMapping("/buscar")
	public ResponseEntity<List<HomeDTO>> buscarTodas(){
		List<HomeDTO> paginas = homeService.buscarTodas();
		return ResponseEntity.ok().body(paginas);
	}
	
	@GetMapping("/existePaginaHomeComLink/{link}")
	public ResponseEntity<HomeDTO> existePaginaHomeComLink(final @PathVariable("link") String link) {
		HomeDTO pagina = homeService.existePaginaHomeComLink(link);
		return ResponseEntity.ok().body(pagina);
	}
	
	@Secured({"ROLE_EDITAR_HOME"})
	@DeleteMapping("/excluirPaginaHome/{id}")
	public ResponseEntity<Void> excluirPaginaHome(@PathVariable("id") Long id) {
		homeService.excluirPaginaHome(id);
		return ResponseEntity.noContent().build();
	}
	
}

