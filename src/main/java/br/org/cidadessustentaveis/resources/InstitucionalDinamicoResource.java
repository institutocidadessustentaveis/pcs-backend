package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;

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
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoImagemDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao01DTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao02DTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao03DTO;
import br.org.cidadessustentaveis.dto.InstitucionalDinamicoSecao04DTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamico;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoImagem;
import br.org.cidadessustentaveis.services.InstitucionalDinamicoImagemService;
import br.org.cidadessustentaveis.services.InstitucionalDinamicoService;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/institucionalDinamico")
public class InstitucionalDinamicoResource {
		
	@Autowired
	private InstitucionalDinamicoService institucionalDinamicoService;
	
	@Autowired
	private InstitucionalDinamicoImagemService institucionalDinamicoImagemService;		
	
	
	//INSTITUCIONAL DINÂMICO
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@GetMapping("/buscarParaEdicao/{id}")
	public ResponseEntity<InstitucionalDinamicoDTO> buscarParaEdicao(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(institucionalDinamicoService.buscarParaEdicao(id));
	}
	
	
	@Secured({"ROLE_CADASTRAR_PAGINA_INSTITUCIONAL"})
	@PostMapping()
	public ResponseEntity<InstitucionalDinamicoDTO> inserir(@Valid @RequestBody InstitucionalDinamicoDTO institucionalDinamicoDTO) throws IOException {
		InstitucionalDinamico institucionalDinamico = institucionalDinamicoService.inserir(institucionalDinamicoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
																	.buildAndExpand(institucionalDinamico.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/{id}")
	public ResponseEntity<InstitucionalDinamicoDTO> alterar(final @PathVariable("id") Long id,
													@RequestBody InstitucionalDinamicoDTO institucionalDinamicoDTO) throws Exception {
		InstitucionalDinamico institucionalDinamico = institucionalDinamicoService.alterar(id, institucionalDinamicoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
												.path("/{id}").buildAndExpand(institucionalDinamico.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@GetMapping("/{id}")
	public ResponseEntity<InstitucionalDinamico> listarPorId(final @PathVariable("id") Long id) {
		return ResponseEntity.ok().body(institucionalDinamicoService.findById(id));
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping("/excluirInstitucionalDinamico/{id}")
	public ResponseEntity<Void> excluirInstitucionalDinamico(@PathVariable("id") Long id) {
		institucionalDinamicoService.excluirInstitucionalDinamico(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/buscar")
	public ResponseEntity<List<InstitucionalDinamicoDTO>> buscarTodas(){
		List<InstitucionalDinamicoDTO> paginas = institucionalDinamicoService.buscarTodas();
		return ResponseEntity.ok().body(paginas);
	}
	
	@GetMapping("/buscarInstitucionalDinamicoPorId/{id}")
	public ResponseEntity<InstitucionalDinamicoDTO> buscarInstitucionalDinamicoPorId(final @PathVariable("id") Long id) {
		InstitucionalDinamicoDTO institucionalDinamicoDTO = institucionalDinamicoService.buscarIdsPaginaInstitucionalDinamicoPorId(id);
		return ResponseEntity.ok().body(institucionalDinamicoDTO);
	}
	
	
	@GetMapping("/buscarInstitucionalDinamicoPorLink/{link}")
	public ResponseEntity<InstitucionalDinamicoDTO> buscarInstitucionalDinamicoPorLink(final @PathVariable("link") String link) {
		InstitucionalDinamicoDTO institucionalDinamico = institucionalDinamicoService.buscarInstitucionalDinamicoPorLink(link);
		return ResponseEntity.ok().body(institucionalDinamico);
	}
	

	@GetMapping("/buscarIdsInstitucionalDinamicoPorLink/{link}")
	public ResponseEntity<InstitucionalDinamicoDTO> buscarIdsInstitucionalDinamicoPorLink(final @PathVariable("link") String link) {
		InstitucionalDinamicoDTO institucionalDinamico = institucionalDinamicoService.buscarIdsInstitucionalDinamicoPorLink(link);
		return ResponseEntity.ok().body(institucionalDinamico);
	}
	
	@GetMapping("/existeInstitucionalDinamicoComLink/{link}")
	public ResponseEntity<InstitucionalDinamicoDTO> existeInstitucionalDinamicoComLink(final @PathVariable("link") String link) {
		InstitucionalDinamicoDTO pagina = institucionalDinamicoService.existePaginaInstitucionalComLink(link);
		return ResponseEntity.ok().body(pagina);
	}
	
	
	//ÍNDICES
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/editarIndiceSecao/{tipo}/{id}/{indice}")
	public ResponseEntity<Long> editarIndiceSecao(final @PathVariable("id") Long id, final @PathVariable("tipo") String tipo, 
			final @PathVariable("indice") Long indice) throws Exception {
		Long idSecao = institucionalDinamicoService.editarIndiceSecao(id, tipo, indice);

		return ResponseEntity.ok().body(idSecao);
	}
	
	
	//INSTITUCIONAL IMAGENS
	
    @GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> buscarImagem(final @PathVariable("id") Long id) throws NoSuchAlgorithmException {
    	InstitucionalDinamicoImagem imagem = institucionalDinamicoImagemService.find(id);			

        if(imagem == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(imagem.getBytes()));

        return new ResponseEntity<byte[]>(imagem.getBytes(), headers, HttpStatus.OK);
    }

	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/editarGaleria/{id}")
	public ResponseEntity<InstitucionalDinamicoImagemDTO> editarGaleria(final @PathVariable("id") Long id,
													@RequestBody InstitucionalDinamicoImagemDTO institucionalDinamicoImagemDTO) throws Exception {
		InstitucionalDinamicoImagemDTO imagem = institucionalDinamicoImagemService.editarGaleria(id, institucionalDinamicoImagemDTO);	
		return ResponseEntity.ok().body(imagem);
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping("/excluirImagem/{idImagem}")
	public ResponseEntity<Void> excluirImagem(@PathVariable("idImagem") Long idImagem) {
		institucionalDinamicoImagemService.excluirImagem(idImagem);
		return ResponseEntity.noContent().build();
	}
	
	
    //PRIMEIRA SEÇÃO

	@GetMapping("/buscarInstitucionalDinamicoSecao01PorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao01DTO>> buscarInstitucionalDinamicoSecao01PorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao01DTO> listaPrimeiraSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao01PorId(id);
		return ResponseEntity.ok().body(listaPrimeiraSecaoDTO);
	}
	
	@GetMapping("/buscarListaInstitucionalDinamicoSecao01ResumidaPorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao01DTO>> buscarListaInstitucionalDinamicoSecao01ResumidaPorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao01DTO> listaPrimeiraSecaoDTO = institucionalDinamicoService.buscarListaInstitucionalDinamicoSecao01ResumidaPorId(id);
		return ResponseEntity.ok().body(listaPrimeiraSecaoDTO);
	}
	
	@GetMapping("/buscarInstitucionalDinamicoSecao01Detalhe/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao01DTO> buscarInstitucionalDinamicoSecao01Detalhe(final @PathVariable("id") Long id) {
		InstitucionalDinamicoSecao01DTO primeiraSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao01Detalhe(id);
		return ResponseEntity.ok().body(primeiraSecaoDTO);
	}

	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/editarInstitucionalDinamicoSecao01/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao01DTO> editarInstitucionalDinamicoSecao01(final @PathVariable("id") Long id,
													@RequestBody InstitucionalDinamicoSecao01DTO institucionalDinamicoSecao01DTO) throws Exception {
		institucionalDinamicoService.editarInstitucionalDinamicoSecao01(id, institucionalDinamicoSecao01DTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping("/excluirInstitucionalDinamicoSecao01/{id}")
	public ResponseEntity<Void> excluirInstitucionalDinamicoSecao01(@PathVariable("id") Long id) {
		institucionalDinamicoService.excluirInstitucionalDinamicoSecao01(id);
		return ResponseEntity.noContent().build();
	}

	
	
    //SEGUNDA SEÇÃO

	@GetMapping("/buscarInstitucionalDinamicoSecao02PorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao02DTO>> buscarInstitucionalDinamicoSecao02PorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao02DTO> listaSegundaSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao02PorId(id);
		return ResponseEntity.ok().body(listaSegundaSecaoDTO);
	}
	
	@GetMapping("/buscarListaInstitucionalDinamicoSecao02ResumidaPorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao02DTO>> buscarListaInstitucionalDinamicoSecao02ResumidaPorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao02DTO> listaSegundaSecaoDTO = institucionalDinamicoService.buscarListaInstitucionalDinamicoSecao02ResumidaPorId(id);
		return ResponseEntity.ok().body(listaSegundaSecaoDTO);
	}
	
	@GetMapping("/buscarInstitucionalDinamicoSecao02Detalhe/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao02DTO> buscarInstitucionalDinamicoSecao02Detalhe(final @PathVariable("id") Long id) {
		InstitucionalDinamicoSecao02DTO segundaSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao02Detalhe(id);
		return ResponseEntity.ok().body(segundaSecaoDTO);
	}

	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/editarInstitucionalDinamicoSecao02/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao02DTO> editarInstitucionalDinamicoSecao02(final @PathVariable("id") Long id,
													@RequestBody InstitucionalDinamicoSecao02DTO institucionalDinamicoSecao02DTO) throws Exception {
		institucionalDinamicoService.editarInstitucionalDinamicoSecao02(id, institucionalDinamicoSecao02DTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping("/excluirInstitucionalDinamicoSecao02/{id}")
	public ResponseEntity<Void> excluirInstitucionalDinamicoSecao02(@PathVariable("id") Long id) {
		institucionalDinamicoService.excluirInstitucionalDinamicoSecao02(id);
		return ResponseEntity.noContent().build();
	}
	

	
	
    //TERCEIRA SEÇÃO

	@GetMapping("/buscarInstitucionalDinamicoSecao03PorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao03DTO>> buscarInstitucionalDinamicoSecao03PorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao03DTO> listaTerceiraSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao03PorId(id);
		return ResponseEntity.ok().body(listaTerceiraSecaoDTO);
	}
	
	@GetMapping("/buscarListaInstitucionalDinamicoSecao03ResumidaPorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao03DTO>> buscarListaInstitucionalDinamicoSecao03ResumidaPorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao03DTO> listaTerceiraSecaoDTO = institucionalDinamicoService.buscarListaInstitucionalDinamicoSecao03ResumidaPorId(id);
		return ResponseEntity.ok().body(listaTerceiraSecaoDTO);
	}
	
	@GetMapping("/buscarInstitucionalDinamicoSecao03Detalhe/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao03DTO> buscarInstitucionalDinamicoSecao03Detalhe(final @PathVariable("id") Long id) {
		InstitucionalDinamicoSecao03DTO terceiraSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao03Detalhe(id);
		return ResponseEntity.ok().body(terceiraSecaoDTO);
	}

	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/editarInstitucionalDinamicoSecao03/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao03DTO> editarInstitucionalDinamicoSecao03(final @PathVariable("id") Long id,
													@RequestBody InstitucionalDinamicoSecao03DTO institucionalDinamicoSecao03DTO) throws Exception {
		institucionalDinamicoService.editarInstitucionalDinamicoSecao03(id, institucionalDinamicoSecao03DTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping("/excluirInstitucionalDinamicoSecao03/{id}")
	public ResponseEntity<Void> excluirInstitucionalDinamicoSecao03(@PathVariable("id") Long id) {
		institucionalDinamicoService.excluirInstitucionalDinamicoSecao03(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
	//QUARTA SEÇÃO

	@GetMapping("/buscarInstitucionalDinamicoSecao04PorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao04DTO>> buscarInstitucionalDinamicoSecao04PorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao04DTO> listaQuartaSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao04PorId(id);
		return ResponseEntity.ok().body(listaQuartaSecaoDTO);	
	}
	
	@GetMapping("/buscarListaInstitucionalDinamicoSecao04ResumidaPorId/{id}")
	public ResponseEntity<List<InstitucionalDinamicoSecao04DTO>> buscarListaInstitucionalDinamicoSecao04ResumidaPorId(final @PathVariable("id") Long id) {
		List<InstitucionalDinamicoSecao04DTO> listaQuartaSecaoDTO = institucionalDinamicoService.buscarListaInstitucionalDinamicoSecao04ResumidaPorId(id);
		return ResponseEntity.ok().body(listaQuartaSecaoDTO);
	}
	
	@GetMapping("/buscarInstitucionalDinamicoSecao04Detalhe/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao04DTO> buscarInstitucionalDinamicoSecao04Detalhe(final @PathVariable("id") Long id) {
		InstitucionalDinamicoSecao04DTO quartaSecaoDTO = institucionalDinamicoService.buscarInstitucionalDinamicoSecao04Detalhe(id);
		return ResponseEntity.ok().body(quartaSecaoDTO);
	}

	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping(value = "/editarInstitucionalDinamicoSecao04/{id}")
	public ResponseEntity<InstitucionalDinamicoSecao04DTO> editarInstitucionalDinamicoSecao04(final @PathVariable("id") Long id,
													@RequestBody InstitucionalDinamicoSecao04DTO institucionalDinamicoSecao04DTO) throws Exception {
		institucionalDinamicoService.editarInstitucionalDinamicoSecao04(id, institucionalDinamicoSecao04DTO);

		return ResponseEntity.noContent().build();
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping("/excluirInstitucionalDinamicoSecao04/{id}")
	public ResponseEntity<Void> excluirInstitucionalDinamicoSecao04(@PathVariable("id") Long id) {
		institucionalDinamicoService.excluirInstitucionalDinamicoSecao04(id);
		return ResponseEntity.noContent().build();
	}
		


}

