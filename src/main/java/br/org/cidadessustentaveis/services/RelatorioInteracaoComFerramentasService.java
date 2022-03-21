package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.dto.RelatorioIndicadoresPreenchidosDTO;
import br.org.cidadessustentaveis.dto.RelatorioInteracaoComFerramentasDTO;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.sistema.RelatorioInteracaoFerramentas;

@Service
public class RelatorioInteracaoComFerramentasService {
	
	@Autowired
	private HistoricoRelatorioGeradoService historicoRelatorioGeradoService;
	
	@Autowired
	private EntityManager em;

	public List<RelatorioInteracaoComFerramentasDTO> buscar(RelatorioInteracaoComFerramentasDTO filtro){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioInteracaoComFerramentasDTO> query = cb.createQuery(RelatorioInteracaoComFerramentasDTO.class);
		
		Root<RelatorioInteracaoFerramentas> interecaoFerramentas = query.from(RelatorioInteracaoFerramentas.class);
		
		query.multiselect(interecaoFerramentas.get("usuario"), interecaoFerramentas.get("dataHora"), interecaoFerramentas.get("ferramenta"), interecaoFerramentas.get("tipoInteracao"));
	
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> dataHora = cb.function("date", LocalDate.class, interecaoFerramentas.get("dataHora"));
			predicateList.add(cb.greaterThanOrEqualTo(dataHora, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> dataHora = cb.function("date", LocalDate.class, interecaoFerramentas.get("dataHora"));
			predicateList.add(cb.lessThanOrEqualTo(dataHora, filtro.getDataFim()));
		}
		
		if(filtro.getNomeUsuario() != null && !filtro.getNomeUsuario().isEmpty()) {
			Path<String> campoNomeUsuario = interecaoFerramentas.get("usuario");
			predicateList.add(cb.like(cb.lower(campoNomeUsuario), "%" + filtro.getNomeUsuario().toLowerCase() + "%"));
		}
		
		if(filtro.getFerramenta() != null && !filtro.getFerramenta().isEmpty()) {
			Path<String> campoNomeFerramenta = interecaoFerramentas.get("ferramenta");
			predicateList.add(cb.like(cb.lower(campoNomeFerramenta), "%" + filtro.getFerramenta().toLowerCase() + "%"));
		}
		
		if(filtro.getTipoInteracao() != null && !filtro.getTipoInteracao().isEmpty()) {
			Path<String> campoTipoInteracao =  interecaoFerramentas.get("tipoInteracao");
			predicateList.add(cb.like(cb.lower(campoTipoInteracao), "%" + filtro.getTipoInteracao().toLowerCase() + "%"));
		}	
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(interecaoFerramentas.get("dataHora")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		
		List<RelatorioInteracaoComFerramentasDTO> relatorioInteracaoFerramentas = em.createQuery(query).getResultList();
		return relatorioInteracaoFerramentas;
	}

	public void gravarLog(String usuarioLogado, String tipoRelatorio){
		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);

		historicoRelatorioGeradoService.insert(objeto);
	}

}
