package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.EixoIndicadoresDTO;
import br.org.cidadessustentaveis.dto.IndicadorDTO;
import br.org.cidadessustentaveis.dto.IndicadorParaComboDTO;
import br.org.cidadessustentaveis.dto.IndicadorcomparativomesmacidadeDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.ValorReferenciaDTO;
import br.org.cidadessustentaveis.dto.VariavelDTO;
import br.org.cidadessustentaveis.dto.VariavelIndicadorDuplicadaDTO;
import br.org.cidadessustentaveis.dto.VisualizarIndicadorDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.repository.IndicadorRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;

@Service
public class IndicadorService {

	@Autowired
	private IndicadorRepository indicadorRepository;
	
	@Autowired
	EixoService eixoService;
	
	@Autowired
	ObjetivoDesenvolvimentoSustentavelService odsService;
	
	@Autowired
	MetaObjetivoDesenvolvimentoSustentavelService metaService;
	
	@Autowired
	private CalculadoraFormulaUtil calculadoraFormulaUtil;
	
	@Autowired
	VariavelService variavelService;
	
	@Autowired
	AlertaService alertaService;

	@Autowired
	private EntityManager em;

	private int String;

	public List<Indicador> listar() {
		return indicadorRepository.findAll();
	}
	
	public List<Indicador> listar(Long idOds) {
		return indicadorRepository.findAllByIdOds(idOds);
	}

	public List<Indicador> listar(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return indicadorRepository.findAll(pageRequest).getContent();
	}

	public List<Indicador> listar(Prefeitura prefeitura, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return indicadorRepository.findByPrefeitura(prefeitura.getId(), pageRequest);
	}

	public List<Indicador> listar(Prefeitura prefeitura) {
		return indicadorRepository.findByPrefeitura(prefeitura.getId());
	}
	
	public List<Indicador> listarTodosParaPrefeitura(Prefeitura prefeitura) {
		return indicadorRepository.findForPrefeitura(prefeitura);
	}
	
	public List<IndicadorcomparativomesmacidadeDTO> listarTodosIndicadoresPorIdCidade(Long idCidade){
		return indicadorRepository.findByIdCidade(idCidade);
	}
	
	public Indicador listarById(final Long id) {
	  Optional<Indicador> indicador = indicadorRepository.findById(id);
	  return indicador.orElseThrow(() -> new ObjectNotFoundException("Indicador não encontrado!"));
	}
	
	public Indicador listarById(Prefeitura prefeitura, final Long id) {
	  Optional<Indicador> indicador = indicadorRepository.findByPrefeituraAndId(prefeitura, id);
	  return indicador.orElseThrow(() -> new ObjectNotFoundException("Indicador não encontrado!"));
	}
	

	
	public Indicador inserir(IndicadorDTO indicadorDTO) {
		return inserir(null, indicadorDTO);
	}
	
	public Indicador inserir(Prefeitura prefeitura, IndicadorDTO indicadorDTO) {
		
		if(indicadorDTO.getValoresReferencia() != null && (indicadorDTO.getValoresReferencia().isEmpty() || (prefeitura == null && !validarValoresReferencia(indicadorDTO.getValoresReferencia())))) return null;
	    if(indicadorDTO.getEixo().getId() == null) return null;
	    if(indicadorDTO.getOds().getId() == null) return null;
	    if(indicadorDTO.getMetaODS().getId() == null) return null;
	    
		Eixo eixo = eixoService.listarById(indicadorDTO.getEixo().getId());
		ObjetivoDesenvolvimentoSustentavel ods = odsService.listarPorId(indicadorDTO.getOds().getId());
		MetaObjetivoDesenvolvimentoSustentavel metaODS = metaService.find(indicadorDTO.getMetaODS().getId());
				
		if(null == metaODS) {
		    return null;
		}

		Indicador indicador = indicadorDTO.toEntityInsert(eixo, ods, metaODS, prefeitura);
		indicadorRepository.save(indicador);
		
		String mensagem = null;
		if(prefeitura == null) {
			mensagem = "O indicador '" + indicadorDTO.getNome() + "' foi cadastrado na Plataforma.";
		} else {
			mensagem = "O indicador '" + indicadorDTO.getNome() + "' foi cadastrado na Plataforma pela prefeitura de " + prefeitura.getCidade().getNome(); 
		}
		
		alertaService.salvar(Alerta.builder()
				.mensagem(mensagem)
					.link("/indicadores")
					.tipoAlerta(TipoAlerta.CADASTRO_INDICADOR)
					.data(LocalDateTime.now())
					.build());
		
		return indicador;
	}

	public Indicador alterar(final Long id, final IndicadorDTO indicadorDTO) throws Exception {
		return alterar(null, id, indicadorDTO);
	}

	public Indicador alterar(final Prefeitura prefeitura, final Long id, final IndicadorDTO indicadorDTO) throws Exception {
		
		if (!(id == indicadorDTO.getId())) {
			throw new Exception("Campo id divergente.");
		}

		if(indicadorDTO.getEixo() == null || indicadorDTO.getEixo().getId() == null) {
		    throw new Exception("Eixo não pode ser nulo ou ter id nulo");
		}

	    if(indicadorDTO.getMetaODS() == null || indicadorDTO.getMetaODS().getId() == null) {
	        throw new Exception("Meta não pode ser nula ou ter id nulo");
	    }

	    if(indicadorDTO.getOds() == null || indicadorDTO.getOds().getId() == null) {
	        throw new Exception("ODS não pode ser nulo ou ter id nulo");
	    }

	    if(indicadorDTO.getValoresReferencia() != null && 
	       indicadorDTO.getValoresReferencia().size() < 4) {
	        throw new Exception("Deve haver no mínimo quatro valores de referência");
	    }
	    
		Indicador indicador = (null == prefeitura) ? listarById(id) : listarById(prefeitura, id);

		indicador.setEixo(eixoService.listarById(indicadorDTO.getEixo().getId()));
		indicador.setMetaODS(metaService.find(indicadorDTO.getMetaODS().getId()));
		indicador.setOds(odsService.listarPorId(indicadorDTO.getOds().getId()));
		
		Map<Long, Variavel> variaveisCadastradas = indicador.getVariaveis().stream()
								.collect(Collectors.toMap(Variavel::getId, variavel -> variavel));
		
		indicador.getVariaveis().clear();
		List<VariavelDTO> listaVericacaoVariaveis = new ArrayList<VariavelDTO>(); 
		for(VariavelDTO variavel : indicadorDTO.getVariaveis()) {
			if(!listaVericacaoVariaveis.contains(variavel)) {
				if (variaveisCadastradas.containsKey(variavel.getId())) {
					indicador.getVariaveis().add(variaveisCadastradas.get(variavel.getId()));
				} else {
					Variavel adicionado = variavelService.listarById(variavel.getId());
					indicador.getVariaveis().add(adicionado);
				}
			    if(variavel.getId() == null) {
			        indicador.getVariaveis().add(variavel.toEntityInsert(prefeitura));
			    }
			}
		    listaVericacaoVariaveis.add(variavel);
		}

		List<ValorReferencia> valoresReferencia = new ArrayList<>();
		
		if ( null == prefeitura ) {
			if ((indicador.getValoresReferencia() == null || indicador.getValoresReferencia().isEmpty()) && indicadorDTO.getValoresReferencia() != null) {
				indicadorDTO.getValoresReferencia().stream().forEach(referencia -> valoresReferencia.add(referencia.toEntityInsert()));
				indicador.setValoresReferencia(valoresReferencia);

			}else if (indicador.getValoresReferencia() != null && indicadorDTO.getValoresReferencia() != null) {
				List<ValorReferencia> valores = indicador.getValoresReferencia();
				for(int i = 0; i < valores.size(); i++) {
					ValorReferencia valorAtual = valores.get(i);
					valorAtual.setValorde(indicadorDTO.getValoresReferencia().get(i).getValorde());
					valorAtual.setValorate(indicadorDTO.getValoresReferencia().get(i).getValorate());
				}

			} else if (indicador.getValoresReferencia() != null && indicadorDTO.getValoresReferencia() == null){
				indicador.setValoresReferencia(new ArrayList<>());
			}
		}
		indicador.setFormulaResultado(indicadorDTO.getFormulaResultado());
		indicador.setFormulaReferencia(indicadorDTO.getFormulaReferencia());
		indicadorRepository.saveAndFlush(indicadorDTO.toEntityUpdate(indicador));
		return indicador;
	}
	
	public void deletar(Long id) throws Exception {
		Indicador indicador = listarById(id);
		try {
			indicadorRepository.delete(indicador);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void deletar(Prefeitura prefeitura, Long id) throws Exception {
		Indicador indicador =  listarById(prefeitura, id);
		try {
			indicadorRepository.delete(indicador);			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean validarValoresReferencia(List<ValorReferenciaDTO> valoresReferencia) {

		List<ValorReferenciaDTO> listaAux = new ArrayList<>();

		for (ValorReferenciaDTO valorReferencia : valoresReferencia) {

			if (valorReferencia.getValorde() > valorReferencia.getValorate()) {
				return false;
			}
			
			if (listaAux.isEmpty()) {
				listaAux.add(valorReferencia);
			}else {
				for (ValorReferenciaDTO valorReferenciaAux : listaAux) {
					if (valorReferencia.getValorde() >= valorReferenciaAux.getValorde() && valorReferencia.getValorde() <= valorReferenciaAux.getValorate()) {
						return false;						
					}else if (valorReferencia.getValorate() >= valorReferenciaAux.getValorde() && valorReferencia.getValorate() <= valorReferenciaAux.getValorate()) {
						return false;						
					}
				}
				
				listaAux.add(valorReferencia);
			}
		}
		return true;
	}

	public List<IndicadorDTO> buscarIndicadoresPcs() {
		List<Indicador> listaIndicador = indicadorRepository.buscarIndicadoresPcs();
		
		for(Indicador i : listaIndicador) {
			calculadoraFormulaUtil.formatarFormula(i);
		}
		
		List<IndicadorDTO> listaIndicadorDTO = listaIndicador.stream().map(obj -> new IndicadorDTO(obj)).collect(Collectors.toList());
		
		return listaIndicadorDTO;
	}
	
	public List<Indicador> buscarReferenciaIndicadoresPcs() {
		List<Indicador> listaIndicador = indicadorRepository.buscarIndicadoresPcs();
		return listaIndicador;
	}
	
	public List<IndicadorcomparativomesmacidadeDTO> listarTodosIndicadoresPorIdCidadePorAno(Long idCidade, String ano){
		Short anoAux = Short.valueOf(ano);
		return indicadorRepository.findTodosIndicadoresPorIdCidadePorAno(idCidade,anoAux);
	}
	
	public VisualizarIndicadorDTO buscarVisualizarIndicador(Long idIndicador) {
		Indicador indicador = listarById(idIndicador);
		
		calculadoraFormulaUtil.formatarFormula(indicador);
		String meta = indicador.getMetaODS() != null ? indicador.getMetaODS().getNumero()+": "+indicador.getMetaODS().getDescricao() : "não vinculada";
		String odsTitulo = indicador.getOds() != null ? indicador.getOds().getTitulo() : "Ods não vinculado";
		Long odsId = indicador.getOds() != null ? indicador.getOds().getId() : null;
		Integer odsNumero = indicador.getOds() != null ? indicador.getOds().getNumero() : null;
		VisualizarIndicadorDTO dto = VisualizarIndicadorDTO.builder()
				.nome(indicador.getNome())
				.descricao(indicador.getDescricao())
				.formula(indicador.getFormulaResultado())
				.metaODS("Meta "+ meta)
				.odsId(odsId)
				.odsNumero(odsNumero)
				.odsTitulo(odsTitulo)
				.build();

		return dto;
	}
	
	public List<IndicadorParaComboDTO> buscarIndicadoresPcsParaCombo() {
		return indicadorRepository.buscarIndicadoresPcsParaCombo();
	}
	
	public List<Indicador> buscarIndicadoresPCSporEixo(Long idEixo) {
		List<Indicador> lista = indicadorRepository.findByEixoIdAndPrefeituraIsNullOrderByNome(idEixo);
		return lista;
	}
	
	public List<ItemComboDTO> buscarPorIdOds(final Long id) {
	  return indicadorRepository.buscarOdsPorIdOds(id);
	}
	
	public List<ItemComboDTO> buscarItemCombo() {
		  return indicadorRepository.buscarItemCombo();
		}
	
	public List<ItemComboDTO> buscarIndicadoresPorIdEixoItemCombo(List<Long> idEixo) {
		  return indicadorRepository.buscarIndicadoresPorIdEixoItemCombo(idEixo);
		}

	public Long count() {
		return indicadorRepository.count();
	}
	
	public List<ItemComboDTO> filtrarIndicadoresInicial(Long idEixo, Long idOds) {

		List<Indicador> indicadoresFiltrados = buscarIndicadoresPorEixoOdsPopulacao(idEixo, idOds);
		
		List<ItemComboDTO> dtosRetorno = new ArrayList<>();

		for (Indicador indicador : indicadoresFiltrados) {
			dtosRetorno.add(new ItemComboDTO(indicador.getId(), indicador.getNome()));
		}

		return dtosRetorno;
	}
	
	private List<Indicador> buscarIndicadoresPorEixoOdsPopulacao(Long idEixo, Long idOds) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Indicador> query = cb.createQuery(Indicador.class);

		Root<Indicador> indicador = query.from(Indicador.class);

		Join<IndicadorPreenchido, Eixo> joinEixo = indicador.join("eixo", JoinType.LEFT);
		Join<IndicadorPreenchido, ObjetivoDesenvolvimentoSustentavel> joinOds = indicador.join("ods", JoinType.LEFT);
		query.multiselect(indicador.get("id"), indicador.get("nome")).distinct(true);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		predicateList.add(indicador.get("prefeitura").isNull());

		if (idEixo != null) {
			Path<Long> idEixoAux = joinEixo.get("id");
			predicateList.add(cb.equal(idEixoAux, idEixo));
		}

		if (idOds != null) {
			Path<Long> idOdsAux = joinOds.get("id");
			predicateList.add(cb.equal(idOdsAux, idOds));
		}

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(cb.asc(indicador.get("nome")));

		query.orderBy(orderList);

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<Indicador> typedQuery = em.createQuery(query);

		List<Indicador> listCidades = typedQuery.getResultList();

		return listCidades;
	}
	
	public List<Indicador> buscarIndicadoresDaCidade(Long idCidade) {
		List<Indicador> lista = indicadorRepository.findByPrefeituraCidadeIdOrderByNome(idCidade);
		return lista;
	}
	
	public boolean cidadePossuiIndicadoresCadastrados(Long idCidade) {
		return indicadorRepository.cidadePossuiIndicadoresCadastrados(idCidade);
	}

	public List<Long> listarApenasId() {
		List<Long> lista = indicadorRepository.findAllId();
		return lista;
	}

	public List<Long> listarApenasIdPcs() {
		List<Long> lista = indicadorRepository.findPcsId();
		return lista;
	}

	public List<Long> listarApenasIdPrefeituras() {
		List<Long> lista = indicadorRepository.findPrefeituraId();
		return lista;
	}

	public List<IndicadorParaComboDTO> buscarIndicadoresPcsParaComboPorVariavel(Long id) {
		Variavel variavel = variavelService.listarById(id);
		return indicadorRepository.buscarIndicadoresPcsParaComboPorVariavel(variavel);
	}

	public void corrigirFormula(Long idMinimo) {
		List<Indicador> indicadores = indicadorRepository.findByIdGreaterThanEqual(idMinimo);
		for(Indicador indicador : indicadores) {
			if (null != indicador.getVariaveis() && !indicador.getVariaveis().isEmpty()) {
				for(Variavel variavel : indicador.getVariaveis()) {
					while(indicador.getFormulaResultado().contains("$"+variavel.getId())) {
						String formula = indicador.getFormulaResultado();
						formula = formula.replace("$"+variavel.getId(), "#"+variavel.getId()+"#");
						indicador.setFormulaResultado(formula);
					}

				}
			}
			String formula = indicador.getFormulaResultado();
			if(formula.startsWith("CONCATENAR")) {
				formula = formula.replace("CONCATENAR", "concat(");
				formula = formula+")";
				indicador.setFormulaResultado(formula);
			}
			if(formula.contains("##")) {
				formula = formula.replace("##", "#;#");
				indicador.setFormulaResultado(formula);
			}
			if(formula.contains("CONCATENAR")) {
				formula = formula.replace("CONCATENAR", "");
				formula = "concat( "+formula+" )";
				indicador.setFormulaResultado(formula);
			}
			if(formula.contains("#;#")) {
				if(!formula.contains("concat")) {
					formula = "concat( "+formula+" )";
					indicador.setFormulaResultado(formula);
				}
			}
			if(formula.contains("+ )")) {
				formula = formula.replace("+ )", " )");
			}
			while(formula.contains("$")) {
				int inicio = formula.indexOf("$");
				String subformula = formula.substring(inicio+1,formula.length());
				inicio = subformula.indexOf(" ");
				subformula = subformula.substring(0,inicio);
				Long idVariavel = Long.valueOf(subformula);
				Variavel variavel = variavelService.listarById(idVariavel);
				if(null == indicador.getVariaveis()) {
					indicador.setVariaveis(new ArrayList<>());
					indicador.getVariaveis().add(variavel);
				} else {
					indicador.getVariaveis().add(variavel);
				}
				while(formula.contains("$"+variavel.getId())) {
					formula = formula.replace("$"+variavel.getId(), "#"+variavel.getId()+"#");
					indicador.setFormulaResultado(formula);
				}
			}

			indicadorRepository.save(indicador);
		}
	}
	
	public List<VariavelIndicadorDuplicadaDTO> buscarVariavelIndicadorDuplicada() {
		List<VariavelIndicadorDuplicadaDTO> duplicadas = indicadorRepository.buscarVariavelIndicadorDuplicada();
		return duplicadas;
	}
	
	public void excluirVariaveisDuplicadas() {
		List<VariavelIndicadorDuplicadaDTO> duplicadas = buscarVariavelIndicadorDuplicada();
		for(VariavelIndicadorDuplicadaDTO duplicada : duplicadas) {
			Indicador indicador = listarById(duplicada.getIdIndicador());
			for(int i=0; i<indicador.getVariaveis().size(); i++) {
				Variavel variavel = indicador.getVariaveis().get(i); 
				Long quantidade = indicador.getVariaveis().stream().filter(v -> v.getId() == variavel.getId()).count();
				if(quantidade > 1) {
					indicador.getVariaveis().remove(i);
					i--;
				}
			}
			indicadorRepository.save(indicador);
		}
	}
	
	public List<Indicador> buscarIndicadoresPorIds(List<Long> ids) {
		return indicadorRepository.buscarIndicadoresPorIds(ids);
	}
	
	public List<IndicadorParaComboDTO> buscarIndicadoresParaComboPorPreenchidos() {
		return indicadorRepository.buscarIndicadoresParaComboPorPreenchidos();
	}
	
	public List<Short> carregarComboAnosPreenchidosPorIdIndicador(Long idIndicadorSelecionada) {
		return indicadorRepository.carregarComboAnosPreenchidosPorIdIndicador(idIndicadorSelecionada);
	}

	@Transactional
	public void setaTipoConteudo() throws Exception {
		List<Indicador> indicadores = this.indicadorRepository.findByTipoConteudoIsNullAndPrefeituraIsNull();
		for(Indicador indicador : indicadores){
			if(indicador.isNumerico()){
				atualizarTipoRapido(indicador.getId(), "Numerico");
			} else {
				atualizarTipoRapido(indicador.getId(), "Texto");
			}
		}

	}

	@Transactional
	public void atualizarTipoRapido(Long id, String tipo) throws Exception {
		
		Query q  = em.createQuery("update Indicador set tipoConteudo = :tipo where id = :id");
		q.setParameter("id", id);
		q.setParameter("tipo", tipo);
		q.executeUpdate();
	}

	public List<EixoIndicadoresDTO> buscarComboEixosIndicadores(Long idCidade) {
		List<EixoIndicadoresDTO> dtos = new ArrayList<>();
		List<Eixo> eixos = this.eixoService.listar();
		for(Eixo eixo: eixos){
			EixoIndicadoresDTO dto = new EixoIndicadoresDTO(eixo);
			List<Indicador> indicadores = this.buscarIndicadoresPCSporEixo(eixo.getId());
			for(Indicador indicador: indicadores){
				IndicadorParaComboDTO indicadorDTO = new IndicadorParaComboDTO(indicador);
				dto.getIndicadores().add(indicadorDTO);
			}
			dtos.add(dto);
		}

		List<Indicador> indicadores = this.buscarIndicadoresDaCidade(idCidade);
		if(indicadores != null && !indicadores.isEmpty()){
			EixoIndicadoresDTO dto = new EixoIndicadoresDTO(0l, "Indicadores da Cidade", new ArrayList<>());
			for (Indicador indicador: indicadores) {
				IndicadorParaComboDTO indicadorDTO = new IndicadorParaComboDTO(indicador);
				dto.getIndicadores().add(indicadorDTO);
			}
			dtos.add(dto);
		}


		return dtos;
	}
	
	public Indicador buscarIndicadorPorId(Long idIndicador) {
		return indicadorRepository.buscarIndicadorPorId(idIndicador);
	}
	
	public Indicador buscarIndicadorPorNome(String nome) {
		return indicadorRepository.buscarIndicadorPorNome(nome);
	}
}
