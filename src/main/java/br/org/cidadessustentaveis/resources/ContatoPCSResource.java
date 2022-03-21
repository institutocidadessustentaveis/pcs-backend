package br.org.cidadessustentaveis.resources;

import br.org.cidadessustentaveis.dto.ContatoPcsDTO;
import br.org.cidadessustentaveis.model.administracao.ContatoPCS;
import br.org.cidadessustentaveis.services.ContatoPCSService;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@ApiIgnore
@Controller
@RequestMapping("/contatoPcs")
public class ContatoPCSResource {

    @Autowired
    private ContatoPCSService contatoPCSService;

    @Secured({"ROLE_CADASTRAR_CONTATO_PCS"})
    @PutMapping
    public ResponseEntity<Void> salvar(@Valid @RequestBody ContatoPcsDTO contato) {
       contatoPCSService.save(contato);
       return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ContatoPcsDTO> buscarContatoMaisRecente() {
        ContatoPCS contato = contatoPCSService.buscarContatoMaisRecente();
        ContatoPcsDTO dto  = new ContatoPcsDTO(contato);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cache")
    public ResponseEntity<ContatoPcsDTO> buscarContatoMaisRecenteComCache() {
        ContatoPCS contato = contatoPCSService.buscarContatoMaisRecenteComCache();
        ContatoPcsDTO dto  = new ContatoPcsDTO(contato);
        return ResponseEntity.ok(dto);
    }

}