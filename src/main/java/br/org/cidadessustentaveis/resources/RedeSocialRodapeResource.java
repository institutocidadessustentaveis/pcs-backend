package br.org.cidadessustentaveis.resources;

import br.org.cidadessustentaveis.dto.RedeSocialRodapeDTO;
import br.org.cidadessustentaveis.model.administracao.RedeSocialRodape;
import br.org.cidadessustentaveis.services.RedeSocialRodapeService;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@RestController
@RequestMapping("/redeSocialRodape")
public class RedeSocialRodapeResource {

    @Autowired
    private RedeSocialRodapeService redeSocialRodapeService;

    @GetMapping
    public ResponseEntity<List<RedeSocialRodapeDTO>> buscarRedesSociais() {
        List<RedeSocialRodape> redes = redeSocialRodapeService.buscarRedesSociais();
        List<RedeSocialRodapeDTO> dtos = redes.stream()
                                                    .map((r) -> new RedeSocialRodapeDTO(r))
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/cache")
    public ResponseEntity<List<RedeSocialRodapeDTO>> buscarRedesSociaisComCache() {
        List<RedeSocialRodape> redes = redeSocialRodapeService.buscarRedesSociais();
        List<RedeSocialRodapeDTO> dtos = redes.stream()
                                                    .map((r) -> new RedeSocialRodapeDTO(r))
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Secured({"ROLE_CADASTRAR_REDES_SOCIAIS_PCS"})
    @PutMapping
    public ResponseEntity<Void> salvarRedesSociais(@RequestBody List<RedeSocialRodapeDTO> redes) {
        redeSocialRodapeService.salvarRedesSociais(redes);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_EXCLUIR_REDES_SOCIAIS_PCS"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRedeSocial(@PathVariable("id") Long id) {
        redeSocialRodapeService.excluirRedeSocial(id);
        return ResponseEntity.ok().build();
    }

}
