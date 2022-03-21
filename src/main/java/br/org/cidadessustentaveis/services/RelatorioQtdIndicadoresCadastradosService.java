package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.dto.RelatorioQuantidadeIndicadorCadastradoDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.indicadores.Indicador;

@Service
public class RelatorioQtdIndicadoresCadastradosService {
	
	@Autowired
	private HistoricoRelatorioGeradoService historicoRelatorioGeradoService;
	
	@Autowired
	EntityManager em;
	
	public List<RelatorioQuantidadeIndicadorCadastradoDTO> buscar(RelatorioQuantidadeIndicadorCadastradoDTO filtro){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioQuantidadeIndicadorCadastradoDTO> query = cb.createQuery(RelatorioQuantidadeIndicadorCadastradoDTO.class);
		
		Root<Indicador> indicador = query.from(Indicador.class);
		
		Join<Indicador, Prefeitura> joinPrefeitura = indicador.join("prefeitura");
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");
		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");
		
		query.multiselect(joinCidade.get("nome"), joinCidade.get("codigoIbge"), joinProvinciaEstado.get("nome"), joinProvinciaEstado.get("sigla"), cb.function("year", Integer.class, indicador.get("dataCadastro")), cb.count(indicador.get("prefeitura")));
		
		query.groupBy(joinCidade.get("nome"), joinCidade.get("codigoIbge"), joinProvinciaEstado.get("nome"), joinProvinciaEstado.get("sigla"), cb.function("year", Integer.class, indicador.get("dataCadastro")));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getAnoInicio() != null) {
			predicateList.add(cb.greaterThanOrEqualTo(cb.function("year", Short.class, indicador.get("dataCadastro")), filtro.getAnoInicio()));
		}
		
		if(filtro.getAnoFim() != null) {
			predicateList.add(cb.lessThanOrEqualTo(cb.function("year", Short.class, indicador.get("dataCadastro")), filtro.getAnoFim()));
		}
		
		if(filtro.getPrefeitura()!= null && !filtro.getPrefeitura().isEmpty()) {
			String nomeCidade = filtro.getPrefeitura().substring(0, filtro.getPrefeitura().indexOf(" -"));
			Path<String> campoNomePrefeitura = joinCidade.get("nome");
			predicateList.add(cb.equal(campoNomePrefeitura, nomeCidade));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(cb.function("year", Integer.class, indicador.get("dataCadastro"))));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		
		List<RelatorioQuantidadeIndicadorCadastradoDTO> lista = em.createQuery(query).getResultList();
		return lista;
	}

	public void gravarLog(String usuarioLogado, String tipoRelatorio){

		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);

		historicoRelatorioGeradoService.insert(objeto);
	}

}
