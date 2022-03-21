package br.org.cidadessustentaveis.resources;

import br.org.cidadessustentaveis.dto.FiltroHistoricoShapeQueryDTO;
import br.org.cidadessustentaveis.dto.HistoricoShapeDTO;
import br.org.cidadessustentaveis.dto.HistoricoShapePaginacaoDTO;
import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoShape;
import br.org.cidadessustentaveis.services.HistoricoShapeService;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@RestController
@RequestMapping("/historicoShapes")
public class HistoricoShapeResource {

    @Autowired
    private HistoricoShapeService historicoShapeService;

    @Secured({"ROLE_VISUALIZAR_HISTORICO_SHAPES"})
    @GetMapping("/filtrar")
    public ResponseEntity<HistoricoShapePaginacaoDTO> filtrarHistorico(
                                        @RequestParam(name = "dataCriacao", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dataCriacao,
                                        @RequestParam(name = "dataEdicao", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dataEdicao,
                                        @RequestParam(name = "usuario", required = false) String usuario,
                                        @RequestParam(name = "nomeArquivo", required = false) String nomeArquivo,
                                        @RequestParam(name = "tema", required = false) Long tema,
                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer itemsPerPage) {
        FiltroHistoricoShapeQueryDTO historico = historicoShapeService.filtrarHistorico(dataCriacao, dataEdicao,
                                                                                        usuario, nomeArquivo, tema,
                                                                                        page, itemsPerPage);
        List<HistoricoShapeDTO> dtos = historico.getHistorico().stream()
                                                                    .map((h) -> new HistoricoShapeDTO(h))
                                                                .collect(Collectors.toList());

        HistoricoShapePaginacaoDTO dto = new HistoricoShapePaginacaoDTO(dtos, historico.getTotalCount());

        return ResponseEntity.ok(dto);
    }

    @Secured({"ROLE_VISUALIZAR_HISTORICO_SHAPES"})
    @GetMapping
    public ResponseEntity<HistoricoShapePaginacaoDTO> buscarHistorico(
                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer itemsPerPage,
                                        @RequestParam(name = "orderBy", defaultValue = "dataCriacao") String orderBy,
                                        @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        List<HistoricoShape> historico = historicoShapeService.buscarHistorico(page, itemsPerPage, orderBy, direction);
        List<HistoricoShapeDTO> dtos = historico.stream()
                                                    .map((h) -> new HistoricoShapeDTO(h))
                                                .collect(Collectors.toList());

        HistoricoShapePaginacaoDTO dto = new HistoricoShapePaginacaoDTO(dtos, historicoShapeService.count());

        return ResponseEntity.ok(dto);
    }

}
