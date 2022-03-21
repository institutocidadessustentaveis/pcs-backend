package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.FiltroHistoricoUsoShapeQueryDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.HistoricoPlanoMetasPrestacaoContas;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.SimNao;
import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoUsoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TipoUsoShape;
import br.org.cidadessustentaveis.repository.HistoricoPlanoMetasPrestacaoContasRepository;

@Service
public class HistoricoPlanoMetasPrestacaoContasService {

    @Autowired
    private HistoricoPlanoMetasPrestacaoContasRepository repository;

	@Autowired
	private PrefeituraService prefeituraService;

	@Autowired
	private EntityManager em;

	public void gerarHistoricoPlanoMetasPrestacaoContas(Cidade cidade, Usuario usuario) {

		Prefeitura prefeitura = prefeituraService.buscarPrefeituraPorIdUsuario(usuario.getId());
		
		 HistoricoPlanoMetasPrestacaoContas historicoPlanoMetasPrestacaoContas = repository
				.buscaHistoricoPlanoMetasPrestacaoContasPorIdPrefeitura(prefeitura.getId());

		if (historicoPlanoMetasPrestacaoContas == null || historicoPlanoMetasPrestacaoContas.getId() == null) {
			HistoricoPlanoMetasPrestacaoContas historico = new HistoricoPlanoMetasPrestacaoContas();
			if (cidade.getPlanoMetas() != null) {
				historico.setPlanoMetas(SimNao.SIM.getDescricao());
				historico.setDataHoraPlanoMetas(LocalDateTime.now());
			}
			if (cidade.getRelatorioContas() != null) {
				historico.setPrestacaoContas(SimNao.SIM.getDescricao());
				historico.setDataHoraPrestacaoContas(LocalDateTime.now());
			}
			historico.setMandato(prefeitura.getInicioMandato().getYear() + "-" + prefeitura.getFimMandato().getYear());
			historico.setPrefeitura(prefeitura);
			historico.setUsuario(usuario);
			try {
				repository.save(historico);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (cidade.getPlanoMetas() != null) {
				historicoPlanoMetasPrestacaoContas.setPlanoMetas(SimNao.SIM.getDescricao());
				if(historicoPlanoMetasPrestacaoContas.getDataHoraPlanoMetas() == null) {
					historicoPlanoMetasPrestacaoContas.setDataHoraPlanoMetas(LocalDateTime.now());
				}
			}
			else {
				historicoPlanoMetasPrestacaoContas.setPlanoMetas(SimNao.NAO.getDescricao());
				historicoPlanoMetasPrestacaoContas.setDataHoraPlanoMetas(null);
			}
			if (cidade.getRelatorioContas() != null) {
				historicoPlanoMetasPrestacaoContas.setPrestacaoContas(SimNao.SIM.getDescricao());
				if(historicoPlanoMetasPrestacaoContas.getDataHoraPrestacaoContas() == null) {
					historicoPlanoMetasPrestacaoContas.setDataHoraPrestacaoContas(LocalDateTime.now());
				}
			}
			else {
				historicoPlanoMetasPrestacaoContas.setPrestacaoContas(SimNao.NAO.getDescricao());
				historicoPlanoMetasPrestacaoContas.setDataHoraPrestacaoContas(null);
			}
			historicoPlanoMetasPrestacaoContas.setUsuario(usuario);
			try {
				repository.save(historicoPlanoMetasPrestacaoContas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public FiltroHistoricoUsoShapeQueryDTO filtrarHistorico(Date dataHoraAcesso, String cidade, String usuario,
			String titulo, TipoUsoShape tipo, Integer page, Integer itemsPerPage, String orderBy, String direction) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<HistoricoUsoShape> query = cb.createQuery(HistoricoUsoShape.class);

		Root<HistoricoUsoShape> root = query.from(HistoricoUsoShape.class);
		Join<HistoricoUsoShape, Cidade> joinCidade = root.join("cidade", JoinType.LEFT);
		Join<HistoricoUsoShape, Usuario> joinUsuario = root.join("usuario", JoinType.LEFT);
		Join<HistoricoUsoShape, ShapeFile> joinShape = root.join("shape", JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<>();

		if (dataHoraAcesso != null) {
			Path<LocalDateTime> campoDataHoraAcesso = root.get("dataHoraAcesso");

			Calendar c = new GregorianCalendar();
			c.setTime(dataHoraAcesso);

			Predicate year = cb.equal(cb.function("year", Integer.class, campoDataHoraAcesso), c.get(Calendar.YEAR));
			Predicate month = cb.equal(cb.function("month", Integer.class, campoDataHoraAcesso),
					c.get(Calendar.MONTH) + 1);
			Predicate day = cb.equal(cb.function("day", Integer.class, campoDataHoraAcesso),
					c.get(Calendar.DAY_OF_MONTH));

			predicateList.add(cb.and(year, month, day));
		}

		if (cidade != null && !cidade.isEmpty()) {
			Path<String> campoNomeCidade = joinCidade.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeCidade), new String("%" + cidade + "%").toLowerCase()));
		}

		if (usuario != null && !usuario.isEmpty()) {
			Path<String> campoNomeUsuario = joinUsuario.get("nome");
			predicateList.add(cb.like(cb.lower(campoNomeUsuario), new String("%" + usuario + "%").toLowerCase()));
		}

		if (titulo != null && !titulo.isEmpty()) {
			Path<String> campoTituloShape = joinShape.get("titulo");
			predicateList.add(cb.like(cb.lower(campoTituloShape), new String("%" + titulo + "%").toLowerCase()));
		}

		if (tipo != null) {
			Path<String> campoTipo = root.get("tipo");
			predicateList.add(cb.equal(campoTipo, tipo));
		}

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		List<Order> orderList = new ArrayList();

		if (orderBy != null && !orderBy.isEmpty()) {
			Path campoOrder = null;

			if (orderBy.equalsIgnoreCase("dataHoraAcesso")) {
				campoOrder = root.get("dataHoraAcesso");
			}

			if (orderBy.equalsIgnoreCase("cidade")) {
				campoOrder = joinCidade.get("nome");
			}

			if (orderBy.equalsIgnoreCase("usuario")) {
				campoOrder = joinUsuario.get("nome");
			}

			if (orderBy.equalsIgnoreCase("titulo")) {
				campoOrder = joinShape.get("titulo");
			}

			if (direction != null && !direction.isEmpty() && campoOrder != null) {
				if (direction.equalsIgnoreCase("DESC")) {
					orderList.add(cb.desc(campoOrder));
				} else {
					orderList.add(cb.asc(campoOrder));
				}
			}
		}

		query.orderBy(orderList);

		TypedQuery<HistoricoUsoShape> typedQuery = em.createQuery(query);

		if (page != null && itemsPerPage != null) {
			typedQuery = typedQuery.setFirstResult(page * itemsPerPage).setMaxResults(itemsPerPage);
		}

		List<HistoricoUsoShape> result = typedQuery.getResultList();

		return new FiltroHistoricoUsoShapeQueryDTO(result, Long.valueOf(result.size()));
	}

//    public Page<HistoricoUsoShape> buscarHistorico(Integer page, Integer linesPerPage,
//                                                   String orderBy, String direction) {
//        if(orderBy != null) {
//            if(orderBy.equalsIgnoreCase("titulo")) {
//                orderBy = "shape.titulo";
//            }
//        }
//
//        return repository.findAll(PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
//    }
//
//    public Long count() {
//        return repository.count();
//    }

}
