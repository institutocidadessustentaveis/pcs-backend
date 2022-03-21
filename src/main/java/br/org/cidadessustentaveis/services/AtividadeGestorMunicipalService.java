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

import br.org.cidadessustentaveis.dto.AtividadeGestorMunicipalDTO;
import br.org.cidadessustentaveis.model.sistema.AtividadeGestorMunicipal;
import br.org.cidadessustentaveis.repository.AtividadeGestorMunicipalRepository;

@Service
public class AtividadeGestorMunicipalService {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private AtividadeGestorMunicipalRepository repository;

	public List<AtividadeGestorMunicipalDTO> buscar(AtividadeGestorMunicipalDTO filtro) {
		{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			
			CriteriaQuery<AtividadeGestorMunicipalDTO> query = cb.createQuery(AtividadeGestorMunicipalDTO.class);
			
			Root<AtividadeGestorMunicipal> atividadeGestorMunicipal = query.from(AtividadeGestorMunicipal.class);
			
			query.multiselect(atividadeGestorMunicipal.get("dataHora"), atividadeGestorMunicipal.get("nomeUsuario"), atividadeGestorMunicipal.get("cidade"), atividadeGestorMunicipal.get("acao"));
			
			List<Predicate> predicateList = new ArrayList<>();
			
			if(filtro.getDataInicio() != null) {
				Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, atividadeGestorMunicipal.get("dataHora"));
				predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, filtro.getDataInicio()));
			}
			
			if(filtro.getDataFim() != null) {
				Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, atividadeGestorMunicipal.get("dataHora"));
				predicateList.add(cb.lessThanOrEqualTo(campoDataHora, filtro.getDataFim()));
			}
			
			if(filtro.getNomeUsuario() != null && !filtro.getNomeUsuario().isEmpty()) {
				Path<String> campoNomeGestor = atividadeGestorMunicipal.get("nomeUsuario");
				predicateList.add(cb.equal(campoNomeGestor, filtro.getNomeUsuario()));
			}
			
			if(filtro.getEstado() != null && !filtro.getEstado().isEmpty()) {
				Path<String> campoNomeEstado = atividadeGestorMunicipal.get("estado");
				predicateList.add(cb.equal(campoNomeEstado, filtro.getEstado()));
			}
			
			if(filtro.getCidade() != null && !filtro.getCidade().isEmpty()) {
				Path<String> campoNomeCidade = atividadeGestorMunicipal.get("cidade");
				predicateList.add(cb.equal(campoNomeCidade, filtro.getCidade()));
			}
			
			if(filtro.getAcao() != null && !filtro.getAcao().isEmpty()) {
				Path<String> campoNomeAcao = atividadeGestorMunicipal.get("acao");
				predicateList.add(cb.equal(campoNomeAcao,filtro.getAcao()));
			}
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(atividadeGestorMunicipal.get("dataHora")));
			query.orderBy(orderList);

			
			Predicate[] predicates = new Predicate[predicateList.size()];
			predicateList.toArray(predicates);
			query.where(predicates);

			List<AtividadeGestorMunicipalDTO> atividadesGestorMunicipal = em.createQuery(query).getResultList();
			
			return atividadesGestorMunicipal;

		}
	}
	
	public List<AtividadeGestorMunicipal> buscarComboBoxAcao() {
		return repository.findComboBoxAcao();
	}
	
	public void salvarAtividadeGestorMunicipal(AtividadeGestorMunicipalDTO atividadeGestorMunicipalDTO) {
		AtividadeGestorMunicipal atividade = atividadeGestorMunicipalDTO.toEntityInsert();
		repository.save(atividade);
	}
	
}
