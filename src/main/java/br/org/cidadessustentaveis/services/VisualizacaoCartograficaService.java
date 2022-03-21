package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import br.org.cidadessustentaveis.dto.VisualizacaoCartograficaDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.sistema.VisualizacaoCartografica;
import br.org.cidadessustentaveis.repository.VisualizacaoCartograficaRepository;

@Service
public class VisualizacaoCartograficaService {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private VisualizacaoCartograficaRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private IndicadorService indicadorService;
	
	@Autowired
	private CidadeService cidadeService;
	
	public List<VisualizacaoCartograficaDTO> buscar(VisualizacaoCartograficaDTO filtro){
		
		String VISUALIZACAO = "Visualizacao";
		String EXPORTACAO = "Exportacao";
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<VisualizacaoCartograficaDTO> query = cb.createQuery(VisualizacaoCartograficaDTO.class);
		
		Root<VisualizacaoCartografica> visualizacaoCartografica = query.from(VisualizacaoCartografica.class);
		
		Join<VisualizacaoCartografica, Indicador> joinIndicador = visualizacaoCartografica.join("indicador", JoinType.LEFT);
		Join<VisualizacaoCartografica, Cidade> joinCidade = visualizacaoCartografica.join("cidade", JoinType.LEFT);
		Join<VisualizacaoCartografica, Usuario> joinUser = visualizacaoCartografica.join("usuario", JoinType.LEFT);
		
		query.multiselect(
				visualizacaoCartografica.get("id"),
				joinIndicador.get("nome"), 
				joinCidade.get("nome"), 
				visualizacaoCartografica.get("estado"), 
				visualizacaoCartografica.get("data"),
				joinUser.get("nome"),
				visualizacaoCartografica.get("acao")
				);
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> data = (cb.function("date", LocalDate.class, visualizacaoCartografica.get("data")));
			predicateList.add(cb.greaterThanOrEqualTo(data, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> data = (cb.function("date", LocalDate.class, visualizacaoCartografica.get("data")));
			predicateList.add(cb.lessThanOrEqualTo(data, filtro.getDataFim()));
		}
		
		if(filtro.getIndicador() != null && !filtro.getIndicador().isEmpty()) {
			Path<String> campoNomeIndicador = joinIndicador.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeIndicador), "%" + filtro.getIndicador().toLowerCase() + "%"));
		}
		
		if(filtro.getEstado() != null && !filtro.getEstado().isEmpty()) {
			Path<String> campoNomeEstado = visualizacaoCartografica.get("estado");
			predicateList.add(cb.equal(campoNomeEstado,filtro.getEstado()));
		}
		
		if(filtro.getCidade() != null && !filtro.getCidade().isEmpty()) {
			Path<String> campoNomeCidade = joinCidade.get("nome");
			predicateList.add(cb.equal(campoNomeCidade,filtro.getCidade()));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(visualizacaoCartografica.get("data")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<VisualizacaoCartograficaDTO> relatorioVisualizacaoCartografica = em.createQuery(query).getResultList();
		
		
		return relatorioVisualizacaoCartografica;
	
	}
	
public void inserirRelatorioVisualizacaoCartografica(Long idIndicador, Long idCidade, Usuario usuarioLogado, String acao) {
		
		VisualizacaoCartografica newVisualizacaoCartografica = VisualizacaoCartografica.builder()
		.indicador(indicadorService.buscarIndicadorPorId(idIndicador))
		.cidade(idCidade != null ? cidadeService.buscarPorId(idCidade) : null)
		.data(LocalDateTime.now())
		.estado(idCidade != null ? cidadeService.buscarPorId(idCidade).getProvinciaEstado().getNome() : null)
		.usuario(usuarioLogado != null ? usuarioLogado : null)
		.acao(acao)
		.build();
		repository.save(newVisualizacaoCartografica);
	}

}
