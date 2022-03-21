package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import br.org.cidadessustentaveis.config.auth.DetailsService;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.VariavelDTO;
import br.org.cidadessustentaveis.dto.VariavelFiltradaIntegracaoDTO;
import br.org.cidadessustentaveis.dto.VariavelSimplesDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.repository.IndicadorRepository;
import br.org.cidadessustentaveis.repository.VariaveisOpcoesRepository;
import br.org.cidadessustentaveis.repository.VariavelPreenchidaRepository;
import br.org.cidadessustentaveis.repository.VariavelRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class VariavelService {

	@Autowired
	private VariavelRepository variavelRepository;

	@Autowired
	private VariaveisOpcoesRepository variaveisOpcoesRepository;

	@Autowired
	private IndicadorRepository indicadorRepository;

	@Autowired
	private VariavelPreenchidaRepository variavelPreenchidaRepository;
	


	@Autowired
	private EntityManager em;

	private Logger logger = LoggerFactory.getLogger(DetailsService.class);

	public List<Variavel> listar(Prefeitura prefeitura) {
		return variavelRepository.findAll(prefeitura);
	}

	public List<Variavel> listar() {
		return variavelRepository.findAll();
	}
	
	public List<Variavel> findByPrefeitura(Long idPrefeitura) {
		return variavelRepository.findByPrefeitura(idPrefeitura);
	}

	
	public List<Variavel> buscarSemPrefeituraOrderByName() {
		return variavelRepository.buscarSemPrefeituraOrderByName();
	}
	
	public List<Variavel> listarIdDesc() {
		return variavelRepository.findByOrderByIdDesc();
	}
	public List<Variavel> listarApenasPCS() {
		return variavelRepository.findByPrefeituraIsNull();
	}
	
	public List<Variavel> listarApenasPCSEPrefeituraPorId(Long idPrefeitura) {
		return variavelRepository.listarApenasPCSEPrefeituraPorId(idPrefeitura);
	}

	public Variavel listarById(Prefeitura prefeitura, final Long id) {
		Optional<Variavel> variavel = variavelRepository.findByPrefeituraAndId(prefeitura, id);
		return variavel.orElseThrow(() -> new ObjectNotFoundException("Variavel não encontrada!"));
	}

	public Variavel listarById(final Long id) {
		Optional<Variavel> variavel = variavelRepository.findById(id);
		return variavel.orElseThrow(() -> new ObjectNotFoundException("Variavel não encontrada!"));
	}

	public Variavel inserir(VariavelDTO variavelDTO) {
		return inserir(null, variavelDTO);
	}

	public Variavel inserir(Prefeitura prefeitura, VariavelDTO variavelDTO) {
		Variavel variavel = variavelDTO.toEntityInsert(prefeitura);
		variavel.getVariavelResposta().setVariavel(variavel);
		variavel.getVariavelReferencia().forEach(x -> x.setVariavel(variavel));

		if(!variavel.getTipo().contains("lista")) {
			variavel.setMultiplaSelecao(false);
		}
		return variavelRepository.save(variavel);
	}

	public Variavel alterar(final Long id, final VariavelDTO variavelDTO) throws Exception {
		return alterar(null, id, variavelDTO);
	}

	public Variavel alterar(Prefeitura prefeitura, final Long id, final VariavelDTO variavelDTO) throws Exception {
		Variavel variavel = (null == prefeitura) ? listarById(id) : listarById(prefeitura, id);

		//Popula VariaveisOpcoes ------------------------------------
		if ( null != variavelDTO.getVariavelResposta().getListaOpcoes()) {
			for (VariaveisOpcoes opcao: variavelDTO.getVariavelResposta().getListaOpcoes()) {
				if (opcao.getVariavelResposta() == null) {
					opcao.setVariavelResposta(variavelDTO.getVariavelResposta());
				}
			}
		}

		Set<VariaveisOpcoes> listaVar = variavelDTO.getVariavelResposta().getListaOpcoes();

		if(null != variavel.getVariavelResposta() && null != variavel.getVariavelResposta().getListaOpcoes()) {
			for (VariaveisOpcoes opcao: variavel.getVariavelResposta().getListaOpcoes()) {
				if(listaVar!= null && listaVar.stream().filter(x -> x.getId() == opcao.getId()).count() > 0){ // ?????????????????????????????????????????????????????????????????????????????? 
				} else {
					if(opcao.getId() != null) {
						opcao.setVariavelResposta(null);
						variaveisOpcoesRepository.deleteById(opcao.getId());
					}
				}
			}
		}

		//-----------------------------
		if (!variavel.getVariavelReferencia().isEmpty()) {
			for (ValorReferencia referencia: variavel.getVariavelReferencia()) {
				referencia.setVariavel(variavel);
			}
		}

		variavel = variavelDTO.toEntityUpdate(variavel);

		if(!variavel.getTipo().contains("lista")) {
			variavel.setMultiplaSelecao(false);
		}
		return variavelRepository.saveAndFlush(variavel);
	}

	public void deletar(final Long id) {
		deletar(null, id);
	}

	public void deletar(Prefeitura prefeitura, final Long id) {

		Variavel variavel = (null == prefeitura) ? listarById(id) : listarById(prefeitura, id);		

		long countVariavelPreenchida = variavelPreenchidaRepository.countByVariavelId(variavel.getId());
		if(countVariavelPreenchida > 0) {
			throw new DataIntegrityException("Variável já foi preenchida e não pode ser excluída");
		}

		String msgIndicadores = null;
		List<Indicador> listaIndicadores = indicadorRepository.findByVariaveisIn(variavel);
		if(listaIndicadores != null && !listaIndicadores.isEmpty()) {
			List<String> nomeIndicador = listaIndicadores.stream().map(Indicador::getNome).collect(Collectors.toList());
			msgIndicadores = Joiner.on(", ").join(nomeIndicador);
			throw new DataIntegrityException("A variável não pode ser excluída, pois está associada aos indicadores: "+msgIndicadores);
		}

		try {
			variavelRepository.delete(variavel);
		} catch (DataIntegrityViolationException e) {
			logger.error("Variável está sendo usada no sistema e não pode ser excluída");
			throw new DataIntegrityException("Variável está sendo usada no sistema e não pode ser excluída");
		}

	}

	public List<Eixo> eixosPorVariavel(Variavel variavel){
		List<Eixo> eixos  = new ArrayList<>();
		List<Indicador> indicadores = indicadorRepository.findByVariaveisIn(variavel);
		indicadores.forEach(i -> {
			if(!eixos.contains(i.getEixo())) {
				eixos.add(i.getEixo());
			}
		});
		return eixos;
	}

	public List<VariavelSimplesDTO> buscarVariaveisPCSNumericas() {
		List<String> tipos =  new ArrayList<>();
		tipos.add("Numérico decimal");
		tipos.add("Numérico inteiro");
		List<Variavel> variaveis =  this.variavelRepository.findByTipoInAndPrefeituraIsNullOrderByNome(tipos);
		List<VariavelSimplesDTO> listaVariavelDto = variaveis.stream().map(obj -> new VariavelSimplesDTO(obj.getId(), obj.getNome())).collect(Collectors.toList());
		return listaVariavelDto;

	}

	public List<VariavelDTO> buscarPorIdIndicador(List<Long> idsIndicador) {
		return variavelRepository.buscarPorIdIndicador(idsIndicador);
	}

	public List<Long> buscarIdsPorIdIndicador(Long idIndicador) {
		return variavelRepository.buscarIdsPorIdIndicador(idIndicador);
	}
	@Transactional
	public void alterarId(Long id, Long novoId) throws Exception {
		Variavel variavel = listarById(id);
		if(variavel == null) {
			throw new Exception("Esse id não existe");
		}
		Query q  = em.createQuery("update Variavel set id = :novoId where id = :id");
		q.setParameter("id", id);
		q.setParameter("novoId", novoId);
		q.executeUpdate();
		
		
		variavel = listarById(novoId);
		
		List<Indicador> indicadores = indicadorRepository.findByVariaveisIn(variavel);
		if(indicadores != null) {
			for(Indicador indicador : indicadores) {
				if(indicador.getFormulaResultado().contains("#"+id+"#")) {
					String novaFormula = indicador.getFormulaResultado().replaceAll("#"+id+"#", "#"+novoId+"#");
					indicador.setFormulaResultado(novaFormula);
				}
				indicadorRepository.save(indicador);
			}
		}
		
		
		
		
	}
	
	public List<ItemComboDTO> buscarVariaveisPcsParaCombo() {
		List<Variavel> lista = variavelPreenchidaRepository.buscarVariaveisParaCombo();
		return lista.stream().map(obj -> new ItemComboDTO(obj.getId(), obj.getNome())).collect(Collectors.toList());
	}
	
	public List<VariavelFiltradaIntegracaoDTO> buscarVariaveisFiltradasIntegracao(String nomeVariavel, String nomeCidade){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<VariavelFiltradaIntegracaoDTO> query = cb.createQuery(VariavelFiltradaIntegracaoDTO.class);

		Root<Variavel> variavel = query.from(Variavel.class);
		
		Join<Variavel, Prefeitura> joinPrefeitura = variavel.join("prefeitura",JoinType.LEFT);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade",JoinType.LEFT);
		
		query.multiselect(variavel.get("id"),variavel.get("nome"),variavel.get("descricao"),variavel.get("tipo"),variavel.get("unidade"),
				joinCidade.get("nome")).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if (nomeVariavel != null && !nomeVariavel.equals("")) {
			Path<String> nomeVariavelPath = variavel.get("nome");
			javax.persistence.criteria.Predicate predicateForNome = cb.like(cb.lower(nomeVariavelPath), "%" + nomeVariavel.toLowerCase() + "%");
			predicateList.add(predicateForNome);
		}	
		
		if (nomeCidade != null && !nomeCidade.equals("")) {
			Path<String> nomeCidadePath = joinCidade.get("nome");
			javax.persistence.criteria.Predicate predicateForNomeCidade = cb.like(cb.lower(nomeCidadePath), "%" + nomeCidade.toLowerCase() + "%");
			predicateList.add(predicateForNomeCidade);
		}

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(variavel.get("id")));
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<VariavelFiltradaIntegracaoDTO> typedQuery = em.createQuery(query);

		List<VariavelFiltradaIntegracaoDTO> listVariaveis = typedQuery.getResultList(); 
		
		return listVariaveis;
	}
	
	public Variavel buscarVariavelById(final Long id) {
		Optional<Variavel> variavelOpt = variavelRepository.findById(id);
		if(variavelOpt.isPresent()) {
			return variavelOpt.get();
		} else {
			return null;
		}
		
	}
	
	public List<Long> buscarVariavelById(final String nome) {
		List<Long> listaIds = variavelRepository.findByNomeLike(nome.toLowerCase());
		return listaIds;
	}

}
