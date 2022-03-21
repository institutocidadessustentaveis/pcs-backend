package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
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

import br.org.cidadessustentaveis.dto.AtividadeUsuarioDTO;
import br.org.cidadessustentaveis.model.administracao.AtividadeUsuario;
import br.org.cidadessustentaveis.repository.AtividadeUsuarioRepository;

@Service
public class AtividadeUsuarioService {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private AtividadeUsuarioRepository repository; 
	
	public List<AtividadeUsuarioDTO> buscar(AtividadeUsuarioDTO filtro){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<AtividadeUsuarioDTO> query = cb.createQuery(AtividadeUsuarioDTO.class);
		
		Root<AtividadeUsuario> atividadeUsuario = query.from(AtividadeUsuario.class);
		
		query.multiselect(atividadeUsuario.get("nomeUsuario"), atividadeUsuario.get("dataHora"), atividadeUsuario.get("acao"), atividadeUsuario.get("modulo"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, atividadeUsuario.get("dataHora"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, atividadeUsuario.get("dataHora"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, filtro.getDataFim()));
		}
		
		Path<String> campoNomeUsuario = atividadeUsuario.get("nomeUsuario");
		if(filtro.getNomeUsuario() != null && !filtro.getNomeUsuario().isEmpty()) {
			predicateList.add(cb.equal(campoNomeUsuario, filtro.getNomeUsuario()));
		}
		
		if(filtro.getModulo() != null && !filtro.getModulo().isEmpty()) {
			Path<String> campoNomeModulo = atividadeUsuario.get("modulo");
			predicateList.add(cb.like(cb.lower(campoNomeModulo), "%" + filtro.getModulo() + "%"));
		}
		
		predicateList.add(cb.isNotNull(campoNomeUsuario));
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(atividadeUsuario.get("dataHora")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<AtividadeUsuarioDTO> atividadesUsuario = em.createQuery(query).getResultList();
		
		return atividadesUsuario;
	}
	
	public AtividadeUsuario salvarAtividadeUsuario(AtividadeUsuarioDTO atividadeUsuarioDTO) {
		AtividadeUsuario atividadeUsuario = atividadeUsuarioDTO.toEntityInsert();
		return repository.save(atividadeUsuario);
	}
}
