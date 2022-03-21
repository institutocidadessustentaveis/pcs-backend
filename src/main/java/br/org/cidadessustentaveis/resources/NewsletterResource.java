package br.org.cidadessustentaveis.resources;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.xmlbeans.impl.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.BoletimTemplate01DTO;
import br.org.cidadessustentaveis.dto.BoletimTemplate01ToListDTO;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.institucional.Newsletter;
import br.org.cidadessustentaveis.model.noticias.BoletimTemplate01;
import br.org.cidadessustentaveis.services.ArquivoService;
import br.org.cidadessustentaveis.services.BoletimInformativoService;
import br.org.cidadessustentaveis.services.NewsletterService;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/newsletter")
public class NewsletterResource {

    @Autowired
    private NewsletterService newsletterService;
    
    @Autowired
    private BoletimInformativoService boletimService;
    
    @Autowired
    private ArquivoService arquivoService;
    

    @PostMapping
    public ResponseEntity<Void> assinar(@RequestParam String email) {
        if (!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                                Pattern.CASE_INSENSITIVE).matcher(email).find()) {
            return ResponseEntity.badRequest().build();
        }

        if(newsletterService.exists(email)) {
            return ResponseEntity.ok().build();
        }

        newsletterService.save(new Newsletter(email, new Date(), true));

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("buscarBoletinsTemplate01")
    public List<BoletimTemplate01ToListDTO> buscarBonetinsTemplate01() {
    	List<BoletimTemplate01ToListDTO> template01List = boletimService.buscarBoletinsTemplate01();
    	return template01List;
    }
    
    @GetMapping("buscarBoletimTempate01PorId/{id}")
    public ResponseEntity<BoletimTemplate01DTO> buscarBoletimPorId(@PathVariable Long id) {
    	BoletimTemplate01 boletim = boletimService.buscarBoletimTemplate01PorId(id);
    	return ResponseEntity.ok(new BoletimTemplate01DTO(boletim));
    }
    
    @Secured({"ROLE_ENVIAR_BOLETIM", "ROLE_CADASTRAR_BOLETIM"})
    @PostMapping("/salvarBoletimEnviarTemplate01")
    public ResponseEntity<Void> salvarEnviarBoletimTemplate01(@RequestBody BoletimTemplate01DTO boletim) throws Exception {
        newsletterService.salvarEnviarBoletimTemplate01(boletim);
        return ResponseEntity.status(202).build();
    }
    
    @Secured({"ROLE_ENVIAR_BOLETIM", "ROLE_CADASTRAR_BOLETIM"})
    @PostMapping("/salvarBoletimEnviarTesteTemplate01/{emailTeste}")
    public ResponseEntity<Void> salvarEnviarTesteBoletimTemplate01(@RequestBody BoletimTemplate01DTO boletim, @PathVariable String emailTeste) throws Exception {
        newsletterService.salvarEnviarTesteBoletimTemplate01(boletim, emailTeste);
        return ResponseEntity.status(202).build();
    }
    
    @Secured({"ROLE_ENVIAR_BOLETIM"})
    @PostMapping("/enviarBoletimTemplate01")
    public ResponseEntity<Void> enviarBoletimTemplate01(@RequestBody BoletimTemplate01DTO boletim) throws Exception {
        newsletterService.enviarTemplate01(boletim, (Long)null);
        return ResponseEntity.status(202).build();
    }
    
    @Secured({"ROLE_ENVIAR_BOLETIM"})
    @PostMapping("/enviarBoletimTemplate01PorId")
    public ResponseEntity<Void> enviarBoletimTemplate01(@RequestBody Long id) throws Exception {
        newsletterService.enviarTemplate01PorId(id);
        return ResponseEntity.status(202).build();
    }
    
    @Secured({"ROLE_CADASTRAR_BOLETIM"})
    @PostMapping("/salvarBoletimTemplate01")
    public ResponseEntity<Long> salvarBoletimTemplate01(@RequestBody BoletimTemplate01DTO boletim) throws Exception {
       
        return ResponseEntity.ok().body(newsletterService.salvarTemplate01(boletim));
    }
    
    @Secured({"ROLE_EDITAR_BOLETIM"})
    @PostMapping("/editarBoletimTemplate01")
    public ResponseEntity<Void> editarBoletimTemplate01(@RequestBody BoletimTemplate01DTO boletim) throws Exception {
        newsletterService.editarBoletimTemplate01(boletim);
        return ResponseEntity.status(202).build();
    }
    
	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagem(final @PathVariable("id") Long idImagem) throws Exception {


        if(idImagem == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(arquivoService.buscarPorId(idImagem).getConteudo()));

        return new ResponseEntity<byte[]>(Base64.decode(arquivoService.buscarPorId(idImagem).getConteudo().getBytes()), headers, HttpStatus.OK);
	}
    @Secured({"ROLE_EXCLUIR_BOLETIM"})
    @DeleteMapping("/deletar/{id}") 
    public ResponseEntity<Void> deletarBoletim(@PathVariable Long id) {
    	boletimService.deletarBoletim(id);
    	return ResponseEntity.status(202).build();
    }

}
