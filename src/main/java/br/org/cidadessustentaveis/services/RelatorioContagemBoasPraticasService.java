package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.RelatorioContagemBoasPraticasDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;

@Service
public class RelatorioContagemBoasPraticasService {
    
    @Autowired
	private EntityManager em;
    
    public List<RelatorioContagemBoasPraticasDTO> buscarRelatoriosPorIndicador(String tipoBoaPratica) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioContagemBoasPraticasDTO> query = cb.createQuery(RelatorioContagemBoasPraticasDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Indicador> joinIndicadores = boaPratica.join("indicadores",JoinType.LEFT);

		query.multiselect(joinIndicadores.get("id"), joinIndicadores.get("nome"), cb.count(boaPratica.get("id")));
		
		query.groupBy(joinIndicadores.get("id"));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Predicate predicateForTipo = cb.equal(boaPratica.get("tipo"), tipoBoaPratica);
		
		Predicate predicateForNome = cb.isNotNull(joinIndicadores.get("nome"));

		predicateList.add(cb.and(predicateForTipo,predicateForNome));

		predicateList.add(predicateForTipo);


		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinIndicadores.get("nome")));
		
		query.orderBy(orderList);

		TypedQuery<RelatorioContagemBoasPraticasDTO> typedQuery = em.createQuery(query);
		List<RelatorioContagemBoasPraticasDTO> lista = typedQuery.getResultList();

		return lista;
	}
    
    public List<RelatorioContagemBoasPraticasDTO> buscarRelatoriosPorOds(String tipoBoaPratica) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioContagemBoasPraticasDTO> query = cb.createQuery(RelatorioContagemBoasPraticasDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);

		Join<BoaPratica, ObjetivoDesenvolvimentoSustentavel> joinOds = boaPratica.join("ods",JoinType.LEFT);

		query.multiselect(joinOds.get("id"), joinOds.get("titulo"), cb.count(boaPratica.get("id")));
		
		query.groupBy(joinOds.get("id"));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Predicate predicateForTipo = cb.equal(boaPratica.get("tipo"), tipoBoaPratica);
		
		Predicate predicateForNome = cb.isNotNull(joinOds.get("titulo"));

		predicateList.add(cb.and(predicateForTipo,predicateForNome));


		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinOds.get("titulo")));
		
		query.orderBy(orderList);

		TypedQuery<RelatorioContagemBoasPraticasDTO> typedQuery = em.createQuery(query);
		List<RelatorioContagemBoasPraticasDTO> lista = typedQuery.getResultList();

		return lista;
	}
    
    public List<RelatorioContagemBoasPraticasDTO> buscarRelatoriosPorMetaOds(String tipoBoaPratica) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioContagemBoasPraticasDTO> query = cb.createQuery(RelatorioContagemBoasPraticasDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);

		Join<BoaPratica, MetaObjetivoDesenvolvimentoSustentavel> joinMetasOds = boaPratica.join("metasOds",JoinType.LEFT);

		query.multiselect(joinMetasOds.get("id"), joinMetasOds.get("descricao"), cb.count(boaPratica.get("id")));
		
		query.groupBy(joinMetasOds.get("id"));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Predicate predicateForTipo = cb.equal(boaPratica.get("tipo"), tipoBoaPratica);
		
		Predicate predicateForNome = cb.isNotNull(joinMetasOds.get("descricao"));

		predicateList.add(cb.and(predicateForTipo,predicateForNome));

		predicateList.add(predicateForTipo);


		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinMetasOds.get("descricao")));
		
		query.orderBy(orderList);

		TypedQuery<RelatorioContagemBoasPraticasDTO> typedQuery = em.createQuery(query);
		List<RelatorioContagemBoasPraticasDTO> lista = typedQuery.getResultList();

		return lista;
	}
    
    public List<RelatorioContagemBoasPraticasDTO> buscarRelatoriosPorEixo(String tipoBoaPratica) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioContagemBoasPraticasDTO> query = cb.createQuery(RelatorioContagemBoasPraticasDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);

		Join<BoaPratica, Eixo> joinEixo = boaPratica.join("eixo",JoinType.LEFT);

		query.multiselect(joinEixo.get("id"), joinEixo.get("nome"), cb.count(boaPratica.get("id")));
		
		query.groupBy(joinEixo.get("id"));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Predicate predicateForTipo = cb.equal(boaPratica.get("tipo"), tipoBoaPratica);
		
		Predicate predicateForNome = cb.isNotNull(joinEixo.get("nome"));

		predicateList.add(cb.and(predicateForTipo,predicateForNome));

		predicateList.add(predicateForTipo);


		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinEixo.get("nome")));
		
		query.orderBy(orderList);

		TypedQuery<RelatorioContagemBoasPraticasDTO> typedQuery = em.createQuery(query);
		List<RelatorioContagemBoasPraticasDTO> lista = typedQuery.getResultList();

		return lista;
	}
    
    public List<RelatorioContagemBoasPraticasDTO> buscarRelatoriosPorEstado(String tipoBoaPratica) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioContagemBoasPraticasDTO> query = cb.createQuery(RelatorioContagemBoasPraticasDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = boaPratica.join("estado",JoinType.LEFT);

		query.multiselect(joinProvinciaEstado.get("id"), joinProvinciaEstado.get("nome"), cb.count(boaPratica.get("id")));
		
		query.groupBy(joinProvinciaEstado.get("id"));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Predicate predicateForTipo = cb.equal(boaPratica.get("tipo"), tipoBoaPratica);
		
		Predicate predicateForNome = cb.isNotNull(joinProvinciaEstado.get("nome"));

		predicateList.add(cb.and(predicateForTipo,predicateForNome));

		predicateList.add(predicateForTipo);


		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinProvinciaEstado.get("nome")));
		
		query.orderBy(orderList);

		TypedQuery<RelatorioContagemBoasPraticasDTO> typedQuery = em.createQuery(query);
		List<RelatorioContagemBoasPraticasDTO> lista = typedQuery.getResultList();

		return lista;
	}
    
    public List<RelatorioContagemBoasPraticasDTO> buscarRelatoriosPorCidade(String tipoBoaPratica) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioContagemBoasPraticasDTO> query = cb.createQuery(RelatorioContagemBoasPraticasDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, Cidade> joinCidade = boaPratica.join("municipio",JoinType.LEFT);

		query.multiselect(joinCidade.get("id"), joinCidade.get("nome"), cb.count(boaPratica.get("id")));
		
		query.groupBy(joinCidade.get("id"));
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Predicate predicateForTipo = cb.equal(boaPratica.get("tipo"), tipoBoaPratica);
		
		Predicate predicateForNome = cb.isNotNull(joinCidade.get("nome"));

		predicateList.add(cb.and(predicateForTipo,predicateForNome));

		predicateList.add(predicateForTipo);


		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(joinCidade.get("nome")));
		
		query.orderBy(orderList);

		TypedQuery<RelatorioContagemBoasPraticasDTO> typedQuery = em.createQuery(query);
		List<RelatorioContagemBoasPraticasDTO> lista = typedQuery.getResultList();

		return lista;
	}
}
