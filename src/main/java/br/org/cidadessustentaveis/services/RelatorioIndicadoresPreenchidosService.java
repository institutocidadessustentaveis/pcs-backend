package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

import br.org.cidadessustentaveis.dto.RelatorioIndicadoresPreenchidosDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;

@Service
public class RelatorioIndicadoresPreenchidosService {
	
	@Autowired
	EntityManager em;
	
	public List<RelatorioIndicadoresPreenchidosDTO> buscar(RelatorioIndicadoresPreenchidosDTO filtro){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioIndicadoresPreenchidosDTO> query = cb.createQuery(RelatorioIndicadoresPreenchidosDTO.class);
		
		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);
		
		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");
		
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");
		
		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");
		
		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");
		
		Join<Indicador, ObjetivoDesenvolvimentoSustentavel> joinOds = joinIndicador.join("ods");
		
		Join<Indicador, Eixo> joinEixo = joinIndicador.join("eixo");
		
		query.multiselect(
				joinCidade.get("nome"), 
				joinProvinciaEstado.get("sigla"), 
				joinProvinciaEstado.get("nome"), 
				joinIndicador.get("nome"), 
				joinOds.get("titulo"), 
				joinEixo.get("nome"), 
				indicadorPreenchido.get("dataPreenchimento"), 
				joinCidade.get("codigoIbge"),
				indicadorPreenchido.get("ano"));
	
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getAnoInicio() != null) {
			predicateList.add(cb.greaterThanOrEqualTo(indicadorPreenchido.get("ano"), filtro.getAnoInicio()));
		}
		
		if(filtro.getAnoFim() != null) {
			predicateList.add(cb.lessThanOrEqualTo(indicadorPreenchido.get("ano"), filtro.getAnoFim()));
		}
		
		if(filtro.getPrefeitura() != null && !filtro.getPrefeitura().isEmpty()) {
			String nomeCidade = filtro.getPrefeitura().substring(0, filtro.getPrefeitura().indexOf(" -"));
			Path<String> campoNomePrefeitura = joinCidade.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomePrefeitura), "%" + nomeCidade.toLowerCase() + "%"));
		}
		
		if(filtro.getEstadoNomecompleto() != null && !filtro.getEstadoNomecompleto().isEmpty()) {
			Path<String> campoNomeEstado = joinProvinciaEstado.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeEstado), "%" + filtro.getEstadoNomecompleto().toLowerCase() + "%"));
		}
		
		if(filtro.getIndicador() != null && !filtro.getIndicador().isEmpty()) {
			Path<String> campoNomeIndicador = joinIndicador.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeIndicador), "%" + filtro.getIndicador().toLowerCase() + "%"));
		}
		
		if(filtro.getOds() != null && !filtro.getOds().isEmpty()) {
			Path<String> campoNomeODS =  joinOds.get("titulo");
			predicateList.add(cb.like(cb.lower(campoNomeODS), "%" + filtro.getOds().toLowerCase() + "%"));
		}	
		
		if(filtro.getEixo() != null && !filtro.getEixo().isEmpty()) {
			Path<String> campoNomeEixo = joinEixo.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeEixo), "%" + filtro.getEixo().toLowerCase() + "%"));
		}	
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(indicadorPreenchido.get("dataPreenchimento")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		
		List<RelatorioIndicadoresPreenchidosDTO> relatorioIndicadores = em.createQuery(query).getResultList();
		return relatorioIndicadores;

	}
	
	public List<RelatorioIndicadoresPreenchidosDTO> buscarComPaginacao(RelatorioIndicadoresPreenchidosDTO filtro){

		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioIndicadoresPreenchidosDTO> query = cb.createQuery(RelatorioIndicadoresPreenchidosDTO.class);
		
		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);
		
		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");
		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");
		Join<IndicadorPreenchido, Indicador> joinIndicador = indicadorPreenchido.join("indicador");
		Join<Indicador, ObjetivoDesenvolvimentoSustentavel> joinOds = joinIndicador.join("ods");
		Join<Indicador, Eixo> joinEixo = joinIndicador.join("eixo");
		
		query.multiselect(
				joinCidade.get("nome"), 
				joinProvinciaEstado.get("sigla"), 
				joinProvinciaEstado.get("nome"), 
				joinIndicador.get("nome"), 
				joinOds.get("titulo"), 
				joinEixo.get("nome"), 
				indicadorPreenchido.get("dataPreenchimento"), 
				joinCidade.get("codigoIbge"), 
				indicadorPreenchido.get("ano"));
	
		List<Predicate> predicateList = new ArrayList<>();
		
			if(filtro.getAnoInicio() != null && filtro.getAnoFim() != null) {
				predicateList.add(cb.between(indicadorPreenchido.get("ano"), filtro.getAnoInicio(), filtro.getAnoFim()));
			}else {
				if(filtro.getAnoInicio() != null) {
					predicateList.add(cb.greaterThanOrEqualTo(indicadorPreenchido.get("ano"), filtro.getAnoInicio()));
				}
				
				if(filtro.getAnoFim() != null) {
					predicateList.add(cb.lessThanOrEqualTo(indicadorPreenchido.get("ano"), filtro.getAnoFim()));
				}
			}
			
			if(filtro.getPrefeitura() != null && !filtro.getPrefeitura().isEmpty()) {
				String nomeCidade = filtro.getPrefeitura().substring(0, filtro.getPrefeitura().indexOf(" -"));
				Path<String> campoNomePrefeitura = joinCidade.get("nome");
				predicateList.add(cb.like(cb.lower(campoNomePrefeitura), "%" + nomeCidade.toLowerCase() + "%"));
			}
			
			if(filtro.getEstadoNomecompleto() != null && !filtro.getEstadoNomecompleto().isEmpty()) {
				//String nomeEstado = filtro.getEstadoNomecompleto().substring(0, filtro.getEstadoNomecompleto().indexOf(" -"));
				Path<String> campoNomeEstado = joinProvinciaEstado.get("nome");
				predicateList.add(cb.like(cb.lower(campoNomeEstado), "%" + filtro.getEstadoNomecompleto().toLowerCase() + "%"));
			}
			
			if(filtro.getIndicador() != null && !filtro.getIndicador().isEmpty()) {
				Path<String> campoNomeIndicador = joinIndicador.get("nome");
				predicateList.add(cb.like(cb.lower(campoNomeIndicador), "%" + filtro.getIndicador().toLowerCase() + "%"));
			}
			
			if(filtro.getOds() != null && !filtro.getOds().isEmpty()) {
				Path<String> campoNomeODS =  joinOds.get("titulo");
				predicateList.add(cb.like(cb.lower(campoNomeODS), "%" + filtro.getOds().toLowerCase() + "%"));
			}	
			
			if(filtro.getEixo() != null && !filtro.getEixo().isEmpty()) {
				Path<String> campoNomeEixo = joinEixo.get("nome");
				predicateList.add(cb.like(cb.lower(campoNomeEixo), "%" + filtro.getEixo().toLowerCase() + "%"));
			}
		
		List<Order> orderList = new ArrayList<Order>();
		Path<Object> orderBy = indicadorPreenchido.get("dataPreenchimento");

		if ("prefeitura".equals(filtro.getOrderBy())) {
			orderBy = joinCidade.get("nome");
		}else if ("estado".equals(filtro.getOrderBy())) {
			orderBy = joinProvinciaEstado.get("sigla");
		}else if ("indicador".equals(filtro.getOrderBy())) {
			orderBy = joinIndicador.get("nome");
		}else if ("ods".equals(filtro.getOrderBy())) {
			orderBy = joinOds.get("titulo");
		}else if ("eixo".equals(filtro.getOrderBy())) {
			orderBy = joinEixo.get("nome");
		}
		if (filtro.getDirection() != null && "DESC".equals(filtro.getDirection())) {
			orderList.add(cb.desc(orderBy));
		}else {
			orderList.add(cb.asc(orderBy));
		}
		query.orderBy(orderList);
		
		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<RelatorioIndicadoresPreenchidosDTO> typedQuery = em.createQuery(query);
		Long count = new Long(typedQuery.getResultList().size());
		
		if(filtro.getPage() == 0) {
			typedQuery.setFirstResult(filtro.getPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		} else {
			typedQuery.setFirstResult(filtro.getPage() * filtro.getLinesPerPage());
			typedQuery.setMaxResults(filtro.getLinesPerPage());
		}

		List<RelatorioIndicadoresPreenchidosDTO> listIndicadoresPreenchidos = typedQuery.getResultList(); 
		
		
		listIndicadoresPreenchidos.forEach(i -> i.setCount(count));

		return listIndicadoresPreenchidos;
		
		
	}
	
}
