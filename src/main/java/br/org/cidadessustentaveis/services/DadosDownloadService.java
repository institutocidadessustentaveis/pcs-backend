
package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.DadosDownloadDTO;
import br.org.cidadessustentaveis.dto.DadosDownloadPaginacaoDTO;
import br.org.cidadessustentaveis.dto.DownloadsExportacoesDTO;
import br.org.cidadessustentaveis.dto.EmpresaDTO;
import br.org.cidadessustentaveis.dto.FiltroGruposAcademicosDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoCardDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoComboDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoDetalheDTO;
import br.org.cidadessustentaveis.dto.GrupoAcademicoPainelDTO;
import br.org.cidadessustentaveis.dto.ShapeFileDTO;
import br.org.cidadessustentaveis.dto.ShapeFileDetalheDTO;
import br.org.cidadessustentaveis.dto.ShapesPaginacaoDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.DadosDownload;
import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.biblioteca.Biblioteca;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.sistema.HistoricoSessaoUsuario;
import br.org.cidadessustentaveis.repository.DadosDownloadRepository;
import br.org.cidadessustentaveis.repository.GrupoAcademicoRepository;
import br.org.cidadessustentaveis.repository.UsuarioRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class DadosDownloadService {
	
	 @Autowired
	 EntityManager em;

	 @Autowired
	 private DadosDownloadRepository repository;
	 
	 @Autowired
	 private UsuarioService usuarioService;
	 
	 @Autowired
	 private CidadeService cidadeService;
	 
	public DadosDownload inserir(DadosDownloadDTO dadosDownloadDTO) {
		DadosDownload dadosDownload = dadosDownloadDTO.toEntityInsert();
		dadosDownload.setDataDownload(LocalDate.now());
		dadosDownload.setCidade(dadosDownloadDTO.getCidade() != null ? cidadeService.buscarPorId(dadosDownloadDTO.getCidade()) : null);
		dadosDownload.setUsuario(dadosDownloadDTO.getUsuario() != null ? usuarioService.buscarPorId(dadosDownloadDTO.getUsuario()) : null);
		
		repository.save(dadosDownload);
		return dadosDownload;
	}
	
	public List<DadosDownloadDTO> buscarTodosDadosDownload(){
		return repository.buscarTodosDadosDownload();
	}
	
	public List<DadosDownloadDTO> buscarComboBoxAcao() {
		List<DadosDownloadDTO> dados = new ArrayList<>();
		dados = repository.findComboBoxAcao();
		return dados;
	}
	
	public List<DadosDownloadDTO> buscarComboBoxPagina() {
		List<DadosDownloadDTO> dados = new ArrayList<>();
		dados = repository.findComboBoxPagina();
		return dados;
	}
	
	public List<DadosDownloadDTO> buscarComboBoxCidade() {
		List<DadosDownloadDTO> dados = new ArrayList<>();
		dados = repository.findComboBoxCidade();
		return dados;
	}
	
	public List<DadosDownloadDTO> buscarFiltro(DadosDownloadDTO filtro){

		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<DadosDownloadDTO> query = cb.createQuery(DadosDownloadDTO.class);
		
		Root<DadosDownload> dadosDownload = query.from(DadosDownload.class);
		
		Join<DadosDownload, Usuario> joinUsuario = dadosDownload.join("usuario", JoinType.LEFT);
		
		query.multiselect(
				dadosDownload.get("id"),
				dadosDownload.get("email"),
				dadosDownload.get("nome"),
				dadosDownload.get("organizacao"),
				dadosDownload.get("boletim"),
				dadosDownload.get("arquivo"),
				dadosDownload.get("dataDownload"),
				joinUsuario.get("nome"),
				dadosDownload.get("nomeCidade"), 
				dadosDownload.get("acao"),
				dadosDownload.get("pagina"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> campoData = cb.function("date", LocalDate.class, dadosDownload.get("dataDownload"));
			predicateList.add(cb.greaterThanOrEqualTo(campoData, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> campoData = cb.function("date", LocalDate.class, dadosDownload.get("dataDownload"));
			predicateList.add(cb.lessThanOrEqualTo(campoData, filtro.getDataFim()));
		}
		
		if(filtro.getAcao() != null && !filtro.getAcao().isEmpty()) {
			Path<String> campoAcao = dadosDownload.get("acao");
			predicateList.add(cb.equal(campoAcao, filtro.getAcao()));
		}
		
		if(filtro.getPagina() != null && !filtro.getPagina().isEmpty()){
			Path<String> campoPagina = dadosDownload.get("pagina");
			predicateList.add(cb.equal(campoPagina, filtro.getPagina()));
		}
		
		if(filtro.getNomeCidade() != null && !filtro.getNomeCidade().isEmpty()){
			Path<String> campoNomeCidade = dadosDownload.get("nomeCidade");
			predicateList.add(cb.equal(campoNomeCidade, filtro.getNomeCidade()));
		}
			
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(dadosDownload.get("dataDownload")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<DadosDownloadDTO> lista = em.createQuery(query).getResultList();
		return lista;
	}
	
	public DadosDownloadPaginacaoDTO buscarComPaginacao(Integer page, Integer itemsPerPage, String orderBy, String direction) throws Exception {
		DadosDownloadPaginacaoDTO dto = new DadosDownloadPaginacaoDTO();
		
		try {
			Page<DadosDownload> dadosDownload = repository.buscarComPaginacao(PageRequest.of(page, itemsPerPage, Sort.by(orderBy).ascending()));
			List<DadosDownloadDTO> dtos = dadosDownload.stream().map((d) -> new DadosDownloadDTO(d)).collect(Collectors.toList());

			 dto = new DadosDownloadPaginacaoDTO(dtos, dadosDownload.getTotalElements());
		} catch (Exception e) {
			
		}
		return dto;
		
}

	
}