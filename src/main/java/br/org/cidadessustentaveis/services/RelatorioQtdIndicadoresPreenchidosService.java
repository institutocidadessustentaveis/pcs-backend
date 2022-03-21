package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import br.org.cidadessustentaveis.dto.CidadeComPrefeituraDTO;
import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.dto.RelatorioQuantidadeIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;

@Service
public class RelatorioQtdIndicadoresPreenchidosService {
	
	@Autowired
	private HistoricoRelatorioGeradoService historicoRelatorioGeradoService;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	public List<RelatorioQuantidadeIndicadorPreenchidoDTO> buscar(RelatorioQuantidadeIndicadorPreenchidoDTO filtro) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioQuantidadeIndicadorPreenchidoDTO> query = cb.createQuery(RelatorioQuantidadeIndicadorPreenchidoDTO.class);
		
		Root<IndicadorPreenchido> indicadorPreenchido = query.from(IndicadorPreenchido.class);
		
		Join<IndicadorPreenchido, Prefeitura> joinPrefeitura = indicadorPreenchido.join("prefeitura");
		
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade");
		
		Join<Cidade, ProvinciaEstado> joinProvinciaEstado = joinCidade.join("provinciaEstado");
		
		query.multiselect(joinCidade.get("nome"), joinCidade.get("codigoIbge"), joinProvinciaEstado.get("nome"), joinProvinciaEstado.get("sigla"), indicadorPreenchido.get("ano"), cb.count(indicadorPreenchido.get("indicador")), joinCidade.get("populacao"));
		
		query.groupBy(joinCidade.get("nome"), joinCidade.get("codigoIbge"), joinProvinciaEstado.get("nome"), joinProvinciaEstado.get("sigla"), indicadorPreenchido.get("ano"), joinCidade.get("populacao"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		String nomeCidadeFiltro = null;
		
		if(filtro.getAnoInicio() != null) {
			predicateList.add(cb.greaterThanOrEqualTo(indicadorPreenchido.get("ano"), filtro.getAnoInicio()));
		}
		
		if(filtro.getAnoFim() != null) {
			predicateList.add(cb.lessThanOrEqualTo(indicadorPreenchido.get("ano"), filtro.getAnoFim()));
		}
		
		if(filtro.getPrefeitura()!= null && !filtro.getPrefeitura().isEmpty()) {
			String nomeCidade = filtro.getPrefeitura().substring(0, filtro.getPrefeitura().indexOf(" -"));
			nomeCidadeFiltro = nomeCidade;
			Path<String> campoNomePrefeitura = joinCidade.get("nome");
			predicateList.add(cb.equal(campoNomePrefeitura, nomeCidade));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(indicadorPreenchido.get("ano")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		
		List<RelatorioQuantidadeIndicadorPreenchidoDTO> relatorioQtdIndicadores = em.createQuery(query).getResultList();		
			
		return definirCidadesSemIndicadoresPreenchidos(relatorioQtdIndicadores, filtro, nomeCidadeFiltro);
	}

	public void gravarLog(String usuarioLogado, String tipoRelatorio){
		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);

		historicoRelatorioGeradoService.insert(objeto);
	}
	
	public List<RelatorioQuantidadeIndicadorPreenchidoDTO> definirCidadesSemIndicadoresPreenchidos(List<RelatorioQuantidadeIndicadorPreenchidoDTO> relatorioQtdIndicadores, RelatorioQuantidadeIndicadorPreenchidoDTO filtro, String nomeCidadeFiltro) {
		List<RelatorioQuantidadeIndicadorPreenchidoDTO> relatorioQtdIndicadoresCompleto = new ArrayList<RelatorioQuantidadeIndicadorPreenchidoDTO>(relatorioQtdIndicadores);
			
		List<CidadeComPrefeituraDTO> ListaCidadesComPrefeitura = new ArrayList<CidadeComPrefeituraDTO>();
		ListaCidadesComPrefeitura = prefeituraService.buscarCidadesComPrefeitura();
		
		Short anoInicial = 2000;
		Short anoFinal = 2019;
		
		if(filtro.getAnoInicio() != null && filtro.getAnoInicio() > 1999) {
			anoInicial = filtro.getAnoInicio();
		}
		
		if(filtro.getAnoFim() != null && filtro.getAnoFim() < 2020) {
			anoFinal = filtro.getAnoFim();
		}
		
		for (CidadeComPrefeituraDTO cidadeComPrefeituraDTO : ListaCidadesComPrefeitura) {
			List<RelatorioQuantidadeIndicadorPreenchidoDTO> relatorioQtdIndicadoresCidades = new ArrayList<RelatorioQuantidadeIndicadorPreenchidoDTO>();
			relatorioQtdIndicadoresCidades = relatorioQtdIndicadores.stream().filter(relatorio -> relatorio.getPrefeitura().equals(cidadeComPrefeituraDTO.getNomeCidade())).collect(Collectors.toList());
			
			List<Short> anosComIndicadoresPreenchidos = new ArrayList<Short>();
			
			for (RelatorioQuantidadeIndicadorPreenchidoDTO relatorioQtdIndicadoresCidade : relatorioQtdIndicadoresCidades) {
				anosComIndicadoresPreenchidos.add(relatorioQtdIndicadoresCidade.getAno());
			}
			
			
			if(nomeCidadeFiltro == null || nomeCidadeFiltro.equals(cidadeComPrefeituraDTO.getNomeCidade())) {
				for (short i = anoInicial; i <= anoFinal; i++) {
					if(!anosComIndicadoresPreenchidos.contains(i)) {
						
						RelatorioQuantidadeIndicadorPreenchidoDTO novoItem = new RelatorioQuantidadeIndicadorPreenchidoDTO();
						novoItem.setPrefeitura(cidadeComPrefeituraDTO.getNomeCidade());
						novoItem.setCodigoIBGE(cidadeComPrefeituraDTO.getCodigoIbge());
						novoItem.setEstado(cidadeComPrefeituraDTO.getNomeProvinciaEstado());
						novoItem.setEstadoSigla(cidadeComPrefeituraDTO.getEstadoSigla());
						novoItem.setQuantidade((long) 0);
						novoItem.setAno(i);
						novoItem.setPopulacao(cidadeComPrefeituraDTO.getPopulacao());
						relatorioQtdIndicadoresCompleto.add(novoItem);
					}
				}
			}
			
			
		}	
		Collections.sort(relatorioQtdIndicadoresCompleto);
		return relatorioQtdIndicadoresCompleto;
	}

}
