package br.org.cidadessustentaveis.resources;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.cidadessustentaveis.services.ImportacaoAcademicosService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin	
@RestController
@RequestMapping("/indicadores/importacaoAcademicos")
public class ImportacaoAcademicosResource {
	@Autowired
	private ImportacaoAcademicosService importacaoAcademicosService;

	@Secured({"ROLE_CADASTRAR_GRUPO_ACADEMICO"})
	@PostMapping("")
    public List<String> handleFileUpload(@RequestParam("file") MultipartFile file ,Principal principal) throws Exception {
		List<String> listaDados = importacaoAcademicosService.importar(file);
		return listaDados;
    }
	
	
 }
