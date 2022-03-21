package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.EventosFiltradosDTO;
import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.dto.PrefeituraDTO;
import br.org.cidadessustentaveis.dto.RelatorioPlanoDeMetasDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.sistema.RelatorioPlanoDeMetas;
import br.org.cidadessustentaveis.repository.RelatorioPlanoDeMetasRepository;

@Service
public class RelatorioPlanoDeMetasService {
	
	@Autowired
	private RelatorioPlanoDeMetasRepository repository;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private HistoricoRelatorioGeradoService historicoRelatorioGeradoService;
	
	@Autowired
	private PlanoDeMetasService planoDeMetasService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	EntityManager em;
	
	public List<RelatorioPlanoDeMetasDTO> buscar(RelatorioPlanoDeMetasDTO filtro){
		
		//List<Long> planosDeMetasPreenchidos = planoDeMetasService.buscarIdsPlanoDeMetasPreenchidos();
		 
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioPlanoDeMetasDTO> query = cb.createQuery(RelatorioPlanoDeMetasDTO.class);
		
		Root<RelatorioPlanoDeMetas> relatorioPlanoDeMetas = query.from(RelatorioPlanoDeMetas.class);
		
		Join<RelatorioPlanoDeMetas, Cidade> joinCidade = relatorioPlanoDeMetas.join("idCidade", JoinType.LEFT);
				
		query.multiselect(
				relatorioPlanoDeMetas.get("id"), 
				relatorioPlanoDeMetas.get("nomeUsuario"), 
				relatorioPlanoDeMetas.get("dataHora"), 
				relatorioPlanoDeMetas.get("cidade"),
				relatorioPlanoDeMetas.get("estado"),
				relatorioPlanoDeMetas.get("idPlanoDeMetas"),
				joinCidade.get("id"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		//Path<Long> campoId = relatorioPlanoDeMetas.get("id");
		//predicateList.add(campoId.in(planosDeMetasPreenchidos));
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, relatorioPlanoDeMetas.get("dataHora"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, relatorioPlanoDeMetas.get("dataHora"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, filtro.getDataFim()));
		}
		
		if(filtro.getNomeUsuario() != null && !filtro.getNomeUsuario().isEmpty()) {
			Path<String> campoNomeUsuario = relatorioPlanoDeMetas.get("nomeUsuario");
			predicateList.add(cb.like(cb.lower(campoNomeUsuario), "%" + filtro.getNomeUsuario().toLowerCase() + "%"));
		}
		
		if(filtro.getEstado() != null && !filtro.getEstado().isEmpty()) {
			Path<String> campoNomeEstado = relatorioPlanoDeMetas.get("estado");
			predicateList.add(cb.equal(campoNomeEstado, filtro.getEstado()));
		}
		
		if(filtro.getCidade() != null && !filtro.getCidade().isEmpty()) {
			Path<String> campoNomeCidade = relatorioPlanoDeMetas.get("cidade");
			predicateList.add(cb.equal(campoNomeCidade, filtro.getCidade()));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(relatorioPlanoDeMetas.get("dataHora")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<RelatorioPlanoDeMetasDTO> lista = em.createQuery(query).getResultList();
		
		lista.forEach(relatorio -> {
			if(relatorio.getIdCidade() != null) {
				Cidade cidade = cidadeService.buscarPorId(relatorio.getIdCidade());

				if (cidade != null) {
					relatorio.setCodigoIBGE(cidade.getCodigoIbge());
					relatorio.setEstado(cidade.getProvinciaEstado().getNome());
					relatorio.setEstadoSigla(cidade.getProvinciaEstado().getSigla());
				}
			}
			
		});

		List<PrefeituraDTO> prefeitura = new ArrayList<PrefeituraDTO>();
		for (RelatorioPlanoDeMetasDTO relatorio : lista) {
			prefeitura = prefeituraService.buscarMandatoPorIdCidade(relatorio.getIdCidade());

			if (prefeitura != null) {
				relatorio.setInicioMandato(prefeitura.get(0).getInicioMandato());
				relatorio.setFimMandato(prefeitura.get(0).getFimMandato());
			}
		}
		
		return lista;
	}

	public void gravarLog(String usuarioLogado, String tipoRelatorio){
		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);

		historicoRelatorioGeradoService.insert(objeto);
	}
	
	public void inserirRelatorioPlanoDeMetas(String nomeUsuario, String nomeCidade, String nomeEstado, Long idPlanoDeMetas, Cidade cidadeObj) {
		
		RelatorioPlanoDeMetas newRelatorioPlanoDeMetas = RelatorioPlanoDeMetas.builder()
		.nomeUsuario(nomeUsuario)
		.dataHora(LocalDateTime.now())
		.cidade(nomeCidade)
		.estado(nomeEstado)
		.idPlanoDeMetas(idPlanoDeMetas)
		.idCidade(cidadeObj)
		.build();
		repository.save(newRelatorioPlanoDeMetas);
	}
}
