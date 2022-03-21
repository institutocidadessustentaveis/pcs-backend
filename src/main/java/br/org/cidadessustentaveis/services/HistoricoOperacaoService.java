package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.org.cidadessustentaveis.dto.ModuloHistoricoDTO;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.Modulo;
import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;
import br.org.cidadessustentaveis.repository.HistoricoOperacaoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class HistoricoOperacaoService {

	@Autowired
	private HistoricoOperacaoRepository historicoOperacaoRepository;

	@Autowired
	private EntityManager em;

	public List<HistoricoOperacao> listar() {
		return historicoOperacaoRepository.findAll();
	}
	
	public HistoricoOperacao listarById(final Long id) {
	  Optional<HistoricoOperacao> eixo = historicoOperacaoRepository.findById(id);
	  return eixo.orElseThrow(() -> new ObjectNotFoundException("Eixo não encontrado!"));
	}
	
	public HistoricoOperacao inserir(HistoricoOperacao historicoOperacao) {
		historicoOperacao.setIp(getIP());
		return historicoOperacaoRepository.save(historicoOperacao);
	}
	
	public HistoricoOperacao alterar(final Long id, final HistoricoOperacao historicoOperacao) throws Exception {
		if (id != historicoOperacao.getId()) {
			throw new Exception("Campo id divergente.");
		}
		return historicoOperacaoRepository.saveAndFlush(historicoOperacao);
	}
	
	public void deletar(final Long id) {
		listarById(id);
		historicoOperacaoRepository.deleteById(id);
	}

	public List<HistoricoOperacao> carregarHistoricoOperacao(int page, int quantity, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, quantity, Sort.Direction.valueOf(direction), orderBy);
		return historicoOperacaoRepository.carregarHistoricoOperacao(pageRequest);
	}

	public List<HistoricoOperacao> filtrarHistoricoOperacao(String usuario, Modulo modulo,
															Date dataInicio, Date dataFinal,
															int page, int quantity) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HistoricoOperacao> query = cb.createQuery(HistoricoOperacao.class);

		Root<HistoricoOperacao> root = query.from(HistoricoOperacao.class);
		List<Predicate> predicateList = new ArrayList<>();

		if(usuario != null && !usuario.isEmpty()) {
			Join<HistoricoOperacao, Usuario> joinUsuario = root.join("usuario");
			Path<String> campoNomeUsuario = joinUsuario.get("nome");

			predicateList.add(cb.like(cb.lower(campoNomeUsuario), "%" + usuario.toLowerCase() + "%"));
		}

		if(modulo != null) {
			Path<Modulo> campoModuloHistorico = root.get("modulo");

			predicateList.add(cb.equal(campoModuloHistorico, modulo));
		}

		Path<LocalDateTime> campoDataHistorico = root.get("data");

		if(dataInicio != null) {
			LocalDateTime dataInicial = dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			predicateList.add(cb.greaterThanOrEqualTo(campoDataHistorico, dataInicial));
		}

		if(dataFinal != null) {
			LocalDateTime dataFim = dataFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			predicateList.add(cb.lessThanOrEqualTo(campoDataHistorico, dataFim));
		}

		List<Order> orderList = new ArrayList();
		orderList.add(cb.desc(campoDataHistorico));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		return em.createQuery(query).setFirstResult(page).setMaxResults(quantity).getResultList();
	}
	

	public List<HistoricoOperacao> filtrarHistoricoOperacaoSemPaginacao(String usuario, Modulo modulo,
			Date dataInicio, Date dataFinal,
			int page, int quantity) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HistoricoOperacao> query = cb.createQuery(HistoricoOperacao.class);

		Root<HistoricoOperacao> root = query.from(HistoricoOperacao.class);
		List<Predicate> predicateList = new ArrayList<>();

		if(usuario != null && !usuario.isEmpty()) {
			Join<HistoricoOperacao, Usuario> joinUsuario = root.join("usuario");
			Path<String> campoNomeUsuario = joinUsuario.get("nome");

			predicateList.add(cb.like(cb.lower(campoNomeUsuario), "%" + usuario.toLowerCase() + "%"));
		}

		if(modulo != null) {
			Path<Modulo> campoModuloHistorico = root.get("modulo");

			predicateList.add(cb.equal(campoModuloHistorico, modulo));
		}

		Path<LocalDateTime> campoDataHistorico = root.get("data");

		if(dataInicio != null) {
			LocalDateTime dataInicial = dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			predicateList.add(cb.greaterThanOrEqualTo(campoDataHistorico, dataInicial));
		}

		if(dataFinal != null) {
			LocalDateTime dataFim = dataFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			predicateList.add(cb.lessThanOrEqualTo(campoDataHistorico, dataFim));
		}

		List<Order> orderList = new ArrayList();
		orderList.add(cb.desc(campoDataHistorico));

		query.orderBy(orderList);

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		return em.createQuery(query).getResultList();
	}

	public Long countFiltrarHistoricoOperacao(String usuario, Modulo modulo, Date dataInicio, Date dataFinal) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);

		Root<HistoricoOperacao> root = query.from(HistoricoOperacao.class);
		List<Predicate> predicateList = new ArrayList<>();

		if(usuario != null && !usuario.isEmpty()) {
			Join<HistoricoOperacao, Usuario> joinUsuario = root.join("usuario");
			Path<String> campoNomeUsuario = joinUsuario.get("nome");

			predicateList.add(cb.like(cb.lower(campoNomeUsuario), "%" + usuario.toLowerCase() + "%"));
		}

		if(modulo != null) {
			Path<Modulo> campoModuloHistorico = root.get("modulo");

			predicateList.add(cb.equal(campoModuloHistorico, modulo));
		}

		Path<LocalDateTime> campoDataHistorico = root.get("data");

		if(dataInicio != null) {
			LocalDateTime dataInicial = dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			predicateList.add(cb.greaterThanOrEqualTo(campoDataHistorico, dataInicial));
		}

		if(dataFinal != null) {
			LocalDateTime dataFim = dataFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			predicateList.add(cb.lessThanOrEqualTo(campoDataHistorico, dataFim));
		}

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);
		query.select(cb.count(root.get("id")).alias("count"));

		return em.createQuery(query).getSingleResult();
	}

	public Long countHistoricoOperacao() {
		return historicoOperacaoRepository.count();
	}

	public List<ModuloHistoricoDTO> listarModulosHistoricoOperacao() {
		List<Modulo> modulos = Arrays.asList(Modulo.values());
		return modulos.stream().map(m -> new ModuloHistoricoDTO(m)).collect(Collectors.toList());
	}

	public static HttpServletRequest getCurrentRequest() {
	     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	     Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
	     Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
	     HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
	     Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
	     return servletRequest;
	 }
	
	private String getIP() {
		try {
			String userIpAddress = getCurrentRequest().getHeader("X-Forwarded-For");
			if(userIpAddress == null) {
				userIpAddress = getCurrentRequest().getRemoteAddr();
			}
			return userIpAddress;	
		} catch (Exception e) {
			return "0:0:0:0:0:0:0:1";
		}
	}
}
