package br.org.cidadessustentaveis.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.org.cidadessustentaveis.dto.HistoricoCompartilhamentoDTO;
import br.org.cidadessustentaveis.services.RelatorioConteudoCompartilhadoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/historicoCompartilhamento")
public class HistoricoCompartilhamentoResource {

    @Autowired
    private RelatorioConteudoCompartilhadoService serviceConteudoCompartilhado;

    @PostMapping
    public ResponseEntity<Void> salvarHistoricoCompartilhamento(
                                                        @RequestBody HistoricoCompartilhamentoDTO historicoDto) {
        serviceConteudoCompartilhado.insert(historicoDto.toEntity());
        return ResponseEntity.status(201).build();
    }

}
