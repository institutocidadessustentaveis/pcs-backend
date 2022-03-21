package br.org.cidadessustentaveis.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.RelatorioBoaPraticaPcsDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.boaspraticas.BoaPratica;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.repository.BoaPraticaRepository;

@Service
public class RelatorioBoaPraticaPcsService {

    @Autowired
    private BoaPraticaRepository boaPraticaRepository;
    
    @Autowired
	private EntityManager em;
    
    public List<RelatorioBoaPraticaPcsDTO> buscarRelatorios(String titulo, Long idPais, Long idEstado, Long idCidade, Long idEixo, Long idOds, Long idMetaOds) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioBoaPraticaPcsDTO> query = cb.createQuery(RelatorioBoaPraticaPcsDTO.class);

		Root<BoaPratica> boaPratica = query.from(BoaPratica.class);
		
		Join<BoaPratica, ProvinciaEstado> joinProvinciaEstado = boaPratica.join("estado",JoinType.LEFT);
		Join<BoaPratica, Cidade> joinCidade = boaPratica.join("municipio",JoinType.LEFT);
		Join<BoaPratica, Eixo> joinEixo = boaPratica.join("eixo",JoinType.LEFT);
		Join<BoaPratica, Prefeitura> joinPrefeitura = boaPratica.join("prefeitura",JoinType.LEFT);
		Join<BoaPratica, Pais> joinPais = joinProvinciaEstado.join("pais",JoinType.LEFT);
		Join<BoaPratica, ObjetivoDesenvolvimentoSustentavel> joinOds = boaPratica.join("ods",JoinType.LEFT);
		Join<BoaPratica, MetaObjetivoDesenvolvimentoSustentavel> joinMetasOds = boaPratica.join("metasOds",JoinType.LEFT);
		
		query.multiselect(boaPratica).distinct(true);
		
		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		Boolean verificado = false;
		
		Predicate predicateForTipo = cb.equal(boaPratica.get("tipo"), "PCS");
		
		if(idPais != null) {
			Path<Long> idPaisPath = joinPais.get("id");
			Predicate predicateForPais = cb.equal(idPaisPath, idPais);
			predicateList.add(cb.and(predicateForTipo, predicateForPais));
			verificado = true;
		}
		
		if(idEstado != null) {
			Path<Long> idProvinciaEstadoPath = joinProvinciaEstado.get("id");
			Predicate predicateForEstado = cb.equal(idProvinciaEstadoPath, idEstado);
			predicateList.add(cb.and(predicateForTipo, predicateForEstado));
			verificado = true;
		}

		if(idCidade != null) {
			Path<Long> idCidadePath = joinCidade.get("id");
			Predicate predicateForCidade = cb.equal(idCidadePath, idCidade);
			predicateList.add(cb.and(predicateForTipo, predicateForCidade));
			verificado = true;
		}

		if (titulo != null && !titulo.equals("")) {
			Path<String> tituloBoaPratica = boaPratica.get("titulo");
			Predicate predicateForTitulo = cb.like(cb.lower(tituloBoaPratica), "%" + titulo.toLowerCase() + "%");
			predicateList.add(cb.and(predicateForTipo, predicateForTitulo));
			verificado = true;
		}

		if(idEixo != null) {
			Path<Long> idEixoPath = joinEixo.get("id");
			Predicate predicateForEixo = cb.equal(idEixoPath, idEixo);
			predicateList.add(cb.and(predicateForTipo, predicateForEixo));
			verificado = true;
		}

		if (idOds != null) {
			In<Long> inClause = cb.in(joinOds.get("id"));
			inClause.value(idOds);
			predicateList.add(cb.and(predicateForTipo, inClause));
			verificado = true;
		}

		if (idMetaOds != null) {
			In<Long> inClause = cb.in(joinMetasOds.get("id"));
			inClause.value(idMetaOds);
			predicateList.add(cb.and(predicateForTipo, inClause));
			verificado = true;
		}

		if(!verificado) {
			predicateList.add(predicateForTipo);
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<RelatorioBoaPraticaPcsDTO> typedQuery = em.createQuery(query);
		List<RelatorioBoaPraticaPcsDTO> lista = typedQuery.getResultList();

		return lista;
	}
    
	public List<Indicador> findIndicadorById(Long idBoaPratica){
		return boaPraticaRepository.buscarIndicadoresDaBoaPratica(idBoaPratica);
	}
	
	public List<MetaObjetivoDesenvolvimentoSustentavel> findMetaOdsById(Long idBoaPratica){
		return boaPraticaRepository.buscarMetasOdsDaBoaPratica(idBoaPratica);
	}
	
	public List<ObjetivoDesenvolvimentoSustentavel> findOdsById(Long idBoaPratica){
		return boaPraticaRepository.buscarOdsDaBoaPratica(idBoaPratica);
	}
}
