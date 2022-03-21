package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.Orgao;
import br.org.cidadessustentaveis.repository.OrgaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class OrgaoService {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private OrgaoRepository repository;
	
	public List<Orgao> buscarComboBoxOrgao(Long id) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Orgao> query = cb.createQuery(Orgao.class);
		
		Root<Orgao> orgao = query.from(Orgao.class);
		
		query.multiselect(orgao.get("id"), orgao.get("nome"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(id != null) {
			Path<Long> idInstancia = orgao.get("instanciaOrgao");
			predicateList.add(cb.equal(idInstancia, id));
			predicateList.add(cb.notEqual(idInstancia, 1));
			predicateList.add(cb.notEqual(idInstancia, 2));
			predicateList.add(cb.notEqual(idInstancia, 3));
		}
		
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(orgao.get("nome")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<Orgao> lista = em.createQuery(query).getResultList();
		return lista;
	}
	
	
	public List<Orgao> buscarComboBoxOrgao() {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Orgao> query = cb.createQuery(Orgao.class);
		
		Root<Orgao> orgao = query.from(Orgao.class);
		
		query.multiselect(orgao.get("id"), orgao.get("nome"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		/*
		if(id != null) {
			Path<Long> idInstancia = orgao.get("instanciaOrgao");
			predicateList.add(cb.equal(idInstancia, id));
			predicateList.add(cb.notEqual(idInstancia, 1));
			predicateList.add(cb.notEqual(idInstancia, 2));
			predicateList.add(cb.notEqual(idInstancia, 3));
		}
		*/
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(orgao.get("nome")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<Orgao> lista = em.createQuery(query).getResultList();
		return lista;
	}
	
	public Orgao inserirOrgao(Orgao orgao) {
		orgao = repository.save(orgao);
		
		return orgao;
	}
	
	public Orgao buscarPorId(Long id) {
		Optional<Orgao> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Orgão não encontrado!"));
	}


	public Orgao buscarPorNome(String nomeOrgaoFederal, String nomeInstanciaOrgao) {
		return repository.findByNomeAndInstanciaOrgaoNome(nomeOrgaoFederal,nomeInstanciaOrgao);
	}

	
}
