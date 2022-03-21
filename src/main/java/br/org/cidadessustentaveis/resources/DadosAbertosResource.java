package br.org.cidadessustentaveis.resources;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.services.DadosAbertosService;
import br.org.cidadessustentaveis.services.PrefeituraService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/dados-abertos")
public class DadosAbertosResource {

    @Autowired
    private DadosAbertosService dadosAbertosService;

    @Autowired
    private PrefeituraService prefeituraService;

    @GetMapping(value = "/indicadores{extension}", produces = { "application/json",
                                                                "application/xml",
                                                                "text/csv",
                                                                "application/vnd.ms-excel" })
    public @ResponseBody ResponseEntity<byte[]> buscarIndicadoresDadosAbertos(
                                                    @PathVariable("extension") String extension,
                                                    @RequestParam(value = "idCidade",
                                                                    defaultValue = "-1") Long idCidade,
                                                    @RequestParam(value = "idIndicador",
                                                                    defaultValue = "-1") Long idIndicador,
                                                                                        HttpServletResponse response)
                                                                                throws IOException,
                                                                                        ParserConfigurationException,
                                                                                        TransformerException {
        if(!this.validateDadosAbertosExtension(extension)) {
            return ResponseEntity.badRequest().build();
        }

        this.setFileHeaders(response, "indicadores", extension);

        byte[] content = dadosAbertosService.getIndicadoresFileContent(idCidade, idIndicador, extension);
        return ResponseEntity.ok(content);
    }

    @GetMapping(value = "/variaveis{extension}", produces = { "application/json",
                                                                "application/xml",
                                                                "text/csv",
                                                                "application/vnd.ms-excel" })
    public @ResponseBody ResponseEntity<byte[]> buscarVariaveisDadosAbertos(
                                                    @PathVariable("extension") String extension,
                                                    @RequestParam(value = "idCidade",
                                                                    defaultValue = "-1") Long idCidade,
                                                    @RequestParam(value = "idIndicador",
                                                                    defaultValue = "-1") Long idIndicador,
                                                                                        HttpServletResponse response)
                                                                                throws IOException,
                                                                                        ParserConfigurationException,
                                                                                        TransformerException {
        if(!this.validateDadosAbertosExtension(extension)) {
            return ResponseEntity.badRequest().build();
        }


        this.setFileHeaders(response, "variaveis", extension);

        byte[] content = dadosAbertosService.getVariaveisFileContent(idCidade, idIndicador, extension);
        return ResponseEntity.ok(content);
    }

    @GetMapping(value = "/cidades.xlsx", produces = { "application/vnd.ms-excel" })
    public @ResponseBody ResponseEntity<byte[]> baixarCidadesSignatarias(HttpServletResponse response)
                                                                                                throws IOException {
        this.setFileHeaders(response, "cidades", ".xlsx");

        byte[] arquivo = prefeituraService.gerarArquivoExcelCidadesSignatarias();
        return ResponseEntity.ok(arquivo);
    }

    private boolean validateDadosAbertosExtension(String extension) {
        if(extension == null || extension.isEmpty()) return false;

        List<String> extensions = Arrays.asList(".xls", ".csv", ".xml", ".json");

        if(extensions.contains(extension.toLowerCase())) return true;

        return false;
    }

    private void setFileHeaders(HttpServletResponse response, String filename, String extension) {
        response.setCharacterEncoding("UTF-8");

        if(extension.equalsIgnoreCase(".xlsx")) {
            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + ".xlsx\"");
            response.setContentType("application/vnd.ms-excel");
        }

        if(extension.equalsIgnoreCase(".xls")) {
            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + ".xls\"");
            response.setContentType("application/vnd.ms-excel");
        }

        if(extension.equalsIgnoreCase(".csv")) {
            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + ".csv\"");
            response.setContentType("text/csv");
        }

        if(extension.equalsIgnoreCase(".xml")) {
            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + ".xml\"");
            response.setContentType("application/xml");
        }

        if(extension.equalsIgnoreCase(".json")) {
            response.setHeader("Content-disposition", "attachment; filename=\"" + filename + ".json\"");
            response.setContentType("application/json");
        }
    }

}
