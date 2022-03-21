package br.org.cidadessustentaveis.resources;

import br.org.cidadessustentaveis.dto.LinkRodapeDTO;
import br.org.cidadessustentaveis.model.administracao.LinkRodape;
import br.org.cidadessustentaveis.services.LinkRodapeService;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@RestController
@RequestMapping("/linksRodape")
public class LinkRodapeResource {

    @Autowired
    private LinkRodapeService linkRodapeService;

    @GetMapping
    public ResponseEntity<List<LinkRodapeDTO>> buscarLinksOdernados() {
        List<LinkRodape> links = linkRodapeService.buscarLinksOdernados();
        List<LinkRodapeDTO> dtos = links.stream()
                                            .map((l) ->  new LinkRodapeDTO(l))
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/cache")
    public ResponseEntity<List<LinkRodapeDTO>> buscarLinksOdernadosComCache() {
        List<LinkRodape> links = linkRodapeService.buscarLinksOdernadosComCache();
        List<LinkRodapeDTO> dtos = links.stream()
                                            .map((l) ->  new LinkRodapeDTO(l))
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Secured({"ROLE_CADASTRAR_LINK_RODAPE"})
    @PutMapping
    public ResponseEntity<Void> atualizarListaLinks(@RequestBody List<LinkRodapeDTO> links) {
        linkRodapeService.atualizarLinks(links);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_APAGAR_LINK_RODAPE"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarLink(@PathVariable("id") Long id) {
        linkRodapeService.removerLink(id);
        return ResponseEntity.ok().build();
    }

}
