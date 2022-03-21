package br.org.cidadessustentaveis.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.cidadessustentaveis.dto.BoaPraticaItemDTO;
import br.org.cidadessustentaveis.dto.InstitucionalDTO;
import br.org.cidadessustentaveis.dto.NoticiaDTO;
import br.org.cidadessustentaveis.dto.PaginaInicialBannerDTO;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.institucional.Institucional;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.services.BoaPraticaService;
import br.org.cidadessustentaveis.services.InstitucionalService;
import br.org.cidadessustentaveis.services.NoticiaService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/pagina-inicial")
@Validated
public class PaginaInicialResource {
	
	@Autowired
	private NoticiaService noticiaService;
	
	@Autowired
	private BoaPraticaService boaPraticaService;
	
	@Autowired
	private InstitucionalService institucionalService;


	@GetMapping("/bannerPrincipal")
	public ResponseEntity<List<PaginaInicialBannerDTO>> buscarBannerPrincipal() {
		
		List<PaginaInicialBannerDTO> listaBannerDTO = new ArrayList<PaginaInicialBannerDTO>();
		
		List<Noticia> listaNoticia = noticiaService.carregarNoticiaBannerPaginaInicial(1);
		List<NoticiaDTO> listaNoticiaDto = listaNoticia.stream().map(obj -> new NoticiaDTO(obj.getId(),obj.getTitulo(),obj.getSubtitulo(), obj.getUrl(),"")).collect(Collectors.toList());
		for(NoticiaDTO nDTO :listaNoticiaDto) {
			PaginaInicialBannerDTO p = new PaginaInicialBannerDTO();
			p.setIdNoticia(nDTO.getId());
			p.setTitulo(nDTO.getTitulo());
			p.setSubtitulo(nDTO.getSubtitulo());
			listaBannerDTO.add(p);
		}
		
		List<BoaPratica> listaBoaPratica = boaPraticaService.ultimasBoasPraticas(1);
		List<BoaPraticaItemDTO> listaBoaPraticaItemDTO = listaBoaPratica.stream().map(obj -> new BoaPraticaItemDTO(obj)).collect(Collectors.toList());
		for(BoaPraticaItemDTO bDTO :listaBoaPraticaItemDTO) {
			PaginaInicialBannerDTO p = new PaginaInicialBannerDTO();
			p.setIdBoaPratica(bDTO.getIdBoaPratica());
			p.setTitulo(bDTO.getTitulo());
			p.setSubtitulo(bDTO.getSubtitulo());
			listaBannerDTO.add(p);
		}
		
		List<Institucional> listaInstitucional = institucionalService.ultimasInstitucional(1);
		List<InstitucionalDTO> listaInstitucionalDTO = listaInstitucional.stream().map(obj -> new InstitucionalDTO(obj.getId(),obj.getTitulo() ,
				obj.getSubtitulo(),obj.getLink_pagina())).collect(Collectors.toList());
		for(InstitucionalDTO iDTO :listaInstitucionalDTO) {
			PaginaInicialBannerDTO p = new PaginaInicialBannerDTO();
			p.setIdInstitucional(iDTO.getId());
			p.setTitulo(iDTO.getTitulo());
			p.setSubtitulo(iDTO.getSubtitulo());
			p.setLinkInstitucional(iDTO.getLink_pagina());
			listaBannerDTO.add(p);
		}
	
		return ResponseEntity.ok().body(listaBannerDTO);
	}

}
