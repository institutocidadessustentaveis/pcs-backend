package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.RelatorioApiPublicaDTO;
import br.org.cidadessustentaveis.dto.RelatorioPlanoDeMetasDTO;
import br.org.cidadessustentaveis.model.sistema.RelatorioApiPublica;
import br.org.cidadessustentaveis.repository.RelatorioApiPublicaRepository;

@Service
public class RelatorioApiPublicaService {
	
	@Autowired
	RelatorioApiPublicaRepository repository;
	
	@Autowired
	EntityManager em;
	
	public void salvarRegistro(String endpoint, String origemReq) {
		
		RelatorioApiPublica relatorio = RelatorioApiPublica.builder()
				.endpoint(endpoint)
				.dataHora(LocalDateTime.now())
				.origemRequisicao(origemReq)
				.build();
		
		repository.save(relatorio);
	}
	
	public List<RelatorioApiPublicaDTO> buscarRelatorioFiltrado(RelatorioApiPublicaDTO filtro) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioApiPublicaDTO> query = cb.createQuery(RelatorioApiPublicaDTO.class);
		
		Root<RelatorioApiPublica> relatorio = query.from(RelatorioApiPublica.class);
		
		query.multiselect(
				relatorio.get("id"),
				relatorio.get("endpoint"),
				relatorio.get("dataHora"),
				relatorio.get("origemRequisicao"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDateTime> campoDataHora = cb.function("date", LocalDateTime.class, relatorio.get("dataHora"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDateTime> campoDataHora = cb.function("date", LocalDateTime.class, relatorio.get("dataHora"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, filtro.getDataFim()));
		}
		
		if(filtro.getEndpoint() != null && !filtro.getEndpoint().isEmpty()) {
			Path<String> campoEndpoint = relatorio.get("endpoint");
			predicateList.add(cb.equal(campoEndpoint, filtro.getEndpoint()));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(relatorio.get("dataHora")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<RelatorioApiPublicaDTO> lista = em.createQuery(query).getResultList();
		
		return lista;
	}

}
