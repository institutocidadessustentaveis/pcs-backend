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

import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.model.sistema.HistoricoRelatorioGerado;
import br.org.cidadessustentaveis.repository.HistoricoRelatorioGeradoRepository;

@Service
public class HistoricoRelatorioGeradoService {
	
	@Autowired
	private HistoricoRelatorioGeradoRepository repository;
	
	@Autowired
	EntityManager em;
	
	public List<HistoricoRelatorioGeradoDTO> buscar(HistoricoRelatorioGeradoDTO filtro){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<HistoricoRelatorioGeradoDTO> query = cb.createQuery(HistoricoRelatorioGeradoDTO.class);
		
		Root<HistoricoRelatorioGerado> relatorioGerado = query.from(HistoricoRelatorioGerado.class);
		
		query.multiselect(relatorioGerado.get("nomeUsuario"), relatorioGerado.get("dataHora"), relatorioGerado.get("nomeRelatorio"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> dataCadastro = (cb.function("date", LocalDate.class, relatorioGerado.get("dataHora")));
			predicateList.add(cb.greaterThanOrEqualTo(dataCadastro, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> dataCadastro = (cb.function("date", LocalDate.class, relatorioGerado.get("dataHora")));
			predicateList.add(cb.lessThanOrEqualTo(dataCadastro, filtro.getDataFim()));
		}
		
		if(filtro.getNomeUsuario()!= null && !filtro.getNomeUsuario().isEmpty()) {
			Path<String> campoNomeUsuario = relatorioGerado.get("nomeUsuario");
			predicateList.add(cb.equal(campoNomeUsuario, filtro.getNomeUsuario()));
		}
		
		if(filtro.getNomeRelatorio() != null && !filtro.getNomeRelatorio().isEmpty()) {
			Path<String> campoNomeRelatorio = relatorioGerado.get("nomeRelatorio");
			predicateList.add(cb.like(cb.lower(campoNomeRelatorio), "%" + filtro.getNomeRelatorio() + "%"));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(relatorioGerado.get("dataHora")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		
		List<HistoricoRelatorioGeradoDTO> relatorioHistoricoRelGerado = em.createQuery(query).getResultList();
		return relatorioHistoricoRelGerado;
	}
	
	public void insert(HistoricoRelatorioGeradoDTO historicoRelatorioGeradoDTO) {
		HistoricoRelatorioGerado historicoRelatorioGerado = historicoRelatorioGeradoDTO.toEntityInsert();
		historicoRelatorioGerado = repository.save(historicoRelatorioGerado);
	}
	
	public void gravarLog(String usuarioLogado, String tipoRelatorio)
	{
		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);
		this.insert(objeto);
	}
}
