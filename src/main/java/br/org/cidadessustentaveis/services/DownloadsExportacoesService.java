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

import br.org.cidadessustentaveis.dto.DownloadsExportacoesDTO;
import br.org.cidadessustentaveis.dto.ItemComboDTO;
import br.org.cidadessustentaveis.dto.RelatorioQuantidadeIndicadorPreenchidoDTO;
import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.repository.DownloadsExportacoesRepository;

@Service
public class DownloadsExportacoesService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	private DownloadsExportacoesRepository repository;
	
	public List<DownloadsExportacoesDTO> buscar(DownloadsExportacoesDTO filtro){

		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<DownloadsExportacoesDTO> query = cb.createQuery(DownloadsExportacoesDTO.class);
		
		Root<DownloadsExportacoes> downloadExportacoes = query.from(DownloadsExportacoes.class);
		
		query.multiselect(downloadExportacoes.get("dataHora"), downloadExportacoes.get("nomeUsuario"), downloadExportacoes.get("nomeArquivo"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, downloadExportacoes.get("dataHora"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, downloadExportacoes.get("dataHora"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, filtro.getDataFim()));
		}
		
		if(filtro.getNomeUsuario() != null && !filtro.getNomeUsuario().isEmpty()) {
			Path<String> campoNomeUsuario = downloadExportacoes.get("nomeUsuario");
			predicateList.add(cb.equal(campoNomeUsuario, filtro.getNomeUsuario()));
		}
		
		if(filtro.getNomeArquivo() != null && !filtro.getNomeArquivo().isEmpty()){
			Path<String> campoNomeArquivo = downloadExportacoes.get("nomeArquivo");
			predicateList.add(cb.equal(campoNomeArquivo, filtro.getNomeArquivo()));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(downloadExportacoes.get("dataHora")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<DownloadsExportacoesDTO> lista = em.createQuery(query).getResultList();
		return lista;
	}
	
	public void insert(DownloadsExportacoesDTO downloadsExportacoesDTO) {
		DownloadsExportacoes downloadsExportacoes = downloadsExportacoesDTO.toEntityInsert();
		downloadsExportacoes = repository.save(downloadsExportacoes);
	}
	
	public void gravarLog(String usuarioLogado, String tipoRelatorio)
	{
		DownloadsExportacoesDTO objeto = new DownloadsExportacoesDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeArquivo(tipoRelatorio);
		this.insert(objeto);
	}
	
	public List<DownloadsExportacoes> buscarComboBoxArquivo() {
		List<DownloadsExportacoes> downloadsExportacoes = new ArrayList<>();
		downloadsExportacoes = repository.findComboBoxArquivo();
		return downloadsExportacoes;
	}
}
