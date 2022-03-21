package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.net.URI;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.InstitucionalDinamicoPublicacaoDTO;
import br.org.cidadessustentaveis.model.institucional.InstitucionalDinamicoPublicacao;
import br.org.cidadessustentaveis.services.InstitucionalDinamicoPublicacaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/institucionalDinamico/publicacao")
public class InstitucionalDinamicoPublicacaoResource {
	
	@Autowired
	private InstitucionalDinamicoPublicacaoService service;	
	
	@Secured({"ROLE_CADASTRAR_PAGINA_INSTITUCIONAL"})
	@PostMapping("/inserir")
	public ResponseEntity<Void> inserir(@RequestBody InstitucionalDinamicoPublicacaoDTO publicacaoDTO) throws IOException {	
		InstitucionalDinamicoPublicacao publicacao = service.inserir(publicacaoDTO);	
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(publicacao.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping("/editar/{id}")
	public ResponseEntity<InstitucionalDinamicoPublicacaoDTO> alterar(final @PathVariable("id") Long id, @RequestBody InstitucionalDinamicoPublicacaoDTO publicacaoDTO) throws Exception {
		InstitucionalDinamicoPublicacao publicacao = service.editar(publicacaoDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(publicacao.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}
	
	@Secured({"ROLE_EDITAR_PAGINA_INSTITUCIONAL"})
	@PutMapping("/ordem-exibicao/{id}/{ordem}")
	public ResponseEntity<InstitucionalDinamicoPublicacaoDTO> editarOrdemExibicao(final @PathVariable("id") Long id,final @PathVariable("ordem") Long ordem) throws Exception {
		InstitucionalDinamicoPublicacao publicacao = service.editarOrdemExibicao(id,ordem.intValue());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(publicacao.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}

	@Secured({"ROLE_DELETAR_PAGINA_INSTITUCIONAL"})
	@DeleteMapping(value = "/excluir/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}


	@GetMapping("/buscarPublicacaoPorId/{id}")
	public ResponseEntity<InstitucionalDinamicoPublicacaoDTO> buscarPorId(@RequestParam() Long id) {
		InstitucionalDinamicoPublicacaoDTO dto = service.buscarPublicacaoPorId(id);
		return ResponseEntity.ok(dto) ;
	}
	
	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagem(final @PathVariable("id") Long id) throws Exception {
		InstitucionalDinamicoPublicacao publicacao = service.buscarPorId(id);

        if(publicacao == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");

        return new ResponseEntity<byte[]>((publicacao.getImagem().getBytes()), headers, HttpStatus.OK);
    }


}
