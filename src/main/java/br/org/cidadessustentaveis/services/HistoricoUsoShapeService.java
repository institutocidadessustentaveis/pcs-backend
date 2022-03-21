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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.FiltroHistoricoUsoShapeQueryDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoExportacaoHitoricoExportacaoCatalogoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoUsoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TipoUsoShape;
import br.org.cidadessustentaveis.repository.HistoricoUsoShapeRepository;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class HistoricoUsoShapeService {

    @Autowired
    private HistoricoUsoShapeRepository repository;

    @Autowired
    private ShapeFileService shapeFileService;

    @Autowired
    private EntityManager em;
    
    @Autowired
    private UsuarioContextUtil usuarioContextUtil;

    public HistoricoUsoShape gerarHistoricoUsoShape(ShapeFile shape, TipoUsoShape tipo, TipoExportacaoHitoricoExportacaoCatalogoShape tipoArquivo) {
        if(shape == null) throw new IllegalArgumentException("Shape não pode ser nulo");
        if(tipo == null) throw new IllegalArgumentException("Tipo de uso do shape não pode ser nulo");
        if(shape.getId() == null || !shapeFileService.exists(shape.getId()))
            throw new IllegalStateException("Não é possível gerar um histórico de uso do shape em um shape não salvo");

        HistoricoUsoShape historico = new HistoricoUsoShape();
        
        try {
			if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null) {
				Usuario usuario = usuarioContextUtil.getUsuario();
				if(usuario != null) {
					historico.setUsuario(usuario);
				}
				if (usuario != null && usuario.getPrefeitura() != null && usuario.getPrefeitura().getCidade() != null) {
					historico.setCidade(usuario.getPrefeitura().getCidade());
				}
				historico.setDataHoraAcesso(LocalDateTime.now());
				historico.setShape(shape);
		        historico.setTipo(tipo);
		        historico.setTipoArquivo(tipoArquivo);
			} else {
                historico.setDataHoraAcesso(LocalDateTime.now());
				historico.setShape(shape);
		        historico.setTipo(tipo);
		        historico.setTipoArquivo(tipoArquivo);
            }
		} catch (Exception e) {
            System.out.println(e.getMessage());
            historico.setDataHoraAcesso(LocalDateTime.now());
            historico.setShape(shape);
            historico.setTipo(tipo);
            historico.setTipoArquivo(tipoArquivo);
		}
        return repository.save(historico);
    }

    public FiltroHistoricoUsoShapeQueryDTO filtrarHistorico(Date dataHoraAcesso, String cidade, String usuario,
                                                            String titulo, TipoUsoShape tipo, Integer page,
                                                            Integer itemsPerPage, String orderBy, String direction) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<HistoricoUsoShape> query = cb.createQuery(HistoricoUsoShape.class);

        Root<HistoricoUsoShape> root = query.from(HistoricoUsoShape.class);
        Join<HistoricoUsoShape, Cidade> joinCidade = root.join("cidade", JoinType.LEFT);
        Join<HistoricoUsoShape, Usuario> joinUsuario = root.join("usuario", JoinType.LEFT);
        Join<HistoricoUsoShape, ShapeFile> joinShape = root.join("shape", JoinType.LEFT);

        List<Predicate> predicateList = new ArrayList<>();

        if(dataHoraAcesso != null) {
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

        if(cidade != null && !cidade.isEmpty()) {
            Path<String> campoNomeCidade = joinCidade.get("nome");
            predicateList.add(cb.like(cb.lower(campoNomeCidade),
                                        new String("%" + cidade + "%").toLowerCase()));
        }

        if(usuario != null && !usuario.isEmpty()) {
            Path<String> campoNomeUsuario = joinUsuario.get("nome");
            predicateList.add(cb.like(cb.lower(campoNomeUsuario),
                                            new String("%" + usuario + "%").toLowerCase()));
        }

        if(titulo != null && !titulo.isEmpty()) {
            Path<String> campoTituloShape = joinShape.get("titulo");
            predicateList.add(cb.like(cb.lower(campoTituloShape),
                                            new String("%" + titulo + "%").toLowerCase()));
        }

        if(tipo != null) {
            Path<String> campoTipo = root.get("tipo");
            predicateList.add(cb.equal(campoTipo, tipo));
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.where(predicates);

        List<Order> orderList = new ArrayList();

        if(orderBy != null && !orderBy.isEmpty()) {
            Path campoOrder = null;

            if(orderBy.equalsIgnoreCase("dataHoraAcesso")) {
                campoOrder = root.get("dataHoraAcesso");
            }

            if(orderBy.equalsIgnoreCase("cidade")) {
                campoOrder = joinCidade.get("nome");
            }

            if(orderBy.equalsIgnoreCase("usuario")) {
                campoOrder = joinUsuario.get("nome");
            }

            if(orderBy.equalsIgnoreCase("titulo")) {
                campoOrder = joinShape.get("titulo");
            }

            if(direction != null && !direction.isEmpty() && campoOrder != null) {
                if(direction.equalsIgnoreCase("DESC")) {
                    orderList.add(cb.desc(campoOrder));
                } else {
                    orderList.add(cb.asc(campoOrder));
                }
            }
        }

        query.orderBy(orderList);

        TypedQuery<HistoricoUsoShape> typedQuery = em.createQuery(query);

        if(page != null && itemsPerPage != null) {
            typedQuery = typedQuery.setFirstResult(page * itemsPerPage).setMaxResults(itemsPerPage);
        }

        List<HistoricoUsoShape> result = typedQuery.getResultList();

        return new FiltroHistoricoUsoShapeQueryDTO(result, Long.valueOf(result.size()));
    }

    public Page<HistoricoUsoShape> buscarHistorico(Integer page, Integer linesPerPage,
                                                   String orderBy, String direction) {
        if(orderBy != null) {
            if(orderBy.equalsIgnoreCase("titulo")) {
                orderBy = "shape.titulo";
            }
        }

        return repository.findAll(PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
    }

    public Long count() {
        return repository.count();
    }

}
