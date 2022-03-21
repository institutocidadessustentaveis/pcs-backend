package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.mail.EmailException;
import org.apache.lucene.document.DateTools;
import org.codehaus.jackson.map.util.ISO8601DateFormat;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.github.slugify.Slugify;

import br.org.cidadessustentaveis.dto.CombosFiltrarNoticiasDTO;
import br.org.cidadessustentaveis.dto.FiltroBibliotecasDTO;
import br.org.cidadessustentaveis.dto.FiltroNoticias;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.NoticiaBoletimDTO;
import br.org.cidadessustentaveis.dto.NoticiaDTO;
import br.org.cidadessustentaveis.dto.NoticiaItemDTO;
import br.org.cidadessustentaveis.dto.NoticiasFiltradasDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.noticias.Imagem;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import br.org.cidadessustentaveis.model.noticias.NoticiaHistorico;
import br.org.cidadessustentaveis.repository.NoticiaRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.ImageUtils;
import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class NoticiaService {
	@Autowired
	private NoticiaRepository repository;
	@Autowired
	private EixoService eixoService;
	@Autowired
	private AreaInteresseService areaInteresseService;
	@Autowired
	private ObjetivoDesenvolvimentoSustentavelService odsService;
	@Autowired
	private ProfileUtil profileUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	@Autowired
	private NoticiaHistoricoService noticiaHistoricoService;
	
	@Autowired
	private HistoricoAcessoNoticiaService historicoNoticiaService;

	@Autowired
	private ImagemService imagemService;

	@Autowired
	EntityManager em;

	@Autowired
	private NoticiaRepository noticiaRepository;

	@Autowired
	private EntityManager entityManager;

	private final AtomicBoolean initialized = new AtomicBoolean(false);

	@Autowired
	private Slugify slg;

	@Autowired
	private HttpServletRequest request;

	public Noticia salvar(NoticiaDTO noticiaDto) throws Exception {
		Noticia noticia = noticiaDto.toEntity();
		Noticia noticiaExistente = buscarPorTitulo(noticia.getTitulo());
		
		if(noticia.getIsPublicada()) {
			noticia.setDataHoraPublicacao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		}

		noticia.setDataHoraCriacao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

		if(ImageUtils.guessImageFormat(noticiaDto.getImagemPrincipal()).equalsIgnoreCase("GIF")) {
			throw new DataIntegrityException("Formato de imagem GIF não suportado");
		}

		noticia.setImagemPrincipal(ImageUtils.compressImage(noticiaDto.getImagemPrincipal()));

		String corpoTexto = noticiaDto.getCorpoTexto();

		corpoTexto = this.transformBase64ImagesToBinaryImage(noticiaDto.getCorpoTexto());
		corpoTexto = this.adjustHTMLImagesWidth(corpoTexto);

		noticia.setCorpoTexto(corpoTexto);

		preencherNoticia(noticiaDto, noticia);
		
		if (noticiaExistente != null) {
			throw new Exception("Já existe uma notícia com esse nome!");
		}
		
		noticia = repository.save(noticia);

		NoticiaHistorico noticiaHistorico = new NoticiaHistorico();
		noticiaHistorico.setDataHora(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		noticiaHistorico.setNoticia(noticia);
		noticiaHistorico.setUsuario(usuarioContextUtil.getUsuario());		
		noticiaHistoricoService.salvar(noticiaHistorico);

		noticia.setUrl(slg.slugify(noticia.getTitulo()));
		enviarNoticiaPorEmailAosInteressados(noticia);
		noticia = repository.save(noticia);

		return noticia;
	}

	private String adjustHTMLImagesWidth(String html) {
		Document doc = Jsoup.parse(html);
		Elements imgs = doc.select("img");

		for(Element img : imgs) {
			String style = img.attr("style");

			if(style != null && style.equals("")) {
				img.attr("style", "width: 100%;");
			}
		}

		return doc.html();
	}

	private String transformBase64ImagesToBinaryImage(String html) throws IOException {
		Usuario usuario = usuarioService.buscarPorEmail(
											SecurityContextHolder.getContext().getAuthentication().getName());

		Document doc = Jsoup.parse(html);
		Elements imgs = doc.select("img");

		for(Element img : imgs) {
			String src = img.attr("src");

			if(src != null && src.contains(";base64")) {
				String[] split = src.split(",");

				if(split.length > 1) {
					String base64Image = split[1];

					byte[] imageBytes = ImageUtils.compressImage(Base64.decodeBase64(base64Image));

					Imagem imagem = new Imagem(imageBytes);
					imagem = imagemService.save(imagem);

					String imageUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
																						+  "/imagens/" + imagem.getId();

					img.attr("src", imageUrl);
				}
			}
		}

		return doc.html();
	}

	public List<Noticia> listar() {
		return repository.findAllByOrderByDataHoraCriacaoDesc();
	}

	public List<Noticia> listar(Integer page, Integer linesPerPage) {
		return listarComPaginacao(page, linesPerPage, "dataHoraCriacao", "DESC").getContent();
	}

	public Page<Noticia> listarComPaginacao(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	public int countNoticias() {
		int x = repository.countNoticias();
		return x;

	}

	public List<Noticia> ultimasNoticias(int qtd) {
		PageRequest pageRequest = PageRequest.of(0, qtd, Direction.valueOf("DESC"), "dataHoraPublicacao");
		return repository.carregarUltimasNoticias(pageRequest);
	}
	
	public List<Noticia> idNoticiasEventos() {
		return repository.carregarIdNoticiasEventos();
	}

	public List<Noticia> ultimasNoticiasAgenda(int qtd, String chave) {
		PageRequest pageRequest = PageRequest.of(0, qtd, Direction.valueOf("DESC"), "dataHoraPublicacao");
		return repository.carregarUltimasNoticiasAgenda(chave, pageRequest);
	}

	public Page<Noticia> buscarComPaginacao(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	public Noticia buscarPorId(Long id) {
		Optional<Noticia> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Notícia não encontrada!"));
	}
	
	public Noticia buscarPorIdPublicada(Long id) {
		Optional<Noticia> obj = repository.findByIdPublicada(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Notícia não encontrada!"));
	}

	public Noticia buscarPorTitulo(String titulo) {
		Optional<Noticia> obj = repository.findByTitulo(titulo);
		return obj.orElse(null);
	}

	public Noticia buscarPorTituloId(String titulo, Long id) {
		List<Long> ids = new ArrayList<>();
		ids.add(id);
		Optional<Noticia> obj = repository.findByTituloAndIdNotIn(titulo, ids);
		return obj.orElse(null);
	}

	public Noticia editar(NoticiaDTO noticiaDto, Long id) throws Exception {
		if (noticiaDto.getId().equals(id)) {
			Noticia noticia = noticiaDto.toEntity();
			Noticia noticiaExistente = buscarPorTituloId(noticia.getTitulo(), noticia.getId());


			if(noticia.getIsPublicada() && noticia.getDataHoraPublicacao() == null) {
				noticia.setDataHoraPublicacao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			} else {
				if(noticia.getIsPublicada() == false && noticia.getDataHoraPublicacao() != null) {
					noticia.setDataHoraPublicacao(null);
				}
			}

			preencherNoticia(noticiaDto, noticia);

			if(ImageUtils.guessImageFormat(noticiaDto.getImagemPrincipal()).equalsIgnoreCase("GIF")) {
				throw new DataIntegrityException("Formato de imagem GIF não suportado");
			}

			if(noticiaDto.isImagemEditada()) {
				noticia.setImagemPrincipal(ImageUtils.compressImage(noticiaDto.getImagemPrincipal()));
			}

			String corpoTexto = this.adjustHTMLImagesWidth(noticiaDto.getCorpoTexto());
			corpoTexto = this.transformBase64ImagesToBinaryImage(corpoTexto);

			noticia.setCorpoTexto(corpoTexto);
			
			if (noticiaExistente != null) {
				throw new Exception("Já existe uma notícia com esse nome!");
			}	

			noticia = repository.save(noticia);
			
			NoticiaHistorico noticiaHistorico = new NoticiaHistorico();
			noticiaHistorico.setDataHora(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			noticiaHistorico.setNoticia(noticia);
			noticiaHistorico.setUsuario(usuarioContextUtil.getUsuario());
			noticiaHistoricoService.salvar(noticiaHistorico);

			noticia.setUrl(slg.slugify(noticia.getTitulo()));
			noticia = repository.save(noticia);
			return noticia;
		} else {
			throw new Exception("Notícia não é valida");
		}
	}

	public void deletar(Long id) {
		Noticia noticia = buscarPorId(id);
		if(noticia != null) {
			noticiaHistoricoService.deletarPorIdNoticia(noticia.getId());
			historicoNoticiaService.deletarPorIdNoticia(noticia.getId());
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}
	}

	public Long contar() {
		return repository.count();
	}

	public void preencherNoticia(NoticiaDTO dto, Noticia entidade) throws Exception {
		List<Eixo> listaEixo = new ArrayList<>();
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = new ArrayList<>();
		List<AreaInteresse> listaAreasInteresse = new ArrayList<>();

		if (dto.getEixos() != null) {
			dto.getEixos().forEach(e -> listaEixo.add(eixoService.listarById(e.getId())));
		}
		if (dto.getOdss() != null) {
			dto.getOdss().forEach(e -> listaOds.add(odsService.listarPorId(e.getId())));
		}
		if (dto.getAreasDeInteresse() != null) {
			dto.getAreasDeInteresse()
					.forEach(e -> listaAreasInteresse.add(areaInteresseService.buscarPorId(e.getId())));
		}
		
		entidade.setEixos(listaEixo);
		entidade.setOdss(listaOds);
		entidade.setAreasDeInteresse(listaAreasInteresse);
	}

	public boolean enviarNoticiaPorEmailAosInteressados(Noticia noticia) throws EmailException {
		try {
			String urlPCS = profileUtil.getProperty("profile.frontend");

			if (noticia.getAreasDeInteresse() != null && !noticia.getAreasDeInteresse().isEmpty()) {

				List<String> emails =
							usuarioService.listarEmailUsuarioComAreaDeInteresse(noticia.getAreasDeInteresse());

				if (!emails.isEmpty()) {
					StringBuilder builder = new StringBuilder();

					builder
						.append("<p style='font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#000000;'>");
					builder.append("A Notícia: <a href='");
					builder.append(urlPCS);
					builder.append("/noticia/detalhe/");
					builder.append(noticia.getUrl());
					builder.append("'>");
					builder.append(noticia.getTitulo());
					builder.append("</a> foi adicionada recentemente pela Plataforma Cidades Sustentáveis <br>");
					builder.append("<a href='");
					builder.append(urlPCS);
					builder.append("/noticia/detalhe/");
					builder.append(noticia.getUrl());
					builder.append("'>Clique aqui</a> ou no título da notícia para acessá-la pelo seu navegador!<br>");

					emailUtil.enviarEmailHTML(emails, "Adicionamos uma notícia de seu interesse!", builder.toString());
				}
			}
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	public List<Noticia> buscarNoticiasPorId(List<Long> ids) {
		return noticiaRepository.buscarNoticiasPorId(ids);
	}
	
	public List<NoticiaItemDTO> buscarNoticiasItemPorId(List<Long> ids) {
		List<NoticiaItemDTO> noticias = noticiaRepository.buscarNoticiasItemPorId(ids);
		String urlAPI = profileUtil.getProperty("profile.api");
		
		noticias.forEach(noticia -> {
			noticia.setUrlImagem(urlAPI + "/noticia/imagem/" + noticia.getIdNoticia());
		});
		
		return noticias;
	}

	public List<Noticia> buscarNoticia(String palavraChave) {
		FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
															.buildQueryBuilder()
															.forEntity(Noticia.class).get();

		try {
			org.apache.lucene.search.Query query = queryBuilder
													.keyword()
														.fuzzy()
														.withEditDistanceUpTo(1)
														.withPrefixLength(0)
														.onFields("titulo", "palavraChave", "corpoTexto")
														.matching(palavraChave)
													.createQuery();

			org.hibernate.search.jpa.FullTextQuery jpaQuery =
													fullTextEntityManager.createFullTextQuery(query, Noticia.class);

			return jpaQuery.setMaxResults(15).getResultList();
		} catch(org.hibernate.search.exception.EmptyQueryException e) {
			return new LinkedList<>();
		}
	}
	
	public List<NoticiaBoletimDTO> buscarNoticiaParaBoletim(String palavraChave, String dataInicio, String dataFim) throws ParseException {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<NoticiaBoletimDTO> query = cb.createQuery(NoticiaBoletimDTO.class);
		
		Root<Noticia> noticia = query.from(Noticia.class);
		
		query.multiselect(noticia.get("id"), noticia.get("titulo"), noticia.get("autor"), noticia.get("usuario"), 
				noticia.get("url"), noticia.get("dataHoraCriacao"), noticia.get("dataHoraPublicacao")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		ISO8601DateFormat df = new ISO8601DateFormat();
		LocalDate dtInicio = df.parse(dataInicio).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dtFim = df.parse(dataFim).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if (dtInicio != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, noticia.get("dataHoraPublicacao"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, dtInicio));
		}

		if (dtFim != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, noticia.get("dataHoraPublicacao"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, dtFim));
		}
		
		if(palavraChave != null && !palavraChave.equals("")) {
			Path<String> titulo = noticia.get("titulo");
			Path<String> subtitulo = noticia.get("subtitulo");
			Path<String> corpoTexto = noticia.get("corpoTexto");
			Path<String> palavraChaveNoticiaEntity = noticia.get("palavraChave");
			
			javax.persistence.criteria.Predicate predicateForTitulo = cb.like(cb.lower(titulo), "%" + palavraChave.toLowerCase() + "%");
			javax.persistence.criteria.Predicate predicateForSubtitulo = cb.like(cb.lower(subtitulo), "%" + palavraChave.toLowerCase() + "%");			
			javax.persistence.criteria.Predicate predicateForCorpoTexto = cb.like(cb.lower(corpoTexto), "%" + palavraChave.toLowerCase() + "%");
			javax.persistence.criteria.Predicate predicateForPalavraChaveNoticiaEntity = cb.like(cb.lower(palavraChaveNoticiaEntity), "%" + palavraChave.toLowerCase() + "%");
			
			predicateList.add(cb.or(predicateForTitulo, predicateForSubtitulo, predicateForCorpoTexto, predicateForPalavraChaveNoticiaEntity));
		}
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		TypedQuery<NoticiaBoletimDTO> typedQuery = em.createQuery(query);
		List<NoticiaBoletimDTO> lista = typedQuery.getResultList();

		return lista;
	}
	
	public List<ItemComboDTO> buscarNoticiaTituloEId (){
		return noticiaRepository.buscarNoticiaTituloEId();
	}
	
	public List<ItemComboDTO> buscarUltimasDezNoticiaTituloEId (){
		List<ItemComboDTO> dto = entityManager.createQuery("select new br.org.cidadessustentaveis.dto.ItemComboDTO(n.id, n.titulo) from Noticia n order by n.id desc", ItemComboDTO.class).setMaxResults(10).getResultList();
		return dto;
	}

	public List<Noticia> buscarNoticiaUsandoDataInicioFimPalavraChave(String palavraChave, String strdtInicio,
																	  String strdtFim) throws ParseException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Noticia> query = cb.createQuery(Noticia.class);
		
		ISO8601DateFormat df = new ISO8601DateFormat();
		LocalDate dtInicio = df.parse(strdtInicio).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dtFim = df.parse(strdtFim).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Root<Noticia> root = query.from(Noticia.class);
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (palavraChave != null) {
			Path<String> campoTitulo = root.get("titulo");
			Path<String> campoSubtitulo = root.get("subtitulo");
			Path<String> campoCorpoTexto = root.get("corpoTexto");
			Path<String> campoPalavraChave = root.get("palavraChave");
			predicateList.add(cb.or(cb.like(cb.lower(campoSubtitulo), "%" + palavraChave.toLowerCase() + "%"),
					cb.like(cb.lower(campoCorpoTexto), "%" + palavraChave.toLowerCase() + "%"),
					cb.like(cb.lower(campoPalavraChave), "%" + palavraChave.toLowerCase() + "%"),
					cb.like(cb.lower(campoTitulo), "%" + palavraChave.toLowerCase() + "%")));
		}

		if (dtInicio != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, root.get("dataHoraPublicacao"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, dtInicio));
		}

		if (dtFim != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, root.get("dataHoraPublicacao"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, dtFim));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(root.get("dataHoraPublicacao")));
		query.orderBy(orderList);

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<Noticia> lista = em.createQuery(query).getResultList();
		return lista;

	}

	private FullTextEntityManager getFullTextEntityManager() {
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

		if (initialized.get()) {
			return fullTextEntityManager;
		} else {
			synchronized (initialized) {
				if (!initialized.getAndSet(true)) {
					try {
						fullTextEntityManager.createIndexer().startAndWait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				return fullTextEntityManager;
			}
		}
	}

	public CombosFiltrarNoticiasDTO buscarCombosFiltrarNoticias() {

		List<ItemComboDTO> listaItensEixos = new ArrayList<ItemComboDTO>();
		List<Eixo> listaEixos = eixoService.listar();
		for (Eixo eixo : listaEixos) {
			listaItensEixos.add(new ItemComboDTO(eixo.getId(), eixo.getNome()));
		}

		List<ItemComboDTO> listaItensOds = new ArrayList<ItemComboDTO>();
		List<ObjetivoDesenvolvimentoSustentavel> listaOds = odsService.listar();
		for (ObjetivoDesenvolvimentoSustentavel ods : listaOds) {
			listaItensOds.add(new ItemComboDTO(ods.getId(), ods.getTitulo()));
		}

		List<ItemComboDTO> listaItensAreasInteresse = new ArrayList<ItemComboDTO>();
		List<AreaInteresse> listaAreasInteresse = areaInteresseService.buscaAreaInteresses();
		for (AreaInteresse areaInteresse : listaAreasInteresse) {
			listaItensAreasInteresse.add(new ItemComboDTO(areaInteresse.getId(), areaInteresse.getNome()));
		}

		CombosFiltrarNoticiasDTO combosFiltrarNoticiasDTO = new CombosFiltrarNoticiasDTO();
		combosFiltrarNoticiasDTO.setListaEixos(listaItensEixos);
		combosFiltrarNoticiasDTO.setListaOds(listaItensOds);
		combosFiltrarNoticiasDTO.setListaAreaInteresses(listaItensAreasInteresse);

		return combosFiltrarNoticiasDTO;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Set<Object> seen = ConcurrentHashMap.newKeySet();

		return t -> seen.add(keyExtractor.apply(t));
	}

	public NoticiasFiltradasDTO buscarNoticiasFiltradas(FiltroNoticias filtro) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<NoticiaItemDTO> query = cb.createQuery(NoticiaItemDTO.class);

		Root<Noticia> noticia = query.from(Noticia.class);

		Join<Noticia, Eixo> joinEixo = noticia.join("eixos", JoinType.LEFT);
		Join<Noticia, ObjetivoDesenvolvimentoSustentavel> joinOds = noticia.join("odss", JoinType.LEFT);
		Join<Noticia, AreaInteresse> joinArea = noticia.join("areasDeInteresse", JoinType.LEFT);

		query.multiselect(noticia.get("id")).distinct(true);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (filtro.getPalavraChave() != null && !filtro.getPalavraChave().equals("")) {
			Path<String> campoTitulo = noticia.get("titulo");
			Path<String> campoSubtitulo = noticia.get("subtitulo");
			Path<String> campoCorpoTexto = noticia.get("corpoTexto");
			Path<String> campoPalavraChave = noticia.get("palavraChave");
			predicateList.add(cb.or(cb.like(cb.lower(campoSubtitulo), "%" + filtro.getPalavraChave().toLowerCase() + "%"),
					cb.like(cb.lower(campoCorpoTexto), "%" + filtro.getPalavraChave().toLowerCase() + "%"),
					cb.like(cb.lower(campoPalavraChave), "%" + filtro.getPalavraChave().toLowerCase() + "%"),
					cb.like(cb.lower(campoTitulo), "%" + filtro.getPalavraChave().toLowerCase() + "%")));
		}

		if (filtro.getIdEixo() != null) {
			Path<Long> idEixo = joinEixo.get("id");
			predicateList.add(cb.equal(idEixo, filtro.getIdEixo()));
		}

		if (filtro.getIdOds() != null) {
			Path<Long> idOds = joinOds.get("id");
			predicateList.add(cb.equal(idOds, filtro.getIdOds()));
		}

		if (filtro.getIdAreaInteresse() != null) {
			Path<Long> idMetaOds = joinArea.get("id");
			predicateList.add(cb.equal(idMetaOds, filtro.getIdAreaInteresse()));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(noticia.get("id")));
		query.orderBy(orderList);

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<NoticiaItemDTO> typedQuery = em.createQuery(query);

		if (filtro.getPage() == 0) {
			typedQuery.setFirstResult(filtro.getPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		} else {
			typedQuery.setFirstResult(filtro.getPage() * filtro.getLinesPerPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		}

		List<NoticiaItemDTO> listNoticiasAux = typedQuery.getResultList();
		
		List<Long> idNoticias = null;
		List<NoticiaItemDTO> listNoticias = null;
		if(listNoticiasAux != null && !listNoticiasAux.isEmpty()) {
			idNoticias = listNoticiasAux.stream().map(NoticiaItemDTO::getIdNoticia).collect(Collectors.toList());
			listNoticias = repository.buscarNoticiasDTOPorListaId(idNoticias);
			listNoticias.sort(Comparator.comparing(NoticiaItemDTO::getDataHoraPublicacao,Comparator.reverseOrder()));
		}

		NoticiasFiltradasDTO noticiasFiltradasDTO = new NoticiasFiltradasDTO();

		noticiasFiltradasDTO.setListaNoticias(listNoticias);

		noticiasFiltradasDTO.setCountTotalNoticias(countNoticiasFiltradas(filtro));

		return noticiasFiltradasDTO;
	}
	
//	public NoticiasFiltradasDTO buscarNoticiasDeEvento(List<Long> idsNoticias) {
//
//
//	}

	private Long countNoticiasFiltradas(FiltroNoticias filtro) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Long> query = cb.createQuery(Long.class);

		Root<Noticia> noticia = query.from(Noticia.class);

		Join<Noticia, Eixo> joinEixo = noticia.join("eixos", JoinType.LEFT);
		Join<Noticia, ObjetivoDesenvolvimentoSustentavel> joinOds = noticia.join("odss", JoinType.LEFT);
		Join<Noticia, AreaInteresse> joinArea = noticia.join("areasDeInteresse", JoinType.LEFT);

		query.select(cb.countDistinct(noticia));

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (filtro.getPalavraChave() != null) {
			Path<String> palavraChave = noticia.get("palavraChave");
			predicateList.add(cb.like(cb.lower(palavraChave), "%" + filtro.getPalavraChave().toLowerCase() + "%"));
		}

		if (filtro.getIdEixo() != null) {
			Path<Long> idEixo = joinEixo.get("id");
			predicateList.add(cb.equal(idEixo, filtro.getIdEixo()));
		}

		if (filtro.getIdOds() != null) {
			Path<Long> idOds = joinOds.get("id");
			predicateList.add(cb.equal(idOds, filtro.getIdOds()));
		}

		if (filtro.getIdAreaInteresse() != null) {
			Path<Long> idMetaOds = joinArea.get("id");
			predicateList.add(cb.equal(idMetaOds, filtro.getIdAreaInteresse()));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<Long> typedQuery = em.createQuery(query);
		Long lista = typedQuery.getSingleResult();

		return lista;
	}

	public Noticia findByUrl(String url) {
		return repository.findByUrl(url);
	}
	
	public Noticia findByUrlPublicada(String url) {
		return repository.findByUrlPublicada(url);
	}
	
	public List<Noticia> buscarUltimasNoticiasPaginaInicial() {
		return repository.buscarUltimasNoticiasPaginaInicial();
	}

	public List<Noticia> carregarNoticiaBannerPaginaInicial(int qtd) {
		PageRequest pageRequest = PageRequest.of(0, qtd, Direction.valueOf("DESC"), "id");
		return repository.carregarNoticiaBannerPaginaInicial(pageRequest);
	}

}
