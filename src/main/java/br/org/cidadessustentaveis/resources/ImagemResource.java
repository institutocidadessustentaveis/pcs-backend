package br.org.cidadessustentaveis.resources;

import br.org.cidadessustentaveis.dto.ImageUploadResponseDTO;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import br.org.cidadessustentaveis.services.ImagemService;
import br.org.cidadessustentaveis.services.UsuarioService;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@ApiIgnore
@RestController
@RequestMapping("/imagens")
public class ImagemResource {

    @Autowired
    private ImagemService imagemService;

    @Autowired
    private UsuarioService usuarioService;

    @Secured({"ROLE_CADASTRAR_NOTICIA", "ROLE_CADASTRAR_SOLICITACAO_BOA_PRATICA"})
    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponseDTO> salvarImagem(@RequestParam MultipartFile file,
                                                                            Authentication auth) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if(ImageUtils.guessImageFormat(file.getBytes()).equalsIgnoreCase("GIF")) {
            throw new DataIntegrityException("Formato de imagem GIF não suportado");
        }

        byte[] imageBytes = ImageUtils.compressImage(file.getBytes());
        Imagem imagem = imagemService.save(imageBytes);

        return ResponseEntity.ok(new ImageUploadResponseDTO(imagem.getId(), "imagens/" + imagem.getId()));
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> buscarImagem(final @PathVariable("id") Long id) throws NoSuchAlgorithmException {
        Imagem imagem = imagemService.find(id);

        if(imagem == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(imagem.getBytes()));

        return new ResponseEntity<byte[]>(imagem.getBytes(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarImagem(final @PathVariable("id") Long id, Authentication auth) {
        Imagem imagem = imagemService.find(id);

        if(imagem == null) {
            return ResponseEntity.notFound().build();
        }

        imagemService.delete(id);

        return ResponseEntity.ok().build();
    }

}
