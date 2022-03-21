package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.dto.*;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.sistema.HistoricoBusca;
import br.org.cidadessustentaveis.model.sistema.IPLookup;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.IPUtil;
import br.org.cidadessustentaveis.util.Stopwords;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.institucional.Institucional;
import br.org.cidadessustentaveis.model.noticias.Noticia;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class BuscarService {

	@Autowired
	private HistoricoBuscaService historicoBuscaService;

	@Autowired
	private IPLookupService ipLookupService;

	@Autowired
	EntityManager em;

	@Autowired
	private EntityManager entityManager;

	private final AtomicBoolean initialized = new AtomicBoolean(false);

	public BuscarDTO buscarPorPalavraChave(String palavraChave) throws ParseException {
		BuscarDTO buscarDTO = new BuscarDTO();

		BuscaComScoreDTO buscaNoticiaDTO = this.buscarNoticia(palavraChave);

		buscarDTO.setNoticias(buscaNoticiaDTO.getItens());
		buscarDTO.setPorcentagemNoticiasPalavraChaveExata(buscaNoticiaDTO.getPorcentagemResultadosPalavraChaveExata());

		BuscaComScoreDTO buscaBoaPraticaDTO = this.buscarBoaPratica(palavraChave);

		buscarDTO.setBoasPraticas(buscaBoaPraticaDTO.getItens());
		buscarDTO.setPorcentagemBoasPraticasPalavraChaveExata(
														buscaBoaPraticaDTO.getPorcentagemResultadosPalavraChaveExata());

		BuscaComScoreDTO buscaInstitucionalDTO = this.buscarInstitucional(palavraChave);

		buscarDTO.setInstitucionais(buscaInstitucionalDTO.getItens());
		buscarDTO.setPorcentagemInstitucionaisPalavraChaveExata(
													buscaInstitucionalDTO.getPorcentagemResultadosPalavraChaveExata());

		BuscaComScoreDTO buscaIndicadoresDTO = this.buscarIndicador(palavraChave);

		buscarDTO.setIndicadores(buscaIndicadoresDTO.getItens());
		buscarDTO.setPorcentagemIndicadoresPalavraChaveExata(
													buscaIndicadoresDTO.getPorcentagemResultadosPalavraChaveExata());

		this.registrarHistorico(palavraChave);

		return buscarDTO;
	}
	
	private BuscaComScoreDTO buscarNoticia (String palavraChave) throws ParseException {
		FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
															.buildQueryBuilder()
															.forEntity(Noticia.class).get();
		
		org.apache.lucene.search.Query query = null;

		try {

			if (palavraChave != null && !palavraChave.isEmpty()) {
				org.apache.lucene.search.Query queryTexto = queryBuilder.phrase()
																			.withSlop(2)
																			.onField("titulo")
																			.andField("corpoTexto")
																			.andField("autor")
																			.sentence(palavraChave)
																		.createQuery();

				query = queryBuilder.bool().must(queryTexto).createQuery();
			}
			
			org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager
																	.createFullTextQuery(query, Noticia.class);
			jpaQuery.setProjection("id","titulo","url");

			List<Object[]> listaNoticia = jpaQuery.setMaxResults(15).getResultList();

			List<String> titles = listaNoticia.stream().map((n) -> n[1].toString()).collect(Collectors.toList());
			Double resultsWithKeyWordsPercentage = this.calculateResultsWithKeywordsPercentage(palavraChave, titles);

			List<BuscaItemDTO> listBuscaItemDTO =  new ArrayList<>();

			for(Object[] listObj : listaNoticia) {
				BuscaItemDTO buscaItemDTO = new BuscaItemDTO((Long)listObj[0],(String)listObj[1],(String)listObj[2]);
				listBuscaItemDTO.add(buscaItemDTO);
			}

			return new BuscaComScoreDTO(listBuscaItemDTO, resultsWithKeyWordsPercentage);
		} catch(org.hibernate.search.exception.EmptyQueryException e) {
			return new BuscaComScoreDTO();
		}
	}

	private BuscaComScoreDTO buscarBoaPratica(String palavraChave) throws ParseException {
		FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
															.buildQueryBuilder().forEntity(BoaPratica.class).get();
		
		org.apache.lucene.search.Query query = null;

		try {
			if (palavraChave != null && !palavraChave.isEmpty()) {
				org.apache.lucene.search.Query queryTexto = queryBuilder.phrase()
																			.withSlop(2)
																			.onField("titulo")
																			.andField("subtitulo")
																			.andField("objetivoGeral")
																			.andField("objetivoEspecifico")
																			.andField("principaisResultados")
																			.andField("aprendizadoFundamental")
																			.andField("parceirosEnvolvidos")
																			.andField("resultadosQuantitativos")
																			.andField("resultadosQualitativos")
																			.andField("parametrosContemplados")
																			.andField("publicoAtingido")
																			.andField("fontesReferencia")
																			.sentence(palavraChave)
																		.createQuery();

				query = queryBuilder.bool().must(queryTexto).createQuery();
			}
			
			org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager
																	.createFullTextQuery(query, BoaPratica.class);
			jpaQuery.setProjection("id","titulo");

			List<Object[]> listaBoaPratica = jpaQuery.setMaxResults(15).getResultList();

			List<String> titles = listaBoaPratica.stream().map((n) -> n[1].toString()).collect(Collectors.toList());
			Double resultsWithKeyWordsPercentage = this.calculateResultsWithKeywordsPercentage(palavraChave, titles);
			
			List<BuscaItemDTO> listBuscaItemDTO =  new ArrayList<>();
			for(Object[] listObj : listaBoaPratica) {
				
				BuscaItemDTO buscaItemDTO = new BuscaItemDTO((Long)listObj[0],(String)listObj[1], null);
				
				listBuscaItemDTO.add(buscaItemDTO);
			}

			return new BuscaComScoreDTO(listBuscaItemDTO, resultsWithKeyWordsPercentage);
		} catch(org.hibernate.search.exception.EmptyQueryException e) {
			return new BuscaComScoreDTO();
		}
	}
	
	
	private BuscaComScoreDTO buscarInstitucional(String palavraChave) throws ParseException {
		FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
															.buildQueryBuilder().forEntity(Institucional.class).get();

		org.apache.lucene.search.Query query = null;

		try {
			if (palavraChave != null && !palavraChave.isEmpty()) {
				org.apache.lucene.search.Query queryTexto = queryBuilder.phrase()
																			.withSlop(2)
																			.onField("titulo")
																			.andField("subtitulo")
																			.andField("template01.tituloPrimeiraSecao")
																			.andField("template01.textoPrimeiraSecao")
																			.andField("template01.tituloSegundaSecao")
																			.andField("template01.txtSegundaSecao")
																			.andField("template02.tituloPrimeiraSecao")
																			.andField("template02.textoPrimeiraSecao")
																			.andField("template02.tituloSegundaSecao")
																			.andField("template02.txtSegundaSecao")
																			.andField("template02.tituloTerceiraSecao")
																			.andField("template02.txtTerceiraSecao")
																			.andField("template02.tituloQuartaSecao")
																			.andField("template02.txtQuartaSecao")
																			.sentence(palavraChave)
																		.createQuery();

				query = queryBuilder.bool().must(queryTexto).createQuery();
			}

			org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager
																	.createFullTextQuery(query, Institucional.class);
			jpaQuery.setProjection("id","titulo","link_pagina");

			List<Object[]> listaInstitucional = jpaQuery.setMaxResults(15).getResultList();

			List<String> titles = listaInstitucional.stream().map((n) -> n[1].toString()).collect(Collectors.toList());
			Double resultsWithKeyWordsPercentage = this.calculateResultsWithKeywordsPercentage(palavraChave, titles);
			
			List<BuscaItemDTO> listBuscaItemDTO =  new ArrayList<>();

			for(Object[] listObj : listaInstitucional) {
				BuscaItemDTO buscaItemDTO = new BuscaItemDTO((Long)listObj[0], (String)listObj[1], (String)listObj[2]);
				listBuscaItemDTO.add(buscaItemDTO);
			}

			return new BuscaComScoreDTO(listBuscaItemDTO, resultsWithKeyWordsPercentage);
		} catch(org.hibernate.search.exception.EmptyQueryException e) {
			return new BuscaComScoreDTO();
		}
	}
	
	
	private BuscaComScoreDTO buscarIndicador(String palavraChave) throws ParseException {
		FullTextEntityManager fullTextEntityManager = getFullTextEntityManager();

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Indicador.class).get();
		
		org.apache.lucene.search.Query query = null;

		try {
			if (palavraChave != null && !palavraChave.isEmpty()) {
				org.apache.lucene.search.Query queryTexto = queryBuilder.phrase()
																			.withSlop(2)
																			.onField("nome")
																			.andField("descricao")
																			.sentence(palavraChave)
																		.createQuery();

				query = queryBuilder.bool().must(queryTexto).createQuery();
			}
			
			org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager
																	.createFullTextQuery(query, Indicador.class);
			jpaQuery.setProjection("id", "nome");

			List<Object[]> listaIndicadores = jpaQuery.setMaxResults(15).getResultList();

			List<String> titles = listaIndicadores.stream().map((n) -> n[1].toString()).collect(Collectors.toList());
			Double resultsWithKeyWordsPercentage = this.calculateResultsWithKeywordsPercentage(palavraChave, titles);

			List<BuscaItemDTO> listBuscaItemDTO =  new ArrayList<>();

			for(Object[] listObj : listaIndicadores) {
				BuscaItemDTO buscaItemDTO = new BuscaItemDTO((Long)listObj[0],(String)listObj[1], null);
				listBuscaItemDTO.add(buscaItemDTO);
			}

			return new BuscaComScoreDTO(listBuscaItemDTO, resultsWithKeyWordsPercentage);
		} catch(org.hibernate.search.exception.EmptyQueryException e) {
			return new BuscaComScoreDTO();
		}
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

	private Double calculateResultsWithKeywordsPercentage(String keywords, List<String> results) {
		Long totalResultsWithKeywords = 0l;

		try {
			Set<String> resultsWithKeywords = new HashSet<>();
			List<String> stopwords = Stopwords.getStopwords();

			for(String palavra : keywords.split(" ")) {
				if(!stopwords.contains(palavra.toLowerCase())) {
					List<String> objects = results.stream()
														.filter((n) -> n.toString()
																			.toLowerCase()
																			.contains(palavra.toLowerCase()))
														.collect(Collectors.toList());
					resultsWithKeywords.addAll(objects);
				}
			}

			totalResultsWithKeywords = Long.valueOf(resultsWithKeywords.size());
		} catch (IOException e) {
			totalResultsWithKeywords = Long.valueOf(results.size());
		}

		Double resultsWithKeyWorksPercentage = 0.0;

		if(!results.isEmpty()) {
			resultsWithKeyWorksPercentage = Double.valueOf((100 * totalResultsWithKeywords) / results.size());
		}

		return resultsWithKeyWorksPercentage;
	}

	private HistoricoBusca registrarHistorico(String palavraChave) {
		HistoricoBusca historico = new HistoricoBusca();
		historico.setPalavraChave(palavraChave);
		historico.setDataHoraBusca(LocalDateTime.now());

		Usuario usuario = this.getUsuario();

		if(usuario != null) {
			historico.setUsuario(usuario);
		}

		String ip = this.getIP();

		if(ip != null && !ip.isEmpty()) {
			historico.setIp(ip);
		}

		IPLookup lookup = ipLookupService.findByIp(ip);

		if(lookup == null) {
			IPLookupDTO ipLookupDTO = IPUtil.lookup(ip);

			if(ipLookupDTO != null) {
				lookup = ipLookupService.save(new IPLookup(ipLookupDTO));
			}
		}

		historico.setIpLookup(lookup);

		return historicoBuscaService.save(historico);
	}

	private Usuario getUsuario() {
		ApplicationContext context = SpringContext.getAppContext();
		UsuarioService usuarioService = (UsuarioService)context.getBean("usuarioService");
		String user = SecurityContextHolder.getContext().getAuthentication().getName();

		try {
			return usuarioService.buscarPorEmailCredencial(user);
		} catch(ObjectNotFoundException ex) {
			return null;
		}
	}

	private String getIP() {
		String userIpAddress = getCurrentRequest().getHeader("X-Forwarded-For");

		if(userIpAddress == null) {
			userIpAddress = getCurrentRequest().getRemoteAddr();
		}

		return userIpAddress;
	}

	public static HttpServletRequest getCurrentRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
		Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
		return servletRequest;
	}
}
