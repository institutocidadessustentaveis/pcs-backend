package br.org.cidadessustentaveis.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.SeoDTO;
import br.org.cidadessustentaveis.model.institucional.Seo;
import br.org.cidadessustentaveis.repository.SeoRepository;
import br.org.cidadessustentaveis.services.SeoService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/seo")
public class SeoResource {

	@Autowired
	private SeoRepository seoRep;
	@Autowired
	private SeoService seoService;

	@GetMapping
	public SeoDTO get(){
		Seo seo = seoRep.getOne(1l);
		SeoDTO dto = new SeoDTO(seo.getId(),seo.getTitle(), seo.getDescription(), seo.getImage(), seo.getSlug(), seo.getSite(), seo.getUrl());
		return  dto;
	}

	@PostMapping
	public void post(@RequestBody String pagina){
		try {
			this.seoService.gerarPagina(pagina);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@DeleteMapping
	public void delete(){
		try {
			this.seoService.limpar();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


}
