package br.org.cidadessustentaveis.resources;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.HistoricoOperacaoBuscaDTO;
import br.org.cidadessustentaveis.dto.HistoricoOperacaoPaginacaoDTO;
import br.org.cidadessustentaveis.dto.ModuloHistoricoDTO;
import br.org.cidadessustentaveis.model.enums.Modulo;
import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;
import br.org.cidadessustentaveis.services.HistoricoOperacaoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController()
@RequestMapping("/historicoOperacao")
public class HistoricoOperacaoResource {

    @Autowired
    private HistoricoOperacaoService historicoOperacaoService;

    @Secured({"ROLE_VISUALIZAR_HISTORICO_OPERACAO"})
    @GetMapping()
    public ResponseEntity<HistoricoOperacaoPaginacaoDTO> carregarHistoricoOperacao(
                                    @RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "10") int quantity,
                                    @RequestParam(name = "orderBy", defaultValue = "data") String orderBy,
                                    @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        List<HistoricoOperacao> registros = historicoOperacaoService.carregarHistoricoOperacao(page, quantity,
                                                                                                orderBy, direction);
        List<HistoricoOperacaoBuscaDTO> registrosDto = registros.stream()
                                                                    .map(r -> new HistoricoOperacaoBuscaDTO(r))
                                                                .collect(Collectors.toList());

        HistoricoOperacaoPaginacaoDTO dto = new HistoricoOperacaoPaginacaoDTO();
        dto.setHistorico(registrosDto);
        dto.setTotal(historicoOperacaoService.countHistoricoOperacao());

        return ResponseEntity.ok(dto);
    }

    @Secured({"ROLE_VISUALIZAR_HISTORICO_OPERACAO"})
    @GetMapping("/filtrar")
    public ResponseEntity<HistoricoOperacaoPaginacaoDTO> filtrarHistoricoOperacao(
                                            @RequestParam(required = false, defaultValue = "") String usuario,
                                            @RequestParam(required = false, defaultValue = "") Modulo modulo,
                                            @RequestParam(required = false, defaultValue = "")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dataInicio,
                                            @RequestParam(required = false, defaultValue = "")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dataFinal,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int quantity) {
        List<HistoricoOperacao> registros = historicoOperacaoService.filtrarHistoricoOperacao(usuario, modulo,
                                                                                                dataInicio, dataFinal,
                                                                                                page, quantity);
        List<HistoricoOperacaoBuscaDTO> registrosDto = registros.stream()
                                                                    .map(r -> new HistoricoOperacaoBuscaDTO(r))
                                                                .collect(Collectors.toList());

        HistoricoOperacaoPaginacaoDTO dto = new HistoricoOperacaoPaginacaoDTO();
        dto.setHistorico(registrosDto);
        dto.setTotal(historicoOperacaoService.countFiltrarHistoricoOperacao(usuario, modulo,
                                                                            dataInicio, dataFinal));

        return ResponseEntity.ok(dto);
    }
    
    @Secured({"ROLE_VISUALIZAR_HISTORICO_OPERACAO"})
    @GetMapping("/filtrarSemPaginacao")
    public ResponseEntity<HistoricoOperacaoPaginacaoDTO> filtrarHistoricoOperacaoSemPaginacao(
                                            @RequestParam(required = false, defaultValue = "") String usuario,
                                            @RequestParam(required = false, defaultValue = "") Modulo modulo,
                                            @RequestParam(required = false, defaultValue = "")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dataInicio,
                                            @RequestParam(required = false, defaultValue = "")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dataFinal,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int quantity) {
        List<HistoricoOperacao> registros = historicoOperacaoService.filtrarHistoricoOperacaoSemPaginacao(usuario, modulo,
                                                                                                dataInicio, dataFinal,
                                                                                                page, quantity);
        List<HistoricoOperacaoBuscaDTO> registrosDto = registros.stream()
                                                                    .map(r -> new HistoricoOperacaoBuscaDTO(r))
                                                                .collect(Collectors.toList());

        HistoricoOperacaoPaginacaoDTO dto = new HistoricoOperacaoPaginacaoDTO();
        dto.setHistorico(registrosDto);
        dto.setTotal(historicoOperacaoService.countFiltrarHistoricoOperacao(usuario, modulo,
                                                                            dataInicio, dataFinal));

        return ResponseEntity.ok(dto);
    }

    @Secured({"ROLE_VISUALIZAR_HISTORICO_OPERACAO"})
    @GetMapping("/modulos")
    public ResponseEntity<List<ModuloHistoricoDTO>> listarModulos() {
        return ResponseEntity.ok(historicoOperacaoService.listarModulosHistoricoOperacao());
    }

}
