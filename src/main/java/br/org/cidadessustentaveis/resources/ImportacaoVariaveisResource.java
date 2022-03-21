package br.org.cidadessustentaveis.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.ByteSource;

import br.org.cidadessustentaveis.dto.DadosDownloadDTO;
import br.org.cidadessustentaveis.dto.Message;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.services.DownloadsExportacoesService;
import br.org.cidadessustentaveis.services.ImportacaoVariaveisService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/indicadores/importacaoVariaveis")
public class ImportacaoVariaveisResource {
	@Autowired
	private ImportacaoVariaveisService importacaoVariaveisResource;
	@Autowired
	private DownloadsExportacoesService downloadsExportacoesService;
	@Secured({"ROLE_PREENCHER_INDICADOR"})
	@PostMapping("")
    public List<String> handleFileUpload(@RequestParam("file") MultipartFile file ,Principal principal) throws Exception {
		List<String> listaDados = importacaoVariaveisResource.importar(file);
		/*Message msg = new Message();
		msg.setMensagem(erros);*/
		return listaDados;
    }
	
	@Secured({"ROLE_PREENCHER_INDICADOR"})
	@GetMapping("/download")
	public ResponseEntity<Resource> download(HttpServletRequest request, Principal principal) throws IOException, AuthenticationException{

		File file = importacaoVariaveisResource.gerarArquivo();
		byte[] fileContent = Files.readAllBytes(file.toPath());
		file.delete();
		InputStream targetStream = ByteSource.wrap(fileContent).openStream();
		InputStreamResource resource = new InputStreamResource(targetStream);		
        downloadsExportacoesService.gravarLog(principal.getName(), "tabela_de_variaveis.xlsx");
		return 	ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"tabela_de_variaveis.xlsx\"")
				.body(resource);
		
	}
	
 }
