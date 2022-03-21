package br.org.cidadessustentaveis.services;

import static br.org.cidadessustentaveis.util.VariavelPreenchidaUtil.VALOR_REF_NAO_SE_APLICA_INDICADOR;
import static br.org.cidadessustentaveis.util.VariavelPreenchidaUtil.VALOR_REF_NAO_SE_APLICA_VARIAVEL;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.wololo.geojson.Feature;

import br.org.cidadessustentaveis.dto.AvaliacaoVariavelDTO;
import br.org.cidadessustentaveis.dto.CidadeComboDTO;
import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.CidadeMapaDTO;
import br.org.cidadessustentaveis.dto.CidadeQtIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.EixoDTO;
import br.org.cidadessustentaveis.dto.FatorDesigualdadeDTO;
import br.org.cidadessustentaveis.dto.FiltroIndicadoresDTO;
import br.org.cidadessustentaveis.dto.IndicadorDTO;
import br.org.cidadessustentaveis.dto.IndicadorDadosAbertosDTO;
import br.org.cidadessustentaveis.dto.IndicadorIntegracaoDTO;
import br.org.cidadessustentaveis.dto.IndicadorParaComboDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoIntegracaoDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoMapaDTO;
import br.org.cidadessustentaveis.dto.IndicadorPreenchidoSimplesDTO;
import br.org.cidadessustentaveis.dto.Mandato2DTO;
import br.org.cidadessustentaveis.dto.ObjetivoDesenvolvimentoSustentavelDTO;
import br.org.cidadessustentaveis.dto.ResultadoIndicadorDTO;
import br.org.cidadessustentaveis.dto.ResultadoIndicadorDTO.ResultadoIndicadorDTOBuilder;
import br.org.cidadessustentaveis.dto.ResultadoVariavelDTO;
import br.org.cidadessustentaveis.dto.TabelaComparativoCidadeDTO;
import br.org.cidadessustentaveis.dto.TabelaIndicadorDTO;
import br.org.cidadessustentaveis.dto.ValorPorCidadeDTO;
import br.org.cidadessustentaveis.dto.ValorPorCidadeFiltradoDTO;
import br.org.cidadessustentaveis.dto.VariavelPreenchidaDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Orgao;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoVariavel;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoIndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.repository.IndicadorPreenchidoRepository;
import br.org.cidadessustentaveis.repository.IndicadorRepository;
import br.org.cidadessustentaveis.repository.InstituicaoFonteRepository;
import br.org.cidadessustentaveis.repository.SubdivisaoIndicadorPreenchidoRepository;
import br.org.cidadessustentaveis.repository.SubdivisaoVariavelPreenchidaRepository;
import br.org.cidadessustentaveis.repository.ValorReferenciaRepository;
import br.org.cidadessustentaveis.repository.VariaveisOpcoesRepository;
import br.org.cidadessustentaveis.repository.VariavelPreenchidaRepository;
import br.org.cidadessustentaveis.services.exceptions.BusinessLogicErrorException;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import br.org.cidadessustentaveis.util.MandatoUtil;
import br.org.cidadessustentaveis.util.NumeroUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import br.org.cidadessustentaveis.util.VariavelPreenchidaUtil;

@Service
public class IndicadorPreenchidoService {

	@Autowired
	private IndicadorPreenchidoRepository repository;

	@Autowired
	private SubdivisaoIndicadorPreenchidoRepository subdivisaoIndicadorRepository;
	@Autowired
	private SubdivisaoVariavelPreenchidaRepository subdivisaoVariavelRepository;

	@Autowired
	private InstituicaoFonteRepository instituicaoFonteRepository;
	@Autowired
	private InstituicaoFonteService instituicaoFonteService;

	@Autowired
	private VariavelPreenchidaRepository variavelPreenchidaRepository;

	@Autowired
	private ValorReferenciaRepository valorReferenciaRepository;

	@Autowired
	private IndicadorRepository indicadorRepository;
	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private CalculadoraFormulaUtil calculadora;

	@Autowired
	VariavelPreenchidaUtil variavelPreenchidaUtil;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;

	@Autowired
	VariaveisOpcoesRepository variaveisOpcoesRepository;

	@Autowired
	private EntityManager em;

	@Autowired
	private IndicadorService indicadorService;

	@Autowired
	private OrgaoService orgaoService;

	@Autowired
	private PrefeituraService prefeituraService;

	@Autowired
	private ShapeFileService shapeFileService;

	@Autowired
	private SubdivisaoService subdivisaoService;

	@Autowired
	private VariavelPreenchidaService variavelPrenchidaService;
	
	@Autowired
	private VisualizacaoCartograficaService visualizacaoCartograficaService;

	private Object put;

	public List<IndicadorPreenchido> buscar() {
		return repository.findAll();
	}

	public List<IndicadorPreenchido> buscarPorAno(Indicador indicador, Short ano) {
		return repository.findByIndicadorAndAno(indicador, ano);
	}

	public Optional<IndicadorPreenchido> buscarPorAno(Prefeitura prefeitura, Indicador indicador, Short ano) {
		return repository.findByPrefeituraAndIndicadorAndAno(prefeitura, indicador, ano);
	}

	public List<IndicadorPreenchido> findByPrefeituraAndIndicador(Prefeitura prefeitura, Indicador indicador) {
		return repository.findByPrefeituraAndIndicador(prefeitura, indicador);
	}

	public IndicadorPreenchido buscarPorId(Long id) {
		Optional<IndicadorPreenchido> encontrado = repository.findById(id);

		return encontrado.orElseThrow(() -> new ObjectNotFoundException("Indicador não encontrado!"));
	}

	public List<IndicadorPreenchidoSimplesDTO> listarPorIndicadorECidades(Long idIndicador, List<Long> cidades) {
		if (cidades.isEmpty()) {
			return repository.findByIndicadorIdSimples(idIndicador);
		} else {
			return repository.findByIndicadorAndCidade(idIndicador, cidades);
		}
	}

	public List<IndicadorPreenchido> buscarPorIndicador(Prefeitura prefeitura, Indicador indicador) {
		return repository.findByPrefeituraAndIndicador(prefeitura, indicador);
	}

	public List<IndicadorPreenchido> buscarPorPrefeitura(Prefeitura prefeitura) {
		return repository.findByPrefeitura(prefeitura);
	}

	public List<VariavelPreenchida> buscarVariaveisPreenchidasPorIndicador(Prefeitura prefeitura, Indicador indicador) {
		return variavelPreenchidaRepository.findByIndicador(prefeitura, indicador);
	}

	public void salvar(IndicadorPreenchido indicadorPreenchido) {
		repository.save(indicadorPreenchido);
	}

	// PREENCHER INDICADOR
	public IndicadorPreenchido preencherIndicador(Prefeitura prefeitura, IndicadorPreenchidoDTO dto) {

		Optional<Indicador> indicador = indicadorRepository.findById(dto.getIdIndicador());

		Optional<IndicadorPreenchido> indicadorPorAno = repository.findByPrefeituraAndIndicadorAndAno(prefeitura,
				indicador.orElseThrow(() -> new ObjectNotFoundException("Indicador não encontrado!")), dto.getAno());

		IndicadorPreenchido indicadorPreenchido = indicadorPorAno.orElse(new IndicadorPreenchido());
		if (indicadorPreenchido == null || indicadorPreenchido.getId() == null) {
			indicadorPreenchido = IndicadorPreenchido.builder()
					.indicador(indicador.orElseThrow(() -> new ObjectNotFoundException("Indicador não encontrado!")))
					.ano(dto.getAno()).variaveisPreenchidas(new ArrayList<>()).justificativa(dto.getJustificativa())
					.prefeitura(prefeitura).dataPreenchimento(LocalDateTime.now()).build();
		}

		Boolean possuiJustificativa = possuiJustificativa(dto);
		indicadorPreenchido.setVariaveisPreenchidas(new ArrayList<>());
		for (VariavelPreenchidaDTO variavelPreenchida : dto.getVariaveisPreenchidas()) {
			variavelPreenchida.setAno(dto.getAno());

			VariavelPreenchida preenchida = preencherVariavel(prefeitura, indicador.get(), variavelPreenchida,
					possuiJustificativa);
			indicadorPreenchido.getVariaveisPreenchidas().add(preenchida);
		}

		IndicadorPreenchido entity = null;
		try {
			entity = repository.save(indicadorPreenchido);
			entity = calculaResultadoIndicador(entity);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Indicador já foi preenchido para o ano de " + dto.getAno());
		}

		return entity;
	}

	// PREENCHER VARIÁVEL
	public VariavelPreenchida preencherVariavel(Prefeitura prefeitura, Indicador indicador,
			VariavelPreenchidaDTO variavel, Boolean possuiJustificativa) {
		Optional<Variavel> optional = indicador.getVariaveis().stream()
				.filter(v -> variavel.getIdVariavel().equals(v.getId())).findFirst();
		Variavel encontrada = optional.orElseThrow(
				() -> new ObjectNotFoundException("Variável não encontrada para o indicador " + indicador.getNome()));

		TipoVariavel tipo = TipoVariavel.fromString(encontrada.getTipo());
		Boolean naoPodeSalvarAsVariaveis = !possuiJustificativa;
		variavelPreenchidaUtil.validarValorPreenchido(naoPodeSalvarAsVariaveis, encontrada, tipo, variavel);

		VariavelPreenchida variavelToInsert = null;

		InstituicaoFonte inf = new InstituicaoFonte();

		Optional<VariavelPreenchida> variavelPreenchida = variavelPreenchidaRepository
				.findByPrefeituraAndVariavelAndAno(prefeitura, encontrada, variavel.getAno());

		if (variavel.getFonte() != null) {
			inf = instituicaoFonteService.getById(variavel.getFonte());
		} else {
			inf = instituicaoFonteService.getByNomeAndCidadeId(variavel.getFonteTexto(),
					prefeitura.getCidade().getId());
			if (inf == null) {
				inf = new InstituicaoFonte();
				Orgao o = variavel.getOrgao() != null ? orgaoService.buscarPorId(variavel.getOrgao()) : null;
				inf.setOrgao(o);
				inf.setNome(variavel.getFonteTexto());
				inf.setCidade(prefeitura.getCidade());
				inf = instituicaoFonteRepository.save(inf);
			}
		}
		if (variavelPreenchida.isPresent()) {
			variavelToInsert = variavelPreenchida.get();
			variavelToInsert.setAno(variavel.getAno());
			variavelToInsert.setObservacao(variavel.getObservacao());
			variavelToInsert.setDataPreenchimento(variavel.getDataPreenchimento());
			variavelToInsert.setStatus(variavel.getStatus());
			variavelToInsert.setInstituicaoFonte(inf);
		} else {

			variavelToInsert = VariavelPreenchida.builder().variavel(encontrada).ano(variavel.getAno())
					.observacao(variavel.getObservacao()).dataPreenchimento(variavel.getDataPreenchimento())
					.status(variavel.getStatus()).instituicaoFonte(inf).prefeitura(prefeitura).build();
		}

		if (tipo == TipoVariavel.SIM_NAO) {
			variavelToInsert.setRespostaSimples(variavel.getRespostaSimples());
		}

		if (tipo == TipoVariavel.SIM_NAO_COM_LISTA_OPCOES) {
			variavelToInsert.setRespostaSimples(variavel.getRespostaSimples());
			if (encontrada.isMultiplaSelecao()) {
				variavelToInsert.setOpcoes(this.atribuirOpcao(variavel.getIdOpcoes()));
			} else {
				if (variavel.getIdOpcao() != null && variavel.getIdOpcao() != 0) {
					VariaveisOpcoes opcao = encontrada.getVariavelResposta().getListaOpcoes().stream()
							.filter(variavelOpcao -> variavelOpcao.getId().equals(variavel.getIdOpcao())).findFirst()
							.orElseThrow(() -> new ObjectNotFoundException(
									"Opção não encontrada para a variável " + encontrada.getNome()));
					variavelToInsert.setOpcao(opcao);
				}
			}

		}

		if (tipo == TipoVariavel.LISTA_OPCOES) {
			if (encontrada.isMultiplaSelecao()) {
				variavelToInsert.setOpcoes(this.atribuirOpcao(variavel.getIdOpcoes()));
			} else {
				VariaveisOpcoes opcao = encontrada.getVariavelResposta().getListaOpcoes().stream()
						.filter(variavelOpcao -> variavelOpcao.getId().equals(variavel.getIdOpcao())).findFirst()
						.orElseThrow(() -> new ObjectNotFoundException(
								"Opção não encontrada para a variável " + encontrada.getNome()));
				variavelToInsert.setOpcao(opcao);
			}
		}

		if (tipo == TipoVariavel.INTEIRO || tipo == TipoVariavel.DECIMAL) {
			variavelToInsert.setValor(variavel.getValor());
		}
		if (tipo == TipoVariavel.TEXTO_LIVRE) {
			variavelToInsert.setValorTexto(variavel.getValorTexto());
			variavelToInsert.setValor(null);
		}
		return variavelToInsert;
	}

	// EDITAR INDICADOR
	public IndicadorPreenchido editar(Prefeitura prefeitura, IndicadorPreenchidoDTO dto) {
		Optional<Indicador> optional = indicadorRepository.findById(dto.getIdIndicador());
		Indicador indicador = optional.orElseThrow(() -> new ObjectNotFoundException("Indicador não encontrado!"));
		Optional<IndicadorPreenchido> indicadorPorAno = repository.findByPrefeituraAndIndicadorAndAno(prefeitura,
				indicador, dto.getAno());

		if (!indicadorPorAno.isPresent()) {
			IndicadorPreenchido ip = preencherIndicador(prefeitura, dto);
			return ip;
		}
		IndicadorPreenchido indicadorPreenchido = indicadorPorAno.orElseThrow(
				() -> new ObjectNotFoundException("Indicador preenchido não encontrado para ano " + dto.getAno()));

		indicadorPreenchido.setAno(dto.getAno());
		indicadorPreenchido.setJustificativa(dto.getJustificativa());

		Boolean possuiJustificativa = possuiJustificativa(dto);

		Map<Long, VariavelPreenchida> variavelPreenchidas = new HashMap<>();
		indicadorPreenchido.getVariaveisPreenchidas().forEach(variavelPreenchida -> {
			variavelPreenchidas.put(variavelPreenchida.getVariavel().getId(), variavelPreenchida);
		});

		dto.getVariaveisPreenchidas().forEach(variavel -> {

			variavel.setAno(dto.getAno());
			VariavelPreenchida preenchidaToUpdate = preencherVariavel(prefeitura, indicadorPreenchido.getIndicador(),
					variavel, possuiJustificativa);
			VariavelPreenchida preenchida = variavelPreenchidas.get(preenchidaToUpdate.getVariavel().getId());
			if (preenchida != null) {
				preenchida.setAno(preenchidaToUpdate.getAno());
				preenchida.setValor(preenchidaToUpdate.getValor());
				preenchida.setRespostaSimples(preenchidaToUpdate.getRespostaSimples());
				preenchida.setOpcao(preenchidaToUpdate.getOpcao());
				preenchida.setObservacao(preenchidaToUpdate.getObservacao());
				preenchida.setDataPreenchimento(preenchidaToUpdate.getDataPreenchimento());
				preenchida.setStatus(preenchidaToUpdate.getStatus());
				preenchida.setValorTexto(preenchidaToUpdate.getValorTexto());
				preenchida.setInstituicaoFonte(preenchidaToUpdate.getInstituicaoFonte());
				if (!indicadorPreenchido.getVariaveisPreenchidas().contains(preenchida)) {
					indicadorPreenchido.getVariaveisPreenchidas().add(preenchida);
				} else {
					indicadorPreenchido.getVariaveisPreenchidas().remove(preenchida);
					indicadorPreenchido.getVariaveisPreenchidas().add(preenchida);
				}
			} else {
				indicadorPreenchido.getVariaveisPreenchidas().add(preenchidaToUpdate);
			}
		});

		try {
			IndicadorPreenchido entity = repository.save(indicadorPreenchido);
			entity = calculaResultadoIndicador(entity);

			return entity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultadoIndicadorDTO searchResultadoIndicadorPorId(Long id) {
		return searchResultadoIndicadorPorId(null, id);
	}

	public ResultadoIndicadorDTO searchResultadoIndicadorPorId(Prefeitura prefeitura, Long id) {
		Boolean possuiVariaveisParaAvaliar = false;
		IndicadorPreenchido preenchido = buscarPorId(id);
		preenchido.getVariaveisPreenchidas().size();
		if (null == preenchido.getResultado()) {
			throw new BusinessLogicErrorException("Resultado não encontrado para este indicador");
		}
		Double valorResultado = (null == preenchido.getResultadoReferencia())
				? Double.parseDouble(preenchido.getResultado())
				: preenchido.getResultadoReferencia();
		Optional<ValorReferencia> resultadoIndicador;

		if (null == prefeitura)
			resultadoIndicador = repository.findValorReferenciaByIndicadorPreenchido(preenchido, valorResultado);
		else
			resultadoIndicador = repository.findValorReferenciaByIndicadorPreenchido(prefeitura, preenchido,
					valorResultado);

		ValorReferencia referenciaIndicador = resultadoIndicador.isPresent() ? resultadoIndicador.get()
				: VALOR_REF_NAO_SE_APLICA_INDICADOR;

		ResultadoIndicadorDTOBuilder builder = ResultadoIndicadorDTO.builder();

		ArrayList<ResultadoVariavelDTO> resultadoVariaveis = new ArrayList<ResultadoVariavelDTO>();
		List<Variavel> variaveis = preenchido.getIndicador().getVariaveis();
		Map<Variavel, VariavelPreenchida> map = preenchido.getVariaveisPreenchidas().stream()
				.collect(Collectors.toMap(VariavelPreenchida::getVariavel, preenchida -> preenchida));

		if (map.size() != variaveis.size()) {
			throw new BusinessLogicErrorException(
					"Valor de Referência não encontrado para este indicador. Alguma variável não está preenchida");
		}

		for (Variavel variavel : variaveis) {
			Double valor = variavelPreenchidaUtil.getValorVariavelPreenchida(map.get(variavel));

			Optional<ValorReferencia> resultadoVariavel = valorReferenciaRepository
					.findValorReferenciaByVariavelAndValor(variavel, valor);
			ValorReferencia referenciaVariavel = resultadoVariavel.isPresent() ? resultadoVariavel.get()
					: VALOR_REF_NAO_SE_APLICA_VARIAVEL;
			if (map.get(variavel).getStatus() != null && map.get(variavel).getStatus().equals("Aguardando Avaliação")) {
				referenciaVariavel = VariavelPreenchidaUtil.AGUARDANDO_AVALIACAO;
				possuiVariaveisParaAvaliar = true;
			}

			resultadoVariaveis.add(new ResultadoVariavelDTO(variavel.getNome(), referenciaVariavel.getCor(),
					referenciaVariavel.getLabel()));
		}

		if (possuiVariaveisParaAvaliar) {
			referenciaIndicador = VariavelPreenchidaUtil.AGUARDANDO_AVALIACAO;
		}

		builder.nome(preenchido.getIndicador().getNome());
		builder.resultado(preenchido.getResultado());
		builder.label(referenciaIndicador.getLabel());
		builder.cor(referenciaIndicador.getCor());
		builder.resultadoVariaveis(resultadoVariaveis);

		return builder.build();
	}

	private boolean possuiJustificativa(IndicadorPreenchidoDTO dto) {
		if (dto.getJustificativa() != null && dto.getJustificativa().length() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public List<IndicadorPreenchido> findByIndicadorAnoPrefeitura(Long idIndicador, short anoInicio, short anoFim,
			Long idPrefeitura) {
		return repository.findByIndicadorAnoPrefeitura(idIndicador, anoInicio, anoFim, idPrefeitura);
	}

	private IndicadorPreenchido calculaResultadoIndicador(IndicadorPreenchido entity) {
		calculadora.calcularResultado(entity);
		entity = repository.save(entity);
		return entity;
	}

	private SubdivisaoIndicadorPreenchido calculaResultadoIndicador(SubdivisaoIndicadorPreenchido entity) {
		calculadora.calcularResultado(entity);
		entity = this.subdivisaoIndicadorRepository.save(entity);
		return entity;
	}

	public List<AvaliacaoVariavelDTO> findByAvaliacaoVariavelPorCidade() {
		return repository.findByAvaliacaoVariavelPorCidade();
	}

	public List<AvaliacaoVariavelDTO> findByAvaliacaoVariavelPorCidadeData() {
		return repository.findByAvaliacaoVariavelPorCidadeData();
	}

	public List<CidadeQtIndicadorPreenchidoDTO> contarQuantidadeIndicadoresPreenchidos() {
		return repository.contarQuantidadeIndicadoresPreenchidos();
	}

	public void atualizarIndicadoresPreenchidos(VariavelPreenchida variavelPreenchida) {
		List<IndicadorPreenchido> indicadores = repository.findByVariaveisPreenchidas(variavelPreenchida);
		for (IndicadorPreenchido preenchido : indicadores) {
			calculadora.calcularResultado(preenchido);
			salvar(preenchido);
		}
	}

	public List<IndicadorPreenchido> buscarPorIndicadorCidade(Indicador indicador, Long idCidade) {
		List<IndicadorPreenchido> lista = repository.findByPrefeituraCidadeIdAndIndicadorIdOrderByAno(idCidade,
				indicador.getId());
		return lista;
	}

	public List<IndicadorPreenchido> buscarPorIndicadorCidadeInicioFim(Long indicador, Long cidade, Short anoInicial,
			Short anoFinal) {
		List<IndicadorPreenchido> lista = repository
				.findByPrefeituraCidadeIdAndIndicadorIdAndAnoBetweenOrderByAno(cidade, indicador, anoInicial, anoFinal);
		return lista;
	}

	private List<VariaveisOpcoes> atribuirOpcao(List<Long> opcoes) {
		List<VariaveisOpcoes> lista = new ArrayList<>();
		if (opcoes != null) {
			for (Long id : opcoes) {
				Optional<VariaveisOpcoes> op = variaveisOpcoesRepository.findById(id);
				VariaveisOpcoes var = op.get();
				lista.add(var);
			}
		} else {
			return null;
		}
		return lista;

	}

	public List<IndicadorPreenchido> buscarPorCidade(Cidade cidade) {
		List<IndicadorPreenchido> indicadoresPreenchidos = repository.findByPrefeituraCidade(cidade);
		return indicadoresPreenchidos;
	}

	public List<IndicadorParaComboDTO> buscarIndicadorJaPreenchido(Long idEstado, Long idCidade, Integer populacao,
			Long idEixo, Long idODS) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorParaComboDTO> query = cb.createQuery(IndicadorParaComboDTO.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		query.multiselect(indicadorPreenchido.get("indicador"));

		List<Predicate> predicateList = new ArrayList<>();
		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = null;
		Join<Prefeitura, Cidade> joinCidade = null;

		if (idEstado != null) {
			joinPrefeitura = indicadorPreenchido.join("prefeitura");
			joinCidade = joinPrefeitura.join("cidade");
			Join<Cidade, ProvinciaEstado> joinEstado = joinCidade.join("provinciaEstado");

			Path<Long> campoIdEstado = joinEstado.get("id");
			predicateList.add(cb.equal(campoIdEstado, idEstado));

			if (idCidade != null) {
				Path<Long> campoIdCidade = joinCidade.get("id");
				predicateList.add(cb.equal(campoIdCidade, idCidade));
			}
		}

		if (populacao != null) {
			if (joinPrefeitura == null && joinCidade == null) {
				joinPrefeitura = indicadorPreenchido.join("prefeitura");
				joinCidade = joinPrefeitura.join("cidade");
			}
			Path<Integer> campoPopulacao = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(campoPopulacao, populacao));
		}

		Join<IndicadorPreenchido, Indicador> joinIndicador = null;
		if (idEixo != null) {
			joinIndicador = indicadorPreenchido.join("indicador");
			Join<Indicador, Eixo> joinEixo = joinIndicador.join("eixo");
			Path<Long> campoIdEixo = joinEixo.get("id");
			predicateList.add(cb.equal(campoIdEixo, idEixo));
		}

		if (idODS != null) {
			if (joinIndicador == null) {
				joinIndicador = indicadorPreenchido.join("indicador");
			}
			Join<Indicador, ObjetivoDesenvolvimentoSustentavel> joinODS = joinIndicador.join("ods");
			Path<Long> campoIdODS = joinODS.get("id");
			predicateList.add(cb.equal(campoIdODS, idODS));
		}

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		query.distinct(true);

		List<IndicadorParaComboDTO> dtos = em.createQuery(query).getResultList();

		return dtos;
	}

	public List<TabelaIndicadorDTO> buscarPorIndicadorParaTabela(Long idIndicador, List<Long> cidades) {
		List<IndicadorPreenchido> indicadoresPreenchidos = new ArrayList<>();
		if (cidades != null && !cidades.isEmpty()) {
			indicadoresPreenchidos = repository.findByIndicadorIdAndPrefeituraCidadeIdIn(idIndicador, cidades);
		} else {
			indicadoresPreenchidos = repository.findByIndicadorId(idIndicador);
		}
		List<List<String>> mandatos = MandatoUtil.getMandatos();
		List<TabelaIndicadorDTO> dtos = new ArrayList<>();
		for (List<String> mandato : mandatos) {
			int anoInicial = Integer.parseInt(mandato.get(0));
			int anoFinal = Integer.parseInt(mandato.get(1));

			TabelaIndicadorDTO dto = new TabelaIndicadorDTO();
			dto.setMandato(mandato.get(2));
			dto.setValores(new ArrayList<>());
			dto.setAnoInicial(anoInicial);

			for (int i = 0; (anoInicial + i) <= anoFinal; i++) {
				int ano = anoInicial + i;
				for (IndicadorPreenchido ip : indicadoresPreenchidos) {
					String nomeCidade = ip.getPrefeitura().getCidade().getNome() + " - "
							+ ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
					if (ano == ip.getAno()) {
						boolean cidadeEncontrada = false;
						List<String> valorEncontrado = null;
						for (List<String> valores : dto.getValores()) {
							if (valores.get(0).equals(nomeCidade)) {
								cidadeEncontrada = true;
								valorEncontrado = valores;
								break;
							}
						}
						if (!cidadeEncontrada) {
							String[] registro = new String[6];
							registro[0] = nomeCidade;
							registro[5] = ip.getPrefeitura().getCidade().getId() + "";
							valorEncontrado = Arrays.asList(registro);
							dto.getValores().add(valorEncontrado);
						}

						valorEncontrado.set(1 + i, ip.getResultadoApresentacao());

					}
				}
			}

			dtos.add(dto);
		}

		for (int i = 0; i < dtos.size(); i++) {
			if (dtos.get(i).getValores() == null || dtos.get(i).getValores().isEmpty()) {
				dtos.remove(i);
				i = i - 1;
			}
		}

		return dtos;

	}

	public List<CidadeMapaDTO> buscarPorIndicadorParaMapa(Long indicador, List<Long> cidades) {
		List<IndicadorPreenchido> indicadorPreenchidos = new ArrayList<>();
		if (cidades != null && !cidades.isEmpty()) {
			indicadorPreenchidos = repository.findByIndicadorIdAndPrefeituraCidadeIdIn(indicador, cidades);
		} else {
			indicadorPreenchidos = repository.findByIndicadorId(indicador);
		}
		List<CidadeMapaDTO> dtos = new ArrayList<>();
		for (IndicadorPreenchido ip : indicadorPreenchidos) {
			boolean encontrado = false;
			String nomeCidade = ip.getPrefeitura().getCidade().getNome() + ", "
					+ ip.getPrefeitura().getCidade().getProvinciaEstado().getNome();
			for (CidadeMapaDTO dto : dtos) {
				if (dto.getNomeCidade().equals(nomeCidade)) {
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				CidadeMapaDTO dto = new CidadeMapaDTO();
				dto.setLatitude(ip.getPrefeitura().getCidade().getLatitude());
				dto.setLongitude(ip.getPrefeitura().getCidade().getLongitude());
				dto.setNomeCidade(nomeCidade);
				dto.setId(ip.getPrefeitura().getCidade().getId());
				dtos.add(dto);
			}
		}

		return dtos;
	}

	public List<CidadeDTO> buscarCidadesPreencheramSimplificado(Long indicador) {

		List<CidadeDTO> dtos = repository.findCidadesQueJaPreencheram(indicador);
		return dtos;
	}

	public List<CidadeDTO> buscarCidadesQuePreencheram(Long idIndicador) {
		List<CidadeDTO> dtos = repository.findCidadesQueJaPreencheram(idIndicador);
		return dtos;
	}

	public List<CidadeMapaDTO> buscarPorIndicadorParaMapaVisualizarIndicador(Long indicador, Long estado, Long cidade,
			Long populacao) {
		List<IndicadorPreenchido> indicadoresPreenchidos = new ArrayList<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");

		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");

		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");

		query.multiselect(indicadorPreenchido.get("id"), indicadorPreenchido.get("ano"),
				indicadorPreenchido.get("justificativa"), indicadorPreenchido.get("resultado"),
				indicadorPreenchido.get("resultadoReferencia"), indicadorPreenchido.get("dataPreenchimento"),
				joinPrefeitura.get("id"), joinPrefeitura.get("nome"), joinProvinciaEstado.get("id"),
				joinProvinciaEstado.get("sigla"), joinCidade.get("id"), joinCidade.get("nome"),
				joinCidade.get("latitude"), joinCidade.get("longitude"), joinIndicador.get("id"),
				joinIndicador.get("nome"), joinIndicador.get("descricao"));

		List<Predicate> predicateList = new ArrayList<>();

		Path<Long> idIndicador = joinIndicador.get("id");
		predicateList.add(cb.equal(idIndicador, indicador));

		if (estado != null) {
			Path<Long> idEstado = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idEstado, estado));
		}

		if (cidade != null) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(cb.equal(idCidade, cidade));
		}

		if (populacao != null) {
			Path<Long> populacaoCidade = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacaoCidade, populacao));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinCidade.get("nome")));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		indicadoresPreenchidos = em.createQuery(query).getResultList();

		List<CidadeMapaDTO> dtos = new ArrayList<>();
		for (IndicadorPreenchido ip : indicadoresPreenchidos) {
			boolean encontrado = false;
			String nomeCidade = ip.getPrefeitura().getCidade().getNome() + " - "
					+ ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
			for (CidadeMapaDTO dto : dtos) {
				if (dto.getNomeCidade().equals(nomeCidade)) {
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				CidadeMapaDTO dto = new CidadeMapaDTO();
				dto.setLatitude(ip.getPrefeitura().getCidade().getLatitude());
				dto.setLongitude(ip.getPrefeitura().getCidade().getLongitude());
				dto.setNomeCidade(nomeCidade);
				dto.setId(ip.getPrefeitura().getCidade().getId());
				dtos.add(dto);
			}
		}

		return dtos;
	}

	public List<IndicadorPreenchido> buscarPorIndicadorCidadeVisualizarIndicador(Long indicador, Long estado,
			Long cidade, Long populacao) {
		List<IndicadorPreenchido> indicadoresPreenchidos = new ArrayList<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");

		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");

		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");

		query.multiselect(indicadorPreenchido.get("id"), indicadorPreenchido.get("ano"),
				indicadorPreenchido.get("justificativa"), indicadorPreenchido.get("resultado"),
				indicadorPreenchido.get("resultadoReferencia"), indicadorPreenchido.get("dataPreenchimento"),
				joinPrefeitura.get("id"), joinPrefeitura.get("nome"), joinProvinciaEstado.get("id"),
				joinProvinciaEstado.get("sigla"), joinCidade.get("id"), joinCidade.get("nome"), joinIndicador.get("id"),
				joinIndicador.get("nome"), joinIndicador.get("descricao"));

		List<Predicate> predicateList = new ArrayList<>();

		Path<Long> idIndicador = joinIndicador.get("id");
		predicateList.add(cb.equal(idIndicador, indicador));

		if (estado != null) {
			Path<Long> idEstado = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idEstado, estado));
		}

		if (cidade != null) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(cb.equal(idCidade, cidade));
		}

		if (populacao != null) {
			Path<Long> populacaoCidade = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacaoCidade, populacao));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinCidade.get("nome")));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		indicadoresPreenchidos = em.createQuery(query).getResultList();

		return indicadoresPreenchidos;
	}

	public List<CidadeMapaDTO> buscarCidadesPorIndicadorMandato(Long indicador, List<Long> cidades,
			Short anoInicialMandato) {
		List<Short> listaAnos = new ArrayList<>();
		listaAnos.add(anoInicialMandato);
		listaAnos.add((short) (anoInicialMandato + 1));
		listaAnos.add((short) (anoInicialMandato + 2));
		listaAnos.add((short) (anoInicialMandato + 3));

		List<CidadeMapaDTO> lista = null;
		if (cidades != null && !cidades.isEmpty()) {
			lista = repository.buscarCidadesPorIndicadorMandato(indicador, cidades, listaAnos);
		} else {
			lista = repository.buscarCidadesPorIndicadorMandato(indicador, listaAnos);
		}

		return lista;
	}

	public List<IndicadorPreenchido> buscarPorCidadeEixoInicioFim(Long idCidade, Long idEixo, Short anoInicial,
			Short anoFinal) {
		List<IndicadorPreenchido> list = repository
				.findByIndicadorEixoIdAndPrefeituraCidadeIdAndAnoBetweenOrderByIndicadorIdAscAnoAsc(idEixo, idCidade,
						anoInicial, anoFinal);
		return list;
	}

	// BUSCAR TABELA DO INDICADOR NA PÁGINA DE COMPARAÇÃO DE CIDADES
	public TabelaComparativoCidadeDTO buscarPorIndicadorParaTabelaComparativoCidades(Long indicador,
			List<Long> cidades) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ValorPorCidadeDTO> query = cb.createQuery(ValorPorCidadeDTO.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");

		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");

		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");

		query.multiselect(joinCidade.get("id"), joinProvinciaEstado.get("sigla"), joinCidade.get("nome"),
				joinCidade.get("latitude"), joinCidade.get("longitude"), indicadorPreenchido.get("ano"),
				indicadorPreenchido.get("resultado"), indicadorPreenchido.get("resultadoReferencia"),
				indicadorPreenchido.get("resultadoApresentacao"));

		List<Predicate> predicateList = new ArrayList<>();

		Path<Long> idIndicador = joinIndicador.get("id");
		predicateList.add(cb.equal(idIndicador, indicador));

		if (cidades != null && cidades.size() > 0) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(idCidade.in(cidades));
		}

		predicateList.add(cb.isNotNull(indicadorPreenchido.get("resultado")));
		predicateList.add(cb.notEqual(indicadorPreenchido.get("resultado"), ""));

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinCidade.get("nome")));
		orderList.add(cb.asc(indicadorPreenchido.get("ano")));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<ValorPorCidadeDTO> lista = em.createQuery(query).getResultList();
		List<Mandato2DTO> mandatos = new MandatoUtil().getMandatoNovo();

		List<ValorPorCidadeFiltradoDTO> listaValoresFinal = new ArrayList<>();
		ValorPorCidadeFiltradoDTO valorFiltrado = new ValorPorCidadeFiltradoDTO();
		TabelaComparativoCidadeDTO tabelaComparativoCidade = new TabelaComparativoCidadeDTO();

		for (Mandato2DTO mandato : mandatos) {
			for (ValorPorCidadeDTO valor : lista) {
				if (valorFiltrado.getIdCidade() != null
						&& valorFiltrado.getIdCidade().intValue() != valor.getIdCidade().intValue()) {
					listaValoresFinal.add(valorFiltrado);
					valorFiltrado = new ValorPorCidadeFiltradoDTO();
				}

				if (valor.getAnoPreenchimento() >= mandato.getAnoInicioMandato()
						&& valor.getAnoPreenchimento() <= mandato.getAnoFimMandato()) {
					valorFiltrado.setIdCidade(valor.getIdCidade());
					valorFiltrado.setCidade(valor.getCidade());
					valorFiltrado.setLatitude(valor.getLatitude());
					valorFiltrado.setLongitude(valor.getLongitude());
					valorFiltrado.setPeriodoMandato(mandato.getPeriodo());
					valorFiltrado.setIndicadorMultiplo(valor.getIndicadorMultiplo());
					if (mandato.getAnoInicioMandato().equals(valor.getAnoPreenchimento())) {
						if (valor.getResultado() != null) {
							valorFiltrado.setResultadoAno1(valor.getResultado());
						} else {
							valorFiltrado.setResultadoAno1(" - ");
						}
					} else if ((mandato.getAnoInicioMandato() + 1) == valor.getAnoPreenchimento()) {
						if (valor.getResultado() != null) {
							valorFiltrado.setResultadoAno2(valor.getResultado());
						} else {
							valorFiltrado.setResultadoAno2(" - ");
						}
					} else if ((mandato.getAnoInicioMandato() + 2) == valor.getAnoPreenchimento()) {
						if (valor.getResultado() != null) {
							valorFiltrado.setResultadoAno3(valor.getResultado());
						} else {
							valorFiltrado.setResultadoAno3(" - ");
						}
					} else if ((mandato.getAnoInicioMandato() + 3) == valor.getAnoPreenchimento()) {
						if (valor.getResultado() != null) {
							valorFiltrado.setResultadoAno4(valor.getResultado());
						} else {
							valorFiltrado.setResultadoAno4(" - ");
						}
					} else {
						continue;
					}
				}
			}
			if (valorFiltrado.getIdCidade() != null) {
				listaValoresFinal.add(valorFiltrado);
			}
			valorFiltrado = new ValorPorCidadeFiltradoDTO();
			mandato.setListaValoresPorCidade(listaValoresFinal);
			listaValoresFinal = new ArrayList<>();
		}
		tabelaComparativoCidade.setListaMandatos(mandatos);
		return tabelaComparativoCidade;
	}

	public List<List<String>> geraRegistrosDeCidades(List<Cidade> cidades) {
		List<List<String>> lista = new ArrayList<>();
		for (Cidade cidade : cidades) {
			String nome = cidade.getNome() + " - " + cidade.getProvinciaEstado().getSigla();
			List<String> valorEncontrado = new ArrayList<>();
			String[] registro = new String[8];
			registro[0] = nome;
			registro[5] = cidade.getId() + "";
			registro[6] = cidade.getLatitude() + "";
			registro[7] = cidade.getLongitude() + "";
			valorEncontrado = Arrays.asList(registro);
			lista.add(valorEncontrado);
		}
		return lista;
	}

	public List<CidadeMapaDTO> buscarPorIndicadorParaMapaComparativoCidades(Long indicador, List<Long> cidades) {

		List<IndicadorPreenchido> indicadoresPreenchidos = new ArrayList<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");

		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");

		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");

		query.multiselect(indicadorPreenchido.get("id"), indicadorPreenchido.get("ano"),
				indicadorPreenchido.get("justificativa"), indicadorPreenchido.get("resultado"),
				indicadorPreenchido.get("resultadoReferencia"), indicadorPreenchido.get("dataPreenchimento"),
				joinPrefeitura.get("id"), joinPrefeitura.get("nome"), joinProvinciaEstado.get("id"),
				joinProvinciaEstado.get("sigla"), joinCidade.get("id"), joinCidade.get("nome"),
				joinCidade.get("latitude"), joinCidade.get("longitude"), joinIndicador.get("id"),
				joinIndicador.get("nome"), joinIndicador.get("descricao"));

		List<Predicate> predicateList = new ArrayList<>();

		Path<Long> idIndicador = joinIndicador.get("id");
		predicateList.add(cb.equal(idIndicador, indicador));

		if (cidades != null && cidades.size() > 0) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(idCidade.in(cidades));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinCidade.get("id")));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		indicadoresPreenchidos = em.createQuery(query).getResultList();

		List<CidadeMapaDTO> dtos = new ArrayList<>();
		for (IndicadorPreenchido ip : indicadoresPreenchidos) {
			boolean encontrado = false;
			String nomeCidade = ip.getPrefeitura().getCidade().getNome();
			String siglaEstado = ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
			Long idIndicadorAux = ip.getIndicador().getId();
			String nomeIndicador = ip.getIndicador().getNome();
			for (CidadeMapaDTO dto : dtos) {
				if (dto.getNomeCidade().equals(nomeCidade)) {
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				CidadeMapaDTO dto = new CidadeMapaDTO();
				dto.setLatitude(ip.getPrefeitura().getCidade().getLatitude());
				dto.setLongitude(ip.getPrefeitura().getCidade().getLongitude());
				dto.setNomeCidade(nomeCidade);
				dto.setId(ip.getPrefeitura().getCidade().getId());
				dto.setSiglaEstado(siglaEstado);
				dto.setIdIndicador(idIndicadorAux);
				dto.setNomeIndicador(nomeIndicador);
				dtos.add(dto);
			}
		}

		return dtos;
	}

	public List<IndicadorPreenchido> buscarPorIndicadorCidadeComparativoCidades(Long indicador, List<Long> cidades) {
		List<IndicadorPreenchido> indicadoresPreenchidos = new ArrayList<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");

		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");

		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");

		query.multiselect(indicadorPreenchido.get("id"), indicadorPreenchido.get("ano"),
				indicadorPreenchido.get("justificativa"), indicadorPreenchido.get("resultado"),
				indicadorPreenchido.get("resultadoReferencia"), indicadorPreenchido.get("dataPreenchimento"),
				joinPrefeitura.get("id"), joinPrefeitura.get("nome"), joinProvinciaEstado.get("id"),
				joinProvinciaEstado.get("sigla"), joinCidade.get("id"), joinCidade.get("nome"), joinIndicador.get("id"),
				joinIndicador.get("nome"), joinIndicador.get("descricao"), joinCidade.get("populacao"));

		List<Predicate> predicateList = new ArrayList<>();

		Path<Long> idIndicador = joinIndicador.get("id");
		predicateList.add(cb.equal(idIndicador, indicador));

		if (cidades != null && cidades.size() > 0) {
			Path<Long> idCidade = joinCidade.get("id");
			predicateList.add(idCidade.in(cidades));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinCidade.get("nome")));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		indicadoresPreenchidos = em.createQuery(query).getResultList();

		return indicadoresPreenchidos;
	}

	public List<CidadeMapaDTO> filtrarMapaResultadoPaginaInicialIndicadores(Long idEstado, Long idCidade,
			Long populacaoMin, Long populacaoMax, Long idEixo, Long idOds, Long idIndicador, Short anoInicialMandato) {

		List<Short> listaAnosMandato = new ArrayList<>();
		if (anoInicialMandato != null) {
			listaAnosMandato.add(anoInicialMandato);
			listaAnosMandato.add((short) (anoInicialMandato + 1));
			listaAnosMandato.add((short) (anoInicialMandato + 2));
			listaAnosMandato.add((short) (anoInicialMandato + 3));
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<CidadeMapaDTO> query = cb.createQuery(CidadeMapaDTO.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador", JoinType.LEFT);
		Join<IndicadorPreenchido, Cidade> joinPrefeitura = indicadorPreenchido.join("prefeitura", JoinType.LEFT);
		Join<IndicadorPreenchido, Cidade> joinCidade = joinPrefeitura.join("cidade", JoinType.LEFT);
		Join<IndicadorPreenchido, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado",
				JoinType.LEFT);
		Join<IndicadorPreenchido, Eixo> joinEixo = joinIndicador.join("eixo", JoinType.LEFT);
		Join<IndicadorPreenchido, ObjetivoDesenvolvimentoSustentavel> joinOds = joinIndicador.join("ods",
				JoinType.LEFT);

		query.multiselect(joinCidade.get("nome"), joinCidade.get("id"), joinCidade.get("latitude"),
				joinCidade.get("longitude"), indicadorPreenchido.get("resultado"),
				joinIndicador.get("ordemClassificacao")).distinct(true);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		predicateList.add(cb.isNotNull(indicadorPreenchido.get("resultado")));

		if (idEstado != null) {
			Path<Long> idEstadoAux = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idEstadoAux, idEstado));
		}

		if (idCidade != null) {
			Path<Long> idCidadeAux = joinCidade.get("id");
			predicateList.add(cb.equal(idCidadeAux, idCidade));
		}

		if (populacaoMin != null) {
			Path<Long> populacaoAux = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacaoAux, populacaoMin));
		}

		if (populacaoMax != null) {
			Path<Long> populacaoAux = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacaoAux, populacaoMax));
		}

		if (idEixo != null) {
			Path<Long> idEixoAux = joinEixo.get("id");
			predicateList.add(cb.equal(idEixoAux, idEixo));
		}

		if (idOds != null) {
			Path<Long> idOdsAux = joinOds.get("id");
			predicateList.add(cb.equal(idOdsAux, idOds));
		}

		if (idIndicador != null) {
			Path<Long> idIndicadorAux = joinIndicador.get("id");
			predicateList.add(cb.equal(idIndicadorAux, idIndicador));
		}

		if (anoInicialMandato != null) {
			Path<Short> anoPreenchimento = indicadorPreenchido.get("ano");
			predicateList.add(anoPreenchimento.in(listaAnosMandato));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<CidadeMapaDTO> typedQuery = em.createQuery(query);

		List<CidadeMapaDTO> listCidades = typedQuery.getResultList();

		return listCidades;
	}

	public boolean retornaIndicador(Long idEixo, Long idOds, EixoDTO eixo, ObjetivoDesenvolvimentoSustentavelDTO ods) {
		if (idEixo == null && idOds == null) {
			return true;
		} else if (idEixo != null && idOds == null && eixo != null && eixo.getId().equals(idEixo)) {
			return true;
		} else if (idOds != null && idEixo == null && ods != null && ods.getId().equals(idOds)) {
			return true;
		} else if (idOds != null && idEixo != null && ods != null && eixo != null && ods.getId().equals(idOds)
				&& eixo.getId().equals(idEixo)) {
			return true;
		}
		return false;
	}

	public static <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));

	}

	public List<CidadeComboDTO> filtrarCidadesPaginaInicialIndicadores(Long idEstado, Long populacaoMin,
			Long populacaoMax, Long idEixo, Long idOds) {

		if (idEstado == null && populacaoMin == null && populacaoMax == null && idEixo == null && idOds == null) {
			return prefeituraService.buscarCidadesSignatarias();
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<CidadeComboDTO> query = cb.createQuery(CidadeComboDTO.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura", JoinType.LEFT);
		Join<IndicadorPreenchido, Cidade> joinCidade = joinPrefeitura.join("cidade", JoinType.LEFT);
		Join<IndicadorPreenchido, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado",
				JoinType.LEFT);
		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador", JoinType.LEFT);
		Join<IndicadorPreenchido, Eixo> joinEixo = joinIndicador.join("eixo", JoinType.LEFT);
		Join<IndicadorPreenchido, ObjetivoDesenvolvimentoSustentavel> joinOds = joinIndicador.join("ods",
				JoinType.LEFT);

		query.multiselect(joinCidade.get("id"), joinCidade.get("nome"), joinProvinciaEstado.get("sigla"))
				.distinct(true);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (idEstado != null) {
			Path<Long> idEstadoAux = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idEstadoAux, idEstado));
		}

		if (populacaoMin != null) {
			Path<Long> populacaoAux = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacaoAux, populacaoMin));
		}

		if (populacaoMax != null) {
			Path<Long> populacaoAux = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacaoAux, populacaoMax));
		}

		if (idEixo != null) {
			Path<Long> idEixoAux = joinEixo.get("id");
			predicateList.add(cb.equal(idEixoAux, idEixo));
		}

		if (idOds != null) {
			Path<Long> idOdsAux = joinOds.get("id");
			predicateList.add(cb.equal(idOdsAux, idOds));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinCidade.get("nome")));
		query.orderBy(orderList);

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<CidadeComboDTO> typedQuery = em.createQuery(query);

		List<CidadeComboDTO> listCidades = typedQuery.getResultList();

		return listCidades;
	}

	public List<TabelaIndicadorDTO> filtrarIndicadoresPaginaInicialIndicadores(Long idEstado, Long idCidade,
			Long populacaoMin, Long populacaoMax, Long idEixo, Long idOds, Long idIndicador) {

		if (idCidade != null && idIndicador == null) {
			return filtrarIndicadoresDaCidadePaginaInicialIndicadores(idEstado, idCidade, populacaoMin, populacaoMax,
					idEixo, idOds, idIndicador);
		} else {
			return filtrarIndicadoresECidadesPaginaInicialIndicadores(idEstado, idCidade, populacaoMin, populacaoMax,
					idEixo, idOds, idIndicador);
		}

	}

	public List<TabelaIndicadorDTO> filtrarIndicadoresDaCidadePaginaInicialIndicadores(Long idEstado, Long idCidade,
			Long populacaoMin, Long populacaoMax, Long idEixo, Long idOds, Long idIndicador) {

		Cidade cidadeSelecionada = null;
		if (idCidade != null) {
			cidadeSelecionada = cidadeService.buscarPorId(idCidade);
		}
		if (idEstado != null) {
			List<CidadeComboDTO> cidade = cidadeService.buscarPorCidadeProvinciaEstado(idCidade, idEstado);
			if (cidade.isEmpty()) {
				return new ArrayList<>();
			}
		}

		List<IndicadorDTO> indicadoresPCS = indicadorService.buscarIndicadoresPcs();
		List<IndicadorPreenchido> indicadoresPreenchidos = filtrarIndicadoresPreenchidosPaginaInicialIndicadores(
				idEstado, idCidade, populacaoMin, populacaoMax, idEixo, idOds, idIndicador);

		List<List<String>> mandatos = MandatoUtil.getMandatos();
		List<TabelaIndicadorDTO> dtos = new ArrayList<>();

		for (List<String> mandato : mandatos) {
			int anoInicial = Integer.parseInt(mandato.get(0));
			int anoFinal = Integer.parseInt(mandato.get(1));

			TabelaIndicadorDTO dto = new TabelaIndicadorDTO();
			dto.setMandato(mandato.get(2));
			dto.setValores(new ArrayList<>());
			dto.setAnoInicial(anoInicial);

			for (IndicadorDTO indicadorPCS : indicadoresPCS) {
				List<String> valorEncontrado = null;
				String[] registro = new String[9];
				registro[0] = "";
				registro[5] = "";
				registro[6] = indicadorPCS.getNome();
				registro[7] = indicadorPCS.getId().toString();
				registro[8] = "";

				if (cidadeSelecionada != null) {
					registro[0] = cidadeSelecionada.getNome();
					registro[8] = cidadeSelecionada.getProvinciaEstado().getSigla();
				}

				List<IndicadorPreenchido> valoresIndicador = indicadoresPreenchidos.stream()
						.filter(o -> o.getIndicador().getId().equals(indicadorPCS.getId()))
						.collect(Collectors.toList());

				for (IndicadorPreenchido ip : valoresIndicador) {
					registro[0] = ip.getPrefeitura().getCidade().getNome();
					registro[8] = ip.getPrefeitura().getCidade().getProvinciaEstado().getSigla();
					if (ip.getAno().equals((short) anoInicial)) {
						registro[1] = ip.getResultadoApresentacao();
					} else if (ip.getAno().equals((short) (anoInicial + 1))) {
						registro[2] = ip.getResultadoApresentacao();
					} else if (ip.getAno().equals((short) (anoInicial + 2))) {
						registro[3] = ip.getResultadoApresentacao();
					} else if (ip.getAno().equals((short) anoFinal)) {
						registro[4] = ip.getResultadoApresentacao();
					}
				}

				if (retornaIndicador(idEixo, idOds, indicadorPCS.getEixo(), indicadorPCS.getOds())) {
					valorEncontrado = Arrays.asList(registro);
					dto.getValores().add(valorEncontrado);
				}

			}

			dtos.add(dto);
		}

		return dtos;
	}

	public List<TabelaIndicadorDTO> filtrarIndicadoresECidadesPaginaInicialIndicadores(Long idEstado, Long idCidade,
			Long populacaoMin, Long populacaoMax, Long idEixo, Long idOds, Long idIndicador) {

		List<IndicadorPreenchido> indicadoresPreenchidos = filtrarIndicadoresPreenchidosPaginaInicialIndicadores(
				idEstado, idCidade, populacaoMin, populacaoMax, idEixo, idOds, idIndicador);

		List<Prefeitura> lista = null;
		if (idCidade != null || idEstado != null || populacaoMax != null || populacaoMin != null || idOds != null
				|| idEixo != null) {
			List<Prefeitura> prefeiturasComIndicadorPreenchido = indicadoresPreenchidos.stream().distinct()
					.map(IndicadorPreenchido::getPrefeitura).collect(Collectors.toList());
			lista = prefeiturasComIndicadorPreenchido.stream().filter(distinctByKey(Prefeitura::getId))
					.collect(Collectors.toList());
		} else {
			lista = prefeituraService.buscarPrefeiturasSignatarias();
		}

		List<List<String>> mandatos = MandatoUtil.getMandatos();
		List<TabelaIndicadorDTO> dtos = new ArrayList<>();

		for (List<String> mandato : mandatos) {
			int anoInicial = Integer.parseInt(mandato.get(0));
			int anoFinal = Integer.parseInt(mandato.get(1));

			TabelaIndicadorDTO dto = new TabelaIndicadorDTO();
			dto.setMandato(mandato.get(2));
			dto.setValores(new ArrayList<>());
			dto.setAnoInicial(anoInicial);

			for (Prefeitura prefeitura : lista) {
				List<String> valorEncontrado = null;
				String[] registro = new String[9];
				registro[0] = prefeitura.getCidade().getNome();
				registro[5] = prefeitura.getCidade().getId() + "";
				registro[8] = prefeitura.getCidade().getProvinciaEstado().getSigla();
				valorEncontrado = Arrays.asList(registro);

				List<IndicadorPreenchido> preenchimentosPrefeitura = indicadoresPreenchidos.stream()
						.filter(o -> o.getPrefeitura().getId().equals(prefeitura.getId())).collect(Collectors.toList());

				for (IndicadorPreenchido ip : preenchimentosPrefeitura) {
					registro[6] = ip.getIndicador().getNome();
					registro[7] = ip.getIndicador().getId().toString();
					if (ip.getAno().equals((short) anoInicial)) {
						registro[1] = ip.getResultadoApresentacao();
					} else if (ip.getAno().equals((short) (anoInicial + 1))) {
						registro[2] = ip.getResultadoApresentacao();
					} else if (ip.getAno().equals((short) (anoInicial + 2))) {
						registro[3] = ip.getResultadoApresentacao();
					} else if (ip.getAno().equals((short) anoFinal)) {
						registro[4] = ip.getResultadoApresentacao();
					}
				}

				valorEncontrado = Arrays.asList(registro);

				dto.getValores().add(valorEncontrado);
			}
			dtos.add(dto);
		}
		return dtos;
	}

	public List<IndicadorPreenchido> filtrarIndicadoresPreenchidosPaginaInicialIndicadores(Long idEstado, Long idCidade,
			Long populacaoMin, Long populacaoMax, Long idEixo, Long idOds, Long idIndicador) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador", JoinType.LEFT);
		Join<IndicadorPreenchido, Cidade> joinPrefeitura = indicadorPreenchido.join("prefeitura", JoinType.LEFT);
		Join<IndicadorPreenchido, Cidade> joinCidade = joinPrefeitura.join("cidade", JoinType.LEFT);
		Join<IndicadorPreenchido, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado",
				JoinType.LEFT);
		Join<IndicadorPreenchido, Eixo> joinEixo = joinIndicador.join("eixo", JoinType.LEFT);
		Join<IndicadorPreenchido, ObjetivoDesenvolvimentoSustentavel> joinOds = joinIndicador.join("ods",
				JoinType.LEFT);

		query.multiselect(indicadorPreenchido.get("id"), joinIndicador, indicadorPreenchido.get("ano"),
				indicadorPreenchido.get("justificativa"), indicadorPreenchido.get("resultado"),
				indicadorPreenchido.get("resultadoReferencia"), indicadorPreenchido.get("dataPreenchimento"),
				joinPrefeitura, joinIndicador.get("nome"), joinCidade.get("nome"),
				indicadorPreenchido.get("resultadoApresentacao")).distinct(true);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (idEstado != null) {
			Path<Long> idEstadoAux = joinProvinciaEstado.get("id");
			predicateList.add(cb.equal(idEstadoAux, idEstado));
		}

		if (idCidade != null) {
			Path<Long> idCidadeAux = joinCidade.get("id");
			predicateList.add(cb.equal(idCidadeAux, idCidade));
		}

		if (populacaoMin != null) {
			Path<Long> populacaoAux = joinCidade.get("populacao");
			predicateList.add(cb.greaterThanOrEqualTo(populacaoAux, populacaoMin));
		}

		if (populacaoMax != null) {
			Path<Long> populacaoAux = joinCidade.get("populacao");
			predicateList.add(cb.lessThanOrEqualTo(populacaoAux, populacaoMax));
		}

		if (idEixo != null) {
			Path<Long> idEixoAux = joinEixo.get("id");
			predicateList.add(cb.equal(idEixoAux, idEixo));
		}

		if (idOds != null) {
			Path<Long> idOdsAux = joinOds.get("id");
			predicateList.add(cb.equal(idOdsAux, idOds));
		}

		if (idIndicador != null) {
			Path<Long> idIndicadorAux = joinIndicador.get("id");
			predicateList.add(cb.equal(idIndicadorAux, idIndicador));
		}

		List<Order> orderList = new ArrayList<Order>();

		if (idIndicador != null) {
			orderList.add(cb.asc(joinCidade.get("nome")));
		} else {
			orderList.add(cb.asc(joinIndicador.get("nome")));
		}

		query.orderBy(orderList);

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<IndicadorPreenchido> typedQuery = em.createQuery(query);

		List<IndicadorPreenchido> listCidades = typedQuery.getResultList();

		return listCidades;
	}

	public List<IndicadorDadosAbertosDTO> buscarIndicadoresDadosAbertos(Long idCidade, Long idIndicador) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorDadosAbertosDTO> query = cb.createQuery(IndicadorDadosAbertosDTO.class);

		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");
		Join<Prefeitura, Cidade> cidadeJoin = joinPrefeitura.join("cidade");
		Join<Cidade, ProvinciaEstado> provinciaEstadoJoin = cidadeJoin.join("provinciaEstado");
		Join<IndicadorPreenchido, Indicador> indicadorJoin = indicadorPreenchido.join("indicador");
		Join<Indicador, Eixo> eixoJoin = indicadorJoin.join("eixo");
		Join<Indicador, MetaObjetivoDesenvolvimentoSustentavel> metaOdsJoin = indicadorJoin.join("metaODS");
		Join<Indicador, ObjetivoDesenvolvimentoSustentavel> odsJoin = indicadorJoin.join("ods");

		query.multiselect(cidadeJoin.get("codigoIbge"), cidadeJoin.get("nome"), provinciaEstadoJoin.get("sigla"),
				eixoJoin.get("nome"), indicadorJoin.get("id"), indicadorJoin.get("nome"),
				indicadorJoin.get("formulaResultado"), metaOdsJoin.get("descricao"), odsJoin.get("numero"),
				odsJoin.get("titulo"), indicadorJoin.get("descricao"), indicadorPreenchido.get("ano"),
				indicadorPreenchido.get("resultadoApresentacao"), indicadorPreenchido.get("justificativa"),
				provinciaEstadoJoin.get("nome"), indicadorPreenchido.get("resultado"),
				indicadorPreenchido.get("valorTexto"));

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		Path<String> indicadorPreenchidoPath = indicadorPreenchido.get("resultadoApresentacao");
		predicateList.add(cb.notEqual(indicadorPreenchidoPath, ""));
		predicateList.add(cb.notEqual(indicadorPreenchidoPath, "Infinity"));
		predicateList.add(cb.notEqual(indicadorPreenchidoPath, "-Infinity"));
		predicateList.add(cb.notEqual(indicadorPreenchidoPath, "NaN"));

		if (idCidade != null && idCidade != -1) {
			Path<String> idCidadePath = cidadeJoin.get("id");
			predicateList.add(cb.equal(idCidadePath, idCidade));
		}

		if (idIndicador != null && idIndicador != -1) {
			Path<String> idIndicadorPath = indicadorJoin.get("id");
			predicateList.add(cb.equal(idIndicadorPath, idIndicador));
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(indicadorPreenchido.get("ano")));
		orderList.add(cb.asc(indicadorJoin.get("nome")));
		query.orderBy(orderList);

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<IndicadorDadosAbertosDTO> typedQuery = em.createQuery(query);

		return typedQuery.getResultList();
	}

	public List<IndicadorPreenchido> buscarPorCidadeInicioFim(Long idCidade, Short anoInicial, Short anoFinal) {
		List<IndicadorPreenchido> list = repository
				.findByIndicadorPrefeituraCidadeIdAndAnoBetweenOrderByIndicadorIdAscAnoAsc(idCidade, anoInicial,
						anoFinal);
		return list;
	}

	@Transactional
	public void recalcular(Long idIndicador, int anoInicial) {
		
		Indicador indicador = indicadorService.buscarIndicadorPorId(idIndicador);
		
		int anoFinal = LocalDate.now().getYear();
		List<Long> cidades = new ArrayList<>();
		
		if(indicador.getPrefeitura() == null) {
			cidades = cidadeService.buscarIdCidadesPreencheramUmaDasVariaveisDoIndicador(idIndicador);
			if (idIndicador != null) {
				removerIndicadoresPreenchidos(idIndicador);
				preencherNovosIndicadores(cidades, anoInicial, anoFinal, idIndicador);
			}
		} else {
			Long idCidade = indicador.getPrefeitura().getCidade().getId();
			cidades.add(idCidade);
			if (idIndicador != null) {
				removerIndicadoresPreenchidos(idIndicador);
				preencherNovosIndicadores(cidades, anoInicial, anoFinal, idIndicador);
			}
		}
		
		cidades = null;
	}

	@Transactional
	public void recalcularTudo(boolean calcularPcs, boolean calcularPrefeitura, boolean numerico, boolean texto, int anoInicial ) {
		List<Long> listaIdIndicadores = new ArrayList<>();
		List<Long> indicadoresRecalculo = new ArrayList<>();
		if (calcularPcs && calcularPrefeitura) {
			listaIdIndicadores = indicadorService.listarApenasId();
		} else if (calcularPcs && !calcularPrefeitura) {
			listaIdIndicadores = indicadorService.listarApenasIdPcs();
		} else if (!calcularPcs && calcularPrefeitura) {
			listaIdIndicadores = indicadorService.listarApenasIdPrefeituras();
		}
		for (Long id : listaIdIndicadores) {
			Indicador indicador = indicadorService.listarById(id);
			boolean ehNumerico = indicador.isNumerico();
			boolean add = false;
			if (numerico && ehNumerico) {
				indicadoresRecalculo.add(id);
				add = true;
			}
			if (texto && !ehNumerico && !add) {
				indicadoresRecalculo.add(id);
			}
		}

		for(Long id : indicadoresRecalculo) {
			recalcular(id, anoInicial);
		}

	}

	private void preencherNovosIndicadores(List<Long> cidades, int anoInicial, int anoFinal, Long idIndicador) {
		Indicador indicador = indicadorService.listarById(idIndicador);
		for (Long idCidade : cidades) {
			Prefeitura prefeitura = prefeituraService.buscarAtualPorCidade(idCidade);
			List<Short> anos = variavelPreenchidaRepository
					.findAnoQueCidadeJaPreencheuUmaDasVariaveisDoIndicador(indicador.getId(), idCidade);
			if(prefeitura == null) {
				System.out.println(idCidade+" sem atual");
				prefeitura = prefeituraService.buscarUltimaPrefeituraExistentePorCidade(idCidade);
				if (prefeitura == null) {
					System.out.println(idCidade+" sem prefeitura");
					continue;
				}
			}

			if (anos != null) {
				for (Short ano : anos) {
					try {
						List<VariavelPreenchida> vps = variavelPreenchidaRepository
								.findByIndicadorIdCidadeidAno(idIndicador, idCidade, ano);
						int quantidadeVP = vps.size();
						int quantidadeV = indicador.getVariaveis().size();
						if (quantidadeV == quantidadeVP) {
							IndicadorPreenchido ip = new IndicadorPreenchido();
							ip.setAno(ano);
							ip.setVariaveisPreenchidas(vps);
							ip.setPrefeitura(prefeitura);
							ip.setIndicador(indicador);
							ip.setDataPreenchimento(LocalDateTime.now());
							calculadora.calcularResultado(ip);
							repository.save(ip);
							vps = null;
							ip = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	private void removerIndicadoresPreenchidos(Long idIndicador) {
		Query q = em.createQuery("delete from IndicadorPreenchido where indicador.id = :id");
		q.setParameter("id", idIndicador);
		q.executeUpdate();

	}

	public List<IndicadorPreenchido> filtrarIndicadores(FiltroIndicadoresDTO filtro) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> root = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = root.join("indicador", JoinType.LEFT);
		Join<IndicadorPreenchido, Cidade> joinPrefeitura = root.join("prefeitura", JoinType.LEFT);
		Join<IndicadorPreenchido, Cidade> joinCidade = joinPrefeitura.join("cidade", JoinType.LEFT);
		Join<IndicadorPreenchido, Eixo> joinEixo = joinIndicador.join("eixo", JoinType.LEFT);
		Join<IndicadorPreenchido, ObjetivoDesenvolvimentoSustentavel> joinOds = joinIndicador.join("ods",
				JoinType.LEFT);
		Join<Indicador, Variavel> joinVariavel = joinIndicador.join("variaveis", JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<>();

		if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
			Path<String> campoNome = joinIndicador.get("nome");
			predicateList.add(cb.like(cb.lower(campoNome), new String("%" + filtro.getNome() + "%").toLowerCase()));
		}

		if (filtro.getEixo() != null && filtro.getEixo().getId() != null) {
			Path<String> campoIdEixo = joinEixo.get("id");
			predicateList.add(cb.equal(campoIdEixo, filtro.getEixo().getId()));
		}

		if (filtro.getOds() != null && filtro.getOds().getId() != null) {
			Path<Long> campoIdOds = joinOds.get("id");
			predicateList.add(cb.equal(campoIdOds, filtro.getOds().getId()));
		}

		if (filtro.getVariavel() != null && filtro.getVariavel().getId() != null) {
			Path<Long> campoIdVariavel = joinVariavel.get("id");

			List<Long> ids = new LinkedList<>();
			ids.add(filtro.getVariavel().getId());

			predicateList.add(campoIdVariavel.in(ids));
		}

		if (filtro.getValor() != null && !filtro.getValor().isEmpty()) {
			Path<String> campoFormulaResultado = root.get("resultado");
			predicateList.add(
					cb.like(cb.lower(campoFormulaResultado), new String("%" + filtro.getValor() + "%").toLowerCase()));
		}

		if (filtro.getCidade() != null && filtro.getCidade().getId() != null) {
			Path<Long> campoIdCidade = joinCidade.get("id");

			predicateList.add(cb.equal(campoIdCidade, filtro.getCidade().getId()));
		}

		if (filtro.getPopDe() != null && filtro.getPopDe() >= 0) {
			Path<Long> campoPopulacaoCidade = joinCidade.get("populacao");

			Predicate greaterThan = cb.greaterThanOrEqualTo(campoPopulacaoCidade, Long.valueOf(filtro.getPopDe()));

			predicateList.add(greaterThan);
		}

		if (filtro.getPopAte() != null && filtro.getPopAte() >= 0) {
			Path<Long> campoPopulacaoCidade = joinCidade.get("populacao");

			Predicate lessThen = cb.lessThanOrEqualTo(campoPopulacaoCidade, Long.valueOf(filtro.getPopAte()));

			predicateList.add(lessThen);
		}

		if (filtro.getDataPreenchimento() != null) {
			Path<LocalDateTime> campoDataCadastro = root.get("dataPreenchimento");

			Calendar dateCalendar = Calendar.getInstance();
			dateCalendar.setTime(filtro.getDataPreenchimento());

			Predicate year = cb.equal(cb.function("year", Integer.class, campoDataCadastro),
					dateCalendar.get(Calendar.YEAR));
			Predicate month = cb.equal(cb.function("month", Integer.class, campoDataCadastro),
					dateCalendar.get(Calendar.MONTH) + 1);
			Predicate day = cb.equal(cb.function("day", Integer.class, campoDataCadastro),
					dateCalendar.get(Calendar.DAY_OF_MONTH));

			predicateList.add(cb.and(year, month, day));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		return em.createQuery(query).getResultList();
	}

	public List<IndicadorPreenchidoMapaDTO> buscarCidadesComIndicadorPreenchido(Long idIndicador, Long idEixo,
			Long idOds, Long idCidade, Long popuMin, Long popuMax, String valorPreenchido, Short anoSelecionado,
			Long idxFormula, boolean visualizarComoPontos) throws Exception {
		
		

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> root = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Indicador> joinIndicador = root.join("indicador", JoinType.LEFT);
		Join<Indicador, Eixo> joinEixo = joinIndicador.join("eixo", JoinType.LEFT);
		Join<Indicador, ObjetivoDesenvolvimentoSustentavel> joinOds = joinIndicador.join("ods", JoinType.LEFT);
		Join<Indicador, Cidade> joinPrefeitura = root.join("prefeitura", JoinType.LEFT);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade", JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<>();

		if (idIndicador != null) {
			Path<Long> campoIdIndicador = joinIndicador.get("id");
			predicateList.add(cb.equal(campoIdIndicador, idIndicador));
		}

		if (idEixo != null) {
			Path<Long> campoIdEixo = joinEixo.get("id");
			predicateList.add(cb.equal(campoIdEixo, idEixo));
		}

		if (idOds != null) {
			Path<Long> campoIdOds = joinOds.get("id");
			predicateList.add(cb.equal(campoIdOds, idOds));
		}

		if (idCidade != null) {
			Path<Long> campoIdCidade = joinCidade.get("id");
			predicateList.add(cb.equal(campoIdCidade, idCidade));
		}

		if (popuMin != null && popuMin >= 0) {
			Path<Long> campoPopulacaoCidade = joinCidade.get("populacao");
			Predicate greaterThan = cb.greaterThanOrEqualTo(campoPopulacaoCidade, Long.valueOf(popuMin));
			predicateList.add(greaterThan);
		}

		if (popuMax != null && popuMax >= 0) {
			Path<Long> campoPopulacaoCidade = joinCidade.get("populacao");
			Predicate lessThen = cb.lessThanOrEqualTo(campoPopulacaoCidade, Long.valueOf(popuMax));
			predicateList.add(lessThen);
		}

		if (anoSelecionado != null && anoSelecionado >= 0) {
			Path<Long> ano = root.get("ano");
			predicateList.add(cb.equal(ano, anoSelecionado));

		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<IndicadorPreenchido> typedQuery = em.createQuery(query);

		List<IndicadorPreenchido> lista = typedQuery.getResultList();

		List<IndicadorPreenchidoMapaDTO> listaDTO = new ArrayList<>();

		for (IndicadorPreenchido ind : lista) {

			if (ind.getResultado() == null
					|| (ind.getResultado().contains("NaN") || ind.getResultado().contains("Infinity"))) {
				continue;
			}

			IndicadorPreenchidoMapaDTO indDTO = new IndicadorPreenchidoMapaDTO(ind.getPrefeitura().getCidade().getId(),
					ind.getPrefeitura().getCidade().getNome(), ind.getIndicador().getNome(),
					ind.getPrefeitura().getCidade().getLatitude(), ind.getPrefeitura().getCidade().getLongitude(),
					ind.getValorApresentacaoMapaPlanejamento());

			if (ind.getIndicador().isMultiplo() && idxFormula != null) {

				String[] listaResultados = indDTO.getValorPreenchido().split(";");
				indDTO.setValorPreenchido(listaResultados[idxFormula.intValue()]);

			}

			if (valorPreenchido != "") {

				if (NumeroUtil.isANumber(valorPreenchido) && valorPreenchido.equals(indDTO.getValorPreenchido())) {
					listaDTO.add(indDTO);
				} else if (!NumeroUtil.isANumber(valorPreenchido)
						&& indDTO.getValorPreenchido().toLowerCase().contains(valorPreenchido.toLowerCase())) {
					listaDTO.add(indDTO);
				}

			} else if (indDTO.getValorPreenchido() != null && !indDTO.getValorPreenchido().contains("null")) {
				listaDTO.add(indDTO);
			}
		}

		if (!visualizarComoPontos) {
			for (IndicadorPreenchidoMapaDTO indicadorPreenchidoDTO : listaDTO) {
				List<Feature> shapeZoneamento = shapeFileService
						.buscarShapeZoneamento(indicadorPreenchidoDTO.getIdCidade());
				indicadorPreenchidoDTO.setShapeZoneamento(shapeZoneamento);
				indicadorPreenchidoDTO.setVisualizarComoPontos(false);
			}
		}
		
		if(!listaDTO.isEmpty() && listaDTO.size() > 0) {
			String VISUALIZACAO = "Visualizacao";
			if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null) {
				visualizacaoCartograficaService.inserirRelatorioVisualizacaoCartografica(idIndicador, idCidade, usuarioContextUtil.getUsuario(), VISUALIZACAO);
			} else {
				visualizacaoCartograficaService.inserirRelatorioVisualizacaoCartografica(idIndicador, idCidade, null, VISUALIZACAO);
			}
		}
		
		return listaDTO;

	}

	public List<IndicadorPreenchido> buscarSemApresentacao() {
		return repository.findByResultadoApresentacaoIsNull();
	}

	public void calcularResultadoApresentacao() {
		List<IndicadorPreenchido> ips = buscarSemApresentacao();
		Instant inicio = Instant.now();
		int qtd = ips.size();
		int posicao = 1;
		for (IndicadorPreenchido ip : ips) {
			System.out.println(posicao + " de " + qtd + " iniciada, faltam " + (qtd - posicao));
			calculadora.calcularResultado(ip);
			repository.save(ip);
			posicao++;
		}
		Instant fim = Instant.now();
		Duration duracao = Duration.between(inicio, fim);

		System.out.println("Inicio " + inicio);
		System.out.println("Terminou " + fim);
		System.out.println("Durou " + duracao.toMillis());
	}

	public List<IndicadorPreenchido> buscarPorAnoVpVariavelIdCidadeId(Short ano, Long idVariavel, Long idCidade) {
		List<IndicadorPreenchido> ips = repository.buscarPorAnoVpVariavelIdCidadeId(ano, idVariavel, idCidade);
		return ips;
	}

	public void excluirLista(List<IndicadorPreenchido> ips) {
		repository.deleteAll(ips);

	}

	@Transactional
	public void calcularValorTexto() {
		List<IndicadorPreenchido> indicadoresPreenchidos = this.repository.findByIndicadorTipoConteudo("Texto");
		int qtd = indicadoresPreenchidos.size();
		int posicao = 1;
		for (IndicadorPreenchido ip : indicadoresPreenchidos) {
			// System.out.println(posicao+" de "+ qtd+" iniciada, faltam "+(qtd-posicao));
			String valorTexto = ip.formulaTextualDadosAbertos();
			this.atualizarValorTextoRapido(ip.getId(), valorTexto);
			posicao++;
		}
	}

	@Transactional
	public void atualizarValorTextoRapido(Long id, String valor) {
		Query q = em.createQuery("update IndicadorPreenchido set valorTexto = :valorTexto where id = :id");
		q.setParameter("id", id);
		q.setParameter("valorTexto", valor);
		q.executeUpdate();
	}

	public void recalcularIndicadoresPorVariavelCidadeAno(Variavel variavel, Cidade cidade, Short ano)
			throws Exception {
		Usuario usuario = usuarioContextUtil.getUsuario();
		Long idCidade = usuario.getPrefeitura().getCidade().getId();
		List<Indicador> listaIndicador = this.indicadorRepository.findByVariaveisIn(variavel);
		if (listaIndicador != null) {
			for (Indicador indicador : listaIndicador) {
				IndicadorPreenchido ip = this.repository.findByIndicadorIdAndPrefeituraCidadeIdAndAno(indicador.getId(),
						idCidade, ano);
				if (ip == null) {
					ip = new IndicadorPreenchido();
					ip.setAno(ano);
					// ip.setVariaveisPreenchidas(vps);
					ip.setPrefeitura(usuario.getPrefeitura());
					ip.setIndicador(indicador);
					ip.setDataPreenchimento(LocalDateTime.now());
					// calculadora.calcularResultado(ip);

				} else {
					ip.setDataPreenchimento(LocalDateTime.now());
					ip.setPrefeitura(usuario.getPrefeitura());
				}
				ip.setVariaveisPreenchidas(new ArrayList());
				for (Variavel variavelIndicador : indicador.getVariaveis()) {
					VariavelPreenchida vp = this.variavelPreenchidaRepository
							.findByVariavelIdAndPrefeituraCidadeIdAndAno(variavelIndicador.getId(), idCidade, ano);
					if (vp != null) {
						ip.getVariaveisPreenchidas().add(vp);
					}
				}
				int qtVp = ip.getVariaveisPreenchidas().size();
				int qtV = indicador.getVariaveis().size();
				if (qtVp == qtV) {
					this.calculaResultadoIndicador(ip);
					this.repository.save(ip);
				}

			}
		}
	}

	public void recalcularIndicadoresPorVariavelCidadeAnoSubdivisao(Variavel variavel, Cidade cidade, Short ano,
			Long idSubdivisao) throws Exception {
		Usuario usuario = usuarioContextUtil.getUsuario();
		Long idCidade = usuario.getPrefeitura().getCidade().getId();
		SubdivisaoCidade subdivisao = subdivisaoService.buscarPorId(idSubdivisao);

		List<Indicador> listaIndicador = this.indicadorRepository.findByVariaveisIn(variavel);
		if (listaIndicador != null) {
			for (Indicador indicador : listaIndicador) {
				SubdivisaoIndicadorPreenchido ip = this.subdivisaoIndicadorRepository
						.findByIndicadorIdAndPrefeituraCidadeIdAndAnoAndSubdivisaoId(indicador.getId(), idCidade, ano,
								idSubdivisao);
				if (ip == null) {
					ip = new SubdivisaoIndicadorPreenchido();
					ip.setAno(ano);
					ip.setPrefeitura(usuario.getPrefeitura());
					ip.setIndicador(indicador);
					ip.setDataPreenchimento(LocalDateTime.now());
					ip.setSubdivisao(subdivisao);
				} else {
					ip.setDataPreenchimento(LocalDateTime.now());
					ip.setPrefeitura(usuario.getPrefeitura());
				}
				ip.setVariaveisPreenchidas(new ArrayList());
				for (Variavel variavelIndicador : indicador.getVariaveis()) {
					SubdivisaoVariavelPreenchida vp = this.subdivisaoVariavelRepository
							.findByVariavelIdAndPrefeituraCidadeIdAndAnoAndSubdivisaoId(variavelIndicador.getId(),
									idCidade, ano, idSubdivisao);
					if (vp != null) {
						ip.getVariaveisPreenchidas().add(vp);
					}
				}
				int qtVp = ip.getVariaveisPreenchidas().size();
				int qtV = indicador.getVariaveis().size();
				if (qtVp == qtV) {
					this.calculaResultadoIndicador(ip);
					this.subdivisaoIndicadorRepository.save(ip);
				}

			}
		}
	}

	public SubdivisaoIndicadorPreenchido buscarPorCidadeIndicadorSubdivisaoAno(Long idCidade, Indicador indicador,
			Long idSubdivisao, Short ano) {
		return this.subdivisaoIndicadorRepository.findByIndicadorIdAndPrefeituraCidadeIdAndAnoAndSubdivisaoId(
				indicador.getId(), idCidade, ano, idSubdivisao);
	}

	public List<SubdivisaoIndicadorPreenchido> buscarPorSubdivisaoEIndicadorECidade(Long subdivisao, Long idIndicador,
			Long idCidade) {
		if (idCidade == null) {
			return this.subdivisaoIndicadorRepository.findByIndicadorIdAndSubdivisaoId(idIndicador, subdivisao);
		} else {
			return this.subdivisaoIndicadorRepository.findByIndicadorIdAndPrefeituraCidadeIdAndSubdivisaoId(idIndicador,
					idCidade, subdivisao);
		}
	}

	public List<SubdivisaoVariavelPreenchida> buscarVariaveisPreenchidasPorIndicador(Long idCidade, Long idIndicador,
			Long idSubdivisao) {
		if (idCidade != null) {
			return variavelPreenchidaRepository.findByCidadeIdIndicadorIdSubdivisaoId(idCidade, idIndicador,
					idSubdivisao);
		} else {
			return variavelPreenchidaRepository.findByIndicadorIdSubdivisaoId(idIndicador, idSubdivisao);
		}
	}

	public SubdivisaoIndicadorPreenchido preencherIndicadorSubdivisao(IndicadorPreenchidoDTO dto, Long idSubdivisao) {
		try {
			Usuario usuario = this.usuarioContextUtil.getUsuario();
			Long idCidade = usuario.getPrefeitura().getCidade().getId();

			Optional<Indicador> indicador = indicadorRepository.findById(dto.getIdIndicador());
			SubdivisaoCidade subdivisao = this.subdivisaoService.buscarPorId(idSubdivisao);

			SubdivisaoIndicadorPreenchido indicadorPreenchido = buscarPorCidadeIndicadorSubdivisaoAno(idCidade,
					indicador.get(), idSubdivisao, dto.getAno());
			if (indicadorPreenchido == null || indicadorPreenchido.getId() == null) {
				indicadorPreenchido = SubdivisaoIndicadorPreenchido.builder()
						.indicador(
								indicador.orElseThrow(() -> new ObjectNotFoundException("Indicador não encontrado!")))
						.ano(dto.getAno()).variaveisPreenchidas(new ArrayList<>()).justificativa(dto.getJustificativa())
						.prefeitura(usuario.getPrefeitura()).dataPreenchimento(LocalDateTime.now())
						.subdivisao(subdivisao).build();
			}

			if (subdivisao == null || !indicadorPreenchido.getSubdivisao().getCidade().getId().equals(idCidade)) {
				throw new ObjectNotFoundException("Subdivisao não encontrada!");
			}

			Boolean possuiJustificativa = possuiJustificativa(dto);
			indicadorPreenchido.setVariaveisPreenchidas(new ArrayList<>());
			for (VariavelPreenchidaDTO vpDTO : dto.getVariaveisPreenchidas()) {
				vpDTO.setSubdivisao(idSubdivisao);
				vpDTO.setAno(dto.getAno());

				SubdivisaoVariavelPreenchida preenchida = this.variavelPrenchidaService.preencherComSubdivisao(vpDTO);
				indicadorPreenchido.getVariaveisPreenchidas().add(preenchida);
			}

			SubdivisaoIndicadorPreenchido entity = null;
			try {
				entity = this.subdivisaoIndicadorRepository.save(indicadorPreenchido);
				entity = calculaResultadoIndicador(entity);
			} catch (DataIntegrityViolationException e) {
				throw new DataIntegrityException("Indicador já foi preenchido para o ano de " + dto.getAno());
			}

			return entity;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public List<FatorDesigualdadeDTO> calcularFatorDesigualdade(Long idIndicador, Long idCidade, Long nivel) {
		List<FatorDesigualdadeDTO> dtos = new ArrayList<>();
		HashMap<Short, FatorDesigualdadeDTO> map = new HashMap();
		List<SubdivisaoIndicadorPreenchido> listaSIPS = this.subdivisaoIndicadorRepository
				.findByIndicadorIdAndPrefeituraCidadeIdAndSubdivisaoTipoSubdivisaoNivel(idIndicador, idCidade, nivel);

		if (listaSIPS != null) {
			for (SubdivisaoIndicadorPreenchido sip : listaSIPS) {
				if (!map.containsKey(sip.getAno())) {
					FatorDesigualdadeDTO dto = new FatorDesigualdadeDTO();
					dto.setAno(sip.getAno());
					map.put(sip.getAno(), dto);
				}
			}

			for (SubdivisaoIndicadorPreenchido sip : listaSIPS) {
				try {
					Double resultado = NumeroUtil.toDouble(sip.getResultado());
					String nomeSubdivisao = sip.getSubdivisao().getNome();
					FatorDesigualdadeDTO dto = map.get(sip.getAno());
					if (dto.getMinimo() == null) {
						dto.setMinimo(resultado);
						dto.setSubdivisaoMinimo(nomeSubdivisao);
					}
					if (dto.getMaximo() == null) {
							dto.setMaximo(resultado);
							dto.setSubdivisaoMaximo(nomeSubdivisao);
					}
					if (dto.getMinimo() > resultado ) {
							dto.setMinimo(resultado);
							dto.setSubdivisaoMinimo(nomeSubdivisao);
					}
					if (dto.getMaximo() < resultado ) {
						dto.setMaximo(resultado);
						dto.setSubdivisaoMaximo(nomeSubdivisao);
					}
					
					if (dto.getMaximo() != null && dto.getMinimo() != null) {
						dto.setDesigualdade((dto.getMinimo()*100) / dto.getMaximo()) ;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		map.keySet().forEach(item ->{
			dtos.add(map.get(item));
		});

		
		return dtos;
	}

	public List<SubdivisaoIndicadorPreenchido> subdvisaoIndicadorPreenchidoPorIndicadorCidadeNivelAno(Long idIndicador, Long idCidade, Long nivel, short ano){
		List<SubdivisaoIndicadorPreenchido> listaSIPS = this.subdivisaoIndicadorRepository.findByIndicadorCidadeNivelAno(idIndicador,idCidade, nivel, ano);
		return listaSIPS;
	}
	
	public List<IndicadorIntegracaoDTO> filtrarIndicadoresIntegracao(String nome) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<IndicadorIntegracaoDTO> query = cb.createQuery(IndicadorIntegracaoDTO.class);

		Root<Indicador> indicador = query.from(Indicador.class);

		query.multiselect(indicador.get("id"), indicador.get("nome"), indicador.get("descricao"), indicador.get("ods"),
				indicador.get("metaODS")).distinct(true);

		List<Predicate> predicateList = new ArrayList<>();

		if (nome != null && !nome.equals("")) {
			Path<String> campoNome = indicador.get("nome");
			predicateList.add(cb.like(cb.lower(campoNome), new String("%" + nome + "%").toLowerCase()));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		TypedQuery<IndicadorIntegracaoDTO> typedQuery = em.createQuery(query);

		List<IndicadorIntegracaoDTO> listaIndicadores = typedQuery.getResultList(); 

		return listaIndicadores;
	}
	
	public List<IndicadorPreenchidoIntegracaoDTO> buscarCidadesComIndicadorPreenchidoIntegracao(Long popuMin, Long popuMax, String nomeCidade, String nomeIndicador) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<IndicadorPreenchido> query = cb.createQuery(IndicadorPreenchido.class);

		Root<IndicadorPreenchido> root = query.from(IndicadorPreenchido.class);

		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = root.join("prefeitura",JoinType.LEFT);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);
		Join<IndicadorPreenchido, Indicador> joinIndicador = root.join("indicador",JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<>();

		if (popuMin != null && popuMin >= 0) {
			Path<Long> campoPopulacaoCidade = joinCidade.get("populacao");
			Predicate greaterThan = cb.greaterThanOrEqualTo(campoPopulacaoCidade, Long.valueOf(popuMin));
			predicateList.add(greaterThan);
		}

		if (popuMax != null && popuMax >= 0) {
			Path<Long> campoPopulacaoCidade = joinCidade.get("populacao");
			Predicate lessThen = cb.lessThanOrEqualTo(campoPopulacaoCidade, Long.valueOf(popuMax));
			predicateList.add(lessThen);
		}
		
		if (nomeCidade != null && !nomeCidade.equals("")) {
			Path<String> nomeCidadePath = joinCidade.get("nome");
			javax.persistence.criteria.Predicate predicateForNome = cb.like(cb.lower(nomeCidadePath), "%" + nomeCidade.toLowerCase() + "%");
			predicateList.add(predicateForNome);
		}
		
		if (nomeIndicador != null && !nomeIndicador.equals("")) {
			Path<String> nomeIndicadorPath = joinIndicador.get("nome");
			javax.persistence.criteria.Predicate predicateForNomeIndicador = cb.like(cb.lower(nomeIndicadorPath), "%" + nomeIndicador.toLowerCase() + "%");
			predicateList.add(predicateForNomeIndicador);
		}	

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList
				.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<IndicadorPreenchido> typedQuery = em.createQuery(query);

		List<IndicadorPreenchido> lista = typedQuery.getResultList();

		List<IndicadorPreenchidoIntegracaoDTO> listaDTO = new ArrayList<>();

		for (IndicadorPreenchido ind : lista) {

			if (ind.getResultado() == null
					|| (ind.getResultado().contains("NaN") || ind.getResultado().contains("Infinity"))) {
				continue;
			}

			IndicadorPreenchidoIntegracaoDTO indDTO = new IndicadorPreenchidoIntegracaoDTO(ind.getPrefeitura().getCidade().getId(),
					ind.getPrefeitura().getCidade().getNome(), ind.getIndicador().getNome(),
					ind.getPrefeitura().getCidade().getLatitude(), ind.getPrefeitura().getCidade().getLongitude(),
					ind.getValorApresentacaoMapaPlanejamento());

			if (indDTO.getValorPreenchido() != null && !indDTO.getValorPreenchido().contains("null")) {
				listaDTO.add(indDTO);
			}
		}

		return listaDTO;

	}
	
	public List<IndicadorPreenchidoSimplesDTO> findByPrefeituraSimples(Long idPrefeitura) {
		return repository.findByPrefeituraSimples(idPrefeitura);
	}
	
	public List<IndicadorPreenchidoSimplesDTO> findByPrefeituraSimplesRelatorio(Long idPrefeitura) {
		return repository.findByPrefeituraSimplesRelatorio(idPrefeitura);
	}

}
