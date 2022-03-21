package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.dto.FiltroHistoricoShapeQueryDTO;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;
import br.org.cidadessustentaveis.model.sistema.HistoricoOperacao;
import br.org.cidadessustentaveis.repository.HistoricoShapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class HistoricoShapeService {

    @Autowired
    private HistoricoShapeRepository dao;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PrefeituraService prefeituraService;

    @Autowired
    private EntityManager em;

    public HistoricoShape save(ShapeFile shape) {
        if(shape == null) throw new IllegalStateException("Shape não pode ser nulo");

        HistoricoShape historicoShape = new HistoricoShape();

        historicoShape.setDataCriacao(shape.getDataHoraCadastro());
        historicoShape.setDataEdicao(shape.getDataHoraAlteracao());
        historicoShape.setNomeArquivo(shape.getTitulo());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioService.buscarPorEmail(username);
        
        Prefeitura prefeitura = prefeituraService.buscarPrefeituraPorIdUsuario(usuario.getId());
        
        historicoShape.setUsuario(usuario);
        
        if(prefeitura != null && prefeitura.getId() != null) {
        	historicoShape.setPrefeitura(prefeitura);
        }

        historicoShape.setShape(shape);

        return dao.save(historicoShape);
    }

    public List<HistoricoShape> buscarHistorico(Integer page, Integer linesPerPage, String orderBy, String direction) {
        return dao.buscarHistorico(PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
    }

    public FiltroHistoricoShapeQueryDTO filtrarHistorico(Date dataCriacao, Date dataEdicao, String usuario,
                                                         String nomeArquivo, Long tema, Integer page,
                                                         Integer itemsPerPage) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<HistoricoShape> query = cb.createQuery(HistoricoShape.class);

        Root<HistoricoShape> root = query.from(HistoricoShape.class);
        Join<HistoricoShape, Usuario> joinUsuario = root.join("usuario", JoinType.LEFT);
        Join<HistoricoShape, TemaGeoespacial> joinTema = root.join("tema", JoinType.LEFT);

        List<Predicate> predicateList = new ArrayList<>();

        if(dataCriacao != null) {
            Path<LocalDateTime> campoDataCriacao = root.get("dataCriacao");

            Calendar c = new GregorianCalendar();
            c.setTime(dataCriacao);

            Predicate year = cb.equal(cb.function("year", Integer.class, campoDataCriacao), c.get(Calendar.YEAR));
            Predicate month = cb.equal(cb.function("month", Integer.class, campoDataCriacao),
                                                                                        c.get(Calendar.MONTH) + 1);
            Predicate day = cb.equal(cb.function("day", Integer.class, campoDataCriacao),
                                                                                        c.get(Calendar.DAY_OF_MONTH));

            predicateList.add(cb.and(year, month, day));
        }

        if(dataEdicao != null) {
            Path<LocalDateTime> campodataEdicao = root.get("dataEdicao");

            Calendar c = new GregorianCalendar();
            c.setTime(dataEdicao);

            Predicate year = cb.equal(cb.function("year", Integer.class, campodataEdicao), c.get(Calendar.YEAR));
            Predicate month = cb.equal(cb.function("month", Integer.class, campodataEdicao),
                                                                                        c.get(Calendar.MONTH) + 1);
            Predicate day = cb.equal(cb.function("day", Integer.class, campodataEdicao),
                                                                                        c.get(Calendar.DAY_OF_MONTH));

            predicateList.add(cb.and(year, month, day));
        }

        if(usuario != null && !usuario.isEmpty()) {
            Path<String> campoNomeUsuario = joinUsuario.get("nome");
            predicateList.add(cb.like(cb.lower(campoNomeUsuario), new String("%" + usuario + "%").toLowerCase()));
        }

        if(nomeArquivo != null && !nomeArquivo.isEmpty()) {
            Path<String> campoNomeArquivo = root.get("nomeArquivo");
            predicateList.add(cb.like(cb.lower(campoNomeArquivo),
                                        new String("%" + nomeArquivo + "%").toLowerCase()));
        }

        if (tema != null) {
            Path<String> campoTemaId = joinTema.get("id");
            predicateList.add(cb.equal(campoTemaId, tema));
        }

        Predicate[] predicates =
                                                    new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.where(predicates);

        TypedQuery<HistoricoShape> typedQuery = em.createQuery(query);

        if(page != null && itemsPerPage != null) {
            typedQuery = typedQuery.setFirstResult(page * itemsPerPage).setMaxResults(itemsPerPage);
        }

        return new FiltroHistoricoShapeQueryDTO(typedQuery.getResultList(), Long.valueOf(itemsPerPage));

    }

    public Long count() {
        return dao.count();
    }

}
