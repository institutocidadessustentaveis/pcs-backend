package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import br.org.cidadessustentaveis.dto.RelatorioConteudoCompartilhadoDTO;
import br.org.cidadessustentaveis.model.sistema.RelatorioConteudoCompartilhado;
import br.org.cidadessustentaveis.repository.RelatorioConteudoCompartilhadoRepository;

@Service
public class RelatorioConteudoCompartilhadoService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	private RelatorioConteudoCompartilhadoRepository repository;
	
	@Autowired
	private HistoricoRelatorioGeradoService serviceHistorico;
	
	public List<RelatorioConteudoCompartilhadoDTO> buscar(RelatorioConteudoCompartilhadoDTO filtro){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioConteudoCompartilhadoDTO> query = cb.createQuery(RelatorioConteudoCompartilhadoDTO.class);
		
		Root<RelatorioConteudoCompartilhado> relatorioconteudoCompartilhado = query.from(RelatorioConteudoCompartilhado.class);
		
		List<Predicate> predicateList = new ArrayList<>();
		
		query.multiselect(relatorioconteudoCompartilhado.get("dataHora"), relatorioconteudoCompartilhado.get("nomeUsuario"), relatorioconteudoCompartilhado.get("redeSocial"), relatorioconteudoCompartilhado.get("conteudoCompartilhado"));
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, relatorioconteudoCompartilhado.get("dataHora"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, relatorioconteudoCompartilhado.get("dataHora"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, filtro.getDataFim()));
		}
		
		if(filtro.getNomeUsuario() != null && !filtro.getNomeUsuario().isEmpty()) {
			Path<String> campoNomeUsuario = relatorioconteudoCompartilhado.get("nomeUsuario");
			predicateList.add(cb.equal(campoNomeUsuario, filtro.getNomeUsuario()));
		}
		
		if(filtro.getRedeSocial() != null && !filtro.getRedeSocial().isEmpty()) {
			Path<String> campoNomeRedeSocial = relatorioconteudoCompartilhado.get("redeSocial");
			predicateList.add(cb.like(cb.lower(campoNomeRedeSocial), "%" + filtro.getRedeSocial().toLowerCase() + "%"));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(relatorioconteudoCompartilhado.get("dataHora")));
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<RelatorioConteudoCompartilhadoDTO> relatoriosConteudoComparilhado = em.createQuery(query).getResultList();
		
		return relatoriosConteudoComparilhado;
		
	}
	
	public void insert(RelatorioConteudoCompartilhadoDTO relatorioDTO) {
		RelatorioConteudoCompartilhado relatorio = relatorioDTO.toEntityInsert();
		relatorio = repository.save(relatorio);
		
		if(relatorio != null)		
			this.GravarLog(relatorio.getNomeUsuario(), relatorioDTO.getConteudoCompartilhado());
	}
	
	public void GravarLog(String usuarioLogado, String tipoRelatorio)
	{
		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);
		serviceHistorico.insert(objeto);
	}
}
