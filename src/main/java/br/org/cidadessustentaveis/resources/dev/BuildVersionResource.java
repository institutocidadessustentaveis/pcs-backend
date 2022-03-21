package br.org.cidadessustentaveis.resources.dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.BuildVersionDTO;
import br.org.cidadessustentaveis.model.dev.VersaoBuild;
import br.org.cidadessustentaveis.model.enums.TipoBuild;
import br.org.cidadessustentaveis.services.dev.VersaoBuildService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin()
@RestController
@RequestMapping("/build")
public class BuildVersionResource {
	
	@Autowired
	private VersaoBuildService buildService;

	@GetMapping("/backend")
	public ResponseEntity<BuildVersionDTO> buscar(){
		String versao = "";
		VersaoBuild versaoBuild = buildService.versaoAtual(TipoBuild.BACKEND);
		if( versaoBuild == null ) {
			versao = "-";
		}else {
			versao = versaoBuild.getVersao();
		}
		BuildVersionDTO dto = new BuildVersionDTO();
		dto.setVersao(versao);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/frontend")
	public ResponseEntity<BuildVersionDTO> buscarFrontend(){
		String versao = "";
		VersaoBuild versaoBuild = buildService.versaoAtual(TipoBuild.FRONTEND);
		if( versaoBuild == null ) {
			versao = "-";
		}else {
			versao = versaoBuild.getVersao();
		}
		BuildVersionDTO dto = new BuildVersionDTO();
		dto.setVersao(versao);
		return ResponseEntity.ok().body(dto);
	}
}
