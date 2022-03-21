package br.org.cidadessustentaveis.resources;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.model.institucional.Arquivo;
import br.org.cidadessustentaveis.services.ArquivoService;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/arquivo")
public class ArquivoResource {

	@Autowired
	private ArquivoService arquivoService;
	
	
	@GetMapping(value = "imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagem(final @PathVariable("id") Long id) throws Exception {
        Arquivo imagem = arquivoService.buscarPorId(id);

        if(imagem == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(imagem.getConteudo()));

        return new ResponseEntity<byte[]>(Base64.decode(imagem.getConteudo().getBytes()), headers, HttpStatus.OK);
    }
	
}
