package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

import br.org.cidadessustentaveis.dto.DownloadsExportacoesDTO;
import br.org.cidadessustentaveis.dto.HistoricoRelatorioGeradoDTO;
import br.org.cidadessustentaveis.dto.RelatorioSessaoUsuarioDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.DownloadsExportacoes;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.sistema.HistoricoSessaoUsuario;
import br.org.cidadessustentaveis.repository.HistoricoSessaoUsuarioRepository;

@Service
public class HistoricoSessaoUsuarioService {

	@Autowired
	private HistoricoSessaoUsuarioRepository repository;

	@Autowired
	private HistoricoRelatorioGeradoService historicoRelatorioGeradoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@PersistenceContext
    private EntityManager em;

	
	public void registrarLogin(Usuario usuario) {
		HistoricoSessaoUsuario historicoSessaoUsuario = new HistoricoSessaoUsuario(null, usuario, new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), null);
		repository.saveAndFlush(historicoSessaoUsuario);
	}
	public void registrarLogout(String username) {
		Usuario usuario = usuarioService.buscarPorEmailCredencial(username);
		List<HistoricoSessaoUsuario> historicosUsuario = repository.findByUsuarioOrderByIdDesc(usuario);
		if(!historicosUsuario.isEmpty()) {
			HistoricoSessaoUsuario historicoSessaoUsuario = historicosUsuario.get(0);
			historicoSessaoUsuario.setFimSessao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			repository.save(historicoSessaoUsuario);
		}
	}

	public List<RelatorioSessaoUsuarioDTO> buscar(RelatorioSessaoUsuarioDTO filtro)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<RelatorioSessaoUsuarioDTO> query = cb.createQuery(RelatorioSessaoUsuarioDTO.class);
		
		Root<HistoricoSessaoUsuario> historicoSessaoUsuario = query.from(HistoricoSessaoUsuario.class);
		
		Join<HistoricoSessaoUsuario, Usuario> joinUsuario = historicoSessaoUsuario.join("usuario", JoinType.LEFT);
		Join<Usuario, Prefeitura> joinPrefeitura = joinUsuario.join("prefeitura", JoinType.LEFT);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade", JoinType.LEFT);
		
		query.multiselect(joinUsuario.get("nome"), historicoSessaoUsuario.get("inicioSessao"), historicoSessaoUsuario.get("fimSessao"), joinCidade.get("nome"));
		
		List<Predicate> predicateList = new ArrayList<>();
		
		if(filtro.getDataInicio() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, historicoSessaoUsuario.get("inicioSessao"));
			predicateList.add(cb.greaterThanOrEqualTo(campoDataHora, filtro.getDataInicio()));
		}
		
		if(filtro.getDataFim() != null) {
			Expression<LocalDate> campoDataHora = cb.function("date", LocalDate.class, historicoSessaoUsuario.get("fimSessao"));
			predicateList.add(cb.lessThanOrEqualTo(campoDataHora, filtro.getDataFim()));
		}
		
		if(filtro.getNomeUsuario() != null && !filtro.getNomeUsuario().isEmpty()) {
			Path<String> campoNomeUsuario = joinUsuario.get("nome");
			predicateList.add(cb.equal(campoNomeUsuario, filtro.getNomeUsuario()));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(historicoSessaoUsuario.get("inicioSessao")));
		
		query.orderBy(orderList);
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		
		List<RelatorioSessaoUsuarioDTO> lista = em.createQuery(query).getResultList();
		List<RelatorioSessaoUsuarioDTO> listaFiltrada = new ArrayList<RelatorioSessaoUsuarioDTO>();
		for (RelatorioSessaoUsuarioDTO relatorioSessaoUsuarioDTO : lista) {
			if(relatorioSessaoUsuarioDTO.getFimSessao() != null) {
				listaFiltrada.add(relatorioSessaoUsuarioDTO);
			}
		}	
		return listaFiltrada;
	}

	public void gravarLog(String usuarioLogado, String tipoRelatorio)
	{
		HistoricoRelatorioGeradoDTO objeto = new HistoricoRelatorioGeradoDTO();
		objeto.setNomeUsuario(usuarioLogado);
		objeto.setDataHora(LocalDateTime.now());
		objeto.setNomeRelatorio(tipoRelatorio);

		historicoRelatorioGeradoService.insert(objeto);
	}
	
	
}
