package br.org.cidadessustentaveis.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.mail.EmailException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.secure.spi.IntegrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.AjusteGeralDTO;
import br.org.cidadessustentaveis.dto.AprovacaoPrefeituraSimplesDTO;
import br.org.cidadessustentaveis.dto.CartaCompromissoDTO;
import br.org.cidadessustentaveis.dto.CidadeComBoasPraticasDTO;
import br.org.cidadessustentaveis.dto.CidadeComPrefeituraDTO;
import br.org.cidadessustentaveis.dto.CidadeComboDTO;
import br.org.cidadessustentaveis.dto.CidadeMandatosDTO;
import br.org.cidadessustentaveis.dto.IdPaisIdEstadoIdCidadeDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.PrefeituraDTO;
import br.org.cidadessustentaveis.dto.PrefeituraEdicaoDTO;
import br.org.cidadessustentaveis.dto.PrefeituraImportacaoDTO;
import br.org.cidadessustentaveis.dto.RelatorioPlanoDeMetasPrestacaoDeContasDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.AprovacaoPrefeitura;
import br.org.cidadessustentaveis.model.administracao.CartaCompromisso;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.HistoricoPlanoMetasPrestacaoContas;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.PartidoPolitico;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.repository.CartaCompromissoRepository;
import br.org.cidadessustentaveis.repository.PrefeituraRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.DateUtil;
import br.org.cidadessustentaveis.util.EmailUtil;
import br.org.cidadessustentaveis.util.PDFUtils;

@Service
@CacheConfig(cacheNames={"cache"})
public class PrefeituraService {

	@Autowired
	private PrefeituraRepository prefeituraRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PaisService paisService;

	@Autowired
	private ProvinciaEstadoService provinciaEstadoService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private AprovacaoPrefeituraService aprovacaoPrefeituraService;

	@Autowired
	private PartidoPoliticoService partidoPoliticoService;

	@Autowired
	private CartaCompromissoRepository cartaCompromissoRepository;

	@Autowired
	private AlertaService alertaService;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private AjusteGeralService ajusteGeralService;
	
	public static final String ALERTA_EMAIL = "ALERTA-EMAIL-NOVA-CIDADE";
	
	public static final String TELEFONE_ALERTA = "TELEFONE-ALERTA-BLOQUEIO-CARTA";

	@Autowired
	private EntityManager em;

	public Prefeitura inserir(final PrefeituraDTO prefeituraDTO) {
		
		AjusteGeralDTO ajusteGeralDto = ajusteGeralService.buscarAjustePorLocalApp(TELEFONE_ALERTA);
		
		Prefeitura prefeitura = prefeituraDTO.toEntityInsert();
		prefeitura.setPartidoPolitico(partidoPoliticoService.listarPorId(prefeituraDTO.getPartidoPolitico().getId()));
		atualizaReferenciaCidade(prefeitura, prefeituraDTO);
		
		List<Prefeitura> listaPrefeituraSignatarias = prefeituraRepository.findByCidadeId(prefeituraDTO.getCidade().getId());
		
		if(!listaPrefeituraSignatarias.isEmpty()) {
			throw new IntegrationException(
					"Já existe um cadastro para a cidade de "+prefeitura.getCidade().getNome() +". "+
					"Em caso de dúvidas, entre em contato com o PCS, pelo telefone: " + ajusteGeralDto.getConteudo());
		}
		
		
		
		List<AprovacaoPrefeitura> listaAprovacoesPrefeitura = aprovacaoPrefeituraService.getAprovacoesPendentesByCidade(prefeitura.getCidade());
		if(!listaAprovacoesPrefeitura.isEmpty()) {
			throw new IntegrationException(
					"Já existe um pedido de adesão em aberto para a cidade de "+prefeitura.getCidade().getNome() +". "+
					"Em caso de dúvidas, entre em contato com o PCS, pelo telefone: " + ajusteGeralDto.getConteudo());
		}
		for (CartaCompromisso carta: prefeitura.getCartaCompromisso()) {
			carta.setPrefeitura(prefeitura);
		}
		
		Prefeitura entity = prefeituraRepository.save(prefeitura);
		aprovacaoPrefeituraService.criarPedidoAprovacao(prefeitura);
		alertaService.salvar(Alerta.builder()
				.mensagem("A Prefeitura do município: "+prefeitura.getCidade().getNome()+" enviou um pedido de adesão à Plataforma")
					.link("/aprovacao-prefeitura")
					.tipoAlerta(TipoAlerta.PEDIDO_ADESAO_PREFEITURA)
					.data(LocalDateTime.now())
					.build());

		try {
			this.enviarEmailDeAdesao(prefeitura.getCidade().getNome());
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entity;
	}
	
	public void enviarEmailDeAdesao(String nomeCidade) throws EmailException {
		List<AjusteGeralDTO> listaEmails = ajusteGeralService.buscarListaAjustes(ALERTA_EMAIL);
		List<String> listaDestinatarios = new ArrayList<>();
		
		for(AjusteGeralDTO email : listaEmails) {
			listaDestinatarios.add(email.getConteudo());
		}
		
		String mensagem = "<p>A Prefeitura do município de " + nomeCidade + " enviou um pedido de adesão à Plataforma. </p>"
						+ "<p>" + "<a href='" + "www.cidadessustentaveis.org.br" +"'>www.cidadessustentaveis.org.br</a>" + "</p>";
		
		emailUtil.enviarEmailHTML(listaDestinatarios, "Solicitação de Adesão de Prefeitura", mensagem);
	}

	public Prefeitura alterarCartaCompromisso(PrefeituraDTO prefeituraDTO) {
		if(prefeituraDTO == null || prefeituraDTO.getId() == null) throw new IllegalArgumentException("DTO ou ID nulo");

		Optional<Prefeitura> optional = prefeituraRepository.findById(prefeituraDTO.getId());

		if(!optional.isPresent()) {
			throw new ObjectNotFoundException("Não foi possível encontrar a prefeitura");
		}
		Prefeitura prefeitura = optional.get();
		cartaCompromissoRepository.deletarPorPrefeitura(prefeitura.getId());
		List<CartaCompromisso> cartasCompromissoPrefeitura = prefeituraDTO.getCartaCompromisso();
		for (CartaCompromisso cartaCompromisso : cartasCompromissoPrefeitura) {
			cartaCompromisso.setPrefeitura(prefeitura);
		}
		prefeitura.setCartaCompromisso(cartasCompromissoPrefeitura);
		prefeituraRepository.save(prefeitura);
		return prefeitura;
	}

	public void editar(Prefeitura prefeitura) {
		prefeituraRepository.save(prefeitura);
	}
	
	public void editar(PrefeituraEdicaoDTO prefeituraDto) {
		if(prefeituraDto == null || prefeituraDto.getId() == null) throw new IllegalArgumentException("DTO ou ID nulo");

		Optional<Prefeitura> optional = prefeituraRepository.findById(prefeituraDto.getId());

		if(!optional.isPresent()) {
			throw new ObjectNotFoundException("Não foi possível encontrar a prefeitura");
		}

		Prefeitura prefeitura = optional.get();

		this.preencherPrefeituraEdicao(prefeitura, prefeituraDto);
		this.preencherLocalidadePrefeitura(prefeitura, prefeituraDto);

		prefeituraRepository.save(prefeitura);
	}

	public List<Prefeitura> listar() {
		return prefeituraRepository.findAll();
	}

	public List<Prefeitura> listar(Integer page, Integer itemsPerPage) {
	    return this.listar(page, itemsPerPage, "cidade", "asc");
	}

	public List<Prefeitura> listar(Integer page, Integer itemsPerPage, String orderBy, String direction) {
	    if(page == null) page = 0;
	    if(itemsPerPage == null) itemsPerPage = 10;
	    if(orderBy == null) orderBy = "cidade";
	    if(direction == null) direction = "asc";

	    String orderByColumn = "cidade.nome";

	    if(orderBy.equalsIgnoreCase("estado")) {
            orderByColumn = "cidade.provinciaEstado.nome";
        }

        if(orderBy.equalsIgnoreCase("prefeito")) {
            orderByColumn = "nome";
        }

        if(orderBy.equalsIgnoreCase("partido")) {
            orderByColumn = "partidoPolitico.nome";
        }

        if(orderBy.equalsIgnoreCase("inicio-mandato")) {
            orderByColumn = "inicioMandato";
        }

        if(orderBy.equalsIgnoreCase("fim-mandato")) {
            orderByColumn = "fimMandato";
        }

        Sort sort = Sort.by(orderByColumn).ascending();

        if(direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(orderByColumn).descending();
        }

        return prefeituraRepository.findBySignataria(true, PageRequest.of(page, itemsPerPage, sort)).getContent();
    }

	public Long count() {
		return prefeituraRepository.countBySignataria(true);
	}

	public Prefeitura buscarPorId(Long id) {
		Optional<Prefeitura> prefeitura = prefeituraRepository.findById(id);
		return prefeitura.orElseThrow(() -> new ObjectNotFoundException("Prefeitura não encontrado!"));
	}

	public PrefeituraDTO buscarLogin(Long id) {
		
		PrefeituraDTO prefeitura = prefeituraRepository.buscarLogin(id);
		return prefeitura;
	}
	
	public Prefeitura listarPorIdCidade(Long idCidade) {
		Prefeitura prefeitura = prefeituraRepository.findByIdCidadeOrderByCidadeId(idCidade);
		return prefeitura;
	}

	public List<Prefeitura> listarPorCidade(Cidade cidade) {
		List<Prefeitura> lista = prefeituraRepository.findByCidade(cidade);
		return lista;
	}

	
	private void atualizaReferenciaCidade(Prefeitura prefeitura, PrefeituraDTO prefeituraDTO) {
		prefeitura.setCidade(cidadeService.buscarPorId(prefeituraDTO.getCidade().getId()));
	}

	public void alterarDataMandato(Prefeitura prefeitura, LocalDate inicioMandato, LocalDate fimMandato) {
		prefeitura.setInicioMandato(inicioMandato);
		prefeitura.setFimMandato(fimMandato);
		prefeitura.setSignataria(true);
		cidadeService.alterarCidadeParaSignataria(prefeitura.getCidade().getId());
		prefeituraRepository.save(prefeitura);
		
	}
	
	public IdPaisIdEstadoIdCidadeDTO buscarPaisEstadoCidadePorPrefeitura(Long idPrefeitura) {
		Prefeitura prefeitura = buscarPorId(idPrefeitura);
		return new IdPaisIdEstadoIdCidadeDTO(prefeitura.getCidade().getProvinciaEstado().getPais().getId(), prefeitura.getCidade().getProvinciaEstado().getId(), prefeitura.getCidade().getId());
	}

	@CachePut("prefeituras")
	public List<Prefeitura> listarPrefeiturasSignatariasVigentes() {
		List<Prefeitura> signatarias = prefeituraRepository.buscarPrefeiturasSignatarias();
		Set<Cidade> cidadesEncontradas = signatarias.stream()
														.map(p -> p.getCidade())
													.collect(Collectors.toSet());

		Comparator<Prefeitura> comparatorFimMandato = Comparator.comparing(p -> p.getFimMandato());

		List<Prefeitura> prefeituras = new LinkedList<>();
		for(Cidade c : cidadesEncontradas) {
			Optional<Prefeitura> optionalPrefeituraVigente =
					signatarias.stream()
									.filter(p -> p.getCidade().equals(c))
									.sorted(comparatorFimMandato.reversed())
								.findFirst();
			if(optionalPrefeituraVigente.isPresent()) {
				prefeituras.add(optionalPrefeituraVigente.get());
			}
		}

		Comparator<Prefeitura> comparatorNomeEstado = Comparator.comparing(p -> p.getCidade()
																					.getProvinciaEstado().getNome());
		Comparator<Prefeitura> comparatorNomeCidade = comparatorNomeEstado
															.thenComparing(Comparator
																			.comparing(p -> p.getCidade().getNome()));

		return prefeituras.stream()
								.sorted(comparatorNomeCidade)
							.collect(Collectors.toList());
	}
	

	public ArrayList<List<CidadeMandatosDTO>> buscarCidadesSignatariasDataMandatos() {
		List<CidadeComPrefeituraDTO> cidades = prefeituraRepository.findCidadesSignatariasComPrefeitura();

		ArrayList<List<CidadeMandatosDTO>> signatarias = new ArrayList<List<CidadeMandatosDTO>>();
		for(CidadeComPrefeituraDTO cidade: cidades) {			
			signatarias.add(prefeituraRepository.buscarCidadesSignatariasDataMandatos(cidade.getIdCidade()));
		}
		
		
		
		return signatarias;
	}

	public List<Prefeitura> listarPrefeiturasSignatariasVigentesPorEstado(Long idEstado) {
		List<Prefeitura> signatarias = prefeituraRepository.buscarPrefeiturasSignatariasPorEstado(idEstado);
		Set<Cidade> cidadesEncontradas = signatarias.stream()
														.map(p -> p.getCidade())
													.collect(Collectors.toSet());

		Comparator<Prefeitura> comparatorFimMandato = Comparator.comparing(p -> p.getFimMandato());

		List<Prefeitura> prefeituras = new LinkedList<>();
		for(Cidade c : cidadesEncontradas) {
			Optional<Prefeitura> optionalPrefeituraVigente =
					signatarias.stream()
							.filter(p -> p.getCidade().equals(c))
							.sorted(comparatorFimMandato.reversed())
							.findFirst();
			if(optionalPrefeituraVigente.isPresent()) {
				prefeituras.add(optionalPrefeituraVigente.get());
			}
		}

		Comparator<Prefeitura> comparatorNomeEstado = Comparator.comparing(p -> p.getCidade()
																					.getProvinciaEstado().getNome());
		Comparator<Prefeitura> comparatorNomeCidade = comparatorNomeEstado
															.thenComparing(Comparator
																			.comparing(p -> p.getCidade().getNome()));

		return prefeituras.stream()
								.sorted(comparatorNomeCidade)
							.collect(Collectors.toList());
	}
	
	public List<Prefeitura> listarPrefeituraPorEstadoCidadePartido(Long idEstado, String cidade, String prefeito,
                                                                    Long idPartido, String orderBy, String direction) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Prefeitura> query = cb.createQuery(Prefeitura.class);

		Root<Prefeitura> root = query.from(Prefeitura.class);

		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(cb.isTrue(root.get("signataria")));

		Join<Prefeitura, Cidade> joinCidade = root.join("cidade");
		Join<Cidade, ProvinciaEstado> joinEstado = joinCidade.join("provinciaEstado");
		Join<Prefeitura, PartidoPolitico> joinPartido = root.join("partidoPolitico");

		if(idEstado != null && idEstado != -1) {
			Path<Long> campoEstadoId = joinEstado.get("id");
			predicateList.add(cb.isTrue(campoEstadoId.in(idEstado)));
		}

		if(cidade != null && !cidade.isEmpty()) {
			Path<String> campoNomeCidade = joinCidade.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeCidade), new String("%" + cidade + "%").toLowerCase()));
		}

		if(prefeito != null && !prefeito.isEmpty()) {
			Path<String> campoNomePrefeitura = root.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomePrefeitura),
															new String("%" + prefeito + "%").toLowerCase()));
		}

		if(idPartido != null && idPartido != -1) {
			Path<Long> campoPartidoId = joinPartido.get("id");
			predicateList.add(cb.isTrue(campoPartidoId.in(idPartido)));
		}
		
        List<Order> orderList = new ArrayList<Order>();

        if(orderBy.equalsIgnoreCase("cidade")) {
            Path<String> campoCidadeNome = joinCidade.get("nome");

            if(direction.equalsIgnoreCase("asc")) {
                orderList.add(cb.asc(campoCidadeNome));
            } else {
                orderList.add(cb.desc(campoCidadeNome));
            }
        }
        
		if(orderBy.equalsIgnoreCase("estado")) {
            Path<String> campoEstadoNome = joinEstado.get("nome");

            if(direction.equalsIgnoreCase("asc")) {
                orderList.add(cb.asc(campoEstadoNome));
            } else {
                orderList.add(cb.desc(campoEstadoNome));
            }
        }

        if(orderBy.equalsIgnoreCase("prefeito")) {
            Path<String> campoPrefeitoNome = root.get("nome");

            if(direction.equalsIgnoreCase("asc")) {
                orderList.add(cb.asc(campoPrefeitoNome));
            } else {
                orderList.add(cb.desc(campoPrefeitoNome));
            }
        }

        if(orderBy.equalsIgnoreCase("partido")) {
            Path<String> campoPartidoNome = joinPartido.get("nome");

            if(direction.equalsIgnoreCase("asc")) {
                orderList.add(cb.asc(campoPartidoNome));
            } else {
                orderList.add(cb.desc(campoPartidoNome));
            }
        }

        if(orderBy.equalsIgnoreCase("inicio-mandato")) {
            Path<String> campoInicioMandato = root.get("inicioMandato");

            if(direction.equalsIgnoreCase("asc")) {
                orderList.add(cb.asc(campoInicioMandato));
            } else {
                orderList.add(cb.desc(campoInicioMandato));
            }
        }

        if(orderBy.equalsIgnoreCase("fim-mandato")) {
            Path<String> campoFimMandato = root.get("fimMandato");

            if(direction.equalsIgnoreCase("asc")) {
                orderList.add(cb.asc(campoFimMandato));
            } else {
                orderList.add(cb.desc(campoFimMandato));
            }
        }

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<Prefeitura> prefeiturasEncontradas = em.createQuery(query).getResultList();
		Set<Cidade> cidadesEncontradas = prefeiturasEncontradas.stream()
																	.map(p -> p.getCidade())
																.collect(Collectors.toSet());

		Comparator<Prefeitura> comparatorFimMandato = Comparator.comparing(p -> p.getFimMandato());

		List<Prefeitura> prefeituras = new LinkedList<>();
		for(Cidade c : cidadesEncontradas) {
			Optional<Prefeitura> optionalPrefeituraVigente =
													prefeiturasEncontradas.stream()
																			.filter(p -> p.getCidade().equals(c))
																			.sorted(comparatorFimMandato.reversed())
																		.findFirst();
			if(optionalPrefeituraVigente.isPresent()) {
				prefeituras.add(optionalPrefeituraVigente.get());
			}
		}

		return prefeituras;
	}
	
	public List<Prefeitura> buscarPrefeiturasSignatariasVigentesPorEstadoCidadePartidoPopulacao(Long idEstado, String cidade, String prefeito,
            																		   Long idPartido, Long populacaoMin, Long populacaoMax, String orderBy, String direction) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Prefeitura> query = cb.createQuery(Prefeitura.class);

		Root<Prefeitura> root = query.from(Prefeitura.class);

		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(cb.isTrue(root.get("signataria")));

		Join<Prefeitura, Cidade> joinCidade = root.join("cidade");
		Join<Cidade, ProvinciaEstado> joinEstado = joinCidade.join("provinciaEstado");
		Join<Prefeitura, PartidoPolitico> joinPartido = root.join("partidoPolitico");

		if(idEstado != null && idEstado != -1) {
			Path<Long> campoEstadoId = joinEstado.get("id");
			predicateList.add(cb.isTrue(campoEstadoId.in(idEstado)));
		}

		if(cidade != null && !cidade.isEmpty()) {
			Path<String> campoNomeCidade = joinCidade.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeCidade), new String("%" + cidade + "%").toLowerCase()));
		}

		if(prefeito != null && !prefeito.isEmpty()) {
			Path<String> campoNomePrefeitura = root.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomePrefeitura), new String("%" + prefeito + "%").toLowerCase()));
		}

		if(idPartido != null && idPartido != -1) {
			Path<Long> campoPartidoId = joinPartido.get("id");
			predicateList.add(cb.isTrue(campoPartidoId.in(idPartido)));
		}

		if(populacaoMin != null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacao, populacaoMin));
		}

		if(populacaoMax != null) {
			Path<Long> populacao = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacao, populacaoMax));
		}

		List<Order> orderList = new ArrayList<Order>();

		if(orderBy.equalsIgnoreCase("cidade")) {
			Path<String> campoCidadeNome = joinCidade.get("nome");

			if(direction.equalsIgnoreCase("asc")) {
				orderList.add(cb.asc(campoCidadeNome));
			} else {
				orderList.add(cb.desc(campoCidadeNome));
			}
		}

		if(orderBy.equalsIgnoreCase("populacaoMin")) {
			Path<Long> populacao = joinCidade.get("populacao");

			if(direction.equalsIgnoreCase("asc")) {
				orderList.add(cb.asc(populacao));
			} else {
				orderList.add(cb.desc(populacao));
			}
		}

		if(orderBy.equalsIgnoreCase("populacaoMax")) {
			Path<Long> populacao = joinCidade.get("populacao");

			if(direction.equalsIgnoreCase("asc")) {
				orderList.add(cb.asc(populacao));
			} else {
				orderList.add(cb.desc(populacao));
			}
		}

		if(orderBy.equalsIgnoreCase("estado")) {
			Path<String> campoEstadoNome = joinEstado.get("nome");

			if(direction.equalsIgnoreCase("asc")) {
				orderList.add(cb.asc(campoEstadoNome));
			} else {
				orderList.add(cb.desc(campoEstadoNome));
			}
		}

		if(orderBy.equalsIgnoreCase("prefeito")) {
			Path<String> campoPrefeitoNome = root.get("nome");

			if(direction.equalsIgnoreCase("asc")) {
				orderList.add(cb.asc(campoPrefeitoNome));
			} else {
				orderList.add(cb.desc(campoPrefeitoNome));
			}
		}

		if(orderBy.equalsIgnoreCase("partido")) {
			Path<String> campoPartidoNome = joinPartido.get("nome");

			if(direction.equalsIgnoreCase("asc")) {
				orderList.add(cb.asc(campoPartidoNome));
			} else {
				orderList.add(cb.desc(campoPartidoNome));
			}
		}


		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<Prefeitura> prefeiturasEncontradas = em.createQuery(query).getResultList();
		Set<Cidade> cidadesEncontradas = prefeiturasEncontradas.stream()
																		.map(p -> p.getCidade())
																		.collect(Collectors.toSet());

		Comparator<Prefeitura> comparatorFimMandato = Comparator.comparing(p -> p.getFimMandato());

		List<Prefeitura> prefeituras = new LinkedList<>();
		for(Cidade c : cidadesEncontradas) {
			Optional<Prefeitura> optionalPrefeituraVigente =
					prefeiturasEncontradas.stream()
												  .filter(p -> p.getCidade().equals(c))
												  .sorted(comparatorFimMandato.reversed())
												  .findFirst();
			if(optionalPrefeituraVigente.isPresent()) {
				prefeituras.add(optionalPrefeituraVigente.get());
			}
		}

		return prefeituras;
	}
	
	public List<ItemComboDTO> buscarComboBox() {
		return prefeituraRepository.findComboBoxPrefeitura();
	}

	public byte[] gerarArquivoExcelCidadesSignatarias() throws IOException {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Cidades Signatárias");

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		Row headerRow = sheet.createRow(0);
		List<String> headerTitles = new ArrayList<String>(Arrays.asList("Código IBGE",
                "Nome",
                "Estado",
                "Prefeito",
                "Partido",
                "População"));

		int cellNumber = 0;
		for(String headerTitle : headerTitles) {
			Cell cell = headerRow.createCell(cellNumber);
			cell.setCellValue(headerTitle);
			cell.setCellStyle(headerCellStyle);
			cellNumber++;
		}

		List<Prefeitura> prefeituras = this.listarPrefeiturasSignatariasVigentes();

		int rowNum = 1;
		for(Prefeitura prefeitura : prefeituras) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(prefeitura.getCidade().getCodigoIbge());
			row.createCell(1).setCellValue(prefeitura.getCidade().getNome());
			row.createCell(2).setCellValue(prefeitura.getCidade().getProvinciaEstado().getNome());
			row.createCell(3).setCellValue(prefeitura.getNome());
			row.createCell(4).setCellValue(prefeitura.getPartidoPolitico().getSiglaPartido()
													+ " - " +
													prefeitura.getPartidoPolitico().getNome());
			row.createCell(5).setCellValue(prefeitura.getCidade().getPopulacao() != null ? prefeitura.getCidade().getPopulacao() : 0);
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		workbook.write(output);

		byte[] content = output.toByteArray();

		workbook.close();
		output.close();

		return content;
	}
	
	private Prefeitura preencherPrefeituraEdicao(Prefeitura prefeitura, PrefeituraEdicaoDTO dto) {
		if(prefeitura == null || dto == null) throw new IllegalArgumentException("Prefeitura ou DTO nulo");

		prefeitura.setNome(dto.getNomePrefeito());
		prefeitura.setEmail(dto.getEmail());
		prefeitura.setInicioMandato(dto.getInicioMandato());
		prefeitura.setFimMandato(dto.getFimMandato());

		PartidoPolitico partidoPolitico = partidoPoliticoService.listarPorId(dto.getIdPartido());

		if(partidoPolitico == null) {
			throw new IllegalArgumentException("Partido político não existe");
		}

		if(dto.getCartasCompromisso() != null && !dto.getCartasCompromisso().isEmpty()) {
			if(prefeitura.getCartaCompromisso() != null){
				for(CartaCompromisso carta: prefeitura.getCartaCompromisso()){
					cartaCompromissoRepository.delete(carta.getId());
				}
			}
			prefeitura.setCartaCompromisso(new ArrayList<>());
			for(CartaCompromissoDTO cartaDTO: dto.getCartasCompromisso()){
				CartaCompromisso carta = new CartaCompromisso(cartaDTO.getId(), cartaDTO.getNomeArquivo(),
											cartaDTO.getExtensao(), cartaDTO.getArquivo(), prefeitura );
				prefeitura.getCartaCompromisso().add(carta);
			}
		}

		prefeitura.setPartidoPolitico(partidoPolitico);

		return prefeitura;
	}

	private Prefeitura preencherLocalidadePrefeitura(Prefeitura prefeitura, PrefeituraEdicaoDTO dto) {
		if(prefeitura == null || dto == null) throw new IllegalArgumentException("Prefeitura ou DTO nulo");

		Pais pais = paisService.buscarPorId(dto.getIdPais());
		ProvinciaEstado provinciaEstado = provinciaEstadoService.buscarPorId(dto.getIdEstado());
		Cidade cidade = cidadeService.buscarPorId(dto.getIdCidade());

		if(cidade == null || provinciaEstado == null || pais == null) {
			throw new IllegalStateException("Localidade da prefeitura é inválida");
		}

		boolean cidadePertenceAoEstado = cidade.getProvinciaEstado().getId().equals(provinciaEstado.getId());
		boolean estadoPertenceAoPais = provinciaEstado.getPais().getId().equals(pais.getId());

		if(!cidadePertenceAoEstado || !estadoPertenceAoPais) {
			throw new IllegalStateException("Localidade da prefeitura é inválida");
		}

		prefeitura.setCidade(cidade);

		return prefeitura;
	}
	
	public List<List<String>> geraListaCartas(String nomeArquivoCSV){
		File arquivoCSV = new File (nomeArquivoCSV);
		List<List<String>> lista = new ArrayList<List<String>>();

		try{
			String linhasDoArquivo = new String();
			Scanner leitor = new Scanner(arquivoCSV);
			while(leitor.hasNext()){ 
				linhasDoArquivo = leitor.nextLine();
				lista.add(Arrays.asList(linhasDoArquivo.split(";")));
			}	
			leitor.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		return lista;
	}
	
	public void criarCartas(List<List<String>> registros,String path){
		for( List<String> linha : registros ) {
	    	try { 
	    		if(linha.size() > 1) {				
	    			Long idPrefeitura  = Long.parseLong(linha.get(0).trim());
	    			String nomeArquivo = linha.get(1).toString();
	    			String b64 = PDFUtils.Pdf2String(path+nomeArquivo);
	    			Prefeitura prefeitura = buscarPorId(idPrefeitura);
	    			CartaCompromisso cartaCompromisso = new CartaCompromisso(null, nomeArquivo, "pdf", b64, prefeitura);
	    			prefeitura.setCartaCompromisso( new ArrayList<>());
	    			prefeitura.getCartaCompromisso().add(cartaCompromisso);
	    			editar(prefeitura);	    			
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
			}
		}
		
	}
	
	public List<Prefeitura> buscarPrefeiturasSignatarias() {
		return prefeituraRepository.buscarPrefeiturasSignatarias();
	}
	
	public List<CidadeComboDTO> buscarCidadesSignatarias() {
		return prefeituraRepository.buscarCidadesSignatarias();		
	}
	
	public Prefeitura buscarAtualPorCidade(Long idCidade) {
		return prefeituraRepository.findTopByCidadeIdAndSignatariaOrderByIdDesc(idCidade,true);
	}
	
	public Prefeitura buscarUltimaPrefeituraExistentePorCidade(Long idCidade) {
		return prefeituraRepository.findTopByCidadeIdAndInicioMandatoIsNotNullOrderByIdDesc(idCidade);
	}

	public PrefeituraDTO buscarDetalhesParaAprovacao(Long id) {
		Prefeitura pref = buscarPorId(id);
		return new PrefeituraDTO(pref.getId(), pref.getCidade().getNome(), pref.getCargo(), pref.getNome(), pref.getEmail(), pref.getTelefone(), pref.getCartaCompromisso());
	}
	
	public Prefeitura buscarPrefeituraPorIdUsuario(Long idUsuario) {
		return prefeituraRepository.buscarPrefeituraPorIdUsuario(idUsuario);
	}
	
	public List<RelatorioPlanoDeMetasPrestacaoDeContasDTO> gerarRelatorioPlanoDeMetasPrestacaoDeContas(Long idCidade) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioPlanoDeMetasPrestacaoDeContasDTO> query = cb.createQuery(RelatorioPlanoDeMetasPrestacaoDeContasDTO.class);

		Root<HistoricoPlanoMetasPrestacaoContas> historico = query.from(HistoricoPlanoMetasPrestacaoContas.class);
		
		Join<HistoricoPlanoMetasPrestacaoContas, Prefeitura> joinPrefeitura = historico.join("prefeitura", JoinType.INNER);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade", JoinType.INNER);
		Join<Cidade, ProvinciaEstado> joinEstado = joinCidade.join("provinciaEstado", JoinType.INNER);
		
		query.multiselect(joinEstado.get("nome"), joinEstado.get("sigla"), joinCidade.get("nome"), joinCidade.get("codigoIbge"), joinPrefeitura.get("inicioMandato"), 
				joinPrefeitura.get("fimMandato"), historico.get("planoMetas"), historico.get("dataHoraPlanoMetas"), 
				historico.get("prestacaoContas"), historico.get("dataHoraPrestacaoContas"), historico);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (idCidade != null) {
			Path<Long> cidade = joinCidade.get("id");
			predicateList.add(cb.equal(cidade, idCidade));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<RelatorioPlanoDeMetasPrestacaoDeContasDTO> typedQuery = em.createQuery(query);
		List<RelatorioPlanoDeMetasPrestacaoDeContasDTO> lista = typedQuery.getResultList();

		return lista;
	}
	
	public Prefeitura buscarPorIdCidadeUltimaPrefeitura(Long idCidade) {
		List<Prefeitura> lista = prefeituraRepository.buscarPorIdCidadeUltimaPrefeitura(idCidade);
		if(lista != null && !lista.isEmpty()) {
			return lista.get(0);
		}else {
			return null;
		}
	}
	
	public List<CidadeComPrefeituraDTO> buscarCidadesComPrefeitura() {
		List<CidadeComPrefeituraDTO> cidadesComPrefeitura = prefeituraRepository.findCidadesComPrefeitura();
		return cidadesComPrefeitura;
	}

	public void inativarPrefeituras() {
		LocalDate hoje = LocalDate.now();
		List<Prefeitura> prefeituras = this.listar();
		for( Prefeitura prefeitura : prefeituras ) {
			try {
				if( prefeitura.getSignataria() != null && prefeitura.getInicioMandato() != null
					&& prefeitura.getFimMandato() != null) {
						if(prefeitura.getSignataria() && prefeitura.getFimMandato().isBefore(hoje) && 
							prefeitura.getInicioMandato().isBefore(hoje)) {
							List<Usuario> usuarios = usuarioService.getUsuariosPrefeitura(prefeitura.getId());
							for(Usuario usuario : usuarios) {
								if(!usuario.getCredencial().getSnExcluido()){
									usuario.getCredencial().setSnExcluido(true);
									usuarioService.salvar(usuario);
								}
							}
			
							prefeitura.setSignataria(false);
							prefeitura.getCidade().setIsSignataria(false);
							prefeituraRepository.save(prefeitura);
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void liberarCredencialPrefeituras() {
		LocalDate hoje = LocalDate.now();
		List<Prefeitura> prefeituras = this.listar();
		for( Prefeitura prefeitura : prefeituras ) {
			try {
				if( prefeitura.getSignataria() != null 
					&& prefeitura.getInicioMandato() != null
					&& prefeitura.getFimMandato() != null) {
						if(!prefeitura.getSignataria() && DateUtil.entre(hoje, prefeitura.getInicioMandato(), prefeitura.getFimMandato()) ) {
							List<Usuario> usuarios = usuarioService.getUsuariosPrefeitura(prefeitura.getId());
							for(Usuario usuario : usuarios) {
								if(usuario.getCredencial().getSnBloqueado()){
									usuario.getCredencial().setSnBloqueado(false);
								}
							}
							prefeitura.setSignataria(true);
							prefeitura.getCidade().setIsSignataria(true);
							prefeituraRepository.save(prefeitura);
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void importarPrefeitura(PrefeituraImportacaoDTO dto) {
		Prefeitura prefeitura = new Prefeitura();
		prefeitura.setNome(dto.getNome());
		prefeitura.setEmail(dto.getEmail());
		prefeitura.setTelefone(dto.getTelefone());
		// prefeitura.setCelular(dto.getCelular());
		prefeitura.setCargo(dto.getCargo());
		
		Cidade cidade = cidadeService.findByCodigoIbge(dto.getIbge());
		prefeitura.setCidade(cidade);
		
		PartidoPolitico partido = partidoPoliticoService.findBySigla(dto.getPartido());
		prefeitura.setPartidoPolitico(partido);

		prefeitura.setSignataria(false);
		prefeitura.setInicioMandato(dto.getDataInicio());
		prefeitura.setFimMandato(dto.getDataFim());

		prefeituraRepository.save(prefeitura);
		AprovacaoPrefeitura aprovacao = aprovacaoPrefeituraService.criarPedidoAprovacao(prefeitura);
		AprovacaoPrefeituraSimplesDTO aprovacaoDTO = new AprovacaoPrefeituraSimplesDTO();
		// TODO Adicionar Datas
		aprovacaoDTO.setFimMandato(dto.getDataFim());
		aprovacaoDTO.setInicioMandato(dto.getDataInicio());
		aprovacaoDTO.setId(aprovacao.getId());
		aprovacaoPrefeituraService.aprovar(aprovacaoDTO);

	}


	
	public List<CidadeMandatosDTO> buscarCidadesSignatariasDataMandatosPorIdCidade(Long idCidade) {
		List<CidadeMandatosDTO> cidadeMandatosDTO = prefeituraRepository.buscarPrefeiturasSignatariasPorIdCidade(idCidade);
		
		if(cidadeMandatosDTO != null && !cidadeMandatosDTO.isEmpty()) {
			return cidadeMandatosDTO;
		}
		return new ArrayList<>();
	}
	
	public List<CidadeComBoasPraticasDTO> buscarPrefeiturasSignatariasComBoasPraticas() {
		List<CidadeComBoasPraticasDTO> cidadeComBoasPraticasDTO = prefeituraRepository.buscarPrefeiturasSignatariasComBoasPraticas();
		
		if(cidadeComBoasPraticasDTO != null && !cidadeComBoasPraticasDTO.isEmpty()) {
			return cidadeComBoasPraticasDTO;
		}
		return new ArrayList<>();
	}
	
	public List<PrefeituraDTO> buscarMandatoPorIdCidade(Long idCidade) {
		return prefeituraRepository.buscarMandatoPorIdCidade(idCidade);
	}

	@CachePut("prefeituras")
	public List<Prefeitura> listarDadosCadastraisPrefeiturasSignatariasVigentes() {
		List<Prefeitura> signatarias = prefeituraRepository.buscarDadosCadastraisPrefeiturasSignatarias();
		Set<Cidade> cidadesEncontradas = signatarias.stream()
														.map(p -> p.getCidade())
													.collect(Collectors.toSet());

		Comparator<Prefeitura> comparatorFimMandato = Comparator.comparing(p -> p.getFimMandato());

		List<Prefeitura> prefeituras = new LinkedList<>();
		for(Cidade c : cidadesEncontradas) {
			Optional<Prefeitura> optionalPrefeituraVigente =
					signatarias.stream()
									.filter(p -> p.getCidade().equals(c))
									.sorted(comparatorFimMandato.reversed())
								.findFirst();
			if(optionalPrefeituraVigente.isPresent()) {
				prefeituras.add(optionalPrefeituraVigente.get());
			}
		}

		Comparator<Prefeitura> comparatorNomeEstado = Comparator.comparing(p -> p.getCidade()
																					.getProvinciaEstado().getNome());
		Comparator<Prefeitura> comparatorNomeCidade = comparatorNomeEstado
															.thenComparing(Comparator
																			.comparing(p -> p.getCidade().getNome()));

		return prefeituras.stream()
								.sorted(comparatorNomeCidade)
							.collect(Collectors.toList());
	}
}
