package br.org.cidadessustentaveis.resources;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.xmlbeans.impl.util.Base64;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.cidadessustentaveis.dto.CombosFiltrarNoticiasDTO;
import br.org.cidadessustentaveis.dto.FiltroNoticias;
import br.org.cidadessustentaveis.dto.IdNoticiasDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.NoticiaBoletimDTO;
import br.org.cidadessustentaveis.dto.NoticiaDTO;
import br.org.cidadessustentaveis.dto.NoticiaDetalheDTO;
import br.org.cidadessustentaveis.dto.NoticiaItemDTO;
import br.org.cidadessustentaveis.dto.NoticiasFiltradasDTO;
import br.org.cidadessustentaveis.dto.UltimasNoticiaDTO;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.services.HistoricoAcessoNoticiaService;
import br.org.cidadessustentaveis.services.NoticiaService;
import br.org.cidadessustentaveis.util.ImageUtils;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
@RequestMapping("/noticia")
@Validated
public class NoticiaResource {
	
	@Autowired
	private NoticiaService noticiaService;

	@Autowired
	private HistoricoAcessoNoticiaService historicoAcessoNoticiaService;

	@GetMapping()
	public ResponseEntity<List<NoticiaDTO>> listar() {
		List<Noticia> listaNoticia = noticiaService.listar();
		List<NoticiaDTO> listaNoticiaDto = listaNoticia.stream()
															.map(obj -> new NoticiaDTO(obj))
														.collect(Collectors.toList());
		listaNoticiaDto.forEach(n -> n.setImagemPrincipal(""));

		return ResponseEntity.ok().body(listaNoticiaDto);
	}
	
	
	@GetMapping("/paginacao")
	public ResponseEntity<NoticiasFiltradasDTO> listar(
										@RequestParam(required = false) Integer page,
									   @RequestParam(required = false) Integer itemsPerPage,
									   @RequestParam(name = "orderBy", defaultValue = "dataHoraCriacao") String orderBy,
									   @RequestParam(name = "direction", defaultValue = "DESC") String direction) {

		List<Noticia> listaNoticias = new LinkedList<>();

		if ((page != null && itemsPerPage != null) && (page >= 0 && itemsPerPage > 0)) {
			listaNoticias = noticiaService.listarComPaginacao(page, itemsPerPage, orderBy, direction).getContent();
		} else {
			listaNoticias = noticiaService.listar();
		}

		List<NoticiaItemDTO> listaCidadesDto = listaNoticias.stream()
																.map(obj -> new NoticiaItemDTO(obj))
															.collect(Collectors.toList());
		
		NoticiasFiltradasDTO noticiasFiltradasDTO = new NoticiasFiltradasDTO();
		noticiasFiltradasDTO.setListaNoticias(listaCidadesDto);
		noticiasFiltradasDTO.setCountTotalNoticias(noticiaService.contar());
		return ResponseEntity.ok().body(noticiasFiltradasDTO);
	}


	@GetMapping("/{id}")
	public ResponseEntity<NoticiaDTO> listarPorId(final @PathVariable("id") Long id) {
		NoticiaDTO dto = new NoticiaDTO(noticiaService.buscarPorId(id));
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<NoticiaDTO> listarPorTitulo(final @PathVariable("titulo") String titulo) throws Exception {
		Noticia noticia = noticiaService.buscarPorTitulo(URLDecoder.decode(titulo, StandardCharsets.UTF_8.toString()));
		
		if(noticia == null) {
			throw new Exception("Notícia não encontrada!");
		}

		NoticiaDTO dto = new NoticiaDTO(noticia);
		dto.setImagemPrincipal("");

		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<NoticiaDTO> buscarPorId(final @PathVariable("id") Long id) throws Exception {
		Noticia noticia = noticiaService.buscarPorId(id);

		if(noticia == null) {
			throw new Exception("Notícia não encontrada!");
		}

		NoticiaDTO dto = new NoticiaDTO(noticia);
		dto.setImagemPrincipal("");

		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/idPublicada/{id}")
	public ResponseEntity<NoticiaDetalheDTO> buscarPorIdPublicada(@PathVariable("id") Long id,
																  @RequestHeader(value = "Referer",
																		  			required = false) String referer)
																									throws Exception {
		Noticia noticia = noticiaService.buscarPorIdPublicada(id);
		historicoAcessoNoticiaService.registrarAcesso(noticia, referer);
		return ResponseEntity.ok().body(new NoticiaDetalheDTO(noticia));
	}
	
	@GetMapping("/ultimasNoticias/{qtd}")
	public ResponseEntity<List<UltimasNoticiaDTO>> ultimasNoticias(final @PathVariable("qtd") int qtd) {
		List<Noticia> listaNoticia = noticiaService.ultimasNoticias(qtd);
		List<UltimasNoticiaDTO> listaNoticiaDto = listaNoticia.stream()
																	.map(obj -> new UltimasNoticiaDTO(obj))
																.collect(Collectors.toList());
		return ResponseEntity.ok().body(listaNoticiaDto);
	}
	
	@GetMapping("/idNoticiasEventos")
	public ResponseEntity<List<IdNoticiasDTO>> idNoticiasEventos() {
		List<Noticia> listaNoticia = noticiaService.idNoticiasEventos();
		List<IdNoticiasDTO> listaNoticiaDto = listaNoticia.stream()
																.map(obj -> new IdNoticiasDTO(obj))
																.collect(Collectors.toList());
		return ResponseEntity.ok().body(listaNoticiaDto);
	}
	
	@GetMapping("/countNoticias")
	public ResponseEntity<Integer> countNoticias() {	
		return ResponseEntity.ok().body(noticiaService.countNoticias());
	}

	@Secured({"ROLE_CADASTRAR_NOTICIA"}) 
	@PostMapping()
	public ResponseEntity<NoticiaDTO> inserir(@RequestBody NoticiaDTO noticiaDTO) throws Exception {
		Noticia noticia = noticiaService.salvar(noticiaDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(noticia.getId()).toUri();
		return  ResponseEntity.created(uri).build();
	}

	@Secured({"ROLE_EDITAR_NOTICIA"})
	@PutMapping(value = "/{id}")
	public ResponseEntity<NoticiaDTO> alterar(final @PathVariable("id") Long id,
											  		@RequestBody NoticiaDTO noticiaDTO) throws Exception {
		Noticia noticia = noticiaService.editar(noticiaDTO, id);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(noticia.getId()).toUri();
		return  ResponseEntity.ok().location(uri).build();
	}

	@Secured({"ROLE_DELETAR_NOTICIA"})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(final @PathVariable("id") Long id) throws Exception {
		noticiaService.deletar(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/buscar")
	public List<NoticiaBoletimDTO> buscarNoticia(@RequestParam("q") @Length(min = 2, max = 50) String keywords) {
		List<Noticia> noticias = noticiaService.buscarNoticia(keywords);
		return noticias.stream().map(n -> new NoticiaBoletimDTO(n)).collect(Collectors.toList());
	}
	
	@GetMapping("/buscarNoticiaTituloEId")
	public List<ItemComboDTO> buscarNoticiaTituloEId() {
		return noticiaService.buscarNoticiaTituloEId();		 
	}
	
	@GetMapping("/buscarUltimasDezNoticiaTituloEId")
	public List<ItemComboDTO> buscarUltimasDezNoticiaTituloEId() {
		return noticiaService.buscarUltimasDezNoticiaTituloEId();		 
	}
	
	@GetMapping("/buscarNoticiaUsandoDataInicioFimPalavraChave")
	public List<NoticiaBoletimDTO> buscarNoticiaUsandoDataInicioFimPalavraChave(
													@RequestParam("q") String keywords,
													@RequestParam("dtInicio")
													String dtInicio,
													@RequestParam("dtFim")
													String dtFim)
																								throws ParseException {
		
		return noticiaService.buscarNoticiaParaBoletim(keywords,dtInicio,dtFim);
	}

	@GetMapping("/buscarCombosFiltrarNoticias")
	public ResponseEntity<CombosFiltrarNoticiasDTO> buscarCombosFiltrarNoticias(){
		CombosFiltrarNoticiasDTO combosFiltrarNoticiasDTO = noticiaService.buscarCombosFiltrarNoticias();
		return ResponseEntity.ok().body(combosFiltrarNoticiasDTO);
	}
	
	@PostMapping("/buscarNoticiasFiltradas")	
	public ResponseEntity<NoticiasFiltradasDTO> buscarNoticiasFiltradas(
			@RequestBody FiltroNoticias filtro){
		NoticiasFiltradasDTO lista = noticiaService.buscarNoticiasFiltradas(filtro);
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping("/buscarNoticiasDeEvento")
	public ResponseEntity<List<NoticiaItemDTO>> buscarNoticiasDeEventoFiltradas(@RequestParam List<Long> idsNoticias){
		List<NoticiaItemDTO> lista = noticiaService.buscarNoticiasItemPorId(idsNoticias);
		return ResponseEntity.ok().body(lista);
	}	

	@GetMapping(value = "/imagem/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> buscarImagemNoticia(@RequestHeader(value = "User-Agent") String userAgent, final @PathVariable("id") Long id) throws Exception {
        Noticia noticia = noticiaService.buscarPorId(id);

        if(noticia == null) {
            return ResponseEntity.notFound().build();
        }
        String imagem =  ImageUtils.isRobot(userAgent) ? 
				ImageUtils.compressImageSocialMediaB64(noticia.getImagemPrincipal()) : noticia.getImagemPrincipal();
		
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("public");
        headers.set("ETag", ImageUtils.generateImageETag(imagem));

        return new ResponseEntity<byte[]>(Base64.decode(imagem.getBytes()), headers, HttpStatus.OK);
    }	

    @GetMapping("/url/{url}")
    public ResponseEntity<NoticiaDetalheDTO> buscarNoticiaPorUrl(@PathVariable String url) throws Exception {
		Noticia noticia = noticiaService.findByUrlPublicada(url);

		if(noticia == null) {
			throw new Exception("Notícia não encontrada!");
		}

		NoticiaDetalheDTO dto = new NoticiaDetalheDTO(noticia);

		return ResponseEntity.ok(dto);
	}
    
    @GetMapping("/urlPublicada/{url}")
    public ResponseEntity<NoticiaDetalheDTO> buscarNoticiaPorUrlPublicada(
    														@PathVariable String url,
															@RequestHeader(value = "Referer",
																			required = false) String referer)
																									throws Exception {
		Noticia noticia = noticiaService.findByUrlPublicada(url);

		if(noticia == null) {
			throw new Exception("Notícia não encontrada!");
		}

		historicoAcessoNoticiaService.registrarAcesso(noticia, referer);

		return ResponseEntity.ok(new NoticiaDetalheDTO(noticia));
	}
    
    
    @GetMapping("/ultimasNoticiasAgenda/{qtd}")
    public ResponseEntity<List<NoticiaDetalheDTO>> buscarUltimasNoticiasAgenda(final @PathVariable("qtd") int qtd) {

    	List<Noticia> listaNoticia = noticiaService.ultimasNoticiasAgenda(qtd, "evento");
    	
		if(listaNoticia == null) {
			return ResponseEntity.notFound().build();
		}
		
    	List<NoticiaDetalheDTO> listaNoticiaDto = listaNoticia.stream()
																	.map(obj -> new NoticiaDetalheDTO(obj))
																.collect(Collectors.toList());

		return ResponseEntity.ok(listaNoticiaDto);
	}
    
	@GetMapping("/buscarUltimasNoticiasPaginaInicial")
	public  ResponseEntity<List<NoticiaItemDTO>> buscarUltimasNoticiasPaginaInicial(){
		List<Noticia> noticias = noticiaService.buscarUltimasNoticiasPaginaInicial();
		noticias.sort(Comparator.comparing(Noticia::getDataHoraPublicacao,Comparator.reverseOrder()));
		List<NoticiaItemDTO> listaNoticiaDto = noticias.stream().map(obj -> new NoticiaItemDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaNoticiaDto);
	}

}
