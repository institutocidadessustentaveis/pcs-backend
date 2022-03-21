package br.org.cidadessustentaveis.resources;

import br.org.cidadessustentaveis.dto.*;
import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoUsoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TipoUsoShape;
import br.org.cidadessustentaveis.services.HistoricoUsoShapeService;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@RestController
@RequestMapping("/historicoUsoShape")
public class HistoricoUsoShapeResource {

    @Autowired
    private HistoricoUsoShapeService historicoUsoShapeService;

    @Secured({"ROLE_VISUALIZAR_HISTORICO_SHAPES"})
    @GetMapping("/filtrar")
    public ResponseEntity<HistoricoUsoShapePaginacaoDTO> filtrarHistorico(
                                        @RequestParam(name = "dataHoraAcesso", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dataHoraAcesso,
                                        @RequestParam(name = "cidade", required = false) String cidade,
                                        @RequestParam(name = "usuario", required = false) String usuario,
                                        @RequestParam(name = "titulo", required = false) String titulo,
                                        @RequestParam(name = "tipo", required = false) TipoUsoShape tipo,
                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer itemsPerPage,
                                        @RequestParam(name = "orderBy", defaultValue = "dataHoraAcesso") String orderBy,
                                        @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        FiltroHistoricoUsoShapeQueryDTO historico =
                                historicoUsoShapeService.filtrarHistorico(dataHoraAcesso, cidade, usuario, titulo, tipo,
                                                                            page, itemsPerPage, orderBy, direction);
        List<HistoricoUsoShapeDTO> dtos = historico.getHistorico().stream()
                                                                        .map((h) -> new HistoricoUsoShapeDTO(h))
                                                                    .collect(Collectors.toList());
        HistoricoUsoShapePaginacaoDTO dto = new HistoricoUsoShapePaginacaoDTO(dtos, historico.getTotalCount());
        return ResponseEntity.ok(dto);
    }

    @Secured({"ROLE_VISUALIZAR_HISTORICO_SHAPES"})
    @GetMapping
    public ResponseEntity<HistoricoUsoShapePaginacaoDTO> buscarHistorico(
                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer itemsPerPage,
                                        @RequestParam(name = "orderBy", defaultValue = "dataHoraAcesso") String orderBy,
                                        @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        Page<HistoricoUsoShape> historico = historicoUsoShapeService.buscarHistorico(page, itemsPerPage,
                                                                                        orderBy, direction);
        List<HistoricoUsoShapeDTO> dtos = historico.stream()
                                                        .map((h) -> new HistoricoUsoShapeDTO(h))
                                                    .collect(Collectors.toList());
        return ResponseEntity.ok(new HistoricoUsoShapePaginacaoDTO(dtos, historicoUsoShapeService.count()));
    }

}
